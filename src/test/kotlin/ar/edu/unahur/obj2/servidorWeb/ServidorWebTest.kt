package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    val servidorWeb = ServidorWeb()
    val extensionesImagen = setOf("jpg", "png")
    val extensionesDoc = setOf("docx", "txt")
    val ipsSospechosas = mutableSetOf("102.46.81.0", "102.20.20.0")
    val moduloImagen = Modulo(extensionesImagen, "Imagen", 15 )
    val moduloDocumento = Modulo(extensionesDoc, "Documento", 17)
    val analizadorDemora = AnalizadorDeDemoraEnRespuesta(12)
    val analizadorIp = AnalizadorDeIPsSospechosas(ipsSospechosas)
    val analizadorEst = AnalizadorDeEstadisticas()
    val pedido01 = Pedido("103.57.91.1", URL("http://pepito.com.ar/hola.txt"), LocalDateTime.now())
    val pedido02 = Pedido("102.46.81.0", URL("https://pepito.com.ar/hola.docx"), LocalDateTime.now())
    val pedido03 = Pedido("102.20.20.0", URL("http://pepito.com.ar/hola.wmv"), LocalDateTime.now())
    val pedido04 = Pedido("102.46.81.0", URL("http://pepito.com.ar/hola.jpg"), LocalDateTime.now())
    val pedido05 = Pedido("102.20.20.0", URL("http://pepito.com.ar/hola.png"),
      LocalDateTime.of(2021, 6, 19, 10, 11, 0))
    val pedido06 = Pedido("102.20.20.0", URL("http://pepito.com.ar/hola.jpg"),
      LocalDateTime.of(2021, 6, 18, 10, 11, 0))

    servidorWeb.agregarModulo(moduloImagen)
    servidorWeb.agregarModulo(moduloDocumento)
    servidorWeb.agregarAnalizador(analizadorDemora)
    servidorWeb.agregarAnalizador(analizadorIp)
    servidorWeb.agregarAnalizador(analizadorEst)

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

    describe("Analizadores") {
      servidorWeb.recibirPedido(pedido01)
      servidorWeb.recibirPedido(pedido02)
      servidorWeb.recibirPedido(pedido03)
      servidorWeb.recibirPedido(pedido04)
      servidorWeb.recibirPedido(pedido05)
      servidorWeb.recibirPedido(pedido06)

      describe("Analizador de tiempo de respuesta") {
        it("cantidad de respuestas demoradas moduloDocumento (1)") {
          analizadorDemora.cantDeRespuestasDemoradas(moduloDocumento).shouldBe(1)
        }
      }

      describe("Analizador de IPs sospechosas") {
        it("cantidad de pedidos de una ip (3)") {
          analizadorIp.cantDePedidosHechosPor("102.20.20.0").shouldBe(3)
        }
        it("modulo mas consultado por ips sospechosas (imagen)") {
          analizadorIp.moduloMasConsultado().shouldBe(moduloImagen)
        }
        it("ips que que solicitaron ruta (/hola.jpg)") {
          analizadorIp.ipsQueRequirieron("/hola.jpg").shouldBe(setOf("102.20.20.0", "102.46.81.0"))
        }
      }

      describe("Analizador de estadísticas") {
        it("tiempo de respuesta promedio en Int(13,6)") {
          analizadorEst.tiempoDeRespuestaPromedio().shouldBe(13)
        }
        it("cantidad de pedidos entre 2 fechas (2)") {
          analizadorEst.cantDePedidosEntre(LocalDateTime.of(
            2021, 6, 17, 10, 11, 0), LocalDateTime.of(
            2021, 6, 20, 10, 11, 0)).shouldBe(2)
        }
        it("cantidad de respuestas con body Imagen (3)") {
          analizadorEst.cantDeRespuestasQueIncluyen("imagen").shouldBe(3)
        }
        it("porcentaje de pedidos exitosos cod:200 en Int (66,6)") {
          analizadorEst.porcentajeDePedidosExitosos().shouldBe(66)
        }
      }
    }
  }
})
