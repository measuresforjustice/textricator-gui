package io.mfj.textricator.gui

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*

class TextricatorGuiApp:App(TextricatorGuiView::class) {
	override fun start(stage: Stage) {
		super.start(stage)

		stage.icons += TextricatorGuiApp::class.java.getResourceAsStream("/io/mfj/textricator/gui/textricator-logo-512.png").use { Image(it) }
	}
}
