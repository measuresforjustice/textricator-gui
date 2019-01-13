package io.mfj.textricator.gui

import io.mfj.textricator.Textricator
import io.mfj.textricator.table.config.TableParseConfigUtil

import java.io.File
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

import tornadofx.*

class ParseTableController:Controller() {

	companion object {
		const val MAX_ROWS = 1000
	}

	val mainController:TextricatorGuiController by inject()

	val configFileProperty = SimpleObjectProperty<File>()

	val logProperty = SimpleStringProperty("")

	val headers = mutableListOf<String>()
	val data = mutableListOf<Array<String>>().observable()

	fun parse(): Boolean {

		val config = TableParseConfigUtil.parseYaml(configFileProperty.get())

		val inputFormat = mainController.file!!.extension.toLowerCase()

		val list = mainController.file!!.inputStream().use { input ->
			Textricator.getExtractor( input, inputFormat, config ).use { extractor ->
				Textricator.parseTable(extractor,config)
						.map { record -> arrayOf<String>() }
						.take(MAX_ROWS+1)
						.toList()
				}
		}
		data.setAll(list.take(MAX_ROWS))
		return list.size > MAX_ROWS
	}

}