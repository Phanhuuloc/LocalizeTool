package loc.localize.generate

import java.io.*

/**
 * Created by localadmin on 4/19/2017.
 */
class PhaseReplaceText(private val fileInput: String, private val fileOutput: String) {

    @Throws(IOException::class)
    fun updateFile(fileMap: Map<String, String>) {
        val inputStream = BufferedReader(FileReader(fileInput))
        val UIFile = File(fileOutput)
        // if File doesnt exists, then create it
        if (!UIFile.exists()) {
            UIFile.createNewFile()
        }
        val filewriter = FileWriter(UIFile.absoluteFile)
        val outputStream = BufferedWriter(filewriter)
        var count: String = inputStream.readLine()
        while (count != null) {
            if (count.contains("=") && count.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1) {
                val tmp = count.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val key = tmp[0]
                var value = tmp[1].trim { it <= ' ' }.replace("\t", "")
                if (fileMap.containsKey(value)) {
                    value = fileMap[value].toString()
                }
                count = key + " = " + value
            }
            outputStream.write(count + "\n")
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }
}
