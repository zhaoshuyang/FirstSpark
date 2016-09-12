package com.scala
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.hive.HiveContext
object test3 {
	def main(args: Array[String]) 
	{   
		val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
		val sc = new SparkContext(conf)
    val data=sc.textFile("C:/Users/dj/Desktop/《Spark高级数据分析》--源代码/data/profiledata_06-May-2005/profiledata_06-May-2005/user_artist_data.txt")
    val changeData=data.flatMap { x => x.split(" ") }
		println(changeData.count())
		val cdmap=changeData.map ( x => (x,x.size) ).filter(_._2==2)
		println(cdmap.count())
		cdmap.take(10).foreach {println}
		//cdmap.groupBy(x=>x._2).take(3).foreach {println}
	}
}