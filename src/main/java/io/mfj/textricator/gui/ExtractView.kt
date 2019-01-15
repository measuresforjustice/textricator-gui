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

import io.mfj.textricator.text.Text

import javafx.scene.control.Alert
import javafx.scene.layout.Priority

import tornadofx.*

class ExtractView: View() {

	val controller:ExtractController by inject()

	override val root =
			squeezebox {
				fold("Options") {
					form {
						vgrow = Priority.ALWAYS
						fieldset {
							field("Parser") {
								combobox<String>(controller.parserNameProperty) {
									items = controller.parsers
								}
							}
							field("Pages") {
								textfield(controller.pagesProperty)
							}
							field("Max Row Distance") {
								textfield(controller.maxRowDistanceProperty) {
								}
							}
							field("Box Precision") {
								textfield(controller.boxPrecisionProperty) {
								}
							}
							field("Box Ignore Colors") {
								textfield(controller.boxIgnoreColorsProperty)
							}
						}
						button("Extract") {
							action {
								runAsync {
									controller.extract()
								} ui { truncated ->
									isExpanded = false
									if ( truncated ) {
										alert(
												type = Alert.AlertType.INFORMATION,
												title = "Truncated",
												header = "Only the first ${ExtractController.MAX_ROWS} texts are shown."
										)
									}
								}
							}
						}
					}
				}
				fold("Extracted Text") {
					tableview(controller.extractData) {
						readonlyColumn("page",Text::pageNumber)
						readonlyColumn("ulx",Text::ulx)
						readonlyColumn("uly",Text::uly)
						readonlyColumn("lrx",Text::lrx)
						readonlyColumn("lry",Text::lry)
						readonlyColumn("text",Text::content)
						readonlyColumn("font",Text::font)
						readonlyColumn("font size",Text::fontSize)
						readonlyColumn("color",Text::color)
						readonlyColumn("bgcolor",Text::backgroundColor)
						controller.extractData.onChange {
							runLater {
								println("resize")
								resizeColumnsToFitContent()
							}
						}
					}
				}
				onlyOneOpen()
			}

}