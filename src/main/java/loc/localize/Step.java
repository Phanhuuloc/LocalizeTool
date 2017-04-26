package loc.localize;

import loc.localize.parse.MSExcelParse;
import loc.localize.generate.PhaseReplaceText;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by localadmin on 4/19/2017.
 */
public class Step {
    private static final Logger LOGGER = Logger.getLogger(Step.class);
    private final String name;
    private final String excelFile;
    private final String excelSheet;
    private final Map propertiesMap;
    private final String input;
    private final String output;

    public final HashMap<String, String> resultMap = new HashMap<>();

    public Step(String name, String excelFile, String excelSheet, Map propertiesMap, String input, String output) {
        this.name = "Step-" + name;
        this.excelFile = excelFile;
        this.excelSheet = excelSheet;
        this.propertiesMap = propertiesMap;
        this.input = input;
        this.output = output;
    }

    public void doMainJob() {
        //Phase 2: read excel file and match String
        LOGGER.info(String.format("%s: read from Excel file and generate result map", name));
        MSExcelParse parse = new MSExcelParse(excelFile, excelSheet);

        try {
            parse.poiReader(new MSExcelParse.PoiParseCallback() {
                @Override
                public boolean isMatchCell(String s) {
                    if (StringUtils.isBlank(s)) return false;
                    else return propertiesMap.containsKey(s);
                }

                @Override
                public void doUpdate(String key, String cellValue) {
                    resultMap.put(key, cellValue);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info(String.format("%s: Initialize words map:%s", name, propertiesMap));

        //Phase 3: Put Localize map string to new file
        LOGGER.info(String.format("%s: Generate localize file '%s'", name, output));
        PhaseReplaceText replaceText = new PhaseReplaceText(input, output);
        try {
            replaceText.updateFile(resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
