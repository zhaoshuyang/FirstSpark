package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import org.apache.spark.mllib.linalg.{ SparseVector => SV }
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF

import breeze.linalg._

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.evaluation.MulticlassMetrics
object Word2Vec {
	
	/**
	 * 分词
	 * @param line
	 * @return
	 */
	def tokenize(line: String,stopwords:Seq[String],rareTokens:Seq[String]) = {
	   	val regex = """[^0-9]*""".r    
	    line.split("""\W+""")
    		.map(_.toLowerCase)
    		.filter(token => regex.pattern.matcher(token).matches)
    		.filterNot(token => stopwords.contains(token))
    		.filterNot(token => rareTokens.contains(token))
    		.filter(token => token.size >= 2)
    		.toSeq
  }
	
	/**
	 * 获取停用词
	 * @param line
	 * @return
	 */
	def getStopwords(line:RDD[String]) = {
	      val regext = """[^0-9]*""".r
	      val nonWordSplit = line.flatMap(t => t.split("""\W+""").map(_.toLowerCase))	
  			val filterNumbers = nonWordSplit.filter(a => regext.pattern.matcher(a).matches)
				val tokenCounts = nonWordSplit.map(t => (t, 1)).reduceByKey(_ + _)
        val oreringDesc = Ordering.by[(String, Int), Int](_._2)
        val stopwords =tokenCounts.top(20)(oreringDesc).map(_._1).toSeq
        //sc.parallelize(stopwords).saveAsTextFile("C:/Users/dj/Desktop/tt/d")
        stopwords
	}
	
	/**
	 * 获取低频词
	 * @param line
	 * @return
	 */
	def getRareTokens(line:RDD[String]) ={
	  val regext = """[^0-9]*""".r
    val nonWordSplit = line.flatMap(t => t.split("""\W+""").map(_.toLowerCase))	
		val filterNumbers = nonWordSplit.filter(a => regext.pattern.matcher(a).matches)
		val tokenCounts = nonWordSplit.map(t => (t, 1)).reduceByKey(_ + _)
	  val rareTokens=tokenCounts.filter{case (k, v) => k.size < 2 }.map{case (k,v)=>k}.collect.toSeq
	  rareTokens
	}
	/**
	 * 获取文章分词[去掉“停用词”和“低频词”]
	 * @param line
	 * @return
	 */
	def getTokens(line:RDD[String],stopwords:Seq[String],rareTokens:Seq[String])={
	  line.map { doc => tokenize(doc,stopwords,rareTokens) }
	}
	
	def countSim(breeze1: SparseVector[Double],breeze2:SparseVector[Double])={	   
      val cosineSim = breeze1.dot(breeze2) / (norm(breeze1) * norm(breeze2))
      cosineSim
	}	
	def main(args: Array[String]){ 	
	    val conf = new SparkConf().setAppName("test11").setMaster("local[*]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")  
    	val sc = new SparkContext(conf)  
    	val path = "D:/spark-2.0.0/spark-2.0.0-bin-hadoop2.6/data/20news-bydate/20news-bydate-train/rec.sport.*"
      //val path = "D:/spark-2.0.0/spark-2.0.0-bin-hadoop2.6/data/20news-bydate/20news-bydate-train/*"
    	val rdd = sc.wholeTextFiles(path)
    	println(rdd.partitions.size)
    	val text=rdd.map(_._2)	
    	text.cache()
    	val stopwords=getStopwords(text)
      val rareTokens=getRareTokens(text)      
      val tokens=getTokens(text,stopwords,rareTokens) 
      import org.apache.spark.mllib.feature.Word2Vec
      val word2vec=new Word2Vec()
	    word2vec.setSeed(42)
	    val word2vecModel=word2vec.fit(tokens)
	    word2vecModel.findSynonyms("hockey", 10).foreach(println)
	}
}