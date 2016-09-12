package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._

class test10_c {
  val conf = new SparkConf().setAppName("Simple Application").setMaster("local")  
	val sc = new SparkContext(conf)  
	def drive():Int=
	{ 
		    val path = "D:/Spark1.6.2/spark/data/20news-bydate/20news-bydate-train/alt.atheism/*"
				val rdd = sc.wholeTextFiles(path)
				val text=rdd.map(_._2)
				val newsgroups = rdd.map { case (file, text) => file.split("/").takeRight(2).head }
		    val countByGroup = newsgroups.map(n => (n, 1)).reduceByKey(_ + _).collect.sortBy(-_._2).mkString("\n")
				val whiteSpaceSplit = text.flatMap(t => t.split(" ").map(_.toLowerCase))
				val nonWordSplit = text.flatMap(t => t.split("""\W+""").map(_.toLowerCase))
				val regex = """[^0-9]*""".r
				val filterNumbers = nonWordSplit.filter(token => regex.pattern.matcher(token).matches)
				val tokenCounts = filterNumbers.map(t => (t, 1)).reduceByKey(_ + _)
        val oreringDesc = Ordering.by[(String, Int), Int](_._2)
        val stopwords =tokenCounts.top(100)(oreringDesc).map(_._1).toSeq
        val tokenCountsFilteredStopwords = tokenCounts.filter { case (k, v) => !stopwords.contains(k) }
        val tokenCountsFilteredSize = tokenCountsFilteredStopwords.filter { case (k, v) => k.size >= 2 }
        val tokens=tokenCountsFilteredSize.map(_._1).collect().toSeq
        tokens.size
        
	}
}