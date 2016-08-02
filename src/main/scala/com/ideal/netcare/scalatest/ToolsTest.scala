package com.ideal.netcare.scalatest

import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint

/**
 * Created by syf on 2016/7/18.
 */
object ToolsTest {
  def main(args: Array[String]) {
    //向量
    //密集向量
    println("向量")
    val dv: Vector = Vectors.dense(1.0, 0.0, 3.0)
    //稀疏向量
    val sv: Vector = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
    val sv2: Vector = Vectors.sparse(3, Seq((0, 1.0), (2, 3.0)))
    println(s"dv=${dv}")
    println(s"sv=${sv}")
    println(s"sv2=${sv2}")
    println

    //标量
    println("标量")
    val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
    val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
    println(s"pos=${pos}")
    println(s"neg=${neg}")
    println

    //矩阵
    println("矩阵")
    val dm:Matrix=Matrices.dense(3,2,Array(1.0,3.0,5.0,2.0,4.0,6.0))
    println("dm=")
    println(dm)
    println

    //符号->  A->B表示(A,B)   功能：转化为二元组
    println("符号->")
    val a="abc"
    val b=List(1,2,3)
    val d=Array(4,5)
    val c=a->b->d
    println(s"c._1._1=${c._1._1}")
    c._1._2.foreach(println)
    c._2.toVector.foreach(println)
    println

    //符号<-  用于遍历集合中的元素
    println("符号<-")
    val ver=c._2.toVector
    for(v<-ver){
      println(v)
    }
    println

    println("Option")
    val myMap:Map[String,String]=Map("key1"->"value1")
    val value1:Option[String]=myMap.get("key1")
    val value2:Option[String]=myMap.get("key2")

    value1 match{
      case Some(value) => println(value.length)
      case None => println("None")
    }

    val value3=value2.getOrElse(0)
    println(value3)
    println
  }
}
