package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql.hive.HiveContext
import java.sql.DriverManager
import java.sql.ResultSet
import org.apache.spark.rdd.JdbcRDD

object test6 {

  def createConnection()={
    Class.forName("org.apache.hive.jdbc.HiveDriver").newInstance();
    DriverManager.getConnection("jdbc:hive2://192.168.68.128:10000/hive", "hive", "hive")
  }
  def extractValue(r:ResultSet)={
    (r.getInt(2),r.getString(3))
  }
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local");
	val sc = new SparkContext(conf);
	val data=new JdbcRDD(sc,createConnection,"SELECT * FROM bas_com_info WHERE ?<=ID AND ID<=?",lowerBound=90000,upperBound=100000,numPartitions=2,mapRow=extractValue)
	data.collect.foreach(println)
	sc.stop
  }
   
}