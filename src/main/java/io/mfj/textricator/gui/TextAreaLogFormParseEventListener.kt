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

import io.mfj.textricator.form.FormParseEventListener
import io.mfj.textricator.form.StateValue
import io.mfj.textricator.text.Text

import javafx.scene.control.TextArea
import tornadofx.runLater
import java.lang.StringBuilder

class TextAreaLogFormParseEventListener(private val textarea: TextArea): FormParseEventListener {

	private val bufferSize = 2048

	private val buffer = StringBuilder()

	private fun appendText( text:String ) {
		buffer.append(text)
		if ( buffer.length > bufferSize ) {
			flush()
		}
	}
	private fun flush() {
		textarea.appendText( buffer.toString() )
		buffer.clear()
	}

	override fun onText(text:Text) = runLater {
		appendText("============================\n")
		appendText("text: \"${text.content}\"\n")
		appendText("\tpageNumber: ${text.pageNumber} ul:[ ${text.ulx} , ${text.uly} ] lr: [ ${text.lrx} , ${text.lry} ]\n")
		appendText("\tfont: ${text.font} - ${text.fontSize}\n")
		appendText("\tbgcolor: ${text.backgroundColor}\n")
	}

	override fun onHeader(text:Text) = runLater {
		appendText("\tpart of header. skip\n")
	}

	override fun onFooter(text:Text) = runLater {
		appendText("\tpart of footer. skip\n")
	}

	override fun onLeftMargin(text:Text) = runLater {
		appendText("\tpart of left gutter. skip\n")
	}

	override fun onRightMargin(text:Text) = runLater {
		appendText("\tpart of rigth gutter. skip\n")
	}

	override fun onCheckTransition(currentState:String, condition:String, nextState:String) = runLater {
		appendText("\tcheck transition \"${condition}\" (\"${currentState}\" -> \"${nextState}\")...\n")
	}

	override fun onNoPrevious(source:String) = runLater {
		appendText("\t\tno previous [${source}]\n")
	}

	override fun onCheckTransition(currentState:String, condition:String, nextState:String, match:Boolean, message:String?) = runLater {
		appendText("\t\t${match} ${if (message != null) " (${message})" else "" }\n")
	}

	override fun onCheckCondition(source:String, description:String, match:Boolean) = runLater {
		appendText("\t\tcheck condition [${source}] ${description} : ${match}\n" )
	}

	override fun onPageStateChange(page:Int, state:String) = runLater {
		appendText( "State = ${state} (page:${page} reset)\n" )
	}

	override fun onStateChange(page:Int, state:String) = runLater {
		appendText( "State = ${state} (page:${page})\n")
	}

	override fun onVariableSet(currentState:String, name:String, value:String?) = runLater {
		appendText( "Variable ${name} = ${if ( value != null ) "\"${value}\"" else "null" }\n")
	}

	override fun onFsmEnd() = runLater {
		appendText( "fsm end\n")
	}

	override fun onRecordsEnd() = runLater {
		appendText( "records end\n")
		flush()
	}

	override fun onStateValue(sv:StateValue) = runLater {
		appendText( "parse StateValue: ${sv.stateId} : ${sv.values}\n")
	}

	override fun onNewRecord(typeId:String) = runLater {
		appendText( "\tnew ${typeId} record\n")
	}

	override fun onRecordAppend(typeId:String) = runLater {
		appendText( "\tadd to ${typeId} record\n")
	}
}
