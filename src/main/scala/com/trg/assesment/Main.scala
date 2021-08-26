package com.trg.assesment
import com.trg.assesment.Utils.{loadInputData, loadOutputData, writeDataToParquet}
import com.trg.assesment.config.SparkConfig
import org.apache.spark.sql.DataFrame

object Main extends SparkConfig {
  val inputDataPath: String = "/tmp/trg/data/inputdata/*"
  val outputDataPath: String = "/tmp/trg/data/outputdata"

//  lazy val crimesByLocation: DataFrame = outputData.groupBy("districtName").count()

  def loadSourceData(): Unit = {
    val inputData: DataFrame = loadInputData(inputDataPath)
    writeDataToParquet(inputData, outputDataPath)
  }

  def loadParquetData(): DataFrame = {
    loadOutputData(outputDataPath).cache()
  }


}
