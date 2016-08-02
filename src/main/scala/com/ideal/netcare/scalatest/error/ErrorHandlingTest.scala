package com.ideal.netcare.scalatest.error

import java.io.{FileNotFoundException, InputStream}
import java.net.{MalformedURLException, URL}

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * Created by syf on 2016/5/23.
 */
object ErrorHandlingTest1 {

  case class Customer(age: Int)

  class Cigarettes

  case class UnderAgeException(message: String) extends Exception(message)

  def buyCigarettes(customer: Customer): Cigarettes = if (customer.age < 16) throw UnderAgeException(s"Customer must be older than 16 but was ${customer.age}") else new Cigarettes

  def main(args: Array[String]) {
    val customer = new Customer(17)
    val message = try {
      buyCigarettes(customer)
      "Yo, here are your cancer sticks! Happy smoking!"
    }
    catch {
      case UnderAgeException(msg) => msg
    }

    println(message)
  }
}

object ErrorHandlingTest2 {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  def main(args: Array[String]) {
    val url1 = parseURL("http://www.baidu.com")
    println(url1)

    val url2 = parseURL("whatthefuckman")
    println(url2)

    val url3 = parseURL(Console.readLine("Please Input URL: ")) getOrElse new URL("http://www.baidu.com")
    println(url3)

    val url4 = parseURL("http://www.baidu.com").map(_.getProtocol)
    println(url4)

    val url5 = parseURL("ftp://asdfasfasfadsf").map(_.getProtocol)
    println(url5)
  }
}

object ErrorHandlingTest3 {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  def inputStreamForURL1(url: String): Try[Try[Try[InputStream]]] = parseURL(url).map {
    u => Try(u.openConnection()).map(conn => Try(conn.getInputStream))
  }

  def inputStreamForURL2(url: String): Try[InputStream] = parseURL(url).flatMap {
    u => Try(u.openConnection()).flatMap(conn => Try(conn.getInputStream))
  }

  def main(args: Array[String]) {
    val url1 = inputStreamForURL1("http://www.baidu.com")
    println(url1)

    val url2 = inputStreamForURL2("wwe")
    println(url2)
  }
}

object ErrorHandlingTest4 {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  def parseHttpURL(url: String) = parseURL(url).filter(_.getProtocol == "http")

  def main(args: Array[String]) {
    parseHttpURL("http://www.baidu.com").foreach(println)

    parseHttpURL("ftp://asdfafa").foreach(println)
  }
}

object ErrorHandlingTest5 {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  def getUrlContent(url: String): Try[Iterator[String]] = for {
    url <- parseURL(url)
    conn <- Try(url.openConnection())
    is <- Try(conn.getInputStream)
    source = Source.fromInputStream(is)
  } yield source.getLines()

  def main(args: Array[String]) {
    getUrlContent("http://www.baidu.com") match {
      case Success(lines) => lines.foreach(println)
      case Failure(ex) => println(s"Problem occurred :${ex.getMessage}")
    }
  }
}

object ErrorHandlingTest6 {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  def getUrlContent(url: String): Try[Iterator[String]] = for {
    url <- parseURL(url)
    conn <- Try(url.openConnection())
    is <- Try(conn.getInputStream)
    source = Source.fromInputStream(is)
  } yield source.getLines()

  def main(args: Array[String]) {
    val res = getUrlContent("http://test.com") recover {
      case e: FileNotFoundException => Iterator("Request page does not exist")
      case e: MalformedURLException => Iterator("Please make sure to enter a valid URL")
      case _ => Iterator("An unexpected error has occurred. we are so sorry!")
    }
    res.get.foreach(println)
  }
}
