package com.scala  
import org.apache.spark.SparkContext 
import org.apache.spark.SparkContext._ 
import org.apache.spark.SparkConf 
object test1 
{    
	def main(args: Array[String]) 
	{      
		//val logFile = "D:/spark-1.5.2-bin-hadoop2.6/data/mllib/sample_tree_data.csv" // Should be some file on your system
		val logFile = "hdfs://192.168.68.128:9000/user/hadoop/input/core-site.xml"
	    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
		val sc = new SparkContext(conf)
		val data=sc.textFile(logFile).filter(a=>a.contains("cont"));
		data.collect.foreach(println);
/*		val data=sc.textFile(logFile).map(line => line.split(",")).map(x=>(x(0),x(1),x(2),x(3),x(4)))
		val data_t=sc.textFile(logFile).flatMap(line => line.split(","))
		val numPurchases_t=data_t.count()
		val numPurchases=data.count()
		val uniqueRevenue=data.map{case(a,b,c,d,e)=>a}.distinct().count()
		val uniqueRevenue_t=data.map{a=>a._1}.distinct().count()
		val totalRevenue = data.map{case(a,b,c,d,e)=>c.toDouble}.sum();
		val totalRevenue_t = data.map{c=>c._3.toDouble}.sum();
		val productsByPopularity=data.map{case(a,b,c,d,e)=>(b,1)}.reduceByKey(_+_).collect().sortBy(-_._2);
		println("numPurchases="+numPurchases)
		println("uniqueRevenue="+uniqueRevenue)
		println("totalRevenue="+totalRevenue)
		println("productsByPopularity="+productsByPopularity(0))*/
	} 
} 
