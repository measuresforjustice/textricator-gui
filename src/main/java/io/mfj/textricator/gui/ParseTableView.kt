package io.mfj.textricator.gui

import javafx.scene.control.Alert
import javafx.scene.control.TitledPane
import javafx.scene.layout.Priority
import javafx.stage.FileChooser

import tornadofx.*

class ParseTableView:View() {

	val controller:ParseTableController by inject()

	private var dataFold:TitledPane by singleAssign()

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
				fold("Log") {
					textarea(controller.logProperty) {
						vgrow = Priority.ALWAYS
						hgrow = Priority.ALWAYS
					}
				}
				dataFold = fold("Data") {
					tableview(controller.data) {
						controller.data.onChange {
							columns.clear()
							controller.headers.forEachIndexed { index, header ->
								column<Array<String>,String>(
										title=header,
										valueProvider = { it.value[index].toProperty() }
								)
							}

						}
					}
				}
				onlyOneOpen()
			}

}