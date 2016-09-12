package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.linalg._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.stat.Statistics


object test8 {
	val conf = new SparkConf().setAppName("Simple Application").setMaster("local")   
	val sc = new SparkContext(conf)  
  val logFile = "D:/spark-1.5.2-bin-hadoop2.6/LICENSE" // Should be some file on your system   
  val logData = sc.textFile(logFile,5)
	def main(args: Array[String]) 
	{ 
      val logData = sc.textFile(logFile,1).map ( _.trim().split(" ").map { a => a.length().toDouble } ).map { line => Vectors.dense(line) }
      println(logData.take(3).toArray.foreach { println })
	}
}