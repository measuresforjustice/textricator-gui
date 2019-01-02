package io.mfj.textricator.gui

import io.mfj.textricator.extractor.TextExtractorFactory
import io.mfj.textricator.text.Text
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
					squeezebox {
						extractOptionsFold = fold("Options") {
							isExpanded = true
							form {
								fieldset {
									field("Parser") {
										combobox<String>(controller.parserNameProperty) {
											items = TextExtractorFactory.extractorNames
													.filter { it.startsWith( "pdf.") }
													.toMutableList()
													.observable()
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
									field("Box Ignore Colors")
								}
								button("Extract") {
									action {
										runAsync {
											controller.extract()
										} ui {
											extractOptionsFold.isExpanded = false
											extractTableFold.isExpanded = true
										}
									}
								}
							}
						}
						extractTableFold = fold("Extracted Text") {
							isExpanded = false
							tableview(controller.extractData) {
								readonlyColumn("ulx",Text::ulx)
								readonlyColumn("uly",Text::uly)
								readonlyColumn("lrx",Text::lrx)
								readonlyColumn("lry",Text::lry)
								readonlyColumn("text",Text::content)
							}
						}
					}
				}
				tab("Parse Table") {
					label("TODO")
				}
				tab("Parse Form") {
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

