package com.ideal.netcare.scalatest.highOrderFunction

/**
 * Created by syf on 2016/5/25.
 */
object HighOrderFunctionTest1 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter) = mails.filter(f)

  val sentByOneOf: Set[String] => EmailFilter =
    senders =>
      email => senders.contains(email.sender)

  val notSentByAnyOf: Set[String] => EmailFilter =
    senders =>
      email => !senders.contains(email.sender)

  val minimumSize: Int => EmailFilter =
    n =>
      email => email.text.size >= n

  val maximumSize: Int => EmailFilter =
    n =>
      email => email.text.size <= n

  def main(args: Array[String]) {
    val emailFilter: EmailFilter = notSentByAnyOf(Set("test1@test1.com"))
    val mails = Email(subject = "hello moto", text = "what the fuck man.", sender = "test@test.com", recipient = "me@me.com") :: Nil
    val res = newMailsForUser(mails, emailFilter)
    res.foreach(println)
  }
}

object HighOrderFunctionTest2 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean
  type SizeChecker = Int => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter) = mails.filter(f)

  val sizeConstraint: SizeChecker => EmailFilter =
    f =>
      email => f(email.text.size)

  val minimumSize: Int => EmailFilter =
    n =>
      sizeConstraint(_ >= n)

  val maximumSize: Int => EmailFilter =
    n =>
      sizeConstraint(_ <= n)

  def main(args: Array[String]) {
    val emailFilter: EmailFilter = minimumSize(18)
    val mails = Email(subject = "hello moto", text = "what the fuck man.", sender = "test@test.com", recipient = "me@me.com") :: Nil
    val res = newMailsForUser(mails, emailFilter)
    res.foreach(println)
  }
}

object HighOrderFunctionTest3 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter) = mails.filter(f)

  def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)

  val sentByOneOf: Set[String] => EmailFilter =
    senders =>
      email => senders.contains(email.sender)

  val notSentByAnyOf = sentByOneOf andThen (complement(_))

  def main(args: Array[String]) {
    val emailFilter: EmailFilter = notSentByAnyOf(Set("test1@test1.com"))
    val mails = Email(subject = "hello moto", text = "what the fuck man.", sender = "test@test.com", recipient = "me@me.com") :: Nil
    val res = newMailsForUser(mails, emailFilter)
    res.foreach(println)
  }
}

object HighOrderFunctionTest4 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter) = mails.filter(f)

  def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)

  def any[A](predicates: (A => Boolean)*): A => Boolean =
    a => predicates.exists(pred => pred(a))

  def none[A](predicates: (A => Boolean)*) = complement(any(predicates: _*))

  def every[A](predicates: (A => Boolean)*) = none(predicates.view.map(complement(_)): _*)

  val notSentByAnyOf: Set[String] => EmailFilter =
    senders =>
      email => !senders.contains(email.sender)

  val minimumSize: Int => EmailFilter =
    n =>
      email => email.text.size >= n

  val maximumSize: Int => EmailFilter =
    n =>
      email => email.text.size <= n

  val filter: EmailFilter = every(
    notSentByAnyOf(Set("test1@test1.com")),
    minimumSize(5),
    maximumSize(18)
  )

  def main(args: Array[String]) {

    val emailFilter: EmailFilter = filter
    val mails = Email(subject = "hello moto", text = "what the fuck man.", sender = "test@test.com", recipient = "me@me.com") :: Nil
    val res = newMailsForUser(mails, emailFilter)
    res.foreach(println)
  }
}

object HighOrderFunctionTest5 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  val addMissingSubject = (email: Email) =>
    if (email.subject.isEmpty) email.copy(subject = "No Subject")
    else email

  val checkSpelling = (email: Email) =>
    email.copy(text = email.text.replaceAll("fuck", "fucking"))

  val removeSomeThing = (email: Email) =>
    email.copy(text = email.text.replaceAll("man", "wrong"))

  val addSomeThingToFooter = (email: Email) =>
    email.copy(text = email.text + "\nThis mail is just a piece of shit.")

  val pipeline = Function.chain(Seq(
    addMissingSubject,
    checkSpelling,
    removeSomeThing,
    addSomeThingToFooter
  ))

  def main(args: Array[String]) {
    val mail = Email(subject = "", text = "what the fuck man.", sender = "test@test.com", recipient = "me@me.com")
    val newMail = pipeline(mail)
    println(newMail)
  }
}
