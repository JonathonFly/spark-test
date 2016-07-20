package com.ideal.netcare.test.ml

import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by syf on 2016/7/19.
 */
object SVMWithSGDTest {
  //SGD:梯度下降法
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setAppName("SVM with SGD Classification").setMaster("spark://spark-master:7077").set("spark.executor.memory", "512m")
    val sc = new SparkContext(conf)
    //本地打包的jar的位置  必备
    sc.addJar("target/scala-2.10/spark-test_2.10-1.0.jar")

    //加载、解析训练、测试数据文件
    //将sample_svm_data中的80%作为训练数据，20%作为测试数据
    //训练数据
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

    //设置迭代次数并训练模型
    //10次迭代 ，分类错误率 trainErr = 0.40625
    //20次迭代 ，分类错误率 trainErr = 0.390625
    //50次迭代 ，分类错误率 trainErr = 0.375
    val numIterations = 50
    val model = SVMWithSGD.train(parsedTrainData, numIterations)

    //模型预测测试数据结果
    val labelAndPreds = parsedTestData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    //计算分类错误率
    /*
    * RF和SVM和GBDT用的训练数据相同，测试数据也相同，
    * RF         的 trainErr = 0.328125   50棵分类树
    * SVMWithSGD 的 trainErr = 0.375      迭代次数50
    * GBDT       的 trainErr = 0.453125   迭代次数50
    */
    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / parsedTestData.count
    println(s"trainErr = ${trainErr}")
  }
}
