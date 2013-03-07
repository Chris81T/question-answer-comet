package code.snippet

import net.liftweb._
import http._
import js.{JsCmds, JsCmd}
import JsCmds._
import js.jquery.JqJsCmds._
import util._
import util.Helpers._
import common._
import sitemap._

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

  def answerDialog = {
    
    var answerMessage = ""
    var id = ""
    
    def createAnswer() : JsCmd = {
      println("create a new answer = " +
          answerMessage + " according to question with id = " +
          id)
      Noop
    }
    
    "#modal-answer" #> SHtml.ajaxText(answerMessage, {answerMessage = _}) &
    	"#modal-question-id" #> SHtml.ajaxText(id, {id = _}) &
    	"#modal-answer-button [onclick]" #> SHtml.ajaxInvoke(createAnswer)
  }
  
}
