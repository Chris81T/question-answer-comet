package code.comet

import net.liftweb._
import http._
import actor._

import code.model._

case object HelloQuestionActor
case class IncomingQuestion(question: Question)
case class IncomingAnswer(question: Question, answer: Answer)

object QuestionServer extends LiftActor with ListenerManager {

	def createUpdate = HelloQuestionActor
  
	override def lowPriority = {
	  case msg: IncomingQuestion => updateListeners(msg)
	  case msg: IncomingAnswer => updateListeners(msg)
	}	
}