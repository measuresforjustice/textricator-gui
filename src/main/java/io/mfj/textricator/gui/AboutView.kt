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
			label("Copyright ${Version.copyrightYear} Measures for Justice Institute.")
			label("Licensed under the GNU Affero General Public License, Version 3.")
			label("(Loaded modules may be licensed differently.)")
			label("Source code is available at ${Version.sourceLocation}")
		}
		setOnMouseClicked { event -> close() }
		onDoubleClick { close() }
	}
}

