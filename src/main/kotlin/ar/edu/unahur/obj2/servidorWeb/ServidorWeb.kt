package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime
import java.net.URL


// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class ServidorWeb {
  val modulosIntegrados = mutableListOf<Modulo>()

  fun agregarModulo(modulo: Modulo) = modulosIntegrados.add(modulo)

  fun pedidoAceptado(pedido: Pedido) = pedido.protocolo() == "http"

  fun extensionSoportada(extension: String) =
    modulosIntegrados.any { it.extensiones.contains(extension) }

  fun recibirPedido(pedido: Pedido): Respuesta {
    return if (pedidoAceptado(pedido) && extensionSoportada(pedido.extension())) {
      val modulo = modulosIntegrados.first { it.extensiones.contains(pedido.extension()) }
      Respuesta( CodigoHttp.OK, modulo.respuestaModulo, modulo.tiempoRespuestaModulo, pedido)
      } else if (!pedidoAceptado(pedido)) {
        Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
      } else {
        Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
    }
  }
}

class Pedido(val ip: String, val url: URL, val fechaHora: LocalDateTime) {
  fun protocolo() = url.protocol
  fun ruta() = url.path
  fun extension() = url.toString().substringAfterLast(".")
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)


class Modulo (
  val extensiones: Set<String>, val respuestaModulo: String, val tiempoRespuestaModulo : Int)
