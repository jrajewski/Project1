/**
  * Created by ringo on 10/30/2016.
  */
package edu.towson.cosc.cosc455.jrajewski.project1
import java.awt.Desktop
import java.io._
import scala.io.Source

object Compiler{

  var currentToken : String = ""
  var fileContents : String = ""
  var fileArray: Array[Char] = Array()
  var tokenList = List()
  var tokenCount: Int = 0

  val Scoping = new MySematicAnalyzer
  val Scanner = new MyLexicalAnalyzer
  val Parser = new MySyntaxAnalyzer

  def main(args: Array[String]): Unit = {
    var tokenCount: Int = 0

    checkFile(args)
    readFile(args(0))
    fileArray = fileContents.toCharArray
    Scanner.currentChar = 0

    //token array is the tokenized file
    //tokens are stored in an array
    //tokenizes while there is a next character
    while(fileArray(Scanner.currentChar + 1) != Nil) {
      Scanner.getNextToken()
      Scanner.token :: tokenList
    }

    Parser.gittex()

    Scoping.initStack()
    Scoping.variableRes()

    var count: Int = 0
    val output = new File(args(0) + ".html")
    val bw = new BufferedWriter(new FileWriter(output))
    while(Scoping.convertStack(count) != Nil) {
      bw.write(Scoping.convertStack(count))
      count += 1
    }
    bw.close()

    openHTMLFileInBrowser(args(0))

  }

  def readFile(file : String) = {
    val source = scala.io.Source.fromFile(file)
    fileContents = try source.mkString finally source.close()
  }

  def checkFile(args : Array[String]) = {
    if (args.length != 1) {
      println("USAGE ERROR: wrong number of arguments.")
      System.exit(1)
    }
    else if (! args(0).endsWith(".mkd")) {
      println("USAGE ERROR: wrong file extension.")
      System.exit(1)
    }
  }

  def openHTMLFileInBrowser(htmlFileStr : String) = {
    val file : File = new File(htmlFileStr.trim)
    println(file.getAbsolutePath)
    if (!file.exists())
      sys.error("File " + htmlFileStr + " does not exist.")

    try {
      Desktop.getDesktop.browse(file.toURI)
    }
    catch {
      case ioe: IOException => sys.error("Failed to open file:  " + htmlFileStr)
      case e: Exception => sys.error("He's dead, Jim!")
    }
  }



}