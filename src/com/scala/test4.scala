package com.scala

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.linalg.Vectors

/**
 * Created by xiaojun on 2015/10/19.
 */
object test4 {
	def main(args: Array[String]) {

  		 val conf = new SparkConf().setAppName("TfIdfTest").setMaster("local")
  		 val sc = new SparkContext(conf)
       val input_int=sc.parallelize(List(1,2,3,4))
       val input_double=input_int.map(a=>a.toDouble)
       val stats=input_double.stats()
       val me=stats.max;
       println(me)
       val observations = sc.parallelize(
          Seq(
            Vectors.dense(1.0, 10.0, 100.0),
            Vectors.dense(2.0, 20.0, 200.0),
            Vectors.dense(3.0, 30.0, 300.0)
          )
        )
        
        // Compute column summary statistics.
        val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)
        println(summary.mean)  // a dense vector containing the mean value for each column
        println(summary.variance)  // column-wise variance
        println(summary.numNonzeros)  // number of nonzeros in each column
	}
}
