package converter

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class DimensionConverter(var filePath: String = "", var targetFolderPath: String = "") {

    private var resourceFile: Document? = null
    private var dpMultiplier: Double = 0.0
    private var dpDivider: Double = 0.0
    private var spMultiplier: Double = 0.0
    private var spDivider: Double = 0.0

    fun getUpdatedFile(_dpMultiplier: String, _dpDivider: String, _spMultiplier: String, _spDivider: String): String? {
        return getUpdatedFile(
            _dpMultiplier.toDoubleOrNull() ?: 1.0,
            _dpDivider.toDoubleOrNull() ?: 1.0,
            _spMultiplier.toDoubleOrNull() ?: 1.0,
            _spDivider.toDoubleOrNull() ?: 1.0
        )
    }

    fun getUpdatedFile(_dpMultiplier: Double, _dpDivider: Double, _spMultiplier: Double, _spDivider: Double): String? {
        readXmlFile()
        dpMultiplier = _dpMultiplier
        dpDivider = _dpDivider
        spMultiplier = _spMultiplier
        spDivider = _spDivider
        convert()
        return saveToFile()
    }

    private fun readXmlFile() {
        try {
            val fXmlFile = File(filePath)
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            resourceFile = dBuilder.parse(fXmlFile)
            resourceFile!!.documentElement.normalize()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }

    }

    private fun convert() {
        val nodeList = resourceFile!!.documentElement.childNodes
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                var dimenValue = node.textContent
                var dimenUnit = ""
                if (!node.textContent.contains("@dimen")) {
                    var value: Double
                    try {
                        value = java.lang.Double.valueOf(dimenValue)
                    } catch (e: NumberFormatException) {
                        dimenUnit = dimenValue.substring(dimenValue.length - 2, dimenValue.length)
                        dimenValue = dimenValue.substring(0, dimenValue.length - 2)
                        value = java.lang.Double.valueOf(dimenValue)
                    }
                    val multiplier = if (dimenUnit.equals("sp", true)) {
                        spMultiplier
                    } else dpMultiplier
                    val divider = if (dimenUnit.equals("sp", true)) {
                        spDivider
                    } else dpDivider
                    val newValue = value * multiplier / divider
                    node.textContent = (if (newValue % 1 == 0.0) newValue.toInt().toString() else String.format(
                        "%.2f",
                        newValue
                    )) + dimenUnit
                }
            }
        }
    }

    private fun saveToFile(): String? {
        var transformer: Transformer? = null
        try {
            transformer = TransformerFactory.newInstance().newTransformer()
            val pathname = "$targetFolderPath/dimens.xml"
            val output = StreamResult(File(pathname))
            val input = DOMSource(resourceFile)
            transformer!!.transform(input, output)
            return pathname
        } catch (e: TransformerException) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        val INSTANCE = DimensionConverter()
    }
}
