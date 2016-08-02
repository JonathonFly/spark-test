package com.ideal.netcare.graphx

import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.{Logging, SparkConf, SparkContext}

/**
 * Created by syf on 2016/6/24.
 */
object Dijkstra extends Logging {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    GraphXUtils.registerKryoClasses(conf)
    conf.setAppName("Spark Dijkstra").setMaster("spark://spark-master:7077").set("spark.executor.memory", "512m")
    val sc = new SparkContext(conf)
    //本地打包的jar的位置  必备
    sc.addJar("target/scala-2.10/spark-test_2.10-1.0.jar")

    val fileV = sc.textFile("hdfs://spark-master:9000/syf/spark/data/dijkstra/vertex.txt")
    //(Double,String,String)  指的是  (最短路径长度, 顶点的名称, 最短路径上的点)
    val vertexes: RDD[(VertexId, (Double, String, String))] = fileV.map { line =>
      val parts = line.split("\\s+")
      (parts(0).toLong, (Double.PositiveInfinity, parts(1), ""))
    }.cache()

    val fileE = sc.textFile("hdfs://spark-master:9000/syf/spark/data/dijkstra/edge.txt")
    val edges: RDD[Edge[String]] = fileE.map { line =>
      val parts = line.split("\\s+")
      Edge(parts(0).toLong, parts(1).toLong, parts(2))
    }

    val graph = Graph(vertexes, edges)

    // 输出Graph的信息
    //    graph.vertices.collect().foreach(println(_))
    //    graph.triplets.map(triplet => triplet.srcAttr._2 + "----->" + triplet.dstAttr._2 + "    attr:" + triplet.attr).collect().foreach(println(_))

    val sourceId: VertexId = 1 // The ultimate source
    var sourceName = ""
    vertexes.filter(_._1==sourceId).collect().foreach(rdd=> sourceName=rdd._2._2)

    println("sourceName=" + sourceName)

    val initialGraph = graph.mapVertices((id, attr) => if (id == sourceId) (0.0, attr._2, sourceName) else (Double.PositiveInfinity, attr._2, sourceName))
    initialGraph.cache()

    val sssp = initialGraph.pregel((Double.PositiveInfinity, "", ""))(
      (id, dist, newDist) => {
        if (dist._1 < newDist._1) dist else (newDist._1, dist._2, newDist._3)
      }, // Vertex Program
      triplet => {
        // Send Message
        if (triplet.srcAttr._1 + triplet.attr.toDouble < triplet.dstAttr._1) {
          Iterator((triplet.dstId, ((triplet.srcAttr._1 + triplet.attr.toDouble), triplet.dstAttr._2, triplet.srcAttr._3 + "->" + triplet.dstAttr._2)))
        } else {
          Iterator.empty
        }
      },
      (a, b) => {
        if (a._1 < b._1) a else b
      } // Merge Message
    )
    println(sssp.vertices.map(_._2).collect.mkString("\n"))
  }
}
