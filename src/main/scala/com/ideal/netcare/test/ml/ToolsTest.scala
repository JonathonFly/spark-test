package com.ideal.netcare.test.ml

import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
 * Created by syf on 2016/7/18.
 */
object ToolsTest {
  def main(args: Array[String]) {
    //向量
    //密集向量
    val dv: Vector = Vectors.dense(1.0, 0.0, 3.0)
    //稀疏向量
    val sv: Vector = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
    val sv2: Vector = Vectors.sparse(3, Seq((0, 1.0), (2, 3.0)))
    println(s"dv=${dv}")
    println(s"sv=${sv}")
    println(s"sv2=${sv2}")

    //标量
    val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
    val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
    println(s"pos=${pos}")
    println(s"neg=${neg}")

    //矩阵
    val dm:Matrix=Matrices.dense(3,2,Array(1.0,3.0,5.0,2.0,4.0,6.0))
    println("dm=")
    println(dm)
  }
}
