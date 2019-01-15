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

