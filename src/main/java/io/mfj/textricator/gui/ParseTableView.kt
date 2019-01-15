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

import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TitledPane
import javafx.scene.layout.Priority
import javafx.stage.FileChooser

import tornadofx.*

class ParseTableView:View() {

	val controller:ParseTableController by inject()

	var dataFold:TitledPane by singleAssign()

	override val root =
			squeezebox {
				fold("Options") {
					form {
						vgrow = Priority.ALWAYS
						fieldset {
							field("Config File") {
								textfield(controller.configFileProperty,FileStringConverter) {
									isEditable = false
									hgrow = Priority.ALWAYS
								}
								button("...") {
									action {
										val file = chooseFile(
												title = "Config file",
												filters = arrayOf( FileChooser.ExtensionFilter("Textricator Table Parse Yaml","*.yml") ),
												mode = FileChooserMode.Single
										).firstOrNull()
										if ( file != null ) {
											controller.configFileProperty.set(file)
										}
									}
								}
							}
						}
						button("Parse") {
							action {
								runAsync {
									controller.parse()
								} ui { truncated ->
									dataFold.isExpanded = true
									if ( truncated ) {
										alert(
												type = Alert.AlertType.INFORMATION,
												title = "Truncated",
												header = "Only the first ${ParseTableController.MAX_ROWS} texts are shown."
										)
									}
								}
							}
						}
					}
				}
				dataFold = fold("Data") {
					label("nothing parsed yet")
					controller.data.onChange {
						runLater {
							if ( controller.data.isNotEmpty() ) {
								this@fold.content = tableview(controller.data) {
									controller.headers.forEachIndexed { index, header ->
										column(title = header,
												valueProvider = { cellDataFeatures: TableColumn.CellDataFeatures<Array<String>, String> ->
													cellDataFeatures.value.get(index).toProperty()
												})
									}
								}
							} else {
								this@fold.content = label("No data")
							}
						}
					}
				}
				onlyOneOpen()
			}

}
