package controllers

import com.trg.assesment.Main
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.rand
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import play.api._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class SparkController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * load input data
    */
  lazy val data: DataFrame = Main.loadParquetData()

  implicit val formats = net.liftweb.json.DefaultFormats

  def sampleData() = Action { implicit request: Request[AnyContent] =>
    val resultList = data.orderBy(rand()).limit(50).toJSON.collect().toList.mkString(",")
    Ok(Json.prettyPrint(Json.parse(s"[${resultList}]")))
  }

  def filterData(crimeId: String) = Action { implicit request: Request[AnyContent] =>
    if (crimeId == "")
      Ok("you need to filter data, ex: http://localhost:9000/filter_data?crimeId=\"2df48b560e62bc0478f46090f063a19de8d305ec29e741f445be0ed5ca9a9a76\"")
    else {
      val resultList = data.where(s"crimeId = ${crimeId}").toJSON.collect().toList.mkString(",")
      Ok(Json.prettyPrint(Json.parse(s"[${resultList}]")))
    }
  }

  def crimesByLocation() = Action { implicit request: Request[AnyContent] =>
    val resultList = data.groupBy("districtName").count().toJSON.collect().toList.mkString(",")
    Ok(Json.prettyPrint(Json.parse(s"[${resultList}]")))
  }

  def crimesByCrimeType() = Action { implicit request: Request[AnyContent] =>
    val resultList = data.groupBy("crimeType").count().toJSON.collect().toList.mkString(",")
    Ok(Json.prettyPrint(Json.parse(s"[${resultList}]")))
  }

}
