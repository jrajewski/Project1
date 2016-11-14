/**
  * Created by ringo on 10/30/2016.
  */
package edu.towson.cosc.cosc455.jrajewski.project1

class MySematicAnalyzer {
  var parseTreeStack = new scala.collection.mutable.Stack[String]
  var convertStack = new scala.collection.mutable.Stack[String]
  var parseTree = new MySyntaxAnalyzer
  var reverseStackPlace: Int = parseTree.parseTreeList.length - 1

  var variables = List()

  def initStack(): Unit = {
    while (reverseStackPlace > 0) {
      parseTreeStack.push(parseTree.tokenParsing(reverseStackPlace))
      reverseStackPlace -= 1
    }
  }

  def variableRes(): Unit = {
    var next: String = parseTreeStack.pop()
    var temp: String = ""
    var textList: List[Char] = List()
    var temptextList = List()
    var count: Int = 0

    while (next != Nil) {
      next = parseTreeStack.pop()
      next match {
        case CONSTANTS.DOCB =>
          convertStack.push("<html>")
          next = parseTreeStack.pop()
          while (next.equalsIgnoreCase(CONSTANTS.DEFB)) {
            parseTreeStack.pop()
            parseTreeStack.pop()
            next = parseTreeStack.pop()
            next :: variables
            parseTreeStack.pop()
          }
          variableRes()
          variableRes()
          parseTreeStack.pop() // DOCE
          convertStack.push("</html>")
        case CONSTANTS.TITLEB => //TITLE
          convertStack.push("<title>")
          parseTreeStack.pop()
          temp = parseTreeStack.pop()
          //I ran out of time before being able to account for
          //consecutive text tokens to be accounted for as one
          //text token in the stack
          //thus, the file will be inaccurate because
          //there will be only one character where a string of characters
          /*
          while(CONSTANTS.TEXT.contains(parseTreeStack.pop()){
            parseTreeStack.pop().toCharArray :: textList
          }
          */
          convertStack.push(temp)
          parseTreeStack.pop()
        case CONSTANTS.PARAB => //PARAB
          convertStack.push("<p>")
          variableRes()
          variableRes()
          parseTreeStack.pop()
          convertStack.push("</p")
        case CONSTANTS.HEADING =>
          convertStack.push("<h1>")
          temp = parseTreeStack.pop()
          parseTreeStack.pop()
          convertStack.push(temp + "</h1")
        case CONSTANTS.USEB =>
          next = parseTreeStack.pop()
          if(!variables.contains(temp)) {
            System.out.print("SEMANTIC ERROR. Variable has not been declared")
            System.exit(1)
          }
          parseTreeStack.pop()
        case CONSTANTS.BOLD =>
          convertStack.push("<b>")
          temp = parseTreeStack.pop()
          convertStack.push(temp)
          convertStack.push("</b>")
        case CONSTANTS.ITALICS =>
          convertStack.push("<i>")
          temp = parseTreeStack.pop()
          convertStack.push(temp)
          convertStack.push("</i>")
        case CONSTANTS.LISTITEM =>
          convertStack.push("<li>")
          variableRes()
          variableRes()
          convertStack.push("</li>")
        case CONSTANTS.LINKB =>
          convertStack.push("[")
          temp = parseTreeStack.pop()
          convertStack.push(temp)
          parseTreeStack.pop()
          convertStack.push("]")
          parseTreeStack.pop()
          convertStack.push("(")
          temp = parseTreeStack.pop()
          convertStack.push(temp)
          parseTreeStack.pop()
          convertStack.push(")")
        case CONSTANTS.IMAGEB =>
          convertStack.push("<img src=\\")
          parseTreeStack.pop()
          convertStack.push("\"")
          temp = parseTreeStack.pop()
          convertStack.push(temp)
          parseTreeStack.pop()
          convertStack.push("\"")
          parseTreeStack.pop()
          convertStack.push("alt=\"")
          temp = parseTreeStack.pop()
          convertStack.push(temp)
          parseTreeStack.pop()
          convertStack.push("\">")
        case CONSTANTS.NEWLINE =>
          parseTreeStack.pop()
          convertStack.push("br")
      }
    }
  }
}
