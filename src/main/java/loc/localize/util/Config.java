package loc.localize.util;


import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by localadmin on 4/20/2017.
 */
public class Config {
    private String dir;
    private String baseFile;
    private String output;
    private List<String> excelFiles = new ArrayList<>();
    private List<String> excelSheets = new ArrayList<>();
    private List<Integer> positions = new ArrayList<>();

    public Config() {
        getConfig();
    }

    private void getConfig() {
        Configurations configs = new Configurations();
        try {
            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("localize.xml");

            XMLConfiguration config = new BasicConfigurationBuilder<>(XMLConfiguration.class)
                    .configure(new Parameters().xml())
                    .getConfiguration();
            FileHandler fh = new FileHandler(config);
            fh.load(in);
//            Configuration config = configs.xml(fh);
            dir = config.getString("localize.file[@dir]");
            baseFile = dir + config.getString("localize.file[@in]");
            output = dir + config.getString("localize.file[@out]");
            config.getList(String.class, "localize.steps.step[@e-file]")
                    .forEach(s -> excelFiles.add(dir + s));
            excelSheets = config.getList(String.class, "localize.steps.step[@e-sheet]");
            positions = config.getList(Integer.class, "localize.steps.step[@pos]");
        } catch (ConfigurationException cex) {
            // Something went wrong
        }
    }

    public String getBaseFile() {
        return baseFile;
    }

    public String getOutput() {
        return output;
    }

    public List<String> getExcelFiles() {
        return excelFiles;
    }

    public List<String> getExcelSheets() {
        return excelSheets;
    }

    public String getDir() {
        return dir;
    }
}
