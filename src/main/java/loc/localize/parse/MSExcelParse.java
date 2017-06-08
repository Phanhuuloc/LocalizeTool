package loc.localize.parse;

import loc.localize.util.FileUtils;
import loc.localize.util.LanguageColumn;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * Created by localadmin on 4/19/2017.
 */
public class MSExcelParse {
    private final static Logger LOGGER = Logger.getLogger(MSExcelParse.class);
    private final String excelResourceFile;
    private final String sheetName;

    public MSExcelParse(String excelResourceFile, String sheetName) {
        this.excelResourceFile = excelResourceFile;
        this.sheetName = sheetName;
    }

    public void poiReader(PoiParseCallback callback) throws IOException {
        File fileName = new File(excelResourceFile);
        FileInputStream excelFile = new FileInputStream(fileName);

        Workbook workbook = null;
        String type = FilenameUtils.getExtension(excelResourceFile);
        if ("xls".equals(type)) {
            workbook = new HSSFWorkbook(excelFile);
        } else {
            workbook = new XSSFWorkbook(excelFile);
        }
        int sheetsNum = workbook.getNumberOfSheets();
        Workbook finalWorkbook = workbook;
        IntStream.range(0, sheetsNum)
                .forEach(value -> {
                    String sName = finalWorkbook.getSheetName(value);
                    LOGGER.info(String.format("SHEET---%s", sName));

                    if (StringUtils.isBlank(sheetName) || sheetName.equals(sName)) {
                        LOGGER.info(String.format("\t------> Process sheet---%s", sName));
                        Sheet datatypeSheet = finalWorkbook.getSheetAt(value);
                        Iterator<Row> iterator = datatypeSheet.iterator();
                        while (iterator.hasNext()) {
                            Row currentRow = iterator.next();
                            Iterator<Cell> cellIterator = currentRow.iterator();
                            while (cellIterator.hasNext()) {
                                Cell currentCell = cellIterator.next();
                                String key = currentCell.toString();

                                if (callback.isMatchCell(FileUtils.cleanString(key))) {
                                    IntStream.range(0, LanguageColumn.TH.ordinal())
                                            .forEach(i -> {
                                                if (cellIterator.hasNext()) {
                                                    Cell cell = cellIterator.next();
                                                    if (i == LanguageColumn.TH.ordinal() - 1) {
                                                        callback.doUpdate(key, cell.toString());
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    } else LOGGER.info(String.format("\t------> Ignore sheet %s", sName));
                });
    }

    public String poiFinder(String text) throws IOException {
        File fileName = new File(excelResourceFile);
        FileInputStream excelFile = new FileInputStream(fileName);
        Workbook workbook = null;
        String type = FilenameUtils.getExtension(excelResourceFile);
        if ("xls".equals(type)) {
            workbook = new HSSFWorkbook(excelFile);
        } else {
            workbook = new XSSFWorkbook(excelFile);
        }
        int sheetsNum = workbook.getNumberOfSheets();
        boolean findOut = false;
        String findOutSheet = "";

        for (int i = 0; i < sheetsNum; i++) {
            String sName = workbook.getSheetName(i);
            if (StringUtils.isBlank(sheetName) || sheetName.equals(sName)) {
                Sheet datatypeSheet = workbook.getSheetAt(i);
                Iterator<Row> iterator = datatypeSheet.iterator();
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        String key = currentCell.toString();
                        if (key.toLowerCase().contains(text.toLowerCase())) {
                            findOut = true;
                            findOutSheet = sName;
                            System.out.println((char) 27 + "[32m\n" +
                                    "---find--------------" + key + "\n" +
                                    "---detect sheet------" + sName + "\n" +
                                    "---of file-----------" + excelResourceFile);
                            IntStream.range(0, LanguageColumn.TH.ordinal() + 2)
                                    .forEach(value -> {
                                        if (cellIterator.hasNext()) {
                                            Cell cell = cellIterator.next();
                                            System.out.println((char) 27 + "[35m" +
                                                    "---replace text---> " + cell.toString());
                                        }
                                    });
                            break;
                        }
                    }
                }
            }
        }
        return findOutSheet;
    }

    public interface PoiParseCallback {
        boolean isMatchCell(String s);

        void doUpdate(String key, String cellValue);
    }

    public interface PoiFinderCallback {
        void checkCell(String s);
    }

}
