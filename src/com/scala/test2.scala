package com.scala
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation._

object test2 {
  def main(args: Array[String]) 
	{   
  	val conf = new SparkConf().setAppName("Simple Application").setMaster("local[2]")
		val sc = new SparkContext(conf)
  	val user_data=sc.textFile("D:/spark-1.5.2-bin-hadoop2.6/data/ml-100k/u.data")
  	val rawRatings=user_data.map {a=>a.split("\t").take(3)}
  	val ratings=rawRatings.map { case Array(user,movie,rating) => Rating(user.toInt,movie.toInt,rating.toDouble) }
  	val model = ALS.train(ratings,50,10,0.01)
  	//model.userFeatures.saveAsTextFile("D:/tt")
  	println(model.userFeatures.count())
	}
}