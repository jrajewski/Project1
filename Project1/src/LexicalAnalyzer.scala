package edu.towson.cosc.cosc455.jrajewski.project1

trait LexicalAnalyzer {
  def addChar (c: Char): Unit

  def getChar (): Char

  def getNextToken (): Unit

  def lookup (): Boolean

}