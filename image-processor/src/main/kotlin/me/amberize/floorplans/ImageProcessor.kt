package me.amberize.floorplans

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.*
import me.amberize.floorplans.model.FloorPlan
import mu.KotlinLogging
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.random.Random

class ImageProcessor {

    companion object {

        private val logger = KotlinLogging.logger {}

        private val objectMapper = jacksonObjectMapper()

        private val xmlObjectMapper = XmlMapper()

        @JvmStatic
        fun main(args: Array<String>): Unit = runBlocking {

            val pageFile = File("${Constants.DATA_DIR}/page.json")
            val data = objectMapper.readValue<List<FloorPlan>>(pageFile)

            val roomsFile = File("${Constants.DATA_DIR}/rooms.xml")
            roomsFile.appendText("<FloorPlans>")
            data.map {
                logger.info { "Floor plan: ${it.hash}" }
                async(Dispatchers.IO) {
                    processFloorPlan(it, roomsFile)
                }
            }.awaitAll()
            roomsFile.appendText("</FloorPlans>")

        }

        private suspend fun processFloorPlan(floorPlan: FloorPlan, file: File) = coroutineScope {

            val roomsPlan = getFloorPlan(floorPlan)
            val serialized = xmlObjectMapper.writeValueAsString(roomsPlan)
            file.appendText("$serialized\n")

        }

        private fun getFloorPlan(floorPlan: FloorPlan): FloorPlan {
            val outputName = "${Constants.DATA_DIR}/${floorPlan.hash}.jpg"

            downloadFile(URL(floorPlan.thumbnail), outputName)

            generateWebP(outputName, floorPlan)

            return FloorPlan(
                hash = floorPlan.hash,
                thumbnail = null,
                rooms = Random.nextInt(1, 10), // here should be an API call via REST client
            )
        }

        private fun generateWebP(outputName: String, it: FloorPlan) {

            val arch = System.getProperty("os.arch")
            if (arch != "aarch64") {
                val image = ImageIO.read(File(outputName))
                val result = ImageIO.write(image, "webp", File("${Constants.DATA_DIR}/${it.hash}.webp"))
                logger.info { "WebP ${it.hash} result: $result" }
            }

        }

        private fun downloadFile(url: URL, outputName: String) {

            url.openStream().use { Files.copy(it, Paths.get(outputName)) }

        }

    }

}