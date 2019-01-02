package io.mfj.textricator.gui

import java.io.File

import org.docopt.Docopt

import tornadofx.*

object TextricatorGuiMain {

	private val help = """
		Textricator GUI.

		Usage:
			textricator-gui
			textricator-gui --version
			textricator-gui -h | --help

		Options:
			""".trimIndent().trim()

	@JvmStatic
	fun main( args:Array<String> ) {
		val opts = Docopt(help)
				.withHelp(true)
				.withExit(true)
				.parse(args.toList())

		when {
			opts.b("--version") -> {
				println( "Textricator GUI version: ${Version.version}" )
				println( "Textricator version: ${io.mfj.textricator.Version.version}" )
			}
			else -> {
				launch<TextricatorGuiApp>()
			}
		}
	}

	private fun Map<String,Any>.s(key:String):String? = get(key)?.toString()

	private fun Map<String,Any>.b(key:String,default:Boolean=false):Boolean = get(key)?.toString()?.toBoolean() ?: default

}

