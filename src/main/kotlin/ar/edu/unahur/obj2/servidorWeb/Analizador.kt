package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

abstract class Analizador {
    abstract fun analizar(respuesta: Respuesta, modulo: Modulo)
}

class AnalizadorDeDemoraEnRespuesta(val demoraMinima: Int): Analizador() {
    override fun analizar(respuesta: Respuesta, modulo: Modulo) {
        if (respuesta.tiempo > demoraMinima)
            modulo.respuestasDemoradas.add(respuesta)
    }

    fun cantDeRespuestasDemoradas(modulo: Modulo) = modulo.respuestasDemoradas.size
}

class AnalizadorDeIPsSospechosas(val ipsSospechosas: MutableSet<String>): Analizador() {
    val pedidosSospechosos = mutableListOf<Pedido>()
    val modulos = mutableListOf<Modulo>()

    override fun analizar(respuesta: Respuesta, modulo: Modulo) {
        if (ipsSospechosas.contains(respuesta.pedido.ip)) {
            pedidosSospechosos.add(respuesta.pedido)
            modulos.add(modulo)
        }
    }

    fun cantDePedidosHechosPor(ip: String) = pedidosSospechosos.count { it.ip == ip }

    fun moduloMasConsultado() = modulos.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

    fun ipsQueRequirieron(ruta: String) = pedidosSospechosos.filter { it.ruta() == ruta }.map { it.ip }.toSet()
}

class AnalizadorDeEstadisticas: Analizador() {
    val respuestasPorAnalizar = mutableListOf<Respuesta>()

    override fun analizar(respuesta: Respuesta, modulo: Modulo) {
        respuestasPorAnalizar.add(respuesta)
    }

    fun tiempoDeRespuestaPromedio() = respuestasPorAnalizar.sumBy { it.tiempo } / respuestasPorAnalizar.size

    fun cantDePedidosEntre(primerMomento: LocalDateTime, segundoMomento: LocalDateTime) =
        this.respuestasAPedidos().filter { it.fechaHora.isAfter(primerMomento) &&
                it.fechaHora.isBefore(segundoMomento) }.size

    fun cantDeRespuestasQueIncluyen(string: String) = respuestasPorAnalizar.filter { it.body.contains(string, ignoreCase = true) }.size

    fun porcentajeDePedidosExitosos() = this.respuestasExitosas().size / respuestasPorAnalizar.size * 100

    fun respuestasExitosas() = respuestasPorAnalizar.filter { it.codigo == CodigoHttp.OK }

    fun respuestasAPedidos() = respuestasPorAnalizar.map { it.pedido }
}