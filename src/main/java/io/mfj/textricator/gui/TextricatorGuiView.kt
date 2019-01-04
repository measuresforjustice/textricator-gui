package io.mfj.textricator.gui

import io.mfj.textricator.extractor.TextExtractorFactory
import io.mfj.textricator.text.Text
import javafx.scene.control.Alert
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import tornadofx.*

class TextricatorGuiView: View() {

	val controller:TextricatorGuiController by inject()

	val taskStatus:TaskStatus by inject()

	private var extractOptionsFold:TitledPane by singleAssign()
	private var extractTableFold:TitledPane by singleAssign()

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
				label( "PDF" )
				textfield(controller.fileProperty,FileStringConverter) {
					isEditable = false
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
					squeezebox {
						extractOptionsFold = fold("Options") {
							isExpanded = true
							form {
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
														header = "Only the first ${TextricatorGuiController.MAX_ROWS} texts are shown."
												)
											}
										}
									}
								}
							}
						}
						extractTableFold = fold("Extracted Text") {
							isExpanded = false
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
				tab("Parse Table") {
					isClosable = false
					label("TODO")
				}
				tab("Parse Form") {
					isClosable = false
					label("TODO")
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

