package loc.localize

import loc.localize.generate.PhaseReplaceText
import loc.localize.parse.MSExcelParse
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import java.io.IOException
import java.util.*

/**
 * Created by localadmin on 4/19/2017.
 */
class Step(name: String, private val excelFile: String, private val excelSheet: String, private val propertiesMap: Map<*, *>, private val input: String, private val output: String) {
    private val name: String

    val resultMap = HashMap<String, String>()

    init {
        this.name = "Step-" + name
    }

    fun doMainJob() {
        //Phase 2: read excel file and match String
        LOGGER.info(String.format("%s: read from Excel file and generate result map", name))
        val parse = MSExcelParse(excelFile, excelSheet)

        try {
            parse.poiReader(object : MSExcelParse.PoiParseCallback {
                override fun isMatchCell(s: String): Boolean {
                    if (StringUtils.isBlank(s))
                        return false
                    else
                        return propertiesMap.containsKey(s)
                }

                override fun doUpdate(key: String, cellValue: String) {
                    resultMap.put(key, cellValue)
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }

        LOGGER.info(String.format("%s: Initialize words map:%s", name, propertiesMap))

        //Phase 3: Put Localize map string to new file
        LOGGER.info(String.format("%s: Generate localize file '%s'", name, output))
        val replaceText = PhaseReplaceText(input, output)
        try {
            replaceText.updateFile(resultMap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        private val LOGGER = Logger.getLogger(Step::class.java)
    }
}
