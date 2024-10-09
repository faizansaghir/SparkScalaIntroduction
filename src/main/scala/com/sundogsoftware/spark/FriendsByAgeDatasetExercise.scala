package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{avg, col, round}

case class FriendData(id: Int, name:String, age: Int, friends: Int)

object FriendsByAgeDatasetExercise {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession.builder()
      .appName("FriendsByAgeDatasetExercise")
      .master("local[*]")
      .getOrCreate()

    val df = spark.read.option("header","true")
      .option("inferSchema","true")
      .csv("data/fakefriends.csv")

    df.printSchema()

    import spark.implicits._
    val ds = df.as[FriendData]

    val data = ds.select(col("age"), col("friends"))

    data.groupBy(col("age")).avg("friends").show()

    data.groupBy(col("age")).agg(avg("friends").as("average_friends")).sort(col("average_friends")).show()


    data.groupBy(col("age")).agg(round(avg("friends"), 2).as("average_friends")).sort(col("average_friends")).show()

    spark.close()
  }
}
