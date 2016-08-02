package com.ideal.netcare.scalatest.pathDependent

/**
 * Created by syf on 2016/5/30.
 */
object PathDependentTypeTest1 {

  object Franchise {

    //角色
    case class Character(name: String)

  }

  class Franchise(name: String) {

    import Franchise.Character

    def createFanFiction(lovestruck: Character, objectOfDesire: Character): (Character, Character) = (lovestruck, objectOfDesire)
  }

  def main(args: Array[String]) {
    val starTrek = new Franchise("Star Trek")
    val starWars = new Franchise("Star Wars")

    val quark = Franchise.Character("Quark")
    val jadzia = Franchise.Character("Jadzia Dax")

    val luke = Franchise.Character("Luke Skywalker")
    val yoda = Franchise.Character("Yoda")

    starTrek.createFanFiction(lovestruck = jadzia, objectOfDesire = luke)
  }
}

object PathDependentTypeTest2 {

  class A {

    class B

    var b: Option[B] = None
  }

  val a1 = new A
  val a2 = new A
  val b1 = new a1.B
  val b2 = new a2.B
  a1.b = Some(b1)
  a2.b = Some(b2)
}

object PathDependentTypeTest3 {

  class Franchise(name: String) {

    case class Character(name: String)

    def createFanFictionWith(lovestruck: Character, objectOfDesire: Character): (Character, Character) = (lovestruck, objectOfDesire)
  }

  def createFanFiction(f: Franchise)(lovestruck: f.Character, objectOfDesire: f.Character) = (lovestruck, objectOfDesire)

  def main(args: Array[String]) {
    val starTrek = new Franchise("Star Trek")
    val starWars = new Franchise("Star Wars")

    val quark = starTrek.Character("Quark")
    val jadzia = starTrek.Character("Jadzia Dax")

    val luke = starWars.Character("Luke Skywalker")
    val yoda = starWars.Character("Yoda")

    starTrek.createFanFictionWith(lovestruck = quark, objectOfDesire = jadzia)
    starWars.createFanFictionWith(lovestruck = luke, objectOfDesire = yoda)

    createFanFiction(starTrek)(lovestruck = quark, objectOfDesire = jadzia)
    createFanFiction(starWars)(lovestruck = luke, objectOfDesire = yoda)
  }
}

object PathDependentTypeTest4 {
  implicit def intToString(in: Int): String = in.toString

  object AwesomeDB {

    abstract class Key(name: String) {
      type Value
    }

  }

  import AwesomeDB.Key

  class AwesomeDB {

    import collection.mutable.Map

    val data = Map.empty[Key, Any]

    def get(key: Key): Option[key.Value] = data.get(key).asInstanceOf[Option[key.Value]]

    def set(key: Key)(value: key.Value): Unit = data.update(key, value)
  }

  trait IntValued extends Key {
    type Value = Int
  }

  trait StringValued extends Key {
    type Value = String
  }

  object Keys {

    val foo = new Key("foo") with IntValued
    val bar = new Key("bar") with StringValued
  }

  def main(args: Array[String]) {
    val dataSource = new AwesomeDB
    dataSource.set(Keys.bar)(23)
    val i: Option[String] = dataSource.get(Keys.bar)
    println(i)
  }
}
