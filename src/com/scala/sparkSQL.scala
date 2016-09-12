package com.scala

import org.apache.spark.SparkConf 
import org.apache.spark.SparkContext 
import org.apache.spark.sql.SQLContext

object sparkSQL {
  case class Person(name: String, age: Int)// case class在Scala 2.10里面最多支持22个列，，为了突破这个现实，最好是定义一个类实现Product接口
	def main(arg:Array[String]){
		val conf = new SparkConf().setAppName("Simple Application").setMaster("local");
		val sc = new SparkContext(conf);
		val sqlContext = new org.apache.spark.sql.SQLContext(sc)
		import sqlContext._	
  	val people = sc.textFile("C:/Users/dj/Desktop/learning-spark-master/files/sparkSQL.txt").map(_.split(",")).map(p => Person(p(0), p(1).trim.toInt))
  	/*people.registerAsTable("people");
  	val teenagers = sql("SELECT name FROM people WHERE age >= 13 AND age <= 19")// 直接写sql吧，这个方法是sqlContext提供的
  	teenagers.map(t => "Name: " + t(0)).collect().foreach(println)// teenagers是SchemaRDDs类型，它支持所有普通的RDD操作
*/	}
}