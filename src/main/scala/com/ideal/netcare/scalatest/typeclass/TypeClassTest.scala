package com.ideal.netcare.scalatest.typeclass

import com.ideal.netcare.scalatest.typeclass.Math.NumberLike

/**
 * Created by syf on 2016/5/26.
 */
object TypeClassTest1 {
  def median(xs: Vector[Double]): Double = xs(xs.size / 2)

  def quartiles(xs: Vector[Double]): (Double, Double, Double) = (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr(xs: Vector[Double]): Double = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => upperQuartile - lowerQuartile
  }

  def mean(xs: Vector[Double]): Double = {
    xs.reduce(_ + _) / xs.size
  }

  def main(args: Array[String]) {
    val numbers = Vector(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
    val medianNum = median(numbers)
    println(s"median result = $medianNum")

    val quartilesNum = quartiles(numbers)
    println(s"quartiles result = $quartilesNum")

    val iqrNum = iqr(numbers)
    println(s"iqr result = $iqrNum")

    val meanNum = mean(numbers)
    println(s"mean result = $meanNum")
  }
}

object TypeClassTest2 {

  trait NumberLike[A] {
    def get: A

    def plus(y: NumberLike[A]): NumberLike[A]

    def minus(y: NumberLike[A]): NumberLike[A]

    def divide(y: Int): NumberLike[A]
  }

  case class NumberLikeDouble(x: Double) extends NumberLike[Double] {
    def get: Double = x

    def plus(y: NumberLike[Double]) = NumberLikeDouble(x + y.get)

    def minus(y: NumberLike[Double]) = NumberLikeDouble(x - y.get)

    def divide(y: Int) = NumberLikeDouble(x / y)
  }

  case class NumberLikeInt(x: Int) extends NumberLike[Int] {
    def get: Int = x

    def plus(y: NumberLike[Int]) = NumberLikeInt(x + y.get)

    def minus(y: NumberLike[Int]) = NumberLikeInt(x - y.get)

    def divide(y: Int) = NumberLikeInt(x / y)
  }

  type Quartile[A] = (NumberLike[A], NumberLike[A], NumberLike[A])

  def median[A](xs: Vector[NumberLike[A]]): NumberLike[A] = xs(xs.size / 2)

  def quartiles[A](xs: Vector[NumberLike[A]]): Quartile[A] = (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[A](xs: Vector[NumberLike[A]]): NumberLike[A] = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => upperQuartile.minus(lowerQuartile)
  }

  def mean[A](xs: Vector[NumberLike[A]]): NumberLike[A] = {
    xs.reduce(_.plus(_)).divide(xs.size)
  }

  def main(args: Array[String]) {
    val numbersDouble = Vector(NumberLikeDouble(0.1), NumberLikeDouble(0.2), NumberLikeDouble(0.3), NumberLikeDouble(0.4), NumberLikeDouble(0.5), NumberLikeDouble(0.6), NumberLikeDouble(0.7), NumberLikeDouble(0.8), NumberLikeDouble(0.9), NumberLikeDouble(1.0))
    println("NumberLikeDouble result:")
    val medianDouble = median(numbersDouble)
    println(s"median result = $medianDouble")

    val quartilesDouble = quartiles(numbersDouble)
    println(s"quartiles result = $quartilesDouble")

    val iqrDouble = iqr(numbersDouble)
    println(s"iqr result = $iqrDouble")

    val meanDouble = mean(numbersDouble)
    println(s"mean result = $meanDouble")

    println
    println

    val numbersInt = Vector(NumberLikeInt(1), NumberLikeInt(2), NumberLikeInt(3), NumberLikeInt(4), NumberLikeInt(5), NumberLikeInt(6), NumberLikeInt(7), NumberLikeInt(8), NumberLikeInt(9), NumberLikeInt(10))
    println("NumberLikeInt result:")
    val medianInt = median(numbersInt)
    println(s"median result = $medianInt")

    val quartilesInt = quartiles(numbersInt)
    println(s"quartiles result = $quartilesInt")

    val iqrInt = iqr(numbersInt)
    println(s"iqr result = $iqrInt")

    val meanInt = mean(numbersInt)
    println(s"mean result = $meanInt")
  }
}

object Math {

  trait NumberLike[T] {
    def plus(x: T, y: T): T

    def divide(x: T, y: Int): T

    def minus(x: T, y: T): T
  }

  object NumberLike {

    implicit object NumberLikeDouble extends NumberLike[Double] {
      def plus(x: Double, y: Double): Double = x + y

      def divide(x: Double, y: Int): Double = x / y

      def minus(x: Double, y: Double): Double = x - y
    }

    implicit object NumberLikeInt extends NumberLike[Int] {
      def plus(x: Int, y: Int): Int = x + y

      def divide(x: Int, y: Int): Int = x / y

      def minus(x: Int, y: Int): Int = x - y
    }

  }

}

object TypeClassTest3 {

  def median[T: NumberLike](xs: Vector[T]): T = xs(xs.size / 2)

  def quartiles[T: NumberLike](xs: Vector[T]): (T, T, T) = (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[T: NumberLike](xs: Vector[T]): T = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => implicitly[NumberLike[T]].minus(upperQuartile, lowerQuartile)
  }

  def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T = ev.divide(xs.reduce(ev.plus(_, _)), xs.size)

  def main(args: Array[String]) {
    val numbersDouble = Vector[Double](1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("NumberLikeDouble result:")
    val medianDouble = median(numbersDouble)
    println(s"median result = $medianDouble")

    val quartilesDouble = quartiles(numbersDouble)
    println(s"quartiles result = $quartilesDouble")

    val iqrDouble = iqr(numbersDouble)
    println(s"iqr result = $iqrDouble")

    val meanDouble = mean(numbersDouble)
    println(s"mean result = $meanDouble")

    println
    println

    val numbersInt = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("NumberLikeInt result:")
    val medianInt = median(numbersInt)
    println(s"median result = $medianInt")

    val quartilesInt = quartiles(numbersInt)
    println(s"quartiles result = $quartilesInt")

    val iqrInt = iqr(numbersInt)
    println(s"iqr result = $iqrInt")

    val meanInt = mean(numbersInt)
    println(s"mean result = $meanInt")
  }
}

object TypeClassTest4 {

  import JodaImplicits._
  import org.joda.time.Duration._

  object JodaImplicits {

    import org.joda.time.Duration

    implicit object NumberLikeDuration extends NumberLike[Duration] {
      def plus(x: Duration, y: Duration): Duration = x.plus(y)

      def divide(x: Duration, y: Int): Duration = Duration.millis(x.getMillis / y)

      def minus(x: Duration, y: Duration): Duration = x.minus(y)
    }

  }

  def median[T: NumberLike](xs: Vector[T]): T = xs(xs.size / 2)

  def quartiles[T: NumberLike](xs: Vector[T]): (T, T, T) = (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[T: NumberLike](xs: Vector[T]): T = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => implicitly[NumberLike[T]].minus(upperQuartile, lowerQuartile)
  }

  def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T = ev.divide(xs.reduce(ev.plus(_, _)), xs.size)

  def main(args: Array[String]) {
    val durations = Vector(standardSeconds(20), standardSeconds(57), standardMinutes(2),
      standardMinutes(17), standardMinutes(30), standardMinutes(58), standardHours(2),
      standardHours(5), standardHours(8), standardHours(17), standardDays(1),
      standardDays(4))
    println(mean(durations).getStandardHours)
  }
}
