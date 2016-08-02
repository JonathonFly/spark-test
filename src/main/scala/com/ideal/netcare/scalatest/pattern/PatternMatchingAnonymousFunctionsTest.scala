package com.ideal.netcare.scalatest.pattern

/**
 * Created by syf on 2016/5/23.
 */
object PatternMatchingAnonymousFunctionsTest1 {
  def main(args: Array[String]) {
    val songTitles = List("The White House", "Blue Bird", "Take Me To Your Heart")
    val lowCaseTitles = songTitles.map(_.toLowerCase)
    println(lowCaseTitles)
  }
}

object PatternMatchingAnonymousFunctionsTest2 {
  def wordsWithoutOutliers(wordFrequencies: Seq[(String, Int)]): Seq[String] = wordFrequencies.filter(wc => wc._2.toInt > 10 && wc._2.toInt < 100).map(_._1)

  def wordsWithoutOutliers2(wordFrequencies: Seq[(String, Int)]): Seq[String] = wordFrequencies.filter { case (_, c) => c > 10 && c < 100 } map { case (w, _) => w }

  def main(args: Array[String]) {
    val seq = ("Hello", 12) ::("John", 8) ::("Lucy", 34) ::("Tiny", 200) :: Nil
    val words1 = wordsWithoutOutliers(seq)
    println(words1)

    val words2 = wordsWithoutOutliers2(seq)
    println(words2)
  }
}

object PatternMatchingAnonymousFunctionsTest3 {
  val pf: PartialFunction[(String, Int), String] = {
    case (w, f) if f > 10 && f < 100 => w
  }

  def wordsWithoutOutliers(wordFrequencies:Seq[(String,Int)]):Seq[String]=wordFrequencies.collect{case (w,f) if f>10 && f<100 =>w}

  def main(args: Array[String]) {
    val seq = ("Hello", 12) ::("John", 8) ::("Lucy", 34) ::("Tiny", 200) :: Nil
    val words1 = seq.collect(pf)
    println(words1)

    val words2=wordsWithoutOutliers(seq)
    println(words2)
  }
}

