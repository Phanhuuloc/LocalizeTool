package loc.localize.map

import loc.localize.util.FileUtils
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Created by localadmin on 4/19/2017.
 */
class MapFile(private val fileName: String) {

    fun getInitMap(): Map<*, *> {
        readFromFile(fileName)
        return initMap
    }

    companion object {

        private val initMap = HashMap<String, String>()

        fun readFromFile(fileName: String) {
            //read file into stream, try-with-resources
            try {
                Files.lines(Paths.get(fileName)).use { stream ->
                    stream.filter { s -> s.contains("=") && s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1 }
                            .map { s ->
                                val tmp = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                val key = FileUtils.cleanString(tmp[1])
                                initMap.put(key, key)
                                key
                            }
                            .forEach { s -> }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}
