package io.mfj.textricator.gui

import java.awt.GraphicsEnvironment
import java.io.File

import javafx.scene.control.TitledPane
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.util.StringConverter

import tornadofx.*

/**
 * Call from [UIComponent.onBeforeShow].
 *
 * @carlw in #tornadofx says it should be in [UIComponent.onDock] instead, but when I do that, the window flashes on the
 * wrong screen before showing on the right one.
 */
fun UIComponent.centerOnPrimaryScreen() {
	( currentStage ?: primaryStage ).centerOnPrimaryScreen()
}

fun Stage.centerOnPrimaryScreen() {
	val bounds = getPrimaryScreen().bounds

	// get on the right screen
	x = bounds.minX
	y = bounds.minY

	// center
	centerOnScreen()
}

fun getPrimaryScreen():Screen {

	// AWT gets the primary monitor correctly
	val primaryBounds = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.defaultScreenDevice
			.defaultConfiguration
			.bounds

	// find the JavaFX screen by matching bounds to the primary AWT device
	return Screen
			.getScreens()
			.find { screen ->
				screen.bounds.minX == primaryBounds.minX
						&& screen.bounds.minY == primaryBounds.minY
						&& screen.bounds.width == primaryBounds.width.toDouble()
						&& screen.bounds.height == primaryBounds.height.toDouble()
			} ?: (
			// no match, use the (possibly wrong) primary JavaFX screen
			Screen.getPrimary()
					.apply {
						System.err.println( "No JavaFX screen matched the primary AWT screen, returning the primary JavaFX screen." )
					}
			)
}

object FileStringConverter:StringConverter<File?>() {
	override fun fromString(path: String?): File? = path?.let { File(it) }
	override fun toString(file: File?): String = file?.absolutePath ?: ""
}

fun SqueezeBox.onlyOneOpen() {
	var enabled = true
	var lastOpen:TitledPane? = null
	childrenUnmodifiable
			.asSequence()
			.filterIsInstance<TitledPane>()
			.forEachIndexed { index, fold ->
				fold.isExpanded = ( index == 0 )
				fold.expandedProperty().onChange {
					if ( enabled ) {
						enabled = false
						try {
							val open =
									if ( fold.isExpanded ) {
										fold
									} else {
										lastOpen
												?: childrenUnmodifiable
														.asSequence()
														.filterIsInstance<TitledPane>()
														.filter { it !== fold }
														.first()
									}
							childrenUnmodifiable
									.asSequence()
									.filterIsInstance<TitledPane>()
									.forEach {
										if ( it.isExpanded && it !== open ) lastOpen = it
										it.isExpanded = ( it === open )
									}
						} finally {
							enabled = true
						}
					}
				}
			}
}
