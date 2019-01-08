package io.mfj.textricator.gui

import io.mfj.textricator.Textricator
import io.mfj.textricator.extractor.TextExtractorFactory
import io.mfj.textricator.extractor.TextExtractorOptions
import io.mfj.textricator.text.Text
import io.mfj.textricator.text.toPageFilter

import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleStringProperty

import tornadofx.*

class ExtractController:Controller() {

	companion object {
		const val MAX_ROWS = 1000
	}


	val mainController:TextricatorGuiController by inject()

	val parsers = TextExtractorFactory.extractorNames
			.filter { it.startsWith( "pdf.") }
			.sorted()
			.toMutableList()
			.observable()

	val parserNameProperty = SimpleStringProperty(parsers.firstOrNull())
	val parserName by parserNameProperty

	val pagesProperty = SimpleStringProperty("1-")
	val pages by pagesProperty

	val maxRowDistanceProperty = SimpleFloatProperty(0f)
	val maxRowDistance by maxRowDistanceProperty

	val boxPrecisionProperty = SimpleFloatProperty(0f)
	val boxPrecision by boxPrecisionProperty

	val boxIgnoreColorsProperty = SimpleStringProperty("")
	val boxIgnoreColors by boxIgnoreColorsProperty

	val extractData = mutableListOf<Text>().observable()

	fun extract(): Boolean {

		val options = TextExtractorOptions(
				boxPrecision = boxPrecision,
				boxIgnoreColors = boxIgnoreColors.split(",").toSet() )

		return mainController.file!!.inputStream().use { fileInput ->
			TextExtractorFactory.getFactory(parserName)
					.create( fileInput, options )
					.use { extractor ->
						val texts = Textricator.extractText(
								extractor = extractor,
								pageFilter = pages.toPageFilter(),
								maxRowDistance = maxRowDistance )
						val list = texts.take(MAX_ROWS+1).toList()
						extractData.setAll(list.take(MAX_ROWS))
						list.size > MAX_ROWS
					}
		}
	}

}