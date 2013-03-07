package code.comet

import net.liftweb._
import http._
import util._
import Helpers._

import http.js.{JE, JsCmd}
import http.js.JsCmds._
import http.js.JsCmd._
import json._
import json.JsonDSL._

import code.model._

class QuestionActor extends CometActor with CometListener {
  
  def registerWith = QuestionServer
   
  private def printRef = print("[ actor :: " + toString + " ] ")
  
  override def lowPriority = {
    case IncomingQuestion(question) => renderNewQuestion(question)
  }
  
  private def generateJSON(question: Question) : JObject = {
    ("id" -> question.id) ~
    ("msg" -> question.msg) ~ 
    ("timestamp" -> question.timestamp.toDate.toString)
  }
  
  private def generateJArray(questions: List[Question]) : JArray = 
	  JArray(for (question <- questions) yield generateJSON(question))
  
  private def renderQuestions(questions: List[Question]) : JsCmd = {
    printRef
    println("render existing questions = " + questions)        
    JE.Call("listQuestions", generateJArray(questions)).cmd
  }
    
  private def renderNewQuestion(question: Question) {
    printRef
    println("render incoming new question = " + question)
    partialUpdate(JE.Call("addQuestion", generateJSON(question)).cmd) 
  }
  
  def render = {
	val questions: List[Question] = QuestionStorage.questions
    printRef
	if (!questions.isEmpty) {
	  println("found some questions = " + questions)
	  Script(OnLoad(renderQuestions(questions)))	  
	} else {
	  println("no questions available. Send command to remove the section")
	  Script(OnLoad(JE.Call("removeQuestionsSection").cmd))	  
 	}
  }
}