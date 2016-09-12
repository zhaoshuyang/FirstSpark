package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import org.apache.spark.mllib.linalg.{ SparseVector => SV }
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF

import breeze.linalg._
object test12 {
	
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
    	val path = "D:/Spark1.6.2/spark/data/20news-bydate/20news-bydate-train/rec.sport.hockey/*"
    	val rdd = sc.wholeTextFiles(path,4)
    	val text=rdd.map(_._2)	
    	text.cache()
    	val stopwords=getStopwords(text)
      val rareTokens=getRareTokens(text)    
      val tokens=getTokens(text,stopwords,rareTokens) 
      val hockeyText = rdd.filter { case (file, text) => file.contains("hockey") }
	    val hockeyText_new=hockeyText.map{case (path,content)=>(path.split("/").takeRight(1).head,content)}
	    val dim = math.pow(2, 18).toInt
      val hashingTF = new HashingTF(dim)
	    val tf = hashingTF.transform(tokens)
      tf.cache
	    val idf = new IDF().fit(tf)
	    
      val hockeyTF = hockeyText_new.mapValues(doc => hashingTF.transform(tokenize(doc,stopwords,rareTokens) ))     
      val hockeyTfIdf = hockeyTF.mapValues{idf.transform(_)}
      val givenHockeyTfIdf=hockeyTfIdf.lookup("52554").apply(0)
      val hockey1 = givenHockeyTfIdf.asInstanceOf[SV]
      val breeze1 = new SparseVector(hockey1.indices, hockey1.values, hockey1.size)
      val si=hockeyTfIdf.mapValues {a=>{val hockey2=a.asInstanceOf[SV];val breeze2 = new SparseVector(hockey2.indices, hockey2.values, hockey2.size);countSim(breeze1,breeze2)}}
      val orderingDesc=Ordering.by  [(String,Double),Double] (_._2)
      si.top(3)(orderingDesc).foreach { println }
	}
}