package io.mfj.textricator.gui

import java.util.*

object Version {

	val version:String
	val copyrightYear:String
	val sourceLocation:String

	init {
		val props = Properties().apply {
			Version::class.java.getResourceAsStream( "version.properties" ).use { input ->
				load( input )
			}
		}
		version = props.getProperty("version")
		copyrightYear = props.getProperty("copyright.year")
		sourceLocation = props.getProperty("source.location")
	}

}
