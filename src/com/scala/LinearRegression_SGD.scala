package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors
import breeze.linalg.sum
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint

object LinearRegression_SGD {
  def get_mappint(rdd:RDD[Array[String]],idx:Int)={
      rdd.map { fields => fields(idx)}.distinct().zipWithIndex().collectAsMap()
      
  }
  def main(args: Array[String]){ 
      val conf = new SparkConf().setAppName("test11").setMaster("local[*]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")  
    	val sc = new SparkContext(conf)  
    	val path = "D:/spark-2.0.0/spark-2.0.0-bin-hadoop2.6/data/Bike-Sharing-Dataset/hour.csv"
    	val rdd = sc.textFile(path,4)
    	val text=rdd.filter { lines => !lines.contains("instant") }
      val records=text.map { lines => lines.split(",") }
      records.cache()
      val mappings=for(i<-Range(2,10))yield get_mappint(records,i)
      val cat_len=sum(mappings.map(_.size))
      val num_len=records.first().slice(10,14).size
      val total_len=cat_len+num_len
      
      //linear regression data 此部分代码最重要，主要用于产生训练数据集，按照前文所述处理类别特征和实数特征。
    val data=records.map{record=>
          val cat_vec=Array.ofDim[Double](cat_len)
          var i=0
          var step=0;
          for(filed<-record.slice(2,10)){
              val m=mappings(i)
              val idx=m(filed)
              cat_vec(idx.toInt+step)=1.0
              i=i+1
              step=step+m.size
          }
          val num_vec=record.slice(10,14).map(x=>x.toDouble)
          val features=cat_vec++num_vec
          val label=record(record.size-1).toInt
          LabeledPoint(label,Vectors.dense(features))
      } 
      //println(data.first())
      // val categoricalFeaturesInfo = Map[Int, Int]()
      //val linear_model=DecisionTree.trainRegressor(data,categoricalFeaturesInfo,"variance",5,32)
      val linear_model=LinearRegressionWithSGD.train(data,100,0.01)
      val true_vs_predicted=data.map(p=>(p.label,linear_model.predict(p.features)))
      //输出前五个真实值与预测值
      println( true_vs_predicted.take(5).toVector.toString())
    }   
}