package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class ServidorWeb {
  fun recibirPedido(pedido: Pedido): Respuesta {
    return if (!pedido.url.startsWith("http:")) {
      Respuesta(codigo = CodigoHttp.NOT_IMPLEMENTED, body = "", tiempo = 10, pedido = pedido)
    } else Respuesta(codigo = CodigoHttp.OK, body = "", tiempo = 10, pedido = pedido)
  }

}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime)
class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)