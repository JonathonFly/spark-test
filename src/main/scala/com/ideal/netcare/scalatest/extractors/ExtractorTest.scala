package com.ideal.netcare.scalatest.extractors

/**
 * Created by syf on 2016/5/19.
 */


object ExtractorTest1 {

  case class User(firstName: String, lastName: String, score: Int)

  def advance(xs: List[User]) = xs match {
    case User(_, _, score1) :: User(_, _, score2) :: _ => score1 - score2
    case _ => 0
  }

  def main(args: Array[String]) {
    val user1 = new User("ming", "xiao", 50)
    val user2 = new User("liu", "xiao", 60)
    val userList = List(user1, user2)
    println("advance users score = " + advance(userList))
  }
}

object ExtractorTest2 {

  trait User {
    def name: String
  }

  class FirstUser(val name: String) extends User

  class SecondUser(val name: String) extends User

  object FirstUser {
    def unapply(user: FirstUser): Option[String] = Some(user.name)
  }

  object SecondUser {
    def unapply(user: SecondUser): Option[String] = Some(user.name)
  }

  def main(args: Array[String]) {
    val user: User = new FirstUser("Tom")
    user match {
      case FirstUser(name) => println("Hello " + name)
      case SecondUser(name) => println("Welcome back, dear" + name)
    }
  }
}

object ExtractorTest3 {

  trait User {
    def name: String

    def score: Int
  }

  class FirstStudent(val name: String, val score: Int, val age: Int) extends User

  class SecondStudent(val name: String, val score: Int) extends User

  object FirstStudent {
    def unapply(user: FirstStudent): Option[(String, Int, Int)] = Some((user.name, user.score, user.age))
  }

  object SecondStudent {
    def unapply(user: SecondStudent): Option[(String, Int)] = Some((user.name, user.score))
  }

  def main(args: Array[String]) {
    val user: User = new FirstStudent("Jonathan", 66, 17)
    user match {
      case FirstStudent(name, score, age) => println(s"FirstStudent name = $name, score = $score, age = $age")
      case SecondStudent(name, _) => print(s"SecondStudent, Hello $name.")
    }
  }

}

object ExtractorTest4 {

  trait User {
    def name: String

    def score: Int
  }

  class FreeUser(val name: String, val score: Int, val upgradeProbability: Double) extends User {
    override def toString: String = "name = " + name + ", score = " + score
  }

  object changeToPro {
    def unapply(user: FreeUser): Boolean = user.upgradeProbability > 0.5
  }

  def main(args: Array[String]) {
    val user: User = new FreeUser("Josh", 100, 0.4)
    user match {
      case freeUser@changeToPro() => println(s"Hello Pro User $freeUser.")
      case _ => println(s"Hello Free User.")
    }
  }
}


object ExtractorTest5 {
  def main(args: Array[String]) {
    val stream = 46 #:: 38 #:: 67 #:: 18 #:: Stream.empty


    val result1 = stream match {
      case first #:: second #:: _ => first + second
      case _ => -1
    }

    println(result1)

    val result2 = stream match {
      case #::(first, #::(second, _)) => first + second
      case _ => -1
    }
    println(result2)

  }
}

object ExtractorTest6 {
  def main(args: Array[String]) {
    val xs = 3 :: 6 :: 12 :: Nil
    val res = xs match {
      case List(a, b) => a * b
      case List(a, b, c) => a + b + c
      case _ => 0
    }
    println(res)
  }
}

object ExtractorTest7 {
  def main(args: Array[String]) {
    val xs = 3 :: 6 :: 12 :: 18 :: Nil
    val res = xs match {
      case List(a, b, _*) => a * b
      case _ => 0
    }
    println(res)
  }
}

object ExtractorTest8 {

  object GivenNames {
    def unapplySeq(name: String): Option[Seq[String]] = {
      val names = name.trim.split(" ")
      if (names.forall(_.isEmpty)) None
      else Some(names)
    }
  }

  def getFirstName(name: String) = name match {
    case GivenNames(firstName, _*) => s"hello $firstName."
    case _ => "please give a firstName"
  }

  def main(args: Array[String]) {
    val list = List("Tom", "Johnny Bugger", "Little Ming")
    list.map(getFirstName).foreach(println(_))
  }
}

object ExtractorTest9 {

  object names {
    def unapplySeq(name: String): Option[(String,String,Seq[String])] = {
      val names = name.trim.split(" ")
      if(names.size<2) None
      else Some((names.head,names.last,names.drop(1).dropRight(1).toList))
    }
  }

  def getNames(name: String) = name match {
    case names(firstName,lastName, _*) => s"hello $firstName...$lastName."
    case _ => "please at least give a firstName and a lastName"
  }

  def main(args: Array[String]) {
    val list = List("Tom", "Johnny Bugger", "Little Ming", "Little Sugar Free")
    list.map(getNames).foreach(println(_))
  }
}