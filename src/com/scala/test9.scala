package com.scala
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.random.RandomRDDs._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

object test9 {
  	val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")   
	  val sc = new SparkContext(conf)  
  	def main(args: Array[String]) 
	{ 
     val u = normalRDD(sc, 10L, 2)//生成10个服从正态分布的随机数
     u.collect().foreach { println}
     val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
     println(pos.features)
     println(pos.label)
     val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
	}
} 