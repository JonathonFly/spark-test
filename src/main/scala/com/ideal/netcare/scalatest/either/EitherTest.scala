package com.ideal.netcare.scalatest.either

import java.net.{MalformedURLException, URL}

import scala.io.Source
import scala.util.control.Exception.catching

/**
 * Created by syf on 2016/5/24.
 */
object EitherTest1 {
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("baidu"))
      Left("It's totally a piece of shit.")
    else
      Right(Source.fromURL(url))

  def formatUrlContent(content: Iterator[String]) = content.mkString("\n")

  def main(args: Array[String]) {
    val urlList = List(new URL("http://baidu.com"), new URL("http://jonathonfly.github.io"))
    val resList = urlList.map(getContent).map {
      case Left(msg) => msg
      case Right(source) => formatUrlContent(source.getLines())
    }

    resList.foreach(println)
  }

}

object EitherTest2 {
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("baidu"))
      Left("It's totally a piece of shit.")
    else
      Right(Source.fromURL(url))

  def main(args: Array[String]) {
    val content1: Either[String, Iterator[String]] = getContent(new URL("http://baidu.com")).right.map(_.getLines)
    println(content1)

    val content2: Either[String, Iterator[String]] = getContent(new URL("http://jonathonfly.github.io")).right.map(_.getLines())
    println(content2)
  }
}

object EitherTest3 {
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("baidu"))
      Left("It's totally a piece of shit.")
    else
      Right(Source.fromURL(url))

  def main(args: Array[String]) {
    val part5 = new URL("https://windor.gitbooks.io/beginners-guide-to-scala/content/chp5-the-option-type.html")
    val part6 = new URL("https://windor.gitbooks.io/beginners-guide-to-scala/content/chp6-error-handling-with-try.html")
    val content = getContent(part5).right.flatMap(a => getContent(part6).right.map(b => (a.getLines().size + b.getLines().size) / 2))
    println(content)
  }
}

object EitherTest4 {
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("baidu"))
      Left("It's totally a piece of shit.")
    else
      Right(Source.fromURL(url))

  def averageLineCount(url1: URL, url2: URL): Either[String, Int] = for {
    source1 <- getContent(url1).right
    source2 <- getContent(url2).right
  } yield (source1.getLines().size + source2.getLines().size) / 2

  def main(args: Array[String]) {
    val part5 = new URL("https://windor.gitbooks.io/beginners-guide-to-scala/content/chp5-the-option-type.html")
    val part6 = new URL("https://windor.gitbooks.io/beginners-guide-to-scala/content/chp6-error-handling-with-try.html")
    val res = averageLineCount(part5, part6)
    println(res)
    println(res match { case Left(x) => x case Right(y) => y })
  }
}

object EitherTest5 {
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("baidu"))
      Left("It's totally a piece of shit.")
    else
      Right(Source.fromURL(url))

  def averageLineCount(url1: URL, url2: URL): Either[String, Int] = for {
    source1 <- getContent(url1).right
    source2 <- getContent(url2).right
    lines1 <- Right(source1.getLines().size).right
    lines2 <- Right(source2.getLines().size).right
  } yield (lines1 + lines2) / 2

  def main(args: Array[String]) {
    val part5 = new URL("https://windor.gitbooks.io/beginners-guide-to-scala/content/chp5-the-option-type.html")
    val part6 = new URL("https://windor.gitbooks.io/beginners-guide-to-scala/content/chp6-error-handling-with-try.html")
    val res = averageLineCount(part5, part6)
    println(res)
    println(res match { case Left(x) => x case Right(y) => y })
  }
}

object EitherTest6 {
  def getContent(url: URL): Either[String, Source] =
    if (url.getHost.contains("baidu"))
      Left("It's totally a piece of shit.")
    else
      Right(Source.fromURL(url))

  def main(args: Array[String]) {
    val content1: Iterator[String] = getContent(new URL("http://baidu.com")).fold(Iterator(_), _.getLines())
    content1.foreach(println)
    val content2: Iterator[String] = getContent(new URL("http://jonathonfly.github.io")).fold(Iterator(_), _.getLines())
    content2.foreach(println)
  }
}

object EitherTest7 {
  def handling[Ex <: Throwable, T](exType: Class[Ex])(block: => T): Either[Ex, T] =
    catching(exType).either(block).asInstanceOf[Either[Ex, T]]

  def parseURL(url: String): Either[MalformedURLException, URL] =
    handling(classOf[MalformedURLException])(new URL(url))

  def main(args: Array[String]) {
    val url = parseURL("http://adsf")
    println(url)
  }
}

object EitherTest8 {

  case class Customer(age: Int)

  class Cigarettes

  case class UnderAgeFailure(age: Int, required: Int)

  def buyCigarettes(customer: Customer): Either[UnderAgeFailure, Cigarettes] =
    if (customer.age < 18)
      Left(UnderAgeFailure(customer.age, 18))
    else
      Right(new Cigarettes)

  def main(args: Array[String]) {
    println(buyCigarettes(new Customer(13)))
  }

}

/*
* BlackListedResource 表示黑名单里的网站 URL，外加试图访问这个网址的公民集合。
现在我们想处理这个黑名单，为了标识 “有问题” 的公民，比如说那些试图访问被屏蔽网站的人。
 同时，我们想确定可疑的 Web 网站：如果没有一个公民试图去访问黑名单里的某一个网站，
 那么就必须假定目标对象因为一些我们不知道的原因绕过了筛选器，需要对此进行调查。*/

object EitherTest9 {
  type Citizen = String

  case class BlackListedResource(url: URL, visitors: Set[Citizen])

  def main(args: Array[String]) {
    val blackList = List(
      BlackListedResource(new URL("http://baidu.com"), Set("Tom", "Jonathan")),
      BlackListedResource(new URL("https://www.yahoo.com"), Set.empty),
      BlackListedResource(new URL("http://maps.baidu.com"), Set.empty),
      BlackListedResource(new URL("http://picture.baidu.com"), Set("Lucy"))
    )

    val checkedBlackList: List[Either[URL, Set[Citizen]]] =
      blackList.map(resource =>
        if (resource.visitors.isEmpty) Left(resource.url)
        else Right(resource.visitors))

    val suspiciousResources=checkedBlackList.flatMap(_.left.toOption)
    val problemCitizens=checkedBlackList.flatMap(_.right.toOption).flatten.toSet

    println(suspiciousResources)
    println(problemCitizens)
  }
}