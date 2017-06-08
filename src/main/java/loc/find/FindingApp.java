package loc.find;


import loc.localize.parse.MSExcelParse;
import loc.localize.util.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.List;

/**
 * Created by localadmin on 4/21/2017.
 */
public class FindingApp {

    public static void runApp() throws IOException {

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print((char) (27) + "[31mInsert text to find : ");
                String input = br.readLine();

                if ("q".equals(input)) {
                    System.out.println("Exit!");
                    System.exit(0);
                } else {
                    FindingApp app = new FindingApp();
                    app.findOnBulk(input);
                }

                System.out.println((char) (27) + "[31m\n-----End of Finding text " + input + "------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void findOnBulk(String text) throws IOException {
        List<String> files = FileUtils.getResourceFiles("xlsx");
        for (String file : files) {
//            if (finderOnSingle(text, file)) break;
            finderOnSingle(text, file);
        }
    }

    public boolean finderOnSingle(String text, String fileName) throws UnsupportedEncodingException {
        File file = FileUtils.readResourceFile(FindingApp.class, fileName);
        MSExcelParse parse = new MSExcelParse(file.getAbsolutePath(), null);
        String sheetName = null;
        try {
            sheetName = parse.poiFinder(text);
        } catch (IOException e) {
            e.printStackTrace();
            sheetName = "";
        }
        return StringUtils.isNotBlank(sheetName);
    }
}
