import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime

// Pueden usar este archivo para hacer pruebas rápidas,
// de la misma forma en que usaban el REPL de Wollok.

// OJO: lo que esté aquí no será tenido en cuenta
// en la corrección ni reemplaza a los tests.

listOf(1, 8, 10).average()


val url = URL("http://pepito.com.ar/documentos/doc1.html")
val dir = "abc.jpg"

url.protocol
url.path
url.file

dir.substringAfterLast(".")

val lista = mutableListOf<Int?>(1,2,3,2,4,2,4)

LocalDateTime.now()

lista.add(null)
lista
lista.contains(null)

lista.filterNotNull()

