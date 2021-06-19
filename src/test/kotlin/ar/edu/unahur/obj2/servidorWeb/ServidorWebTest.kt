package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidorWeb = ServidorWeb()
    val extensionesImagen = setOf<String>("jpg", "png")
    val extensionesDoc = setOf<String>("docx", "txt")
    val moduloImagen = Modulo(extensionesImagen, "Imagen", 15 )
    val moduloDocumento = Modulo(extensionesDoc, "Documento", 17)
    val pedido01 = Pedido("103.57.91.1", URL("http://pepito.com.ar/hola.txt"), LocalDateTime.now())
    val pedido02 = Pedido("102.46.81.0", URL("https://pepito.com.ar/hola.docx"), LocalDateTime.now())
    val pedido03 = Pedido("102.20.20.0", URL("http://pepito.com.ar/hola.wmv"), LocalDateTime.now())

    servidorWeb.agregarModulo(moduloImagen)
    servidorWeb.agregarModulo(moduloDocumento)

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
    describe("Una respuesta con código 404") {
      it("recibirPedido") {
        val respuesta = servidorWeb.recibirPedido(pedido03)
        respuesta.codigo.shouldBe(CodigoHttp.NOT_FOUND)
      }
    }
  }
})
