package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql.hive.HiveContext
import java.sql.DriverManager
import java.sql.ResultSet
import org.apache.spark.rdd.JdbcRDD

object test5 {
  def createConnection()={
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    DriverManager.getConnection("jdbc:mysql://192.168.2.231:3306/finance_source?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull","root","root")
  }
  def extractValue(r:ResultSet)={
    (r.getInt(2),r.getString(3))
  }
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()//.setAppName("Test5").setMaster("yarn-client");
	val sc = new SparkContext(conf);
	val data=new JdbcRDD(sc,createConnection,"SELECT * FROM bas_com_info WHERE ?<=ID AND ID<=?",lowerBound=90000,upperBound=100000,numPartitions=2,mapRow=extractValue)
	data.take(3).foreach(println)
	sc.stop
  }
   
}