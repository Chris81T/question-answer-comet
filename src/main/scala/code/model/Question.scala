package code.model

import org.joda.time._

trait MessageHelper {
  val timestamp = new DateTime
}

case class Answer(msg: String, id: String) extends MessageHelper

case class Question(msg: String, id: String) extends MessageHelper {
	var answer: Option[Answer] = None
}

object QuestionStorage {
  
  var questions: List[Question] = Nil
  
  def addQuestion(question: Question) = questions ::= question
    
}