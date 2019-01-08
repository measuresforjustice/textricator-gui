package io.mfj.textricator.gui

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*

class TextricatorGuiApp:App(TextricatorGuiView::class) {

	companion object {
		val logo512 = image("textricator-logo-512.png")
		val logo256 = image("textricator-logo-256.png")

		fun image( name:String ): Image = TextricatorGuiApp::class.java.getResourceAsStream("/io/mfj/textricator/gui/${name}").use { Image(it) }
	}

	override fun start(stage: Stage) {
		super.start(stage)

		stage.icons += logo512
	}
}
