package com.ideal.netcare.scalatest.option

/**
 * Created by syf on 2016/5/23.
 */
object OptionTest1 {

  case class User(id: Int, name: String, age: Int, gender: Option[String])

  object UserRepository {
    private val users = Map(1 -> User(1, "Tom", 10, Some("male")), 2 -> User(2, "xman", 20, None))

    def findById(id: Int): Option[User] = users.get(id)

    def findAll = users.values
  }

  def main(args: Array[String]) {
    val user = User(3, "Josh", 15, None)
    println("Gender:" + user.gender.getOrElse("not specified"))
  }
}

object OptionTest2 {

  case class User(id: Int, name: String, age: Int, gender: Option[String])

  object UserRepository {
    private val users = Map(1 -> User(1, "Tom", 10, Some("male")), 2 -> User(2, "xman", 20, None))

    def findById(id: Int): Option[User] = users.get(id)

    def findAll = users.values
  }

  def main(args: Array[String]) {
    UserRepository.findById(1).foreach(user => println(user.gender))

    val gender = UserRepository.findById(1).flatMap(_.gender)
    println(gender)
  }
}

object OptionTest3 {
  def main(args: Array[String]) {
    val names: List[List[String]] = List(List("Johnny", "Tom", "Lucy"), List(), List("Jonathan", "Lilith"))
    val upNames1 = names.map(_.map(_.toUpperCase))
    println(upNames1)

    val upNames2 = names.flatMap(_.map(_.toUpperCase))
    println(upNames2)
  }
}

object OptionTest4 {
  def main(args: Array[String]) {
    val names: List[Option[String]] = List(Some("Jonathan"), None, Some("Daniel"))
    val upNames1 = names.map(_.map(_.toUpperCase))
    println(upNames1)

    val upNames2 = names.flatMap(_.map(_.toUpperCase))
    println(upNames2)
  }
}

object OptionTest5 {

  case class User(id: Int, name: String, age: Int, gender: Option[String])

  object UserRepository {
    private val users = Map(1 -> User(1, "Tom", 10, Some("male")), 2 -> User(2, "xman", 20, None))

    def findById(id: Int): Option[User] = users.get(id)

    def findAll = users.values
  }

  def main(args: Array[String]) {
    val genderList = for {
      user <- UserRepository.findById(1)
      gender <- user.gender
    } yield gender
    println(genderList)
  }
}

object OptionTest6 {

  case class User(id: Int, name: String, age: Int, gender: Option[String])

  object UserRepository {
    private val users = Map(1 -> User(1, "Tom", 10, Some("male")), 2 -> User(2, "xman", 20, None))

    def findById(id: Int): Option[User] = users.get(id)

    def findAll = users.values
  }

  def main(args: Array[String]) {
    val genderList = for {
      User(_, _, _, Some(gender)) <- UserRepository.findAll
    } yield gender
    println(genderList)
  }
}

object OptionTest7{
  case class Resource(content :String)
  val resourceFromConfigDir:Option[Resource]=None
  val resourceFromClassPath:Option[Resource]=None
  val resourceFromSomeWhere:Option[Resource]=Some(Resource("I was found in somewhere"))
  val resource=resourceFromConfigDir orElse resourceFromClassPath orElse resourceFromSomeWhere

  def main(args: Array[String]) {
    println(resource)
  }
}