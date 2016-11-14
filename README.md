# Project1
Project 1 COSC 455 Dehlinger
Joseph Rajewski

#General
I could not get the project to run. The console told me that it could not find the java main file, as I created a jar file in 
  project structure. Therefore, I was unable to test, but I programmed the scala classes as described below.

#Compiler
 Accepts runtime argument of the file name to read file contents from. After this is validated, it should 
  continue and loop through the contents of the file, tokenizing them with Scanner.getNextToken. These tokens
  are stored in an array of tokens, which is accessed by the syntax analyzer. The tokens are parsed there, and 
  this parse tree is used by the semantic analyzer to check for valid scoping there. After that, the translated
  html stack is written to a file and opened in a browser in Compiler.
  
#Lexical Analyzer
 Check first char of token in getNextToken to see if it needs text, special or whitespace processing.
 Skip whitespace.
 For text, put consecutive chars of text into a charArray, and that is one token.
 For special, go on a case by case basis "matching" the first character of the token.
  Add each subsequent char to the token array, and that becomes a token.
  Return to getNextToken, and lookup the new token. Based on the first charcater of the token the LA
  will check against the BNF for the rest of the chars in the token.
 If it is valid, it becomes the token that is inserted into the token array in compiler.
 Error for false lookup.
 
#Syntax Analyzer
 List of tokens from Compiler is used here to navigate the file. Each production rule is its own method.
 Where a production rule had empty as an available option, error messages were added to all but the first
 element of the rule, so that a null token could still "escape" an error. Everything following the first element
 is required, and thus gets an error message.
 Where there were multiple possible paths, I used match statements to identify which path to take.
 When a production rule needed to be referenced from another in a particular path, I simply called its method.
 Forgot to increment tokenCount after line 178.
 
 #Semantic Analyzer
  My Semantic Analyzer moves all of the parsed tokens into a stack (I made it a reverse stack so that I didn't have
  to think about it as much). From there, Compiler calls variableRes, which does most of the work. It will match 
  the "next" token with a terminal symbol from CONSTANTS. From there, it pushes the appropriate HTML 5 value onto
  the stack to be written to a file, convertStack. It also pops tokens off of the parseTreeStack as it constructs 
  this new stack, but also will check that a variable has been declared before it can be used.
  
    A feature that I ran out of time to implement was the appropriation of text in the html. I explained this in 
    comments at line 49 some, but what I mean is that I did not get to read through each character for a text token.
    Instead, the code as it is now would take just one char and make that the text field for whatever html text
    element, and then everything following that in the translation would be incorrect/incomplete since I did not implement 
    a case for text.
 
 
