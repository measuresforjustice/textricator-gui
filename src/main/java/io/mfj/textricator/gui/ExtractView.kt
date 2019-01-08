package io.mfj.textricator.gui

import io.mfj.textricator.text.Text
import javafx.scene.control.Alert
import javafx.scene.control.TitledPane
import javafx.scene.layout.Priority
import tornadofx.*

class ExtractView: View() {

	val controller:ExtractController by inject()

	private var extractOptionsFold: TitledPane by singleAssign()
	private var extractTableFold:TitledPane by singleAssign()

	override val root =
			squeezebox {
				extractOptionsFold = fold("Options") {
					isExpanded = true
					expandedProperty().onChange {
						extractTableFold.isExpanded = !isExpanded
					}
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
									extractOptionsFold.isExpanded = false
									extractTableFold.isExpanded = true
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
				extractTableFold = fold("Extracted Text") {
					isExpanded = false
					expandedProperty().onChange {
						extractOptionsFold.isExpanded = !isExpanded
					}
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
			}

}