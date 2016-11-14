/**
  * Created by ringo on 10/30/2016.
  */
package edu.towson.cosc.cosc455.jrajewski.project1
/* General methodology for Syntax Analysis:

tokenNum counter to keep current place in token array
  incrememnted by 1 at each terminal symbol to move to next token
Where epsilon is a possible value for a production rule, I put error messages after all but
  the first if statement in the path to the completed implementation of the rule.
  I did this so that a token could be empty without penalty, and then put an error message after
  every other if statement because at that point, the analysis is commited to completing that path.
Where epsilon was not possible, I made sure that an error message was included when checking for the
  terminal symbol and an exit statement.
Text was an interesting case. I decided to parse the characters along with the other tokens by
  looping in the instance of a text token. The actual tokenNum would not change until the text
  would be done processing, only the text count would be incremented in order to move through
  the text until the next token.

*/

class MySyntaxAnalyzer extends SyntaxAnalyzer{
  var parseTreeList = List()
  var tokenNum: Int = 0
  var tokenParsing: List[String] = Compiler.tokenList


  override def gittex(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.DOCB)){
      CONSTANTS.DOCB :: parseTreeList
      tokenNum += 1
      variableDefine()
      title()
      body()
      if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.DOCB)){
        CONSTANTS.DOCE :: parseTreeList
        tokenNum += 1
      } else {
        println("Syntax error. Incorrect document start grammar.")
        System.exit(1)
      }
    } else {
      println("Syntax error. Incorrect document start.")
      System.exit(1)
    }
  }

  override def paragraph(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.PARAB)){
      CONSTANTS.PARAB :: parseTreeList
      tokenNum += 1
      variableDefine()
      innerText()
      if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.PARAE)) {
        CONSTANTS.PARAE :: parseTreeList
        tokenNum += 1
      }else
        println("Syntax error. Incorrect document end.")
    } else
      println("Syntax error. Incorrect document start.")
    System.exit(1)
  }

  override def body(): Unit = {
    val bodyToken: String = tokenParsing(tokenNum)

    bodyToken match {
      case CONSTANTS.USEB =>
        innerText()
        body()
      case CONSTANTS.HEADING =>
        innerText()
        body()
      case CONSTANTS.BOLD =>
        innerText()
        body()
      case CONSTANTS.ITALICS =>
        innerText()
        body()
      case CONSTANTS.LISTITEM =>
        innerText()
        body()
      case CONSTANTS.IMAGEB =>
        innerText()
        body()
      case CONSTANTS.LINKB =>
        innerText()
        body()
      case CONSTANTS.NEWLINE =>
        innerText()
        body()
      case CONSTANTS.PARAB => paragraph()
        body()
      case CONSTANTS.NEWLINE =>
        newline()
        body()
    }

    if(CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
      var textCount: Int = 0
      while (CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
        tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
        textCount += 1
      }
      innerText()
      body()
    }
    //no error message for TEXT because it is optional
  }


  override def innerItem(): Unit = {
    val bodyToken: String = tokenParsing(tokenNum)
    bodyToken match {
      case CONSTANTS.USEB =>
        variableUse()
        innerItem()
      case CONSTANTS.BOLD =>
        bold()
        innerItem()
      case CONSTANTS.ITALICS =>
        italics()
        innerItem()
      case CONSTANTS.LINKB =>
        link()
        innerItem()
    }

    //made if statement for this case because complicated
    //error message because text is required
    if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
      var textCount: Int = 0
      while (CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
        tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
        textCount += 1
      }
      tokenNum += 1
      innerItem()
    }else {
      println("Syntax error. Incorrect bracket end grammar.")
      System.exit(1)
    }
  }

  override def innerText(): Unit = {
    val bodyToken: String = tokenParsing(tokenNum)
    bodyToken match {
      case CONSTANTS.USEB =>
        variableUse()
        innerText()
      case CONSTANTS.HEADING =>
        heading()
        innerText()
      case CONSTANTS.BOLD =>
        bold()
        innerText()
      case CONSTANTS.ITALICS =>
        italics()
        innerText()
      case CONSTANTS.LISTITEM =>
        listItem()
        innerText()
      case CONSTANTS.IMAGEB =>
        image()
        innerText()
      case CONSTANTS.LINKB =>
        link()
        innerText()
      case CONSTANTS.NEWLINE =>
        newline()
        innerText()
    }
    if(CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
      var textCount: Int = 0
      while (CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
        tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
        textCount += 1
      }
      innerText()
    }
  }

  override def link(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.LINKB)){
      CONSTANTS.LINKB :: parseTreeList
      tokenNum += 1
      //special case for text methods
      //parses individual consecutive characters for text, but keeps the token until text
      //processing is done
      if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
        var textCount: Int = 0
        while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        tokenNum += 1
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BRACKETE)){
          CONSTANTS.BRACKETE :: parseTreeList
          tokenNum += 1
          if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.ADDRESSB)){
            CONSTANTS.ADDRESSB :: parseTreeList
            tokenNum += 1
            if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
              CONSTANTS.REQTEXT :: parseTreeList
              tokenNum += 1
              if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.ADDRESSE)){
                CONSTANTS.ADDRESSE :: parseTreeList
                tokenNum += 1
              }else {
                println("Syntax error. Incorrect address end grammar.")
                System.exit(1)
              }
            }else {
              println("Syntax error. Incorrect required text grammar.")
              System.exit(1)
            }
          }else {
            println("Syntax error. Incorrect address begin grammar.")
            System.exit(1)
          }
        }else {
          println("Syntax error. Incorrect bracket end grammar.")
          System.exit(1)
        }
      }else {
        println("Syntax error. Incorrect required text grammar.")
        System.exit(1)
      }
    }
  }

  override def italics(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.ITALICS)){
      CONSTANTS.ITALICS :: parseTreeList
      tokenNum += 1
      if(CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
        var textCount: Int = 0
        while (CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.ITALICS)){
          CONSTANTS.ITALICS :: parseTreeList
          tokenNum += 1
        } else {
          println("Syntax error. Incorrect italics grammar.")
          System.exit(1)
        }
      } //no error message for text here because it can be empty
    }
  }

  override def bold(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BOLD)){
      CONSTANTS.BOLD :: parseTreeList
      tokenNum += 1
      if(CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
        var textCount: Int = 0
        while (CONSTANTS.TEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BOLD)) {
          CONSTANTS.BOLD :: parseTreeList
          tokenNum += 1
        }else {
          println("Syntax error. Incorrect bold grammar.")
          System.exit(1)
        }
      }
    }else {
      println("Syntax error. Incorrect bold grammar.")
      System.exit(1)
    }
  }

  override def newline(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.NEWLINE)) {
      CONSTANTS.NEWLINE :: parseTreeList
      tokenNum += 1
    }
  }

  override def title(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.TITLEB)){
      CONSTANTS.TITLEB :: parseTreeList
      tokenNum += 1
      if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
        var textCount: Int = 0
        while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        tokenNum += 1
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BRACKETE)){
          CONSTANTS.BRACKETE :: parseTreeList
          tokenNum += 1
        }else {
          println("Syntax error. Incorrect title grammar.")
          System.exit(1)
        }
      }else {
        println("Syntax error. Incorrect title grammar.")
        System.exit(1)
      }
    }else {
      println("Syntax error. Incorrect title grammar.")
      System.exit(1)
    }
  }

  override def variableDefine(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.DEFB)){
      CONSTANTS.DEFB :: parseTreeList
      tokenNum += 1
      variableDefine()
      if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
        var textCount: Int = 0
        while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        tokenNum += 1
        variableDefine()
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.EQSIGN)){
          CONSTANTS.EQSIGN :: parseTreeList
          tokenNum += 1
          variableDefine()
          if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
            var textCount: Int = 0
            while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
              tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
              textCount += 1
            }
            tokenNum += 1
            variableDefine()
            if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BRACKETE)){
              CONSTANTS.BRACKETE :: parseTreeList
              tokenNum += 1
              variableDefine()
            }else{
              println("Syntax error. Incorrect variable define grammar.")
              System.exit(1)
            }
          }else{
            println("Syntax error. Incorrect variable define grammar.")
            System.exit(1)
          }
        }else{
          println("Syntax error. Incorrect variable define grammar.")
          System.exit(1)
        }
      }else{
        println("Syntax error. Incorrect variable define grammar.")
        System.exit(1)
      }
    }
  }

  override def image(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.IMAGEB)){
      CONSTANTS.IMAGEB :: parseTreeList
      tokenNum += 1
      if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
        var textCount: Int = 0
        while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        tokenNum += 1
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BRACKETE)){
          CONSTANTS.BRACKETE :: parseTreeList
          tokenNum += 1
          if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.ADDRESSB)){
            CONSTANTS.ADDRESSB :: parseTreeList
            tokenNum += 1
            if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
              var textCount: Int = 0
              while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
                tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
                textCount += 1
              }
              tokenNum += 1
              if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.ADDRESSE)){
                CONSTANTS.ADDRESSE :: parseTreeList
                tokenNum += 1
              }else {
                println("Syntax error. Incorrect image grammar.")
                System.exit(1)
              }
            }else {
              println("Syntax error. Incorrect image grammar.")
              System.exit(1)
            }
          }else {
            println("Syntax error. Incorrect image grammar.")
            System.exit(1)
          }
        }else {
          println("Syntax error. Incorrect image grammar.")
          System.exit(1)
        }
      }else {
        println("Syntax error. Incorrect image grammar.")
        System.exit(1)
      }
    }
  }

  override def variableUse(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.HEADING)) {
      CONSTANTS.HEADING :: parseTreeList
      tokenNum += 1
      if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
        var textCount: Int = 0
        while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        tokenNum += 1
        if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.BRACKETE)) {
          CONSTANTS.BRACKETE :: parseTreeList
          tokenNum += 1
        }else {
          println("Syntax error. Incorrect variable use grammar.")
          System.exit(1)
        }
      }else {
        println("Syntax error. Incorrect variable use grammar.")
        System.exit(1)
      }
    }
  }

  override def heading(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.HEADING)){
      CONSTANTS.HEADING :: parseTreeList
      tokenNum += 1
      if(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))){
        var textCount: Int = 0
        while(CONSTANTS.REQTEXT.contains(tokenParsing(tokenNum).charAt(0))) {
          tokenParsing(tokenNum).charAt(textCount).toString() :: parseTreeList
          textCount += 1
        }
        tokenNum += 1
      }else
        println("Syntax error. Incorrect heading grammar.")
    }else
      println("Syntax error. Incorrect heading grammar.")
    System.exit(1)
  }

  override def listItem(): Unit = {
    if(tokenParsing(tokenNum).equalsIgnoreCase(CONSTANTS.LISTITEM)) {
      CONSTANTS.LISTITEM :: parseTreeList
      tokenNum += 1
      innerItem()
      listItem()
    }
  }
}