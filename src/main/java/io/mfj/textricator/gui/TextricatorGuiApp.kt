/*
This file is part of Textricator GUI.
Copyright 2019 Measures for Justice Institute.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU Affero General Public License version 3 as published by the
Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along
with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package io.mfj.textricator.gui

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*

class TextricatorGuiApp:App(TextricatorGuiView::class) {

	companion object {
		val logo512 = image("textricator-logo-512.png")
		val logo256 = image("textricator-logo-256.png")

		private fun image(name: String): Image =
				TextricatorGuiApp::class.java
						.getResourceAsStream("/io/mfj/textricator/gui/$name")
						.use { Image(it) }
	}

	override fun start(stage: Stage) {
		super.start(stage)

		stage.icons += logo512
	}
}
