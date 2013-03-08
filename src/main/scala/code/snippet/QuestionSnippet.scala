package code.snippet

import net.liftweb._
import http._
import js.{JsCmds, JsCmd}
import js.JE._
import JsCmds._
import js.jquery.JqJsCmds._
import util._
import util.Helpers._
import common._
import sitemap._

import json._
import json.JsonDSL._

import java.util._

import code.model._
import code.comet._

object QuestionSnippet {
  
  def render = {
    
    var questionMessage = ""
    
    def createQuestion() : JsCmd = {
    	println("create a new question = " + questionMessage)
    	
    	if (!questionMessage.isEmpty) {    	
	    	// store new question into storage
    		val question = Question(questionMessage, QuestionStorage.questions.size.toString)
	    	QuestionStorage.addQuestion(question)
	    	
	    	// send message to server
	    	QuestionServer ! IncomingQuestion(question)	
	    	
	    	// clean up
	    	questionMessage = ""
	    	SetValById("question-input", "")
    	} else {
    		println("question field is empty")
    		FadeIn("error-box", 0 second, 2 second) & FadeOut("error-box", 4 second, 2 second)
    	}    	
    }
    
    "#question-input" #> SHtml.ajaxText(questionMessage, {questionMessage = _}) &
    	"#create-question [onclick]" #> SHtml.ajaxInvoke(createQuestion)    
  }

  // second try
  def answerDialogCallback = {
    
    def createAnswer(answerParam: JValue) : JsCmd = {
      println("()()() try to create a new answer = " + answerParam)
      
      answerParam match {
        case answer: JObject =>
	      val answerMessage = answer.values.get("answer")
	      val questionId = answer.values.get("questionId")	      
	      println("()()() answerMessage = " + answerMessage +
	          ", questionId = " + questionId)      
	          
	      if (answerMessage != None && questionId != None) {
	    	  QuestionStorage.findQuestion(questionId.get.asInstanceOf[String]) match {
	    	    case Some(question) =>
	    	      	val answer = Answer(answerMessage.get.asInstanceOf[String])
	    	    	question.answer = Some(answer)
	    	    	println("Found question according to given id with the brand new answer = " + question)
	    	    	QuestionServer ! IncomingAnswer(question, answer)
	    	    case None => println("[WARN] No question according to id is found!")
	    	  }
	      }	else {
	        println("[ WARN ] Some expected values are 'None' !!!")
	      }          
        case _ => println("[ WARN ] A JObject is expected !!!")
      }            
      Noop
    }
    
    "#serverside-script" #> Script(
    	Function("createAnswer", "answer" :: Nil,
    	    SHtml.jsonCall(
    	        JsVar("answer"),
    	        createAnswer _
    	    )._2.cmd    			
    	)
    )
  }
  
  
  def serverSideCall = {
    println("PREPARING SERVERSIDE << hELLOwORLD >> CALL....")
    "*" #> Script(
		Function("serverSideCallback", "question" :: Nil,
		    SHtml.ajaxCall(
		    	JsVar("question"),
		    	(question: String) => serverSideCallback(question)
		    )._2.cmd
		)
	)
  }
  
  private def serverSideCallback(question: String) : JsCmd = {
    println(">>>>>>>>> SERVER SIDE CALLBACK INVOKATION / Question = " + question)
    Noop
  }
  
}
