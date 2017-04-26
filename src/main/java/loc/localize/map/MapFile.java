package loc.localize.map;

import loc.localize.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by localadmin on 4/19/2017.
 */
public class MapFile {

    private static final Map<String, String> initMap = new HashMap<String, String>();
    private final String fileName;

    public MapFile(String fileName) {
        this.fileName = fileName;
    }

    public Map getInitMap() {
        readFromFile(fileName);
        return initMap;
    }

    public static void readFromFile(String fileName) {
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.filter(s -> s.contains("=") && s.split("=").length > 1)
                    .map(s -> {
                        String[] tmp = s.split("=");
                        String key = FileUtils.cleanString(tmp[1]);
                        initMap.put(key, key);
                        return key;
                    })
                    .forEach(s -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
