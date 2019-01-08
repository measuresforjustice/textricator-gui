package io.mfj.textricator.gui

import tornadofx.*

class AboutView:View() {
	init {
		title = "About Textricator GUI"
	}
	override val root = hbox {
		imageview( TextricatorGuiApp.logo256 )

		vbox {
			label("Textricator version: ${io.mfj.textricator.Version.version}")
			label("Textricator GUI version: ${Version.version}")
		}
		setOnMouseClicked { event -> close() }
		onDoubleClick { close() }
	}
}

