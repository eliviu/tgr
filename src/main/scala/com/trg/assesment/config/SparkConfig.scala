package com.trg.assesment.config

import org.apache.spark.sql.SparkSession

trait SparkConfig {

  implicit val sparkSession: SparkSession = SparkSession
    .builder()
    .appName("TRG Assesment")
    .master("local[*]")
    .getOrCreate()
}
