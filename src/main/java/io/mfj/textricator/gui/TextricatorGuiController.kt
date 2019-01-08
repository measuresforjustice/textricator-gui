package io.mfj.textricator.gui

import java.io.File

import javafx.beans.property.SimpleObjectProperty

import tornadofx.*

class TextricatorGuiController: Controller() {

	val fileProperty = SimpleObjectProperty<File?>()
	val file by fileProperty


}
