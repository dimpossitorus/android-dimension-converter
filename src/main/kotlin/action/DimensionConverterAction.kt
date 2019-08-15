package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import dialog.DimensionConverterDialog

class DimensionConverterAction : AnAction() {

    val log = Logger.getInstance(this@DimensionConverterAction::class.java.canonicalName)
    override fun actionPerformed(e: AnActionEvent) {
        openDimensionConverterDialog()
    }

    private fun openDimensionConverterDialog() {
        val dialog = DimensionConverterDialog()
        if (dialog.showAndGet()) {
            log.debug("Ok clicked")
        } else {
            log.debug("Cancel Clicked")
        }
    }
}