package loc.localize.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by localadmin on 4/19/2017.
 */
public class FileUtils {

    public static File readResourceFile(Class clazz, String fileName) throws UnsupportedEncodingException {
        ClassLoader classLoader = clazz.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile().replace("%20"," "));
    }

    public static List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();
        try (InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    public static String buildFile(String dir, String fileName) {
        return dir + fileName;
    }

    public static String cleanString(String s) {
        return s.trim().replace("\t", "");
    }
}
