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
					content = find<ExtractView>().root
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

