package com.sundogsoftware.spark

import com.codahale.metrics.Slf4jReporter.LoggingLevel
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.ScalaReflection.Schema
import org.apache.spark.sql.functions.{col, sum, round}
import org.apache.spark.sql.types.{FloatType, IntegerType, StructType}

case class CustomerOrder(customerId: Int, itemId: Int, price: Float)

object TotalSpendByCustomerDatasetExercise {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .appName("TotalSpendByCustomerDatasetExercise")
      .master("local[*]")
      .getOrCreate()

  val customerOrderSchema = new StructType()
    .add("customerId", IntegerType)
    .add("itemId", IntegerType)
    .add("price", FloatType)

    val df = spark.read
      .schema(customerOrderSchema)
      .csv("data/customer-orders.csv")

    df.printSchema()

    import spark.implicits._
    val ds = df.as[CustomerOrder]

    val totalSpendByCustomerDs = ds.groupBy(col("customerId"))
      .agg(round(sum("price"), 2).as("totalSpend"))
      .sort(col("totalSpend"))

    totalSpendByCustomerDs.show(totalSpendByCustomerDs.count().toInt)

    spark.close()
  }
}
