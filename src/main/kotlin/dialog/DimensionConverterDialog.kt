package dialog

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import converter.DimensionConverter
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class DimensionConverterDialog(var project: Project? = null) : DialogWrapper(project) {

    var fileChooserDescriptor: FileChooserDescriptor
    var folderChooserDescriptor: FileChooserDescriptor
    var rows = 0
    var logger = Logger.getInstance("Dimension Converter")
    val dimensionConverter = DimensionConverter.INSTANCE;


    lateinit var inputFileTextField: TextFieldWithBrowseButton
    lateinit var dpMultiplierInput: JTextField
    lateinit var dpDividerInput: JTextField
    lateinit var spMultiplierInput: JTextField
    lateinit var spDividerInput: JTextField
    lateinit var targetFolderField: TextFieldWithBrowseButton

    init {
        title = "Dimension Converter"
        fileChooserDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        folderChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val layout = GridLayout(0, 1)
        val panel = JPanel(layout)

        val inputFileRow = JPanel(FlowLayout())
        var label = JLabel("Input File")
        inputFileRow.add(label)
        inputFileTextField = TextFieldWithBrowseButton()
        inputFileTextField.addBrowseFolderListener(null, null, project, fileChooserDescriptor)
        inputFileRow.add(inputFileTextField)
        layout.rows = rows++
        panel.add(inputFileRow)

        val dpConverterPanel = JPanel(FlowLayout())
        label = JLabel("Dp Multiplier")
        dpConverterPanel.add(label)
        dpMultiplierInput = JTextField()
        dpConverterPanel.add(dpMultiplierInput)
        label = JLabel(" / ")
        dpConverterPanel.add(label)
        dpDividerInput = JTextField()
        dpConverterPanel.add(dpDividerInput)
        panel.add(dpConverterPanel)

        val spConverterPanel = JPanel(FlowLayout())
        label = JLabel("Sp Multiplier")
        spConverterPanel.add(label)
        spMultiplierInput = JTextField()
        spConverterPanel.add(spMultiplierInput)
        label = JLabel(" / ")
        spConverterPanel.add(label)
        spDividerInput = JTextField()
        spConverterPanel.add(spDividerInput)
        panel.add(spConverterPanel)

        label = JLabel("Target Folder")
        targetFolderField = TextFieldWithBrowseButton()
        targetFolderField.addBrowseFolderListener(null, null, project, folderChooserDescriptor)
        val targetFolderPanel = JPanel(FlowLayout())
        targetFolderPanel.add(label)
        targetFolderPanel.add(targetFolderField)

        panel.add(targetFolderPanel)

        return panel
    }

    override fun doOKAction() {
        super.doOKAction()
        dimensionConverter.filePath = inputFileTextField.text
        dimensionConverter.targetFolderPath = targetFolderField.text
        val pathUrl = dimensionConverter.getUpdatedFile(dpMultiplierInput.text,
            dpDividerInput.text, spMultiplierInput.text, spDividerInput.text)
        println(pathUrl)
    }
}