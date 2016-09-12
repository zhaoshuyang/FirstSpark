package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._

object test7 {
  val url="jdbc:mysql://192.168.2.231:3306/finance_source"
	val prop = new java.util.Properties
	prop.setProperty("user","root")
	prop.setProperty("password","root")
	val conf = new SparkConf().setAppName("Simple Application").setMaster("local");
	val sc = new SparkContext(conf);
	val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  def main(args: Array[String]): Unit = {
    
    //指定读取条件,这里 Array("country_code='CN'") 是where过滤条件
  //val cnFlight = sqlContext.read.jdbc(url,"STKCN_BAS_INFO",Array("com_id LIKE '153%'","exch_code=105"),prop)
    val cnFlight = sqlContext.read.jdbc(url,"STKCN_BAS_INFO",prop)
    cnFlight.agg(Map("id" -> "max", "secu_id" -> "avg"))
    cnFlight.repartition(2)
    println(cnFlight.rdd.partitions.size)
    //println(cnFlight.printSchema)
  /*   //然后进行groupby 操作,获取数据集合
    val emailList = cnFlight.groupBy("gps_city", "user_mail")*/

  }

}