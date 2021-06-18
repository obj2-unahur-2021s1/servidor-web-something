package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidorWeb = ServidorWeb()
    val pedido01 = Pedido("103.57.91.1", "http://pepito.com.ar/hola.txt", LocalDateTime.now())
    val pedido02 = Pedido("102.46.81.0", "https://pepito.com.ar/hola.docx", LocalDateTime.now())

    describe("Una respuesta con código 200") {
      it("recibirPedido") {
        val respuesta = servidorWeb.recibirPedido(pedido01)
        respuesta.codigo.shouldBe(CodigoHttp.OK)
      }
    }
    describe("Una respuesta con código 501") {
      it("recibirPedido") {
        val respuesta = servidorWeb.recibirPedido(pedido02)
        respuesta.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      }
    }
  }
})
