package com.ideal.netcare.scalatest.currying

/**
 * Created by syf on 2016/5/26.
 */
object CurryingTest1 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean
  type IntPairPred = (Int, Int) => Boolean

  def sizeConstraint(pred: IntPairPred, n: Int, email: Email) = pred(email.text.size, n)

  val gt: IntPairPred = _ > _
  val ge: IntPairPred = _ >= _
  val lt: IntPairPred = _ < _
  val le: IntPairPred = _ <= _
  val eq: IntPairPred = _ == _
  val minimumSize: (Int, Email) => Boolean = sizeConstraint(ge, _: Int, _: Email)
  val maximumSize: (Int, Email) => Boolean = sizeConstraint(le, _: Int, _: Email)
  val constr20: (IntPairPred, Email) => Boolean = sizeConstraint(_: IntPairPred, 20, _: Email)
  val constr30: (IntPairPred, Email) => Boolean = sizeConstraint(_: IntPairPred, 30, _: Email)
  val sizeConstraintFn: (IntPairPred, Int, Email) => Boolean = sizeConstraint _

  def main(args: Array[String]) {
    val mail = Email("subject", "this is text.", "test@test.com", "me@me.com")
    val flag = minimumSize(14, mail)
    println(flag)
  }
}

object CurryingTest2 {

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean
  type IntPairPred = (Int, Int) => Boolean

  def sizeConstraint(pred: IntPairPred)(n: Int)(email: Email): Boolean = pred(email.text.size, n)

  val gt: IntPairPred = _ > _
  val ge: IntPairPred = _ >= _
  val lt: IntPairPred = _ < _
  val le: IntPairPred = _ <= _
  val eq: IntPairPred = _ == _
  val sizeConstraintFn: IntPairPred => Int => Email => Boolean = sizeConstraint _
  val minSize: Int => Email => Boolean = sizeConstraintFn(ge)
  val maxSize: Int => Email => Boolean = sizeConstraintFn(le)
  val min20: Email => Boolean = minSize(20)
  val max30: Email => Boolean = maxSize(30)
  val min_20: Email => Boolean = sizeConstraintFn(ge)(20)
  val max_30: Email => Boolean = sizeConstraintFn(le)(30)

  def main(args: Array[String]) {
    val mail = Email("subject", "this is text.", "test@test.com", "me@me.com")
    println(min20(mail))
    println(max30(mail))
    println
    println(min_20(mail))
    println(max_30(mail))
  }
}

object CurryingTest3 {
  val sum: (Int, Int) => Int = _ + _
  val sumCurried: Int => Int => Int = sum.curried

  def main(args: Array[String]) {
    println(sumCurried(10)(10))
  }
}

object CurryingTest4 {

  case class User(name: String)

  case class Email(subject: String, text: String, sender: String, recipient: String)

  type EmailFilter = Email => Boolean

  trait EmailRepository {
    def getMails(user: User, unread: Boolean): Seq[Email]
  }

  trait FilterRepository {
    def getEmailFilter(user: User): EmailFilter
  }

  trait MailboxService {
    def getNewMails(emailRepo: EmailRepository)(filterRepo: FilterRepository)(user: User) =
      emailRepo.getMails(user, true).filter(filterRepo.getEmailFilter(user))

    val newMails: User => Seq[Email]
  }

  object MockEmailRepository extends EmailRepository {
    def getMails(user: User, unread: Boolean): Seq[Email] = Email("subject", "this is text.", "test@test.com", "me@me.com") :: Nil
  }

  object MockFilterRepository extends FilterRepository {
    def getEmailFilter(user: User): EmailFilter = user match {
      case User(name) if (name.contains("@")) => _ => true
      case _ => _ => false
    }
  }

  object MailboxServiceWithMockDeps extends MailboxService {
    val newMails: (User) => Seq[Email] =
      getNewMails(MockEmailRepository)(MockFilterRepository) _
  }

  def main(args: Array[String]) {
    val mails = MailboxServiceWithMockDeps.newMails(User("test1@test1.com"))
    println(mails)
  }


}