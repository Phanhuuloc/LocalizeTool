package loc;

import loc.find.FindingApp;
import loc.localize.LocalizeApp;

import java.io.IOException;

/**
 * Created by localadmin on 4/21/2017.
 */
public class App {
    public static void main(String[] args1) {
        String args[] = {"-f"};
        App app = new App();
        if ("-f".equals(args[0])) {
            app.runFind();
        } else if ("-r".equals(args[0])) {
            app.runLocalize();
        } else {
            System.exit(1);
        }
    }

    private void runLocalize() {
        LocalizeApp.runApp();
    }

    private void runFind() {
        try {
            FindingApp.runApp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
