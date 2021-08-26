package com.trg.assesment

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{ input_file_name, regexp_extract, row_number, when }
import org.apache.spark.sql.{ DataFrame, SaveMode, SparkSession }

object Utils {

  def csvReader(path: String)(implicit sparkSession: SparkSession): DataFrame =
    sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(path)
      .withColumn("filename", input_file_name())

  def loadInputData(inputDataPath: String)(implicit sparkSession: SparkSession): DataFrame = {
    import sparkSession.implicits._

    val dfStreet = csvReader(s"${inputDataPath}/*-street.csv")
    val dfOutcomes = csvReader(s"${inputDataPath}/*-outcomes.csv")

    val dfStreet1 = dfStreet
      .select(
        $"Crime ID".as("crimeID"),
        regexp_extract($"filename", raw"^.*\d{4}-\d{2}-(.*)-street\.csv", 1).as("districtName"),
        $"Latitude".as("latitude"),
        $"Longitude".as("longitude"),
        $"Crime type".as("crimeType"),
        $"Last outcome category".as("outcome"),
        $"Month",
        $"filename"
      )

    val dfOutcomesLast = dfOutcomes
      .select(
        $"Crime ID".as("crimeID"),
        regexp_extract($"filename", raw"^.*\d{4}-\d{2}-(.*)-outcomes\.csv", 1).as("districtName"),
        $"Outcome type".as("outcome"),
        $"Month"
      )
      .withColumn("rank", row_number().over(Window.partitionBy("crimeId", "districtName").orderBy($"Month".desc)))
      .where("rank = 1")

    val dfFinal = dfStreet1
      .as("A")
      .join(
        dfOutcomesLast.as("B"),
        $"A.crimeId" === $"B.crimeId" and $"A.districtName" === $"B.districtName",
        "left_outer"
      )
      .select(
        $"A.crimeID",
        $"A.districtName",
        $"A.latitude",
        $"A.longitude",
        $"A.crimeType",
        when($"B.outcome".isNotNull, $"B.outcome").otherwise($"A.outcome").as("lastOutcome")
      )

    dfFinal
  }

  def writeDataToParquet(df: DataFrame, path: String): Unit = df.write.mode(SaveMode.Overwrite).save(path)

  def loadOutputData(path: String)(implicit sparkSession: SparkSession): DataFrame = {
    sparkSession.read.load(path)
  }
}
