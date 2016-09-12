package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors

import breeze.linalg.sum
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree

object DecisionTree_DT {
    def main(args:Array[String])={
    val conf = new SparkConf().setAppName("test11").setMaster("local[*]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")  
    val sc = new SparkContext(conf) 
    val records=sc.textFile("D:/spark-2.0.0/spark-2.0.0-bin-hadoop2.6/data/Bike-Sharing-Dataset/hour.csv").filter { lines => !lines.contains("instant") }.map(_.split(",")).cache()
    val mappings=for(i<-Range(2,10))yield get_mapping(records,i)
    val cat_len=sum(mappings.map(_.size))
    val num_len=records.first().slice(10,14).size
    val total_len=cat_len+num_len
    val data=records.map{record=>
      val features=record.slice(2,14).map(_.toDouble)
      val label=record(record.size-1).toDouble
      LabeledPoint(label,Vectors.dense(features))
    }
    val categoricalFeaturesInfo = Map[Int, Int]()
    val tree_model=DecisionTree.trainRegressor(data,categoricalFeaturesInfo,"variance",5,32)
    val true_vs_predicted=data.map(p=>(p.label,tree_model.predict(p.features)))
    println( true_vs_predicted.take(5).toVector.toString())
  }
  def get_mapping(rdd:RDD[Array[String]], idx:Int)={
     rdd.map(filed=>filed(idx)).distinct().zipWithIndex().collectAsMap()
  }
}