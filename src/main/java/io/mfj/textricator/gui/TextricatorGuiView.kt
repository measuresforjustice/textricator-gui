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

import javafx.scene.layout.Priority
import javafx.stage.FileChooser

import tornadofx.*

class TextricatorGuiView: View() {

	val controller:TextricatorGuiController by inject()

	val taskStatus:TaskStatus by inject()


	init {
		title = "Textricator"
	}

	override fun onBeforeShow() {
		super.onBeforeShow()
		centerOnPrimaryScreen()
	}

	override val root = borderpane {
		top =
				vbox {
					menubar {
						useSystemMenuBarProperty().set(true)
						menu("File") {
							item("Quit") {
								action {
									System.exit(0)
								}
							}
						}
						menu("Help") {
							item("About") {
								action {
									find(AboutView::class).openModal(
											escapeClosesWindow = true
									)
								}
							}
						}
					}
					/*
					toolbar {
						button("do stuff") {
							action {
								controller.refreshRoot()
							}
						}
					}
					*/
				}
		center = borderpane {
			top = hbox {
				label( "PDF:" )
				textfield(controller.fileProperty,FileStringConverter) {
					isEditable = false
					hgrow = Priority.ALWAYS
				}
				button("...") {
					action {
						val file = chooseFile(
								title = "Select PDF",
								filters = arrayOf( FileChooser.ExtensionFilter("PDF","*.pdf") ),
								mode = FileChooserMode.Single
						).firstOrNull()
						if ( file != null ) {
							controller.fileProperty.set(file)
						}
					}

				}
			}
			center = tabpane {
				tab("Extract") {
					isClosable = false
					add<ExtractView>()
				}
				tab("Parse Table") {
					isClosable = false
					add<ParseTableView>()
				}
				tab("Parse Form") {
					isClosable = false
					add<ParseFormView>()
				}
			}

		}
		bottom = stackpane {
			visibleWhen { taskStatus.running }
			progressbar(taskStatus.progress) {
				useMaxWidth = true
			}
			label(taskStatus.message) {
				useMaxWidth = true
				paddingLeft = 5
			}
		}
	}

}

