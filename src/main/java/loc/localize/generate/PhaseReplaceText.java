package loc.localize.generate;

import java.io.*;
import java.util.Map;

/**
 * Created by localadmin on 4/19/2017.
 */
public class PhaseReplaceText {

    private final String fileInput;
    private final String fileOutput;

    public PhaseReplaceText(String in, String out) {
        this.fileInput = in;
        this.fileOutput = out;
    }

    public void updateFile(Map<String, String> fileMap) throws IOException {
        BufferedReader inputStream = new BufferedReader(new FileReader(fileInput));
        File UIFile = new File(fileOutput);
        // if File doesnt exists, then create it
        if (!UIFile.exists()) {
            UIFile.createNewFile();
        }
        FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
        BufferedWriter outputStream = new BufferedWriter(filewriter);
        String count;
        while ((count = inputStream.readLine()) != null) {
            if (count.contains("=") && count.split("=").length > 1) {
                String tmp[] = count.split("=");
                String key = tmp[0];
                String value = tmp[1].trim().replace("\t", "");
                if (fileMap.containsKey(value)) {
                    value = fileMap.get(value);
                }
                count = key + " = " + value;
            }
            outputStream.write(count + "\n");
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
