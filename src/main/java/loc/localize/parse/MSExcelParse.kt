package loc.localize.parse

import loc.localize.util.FileUtils
import loc.localize.util.LanguageColumn
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.stream.IntStream

/**
 * Created by localadmin on 4/19/2017.
 */
class MSExcelParse(private val excelResourceFile: String, private val sheetName: String?) {

    @Throws(IOException::class)
    fun poiReader(callback: PoiParseCallback) {
        val fileName = File(excelResourceFile)
        val excelFile = FileInputStream(fileName)

        var workbook: Workbook? = null
        val type = FilenameUtils.getExtension(excelResourceFile)
        if ("xls" == type) {
            workbook = HSSFWorkbook(excelFile)
        } else {
            workbook = XSSFWorkbook(excelFile)
        }
        val sheetsNum = workbook.numberOfSheets
        val finalWorkbook = workbook
        IntStream.range(0, sheetsNum)
                .forEach { value ->
                    val sName = finalWorkbook.getSheetName(value)
                    LOGGER.info(String.format("SHEET---%s", sName))

                    if (StringUtils.isBlank(sheetName) || sheetName == sName) {
                        LOGGER.info(String.format("\t------> Process sheet---%s", sName))
                        val datatypeSheet = finalWorkbook.getSheetAt(value)
                        val iterator = datatypeSheet.iterator()
                        while (iterator.hasNext()) {
                            val currentRow = iterator.next()
                            val cellIterator = currentRow.iterator()
                            while (cellIterator.hasNext()) {
                                val currentCell = cellIterator.next()
                                val key = currentCell.toString()

                                if (callback.isMatchCell(FileUtils.cleanString(key))) {
                                    IntStream.range(0, LanguageColumn.TH.ordinal)
                                            .forEach { i ->
                                                if (cellIterator.hasNext()) {
                                                    val cell = cellIterator.next()
                                                    if (i == LanguageColumn.TH.ordinal - 1) {
                                                        callback.doUpdate(key, cell.toString())
                                                    }
                                                }
                                            }
                                }
                            }
                        }
                    } else
                        LOGGER.info(String.format("\t------> Ignore sheet %s", sName))
                }
    }

    @Throws(IOException::class)
    fun poiFinder(text: String): String {
        val fileName = File(excelResourceFile)
        val excelFile = FileInputStream(fileName)
        var workbook: Workbook? = null
        val type = FilenameUtils.getExtension(excelResourceFile)
        if ("xls" == type) {
            workbook = HSSFWorkbook(excelFile)
        } else {
            workbook = XSSFWorkbook(excelFile)
        }
        val sheetsNum = workbook.numberOfSheets
        var findOut = false
        var findOutSheet = ""

        for (i in 0..sheetsNum - 1) {
            val sName = workbook.getSheetName(i)
            if (StringUtils.isBlank(sheetName) || sheetName == sName) {
                val datatypeSheet = workbook.getSheetAt(i)
                val iterator = datatypeSheet.iterator()
                while (iterator.hasNext()) {
                    val currentRow = iterator.next()
                    val cellIterator = currentRow.iterator()
                    while (cellIterator.hasNext()) {
                        val currentCell = cellIterator.next()
                        val key = currentCell.toString()
                        if (key.toLowerCase().contains(text.toLowerCase())) {
                            findOut = true
                            findOutSheet = sName
                            println(27.toChar() + "[32m\n" +
                                    "---find--------------" + key + "\n" +
                                    "---detect sheet------" + sName + "\n" +
                                    "---of file-----------" + excelResourceFile)
                            IntStream.range(0, LanguageColumn.TH.ordinal + 2)
                                    .forEach { value ->
                                        if (cellIterator.hasNext()) {
                                            val cell = cellIterator.next()
                                            println(27.toChar() + "[35m" +
                                                    "---replace text---> " + cell.toString())
                                        }
                                    }
                            break
                        }
                    }
                }
            }
        }
        return findOutSheet
    }

    interface PoiParseCallback {
        fun isMatchCell(s: String): Boolean

        fun doUpdate(key: String, cellValue: String)
    }

    interface PoiFinderCallback {
        fun checkCell(s: String)
    }

    companion object {
        private val LOGGER = Logger.getLogger(MSExcelParse::class.java)
    }

}
