package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime
import java.net.URL

enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class ServidorWeb {
  val modulosIntegrados = mutableListOf<Modulo>()
  val analizadoresIntegrados = mutableListOf<Analizador>()

  fun agregarModulo(modulo: Modulo) = modulosIntegrados.add(modulo)

  fun agregarAnalizador(analizador: Analizador) = analizadoresIntegrados.add(analizador)

  fun pedidoAceptado(pedido: Pedido) = pedido.protocolo() == "http"

  fun extensionSoportada(extension: String) =
    modulosIntegrados.any { it.extensiones.contains(extension) }

  fun procesarAnalizador(respuesta: Respuesta, modulo: Modulo?) {
    if (analizadoresIntegrados.isNotEmpty())
      this.analizadoresIntegrados.forEach { it.analizar(respuesta, modulo) }
  }

  fun recibirPedido(pedido: Pedido): Respuesta {
    return if (pedidoAceptado(pedido) && extensionSoportada(pedido.extension())) {
      val modulo = modulosIntegrados.first { it.extensiones.contains(pedido.extension()) }
      val respuesta = Respuesta(CodigoHttp.OK, modulo.respuestaModulo, modulo.tiempoRespuestaModulo, pedido)
      procesarAnalizador(respuesta, modulo)
      respuesta
      } else if (!pedidoAceptado(pedido)) {
        val modulo = null
        val respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
        procesarAnalizador(respuesta, modulo)
        respuesta
        } else {
        val modulo = null
        val respuesta = Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
        procesarAnalizador(respuesta,modulo)
        respuesta
          }
  }
}

class Pedido(val ip: String, val url: URL, val fechaHora: LocalDateTime) {
  fun protocolo() = url.protocol
  fun ruta() = url.path
  fun extension() = url.toString().substringAfterLast(".")
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)

class Modulo(val extensiones: Set<String>, val respuestaModulo: String, val tiempoRespuestaModulo: Int) {
  val respuestasDemoradas = mutableListOf<Respuesta>()
}