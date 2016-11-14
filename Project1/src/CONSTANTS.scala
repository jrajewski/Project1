/**
  * Created by ringo on 10/30/2016.
  */
package edu.towson.cosc.cosc455.jrajewski.project1

object CONSTANTS {
  val DOCB : String = 	"\\BEGIN"
  val DOCE : String = 	"\\END"
  val TITLEB : String = "\\TITLE["
  val BRACKETE : String = "]"
  val HEADING : String = "#"
  val PARAB : String = "\\PARAB"
  val PARAE : String = "\\PARAE"
  val BOLD : String = "**"
  val ITALICS : String = "*"
  val LISTITEM : String = "+"
  val NEWLINE : String = "\\\\"
  val LINKB : String = "["
  val ADDRESSB : String = "("
  val ADDRESSE : String = ")"
  val IMAGEB : String = "!["
  val DEFB : String = "\\DEF["
  val EQSIGN : String = "="
  val USEB : String = "\\USE["
  val REQTEXT = (('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')).toSet
  val TEXT = (('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')).toSet


}
