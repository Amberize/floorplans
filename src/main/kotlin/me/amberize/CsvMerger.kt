package me.amberize

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.amberize.model.FloorPlan
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class CsvMerger {

    companion object {

        private val objectMapper = jacksonObjectMapper()

        private var xmlObjectMapper = XmlMapper(JacksonXmlModule()).registerKotlinModule()

        @JvmStatic
        fun main(args: Array<String>) {

            val page = objectMapper.readValue<List<FloorPlan>>(File("data/page.json"))
            val hashToImg = page.associate { it.hash to it.thumbnail }

            val rooms = xmlObjectMapper.readValue<List<FloorPlan>>(File("data/rooms.xml"))
            val hashToRooms = rooms.associate { it.hash to it.rooms }

            val merged = hashToImg.keys.map {
                FloorPlan(
                    hash = it,
                    thumbnail = hashToImg[it],
                    rooms = hashToRooms[it],
                )
            }

            val csvFormat = CSVFormat.DEFAULT.builder().build()

            FileWriter("data/output.csv").use { fileWriter ->
                PrintWriter(fileWriter).use { printWriter ->
                    CSVPrinter(printWriter, csvFormat).use { printer ->
                        merged.forEach {
                            printer.printRecord(it.hash, it.thumbnail, it.rooms)
                        }
                    }
                }
            }

        }

    }

}