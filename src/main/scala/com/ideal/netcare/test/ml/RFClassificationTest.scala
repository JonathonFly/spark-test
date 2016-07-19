package com.ideal.netcare.test.ml

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by syf on 2016/7/19.
 */
object RFClassificationTest {
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setAppName("Random Forest Classification").setMaster("spark://spark-master:7077").set("spark.executor.memory", "512m")
    val sc = new SparkContext(conf)
    //本地打包的jar的位置  必备
    sc.addJar("target/scala-2.10/spark-test_2.10-1.0.jar")

    //加载、解析训练、测试数据文件
    //将sample_svm_data中的80%作为训练数据，20%作为测试数据
    val trainData = sc.textFile("hdfs://spark-master:9000/syf/spark/data/ml/svm/sample_svm_train_data.txt")
    val parsedTrainData = trainData.map { line =>
      val parts = line.split("\\s+")
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts.tail.map(x => x.toDouble)))
    }
    //测试数据
    val testData = sc.textFile("hdfs://spark-master:9000/syf/spark/data/ml/svm/sample_svm_test_data.txt")
    val parsedTestData = testData.map { line =>
      val parts = line.split("\\s+")
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts.tail.map(x => x.toDouble)))
    }


    //训练模型
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 3
    val featureSubsetStrategy = "auto"
    val impurity = "gini"
    val maxDepth = 4
    val maxBins = 32

    val model = RandomForest.trainClassifier(parsedTrainData, numClasses, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

    //模型预测测试数据结果
    val labelAndPreds = parsedTestData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    //计算分类错误率
    /*
    * RF和SVM和GBDT用的训练数据相同，测试数据也相同，
    * RF         的 trainErr = 0.34375
    * SVMWithSGD 的 trainErr = 0.390625
    * GBDT       的 trainErr = 0.5
    * 分类效果 ：随机森林(RF) > SVM > 梯度推进决策树(GBDT)
    */
    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / parsedTestData.count
    println(s"trainErr = ${trainErr}")
//    println("Learned classification forest model:\n" + model.toDebugString)

    //保存和加载模型
//    model.save(sc, "/hadoop/spark-model/myRandomForestClassificationModel")
//    val sameModel = RandomForestModel.load(sc, "/hadoop/spark-model/myRandomForestClassificationModel")
  }

}
