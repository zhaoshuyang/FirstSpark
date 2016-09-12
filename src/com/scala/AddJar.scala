package com.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object AddJar {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("spark://192.168.68.128:50020")
    val sc = new SparkContext(conf)
    //sc.addJar("F:\\spark-scala-wordcount-network-assembly-1.0.jar")
  }

}