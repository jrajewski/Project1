/**
  * Created by ringo on 11/6/2016.
  */
package edu.towson.cosc.cosc455.jrajewski.project1

class MyLexicalAnalyzer extends LexicalAnalyzer {
  var c: Char = ' '
  var token: String = ""
  var tokenList = List()
  var tokenChars = List()

  //this could be a problem
  var currentChar = 0


  override def addChar(c: Char): Unit = {
    //do the same with get char here if lookup returns true, AFTER the current token is added
    //maybe don't need to recode getting the token.. pass token here somehow/
    Compiler.fileArray(c) :: tokenChars
  }

  //returns the char of the "current char" of the file
  //need to keep track of how much of the file has already been read, hence using count variable
  def getChar(): Char = {
    Compiler.fileArray(currentChar)
  }

  override def getNextToken(): Unit = {
    c = getChar()
    tokenChars = Nil
    var check: Boolean = false
    //increments the current char in the array for next getChar()
      if (c == ' ') {
        while (c == ' ') {
          c = getChar()
          currentChar += 1
        }
      }
      else if (isSpecial(c)) {
        //return the processing to complete the token
        specialProcess()
        lookup()
      }
      else if (isText(c)) {
        textProcess(c)
        //return
      } else
        lexicalError()
  }


  override def lookup(): Boolean = {
    val c: Char = getChar()

    c match {
      //image
      case '!' =>
        if (tokenChars.toString() == CONSTANTS.IMAGEB) {
          token = CONSTANTS.IMAGEB
          return true
        }

      //for heading
      case '#' =>
        if (tokenChars.toString() == CONSTANTS.HEADING) {
          token = CONSTANTS.HEADING
          return true
        }

      //for List Item
      case '+' =>
        if (tokenChars.toString() == CONSTANTS.LISTITEM) {
          token = CONSTANTS.LISTITEM
          return true
        }

        //for Italics and Bold
      case '*' =>
          if (tokenChars.toString() == CONSTANTS.ITALICS) {
            token = CONSTANTS.ITALICS
            return true
          }
          else if (tokenChars.toString() == CONSTANTS.BOLD) {
          token = CONSTANTS.BOLD
          return true
        }

        //for all / cases
      case '\\' =>
          if(tokenChars.toString() == CONSTANTS.NEWLINE) {
            token = CONSTANTS.NEWLINE
            return true
          }else if(tokenChars.toString() == CONSTANTS.DOCB){
            token = CONSTANTS.DOCB
            return true
          }else if(tokenChars.toString() == CONSTANTS.DOCE) {
            token = CONSTANTS.DOCE
            return true
          }else if(tokenChars.toString() == CONSTANTS.PARAB) {
            token = CONSTANTS.PARAB
            return true
          }else if(tokenChars.toString() == CONSTANTS.PARAE) {
            token = CONSTANTS.PARAE
            return true
          }else if(tokenChars.toString() == CONSTANTS.DEFB) {
            token = CONSTANTS.DEFB
            return true
          }else if(tokenChars.toString() == CONSTANTS.USEB) {
            token = CONSTANTS.USEB
            return true
          }else if(tokenChars.toString() == CONSTANTS.TITLEB) {
            token = CONSTANTS.TITLEB
            return true
          }

        //for LinkB
      case '[' =>
        if (tokenChars.toString() == CONSTANTS.LINKB) {
          token = CONSTANTS.LINKB
          return true
        }

        //for BracketE
      case ']' =>
        if (tokenChars.toString() == CONSTANTS.ITALICS) {
          token = CONSTANTS.ITALICS
          return true
        }

        //for AddressB
      case '(' =>
        if (tokenChars.toString() == CONSTANTS.ITALICS) {
          token = CONSTANTS.ITALICS
          return true
        }

        //for AddressE
      case ')' =>
        if (tokenChars.toString() == CONSTANTS.ITALICS) {
          token = CONSTANTS.ITALICS
          return true
        }

        //for EqSign
      case '=' =>
        if (tokenChars.toString() == CONSTANTS.ITALICS) {
          token = CONSTANTS.ITALICS
          return true
        }
    }

    //if there are no matches
    lexicalError()
    false
  }

  //this defines text essentially as "not the special characters"
    def isText(c: Char): Boolean = {
      if (!(c == '!' || c == '#' || c == '+' || c == '*' || c == '\\' || c == '[' ||
        c == ']' || c == '=')){
        true
      }else
        false
    }

  def isSpecial(c: Char): Boolean = {
    if (c == '!' || c == '#' || c == '+' || c == '*' || c == '\\' || c == '['){
      true
    }else
      false

  }

  def textProcess(x: Char): Unit = {
    //loop until text is done
    //return the text bucket
    //may need to further define text better than as "not special"
    addChar(x)
      while(CONSTANTS.TEXT.contains(getChar())){
        addChar(getChar())
        currentChar += 1
      }
  }

  def specialProcess(): Unit = {
    println("this is lookup implementation!")
    var c: Char = getChar()
    currentChar += 1

    c match {
        //for image
      case '!' =>
        addChar(c)
        currentChar += 1
        c = getChar()
        addChar(c)
        currentChar += 1

        //for header
      case '#' =>
        addChar(c)
        currentChar += 1

        //for list item
      case '+' =>
        addChar(c)
        currentChar += 1

        //for italics
      case '*' =>
        addChar(c)
        currentChar += 1
        //for bold
        if (getChar() == '*') {
          addChar('*')
          currentChar += 1
        }

        //for all \ cases
      case '\\' =>
        //count variable for short loops in this case
        var count: Int = 0
        addChar(c)
        currentChar += 1
        if (getChar() == '\\') {
          addChar('\\')
          currentChar += 1
          return
        } else if(getChar() == 'B' || getChar() == 'P'){
          while(count < 5) {
            addChar(getChar())
            currentChar += 1
            count += 1
          }
          return
        }else if(getChar() == 'E'){
          while(count < 3){
            addChar(getChar())
            currentChar+=1
            count += 1
          }
          return
        }else if(getChar() == 'T'){
          while(count < 6){
            addChar(getChar())
            currentChar += 1
            count += 1
          }
          return
        } else if(getChar() == 'D' || getChar() == 'U'){
            while(count < 4){
              addChar(getChar())
              currentChar += 1
              count += 1
          }
          return
        }

        //for LinkB
      case '[' =>
        addChar('[')
        currentChar += 1
        return

        //for BracketE
      case ']' =>
        addChar(']')
        currentChar += 1
        return

        //for AddressB
      case '(' =>
        addChar('(')
        currentChar += 1
        return

        //for AddressE
      case ')' =>
        addChar(')')
        currentChar += 1
        return

        //for ESign
      case '=' =>
        addChar('=')
        currentChar += 1
        return
    }
    System.out.println("Did not match a special case")
  }

  def lexicalError(): Unit = {
    System.out.println("LEXICAL ERROR")
  }
}
