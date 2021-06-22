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

  fun procesarAnalizador(respuesta: Respuesta, modulo: Modulo? = null) {
    if (analizadoresIntegrados.isNotEmpty())
      this.analizadoresIntegrados.forEach { it.analizar(respuesta, modulo) }
  }

  fun recibirPedido(pedido: Pedido): Respuesta {
    var modulo : Modulo? = null
    var respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
    if (!extensionSoportada(pedido.extension())) {
        respuesta = Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
    } else if (pedidoAceptado(pedido) && extensionSoportada(pedido.extension())) {
      modulo = modulosIntegrados.first { it.extensiones.contains(pedido.extension()) }
      respuesta = Respuesta(CodigoHttp.OK, modulo.respuestaModulo, modulo.tiempoRespuestaModulo, pedido)
      }
    procesarAnalizador(respuesta, modulo)
    return respuesta
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