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
			opts.getBoolean("--version") -> {
				println( "Textricator GUI version: ${Version.version}" )
				println( "Textricator version: ${io.mfj.textricator.Version.version}" )
			}
			else -> {
				launch<TextricatorGuiApp>()
			}
		}
	}

	private fun Map<String,Any>.getString(key:String):String? = get(key)?.toString()

	private fun Map<String,Any>.getBoolean(key: String, default: Boolean = false): Boolean =
			get(key)?.toString()?.toBoolean() ?: default

}

