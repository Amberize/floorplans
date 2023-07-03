package me.amberize.floorplans

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.amberize.floorplans.Constants
import me.amberize.floorplans.model.FloorPlan
import org.jsoup.Jsoup
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists


class PageExtractor {

    companion object {

        private val regex = "floorplans/[a-zA-Z]+".toRegex()

        private val objectMapper = jacksonObjectMapper()

        @JvmStatic
        fun main(args: Array<String>) {

            val doc = Jsoup.connect("https://planner5d.com/gallery/floorplans").get()

            val elements = doc.select("#grid > div > div > div:nth-child(1) > a")

            val floorPlans = elements.map {

                val link = it.attr("href")
                val match = regex.find(link)

                val img = it.select("img").attr("src")

                FloorPlan(
                    hash = match!!.value.split("/")[1],
                    thumbnail = img,
                    rooms = null,
                )
            }

            val output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(floorPlans)

            val dataDir = Paths.get(Constants.DATA_DIR)
            if (!dataDir.exists()) {
                Files.createDirectory(dataDir)
            }
            File("data/page.json").writeText(output)

        }

    }

}