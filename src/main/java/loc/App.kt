package loc

import loc.find.FindingApp
import loc.localize.LocalizeApp

import java.io.IOException

/**
 * Created by localadmin on 4/21/2017.
 */
class App {

    private fun runLocalize() {
        LocalizeApp.runApp()
    }

    private fun runFind() {
        try {
            FindingApp.runApp()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        @JvmStatic fun main(args1: Array<String>) {
            val args = arrayOf("-f")
            val app = App()
            if ("-f" == args[0]) {
                app.runFind()
            } else if ("-r" == args[0]) {
                app.runLocalize()
            } else {
                System.exit(1)
            }
        }
    }


}
