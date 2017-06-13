package loc.localize.util


import org.apache.commons.configuration2.XMLConfiguration
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.apache.commons.configuration2.ex.ConfigurationException
import org.apache.commons.configuration2.io.FileHandler
import java.util.*

/**
 * Created by localadmin on 4/20/2017.
 */
class Config {
    var dir: String? = null
        private set
    var baseFile: String? = null
        private set
    var output: String? = null
        private set
    private val excelFiles = ArrayList<String>()
    var excelSheets: List<String> = ArrayList()
        private set
    private var positions: List<Int> = ArrayList()

    init {
        getConfig()
    }

    private fun getConfig() {
        val configs = Configurations()
        try {
            val `in` = javaClass.classLoader
                    .getResourceAsStream("localize.xml")

            val config = BasicConfigurationBuilder(XMLConfiguration::class.java)
                    .configure(Parameters().xml())
                    .configuration
            val fh = FileHandler(config)
            fh.load(`in`)
            //            Configuration config = configs.xml(fh);
            dir = config.getString("localize.file[@dir]")
            baseFile = dir!! + config.getString("localize.file[@in]")
            output = dir!! + config.getString("localize.file[@out]")
            config.getList(String::class.java, "localize.steps.step[@e-file]")
                    .forEach { s -> excelFiles.add(dir!! + s) }
            excelSheets = config.getList(String::class.java, "localize.steps.step[@e-sheet]")
            positions = config.getList(Int::class.java, "localize.steps.step[@pos]")
        } catch (cex: ConfigurationException) {
            // Something went wrong
        }

    }

    fun getExcelFiles(): List<String> {
        return excelFiles
    }
}
