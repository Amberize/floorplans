import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

class ImageProcessor {

    companion object {

        private val logger = KotlinLogging.logger {}

        private val objectMapper = jacksonObjectMapper()

        private val xmlObjectMapper = XmlMapper()

        @JvmStatic
        fun main(args: Array<String>) {

            val file = File("data/page.json")
            val data = objectMapper.readValue<List<FloorPlan>>(file)

            val toXML = data.map {
                val outputName = "data/${it.hash}.jpg"

                downloadFile(URL(it.thumbnail), outputName)

                val arch = System.getProperty ("os.arch")
                if (arch != "aarch64") {
                    val image = ImageIO.read(File(outputName))
                    val result = ImageIO.write(image, "webp", File("data/${it.hash}.webp"))
                    logger.info { "WebP ${it.hash} result: $result" }
                }

                FloorPlan(
                    hash = it.hash,
                    thumbnail = null,
                    rooms = 1,
                )
            }

            xmlObjectMapper.writeValue(File("data/rooms.xml"), toXML)

        }

        private fun downloadFile(url: URL, outputName: String) {

            url.openStream().use { Files.copy(it, Paths.get(outputName)) }

        }

    }

}