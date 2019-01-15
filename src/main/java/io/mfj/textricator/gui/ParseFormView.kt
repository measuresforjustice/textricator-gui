package io.mfj.textricator.gui

import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TextArea
import javafx.scene.control.TitledPane
import javafx.scene.layout.Priority
import javafx.stage.FileChooser

import tornadofx.*

class ParseFormView:View() {

	val controller:ParseFormController by inject()

	var dataFold:TitledPane by singleAssign()
	var logTextArea: TextArea by singleAssign()
	var logFold:TitledPane by singleAssign()

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
												filters = arrayOf( FileChooser.ExtensionFilter("Textricator Form Parse Yaml","*.yml") ),
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
									runLater {
										logFold.isExpanded = true
									}
									controller.parse(logTextArea)
								} ui { truncated ->
									if ( controller.data.isNotEmpty() ) {
										dataFold.isExpanded = true
									} else {
										logFold.isExpanded = true
									}
									if ( truncated ) {
										alert(
												type = Alert.AlertType.INFORMATION,
												title = "Truncated",
												header = "Only the first ${ParseFormController.MAX_ROWS} texts are shown."
										)
									}
								}
							}
						}
					}
				}
				logFold = fold("Log") {
					logTextArea = textarea {
						vgrow = Priority.ALWAYS
						hgrow = Priority.ALWAYS
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