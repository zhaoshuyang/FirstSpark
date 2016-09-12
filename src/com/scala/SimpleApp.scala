package com.scala  
import org.apache.spark.SparkContext 
import org.apache.spark.SparkContext._ 
import org.apache.spark.SparkConf 
object SimpleApp 
{    
	def main(args: Array[String]) 
	{      
		val logFile = "D:/spark-1.5.2-bin-hadoop2.6/LICENSE" // Should be some file on your system      
		val conf = new SparkConf().setAppName("Simple Application").setMaster("local")   
		val sc = new SparkContext(conf)      
		val logData = sc.textFile(logFile,5)
		val logData_rep=logData.repartition(3)
		
		val numAs = logData.filter(line => line.contains("Apache License")).count()     
		val numBs = logData.filter(line => line.contains("h")).count()      
		println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))   
		println(logData_rep.partitions.size)
		sc.stop();
	} 
} 
