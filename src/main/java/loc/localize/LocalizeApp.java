package loc.localize;

import loc.localize.map.MapFile;
import loc.localize.util.Config;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by localadmin on 4/19/2017.
 */
public class LocalizeApp {
    private final static Logger LOGGER = Logger.getLogger(LocalizeApp.class);

    private static String buildFileOutput(String dir, String step) {
        return dir + "ApplicationResource_step" + step + ".properties";
    }

    public static void runApp() {
        LocalizeApp localizeApp = new LocalizeApp();
        Config cfg = new Config();

        LOGGER.info("Initialize a words map");
        MapFile mapFile = new MapFile(cfg.getBaseFile());
        HashMap<String, String> initMap = (HashMap<String, String>) mapFile.getInitMap();

        LOGGER.info("Initialize steps");
        Step steps[] = localizeApp.buildStep(initMap, cfg);

        Stream.of(steps).forEach(Step::doMainJob);
    }

    private Step[] buildStep(Map initMap, Config cfg) {
        List<String> excelFiles = cfg.getExcelFiles();
        List<String> excelSheets = cfg.getExcelSheets();
        String baseFile = cfg.getBaseFile();
        String output = cfg.getOutput();
        String dir = cfg.getDir();

        int stepSize = excelFiles.size();
        Step steps[] = new Step[stepSize];

        String preOut = null;
        for (int i = 0; i < stepSize; i++) {
            String inp;
            if (i == 0) {
                inp = baseFile;
            } else {
                inp = preOut;
            }

            String tempOut = null;
            if (i == stepSize - 1) {
                tempOut = output;
            } else {
                tempOut = buildFileOutput(dir, String.valueOf(i));
                preOut = tempOut;
            }

            Step step = null;
            if (excelSheets.isEmpty()) {
                step = new Step(String.valueOf(i), excelFiles.get(i), null, initMap, inp, tempOut);
            } else {
                step = new Step(String.valueOf(i), excelFiles.get(i), excelSheets.get(i), initMap, inp, tempOut);
            }
            steps[i] = step;
        }

        return steps;
    }
}
