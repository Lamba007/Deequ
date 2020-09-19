import ValidationLib.{putAnalysers, suggestAnalysers, testAnalysers}
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.SparkSession
//import spark.implicits._

object RealExample {

  val filePath = "src\\resource\\6dce6549-a587-44d3-9ad3-bb69e87af650.csv"

  def main(args: Array[String]): Unit = {

    LogManager.getRootLogger.setLevel(Level.OFF)

    //Instantiating Spark Shell
    val spark: SparkSession = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample")
      .getOrCreate()
//    spark.sparkContext.setLogLevel("OFF")

    //Load file and show schema
    val df2 = spark.read.options(Map("inferSchema"->"true","sep"->",","header"->"true")).csv(filePath)

    df2.show(false)
    df2.printSchema()

    suggestAnalysers(df2,spark)
    putAnalysers(df2,spark)
    testAnalysers(df2,spark)

  }
}
