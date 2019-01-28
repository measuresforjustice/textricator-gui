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

import io.mfj.textricator.Textricator
import io.mfj.textricator.form.FormParseEventListener
import io.mfj.textricator.form.config.FormParseConfigUtil
import io.mfj.textricator.record.Record
import io.mfj.textricator.record.RecordModel
import io.mfj.textricator.record.RecordType

import java.io.File

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TextArea

import tornadofx.*

class ParseFormController: Controller() {

	companion object {
		const val MAX_ROWS = 1000
	}

	val mainController: TextricatorGuiController by inject()

	val configFileProperty = SimpleObjectProperty<File>()

	val headers = mutableListOf<String>()
	val data = mutableListOf<Array<String>>().observable()

	fun parse(textarea: TextArea): Boolean {

		try {
			val config = FormParseConfigUtil.parseYaml(configFileProperty.get())
			val file = mainController.file

			headers.clear()
			headers.addAll( createHeader(config) )

			// rather than forcing the existence, you can create null safety with the safety check
			if (file != null) {
				val inputFormat = file.extension.toLowerCase()

				val eventListener:FormParseEventListener = TextAreaLogFormParseEventListener(textarea)

				val list = file.inputStream().use { input ->
					Textricator.getExtractor(input, inputFormat, config).use { extractor ->
						Textricator.parseForm( extractor, config, eventListener )
								.map { record -> recordToArray(config, record) }
								.flatten()
								.take(MAX_ROWS+1)
								.toList()
					}
				}
				// why set data to list.take(MAX_ROWS) when we already
				// take the result of parseForm() with MaxRows+1?
				data.setAll(list.take(MAX_ROWS))
			}

			// what's the motivation behind checking size for ensuring parsing validation?
			return data.size + 1 > MAX_ROWS
		} catch (e: Exception) {
			data.clear()
			throw e
		}
	}


	private fun createHeader(config: RecordModel): List<String> {
		// What's the rootRecordType is needed for?
		val rootRecordType = config.recordTypes[config.rootRecordType]
				?: throw Exception("Missing type ${config.rootRecordType}")

		return proc(rootRecordType, mutableListOf())
	}

	// nested functions are harder to test, so I pulled out the recursive function
	// the fact this was called proc did make me understand that the point was
	// the function was meant to be internal.

	// TODO - fix this recursive function
	private fun proc(recordType: RecordType, header: MutableList<String>):  MutableList<String> {
		recordType.valueTypes.forEach { valueTypeId ->
			val valueType = config.valueTypes[valueTypeId]
			// what's the motivation behind setting a null value to true?
			if (valueType.include ?: true) {
				val label = valueType.label ?: valueTypeId
				header.add(label)
			}
		}
		recordType.children
				.mapNotNull { config.recordTypes[it] }
				.forEach { childRecordType ->
					proc(childRecordType, header)
				}
	}

	private fun recordToArray(config: RecordModel, record: Record): List<Array<String>> {

		val rows: MutableList<Array<String>> = mutableListOf()
		val map: MutableMap<RecordType, Record> = mutableMapOf()

		fun pr(rec: Record) {
			val type = config.recordTypes[rec.typeId] ?: throw Exception( "Missing type ${rec.typeId}" )
			map[type] = rec

			if (rec.isLeaf) {
				rows.add( createRow(config, map) )
			} else {
				rec.children.values.forEach { it.forEach { pr(it) } }
			}

			map.remove(type)
		}

		pr(record)

		return rows
	}

	private fun createRow(config: RecordModel, map: Map<RecordType, Record>): Array<String> {

		val row: MutableList<String> = mutableListOf()

		var pageNumber: Int? = null
		var pageNumberPriority: Int = -1

		fun printType(recordType: RecordType) {
			val rec = map[recordType]

			if ((rec != null) && (pageNumber == null || recordType.pagePriority > pageNumberPriority)) {
				pageNumber = rec.pageNumber
				pageNumberPriority = recordType.pagePriority
			}

			recordType.valueTypes.forEach { valueTypeId ->
				val valueType = config.valueTypes[valueTypeId]

				if (valueType.include ?: true) {
					row.add(rec.values.get(valueTypeId) ?: "")
				}
			}
			recordType.children
					.map { config.recordTypes[it] ?: throw Exception("missing type ${it}") }
					.forEach { childRecordType ->
						printType( childRecordType )
					}
		}
		val rootRecordType = config.recordTypes[config.rootRecordType] ?: throw Exception("missing type ${config.rootRecordType}")

		printType(rootRecordType)

		return row.toTypedArray()
	}

}