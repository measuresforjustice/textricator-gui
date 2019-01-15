package io.mfj.textricator.gui

import io.mfj.textricator.Textricator
import io.mfj.textricator.record.Record
import io.mfj.textricator.record.RecordModel
import io.mfj.textricator.record.RecordType
import io.mfj.textricator.table.config.TableParseConfigUtil

import java.io.File

import javafx.beans.property.SimpleObjectProperty

import tornadofx.*

class ParseTableController:Controller() {

	companion object {
		const val MAX_ROWS = 1000
	}

	val mainController:TextricatorGuiController by inject()

	val configFileProperty = SimpleObjectProperty<File>()

	val headers = mutableListOf<String>()
	val data = mutableListOf<Array<String>>().observable()

	fun parse(): Boolean {

		try {
			val config = TableParseConfigUtil.parseYaml(configFileProperty.get())

			headers.clear()
			headers.addAll( createHeader(config) )

			val inputFormat = mainController.file!!.extension.toLowerCase()

			val list = mainController.file!!.inputStream().use { input ->
				Textricator.getExtractor( input, inputFormat, config ).use { extractor ->
					Textricator.parseTable( extractor, config )
							.map { record -> recordToArray(config,record) }
							.flatten()
							.take(MAX_ROWS+1)
							.toList()
				}
			}
			data.setAll(list.take(MAX_ROWS))
			return list.size > MAX_ROWS
		} catch ( e:Exception ) {
			data.clear()
			throw e
		}
	}


	private fun createHeader( config:RecordModel ): List<String> {
		val header:MutableList<String> = mutableListOf()

		fun proc( recordType: RecordType) {
			recordType.valueTypes.forEach { valueTypeId ->
				val valueType = config.valueTypes[valueTypeId]
				if ( valueType?.include ?: true ) {
					val label = valueType?.label ?: valueTypeId
					header.add(label)
				}
			}
			recordType.children
					.map { config.recordTypes[it] ?: throw Exception("missing type ${it}") }
					.forEach { childRecordType ->
						proc( childRecordType )
					}
		}
		val rootRecordType = config.recordTypes[config.rootRecordType] ?: throw Exception("missing type ${config.rootRecordType}")
		proc(rootRecordType)

		return header
	}

	private fun recordToArray( config:RecordModel, record: Record ): List<Array<String>> {

		val rows:MutableList<Array<String>> = mutableListOf()

		val map:MutableMap<RecordType, Record> = mutableMapOf()

		fun pr(rec:Record) {
			val type = config.recordTypes[rec.typeId] ?: throw Exception( "Missing type ${rec.typeId}" )
			map[type] = rec

			if ( rec.isLeaf ) {
				rows.add( createRow(config,map) )
			} else {
				rec.children.values.forEach { it.forEach { pr(it) } }
			}

			map.remove(type)
		}

		pr(record)

		return rows
	}

	private fun createRow( config:RecordModel, map:Map<RecordType, Record> ): Array<String> {

		val row:MutableList<String> = mutableListOf()

		var pageNumber:Int? = null
		var pageNumberPriority:Int = -1

		fun printType( recordType:RecordType) {
			val rec = map[recordType]

			if ( ( rec != null ) && ( pageNumber == null || recordType.pagePriority > pageNumberPriority ) ) {
				pageNumber = rec.pageNumber
				pageNumberPriority = recordType.pagePriority
			}

			recordType.valueTypes.forEach { valueTypeId ->
				val valueType = config.valueTypes[valueTypeId]
				if ( valueType?.include ?: true ) {
					row.add( rec?.values?.get(valueTypeId) ?: "" )
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
