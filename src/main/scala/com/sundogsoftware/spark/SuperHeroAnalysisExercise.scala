package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, size, split, sum}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object SuperHeroAnalysisExercise {
  case class SuperheroGraph(value: String)

  case class SuperHeroName(superHeroId: Int, superHeroName: String)

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .appName("SuperHeroAnalysisExercise")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    val superheroGraphRawDS = spark.read.text("data/Marvel-graph.txt").as[SuperheroGraph]

    val SuperHeroIdNumberOdConnectionDS = superheroGraphRawDS
      .withColumn("superHeroId", split(col("value"), " ", 2)(0))
      .withColumn("numberOfConnection", size(split(col("value"), " "))-1)

    val mostPopularSuperHeroEntry = SuperHeroIdNumberOdConnectionDS
      .groupBy(col("superHeroId"))
      .agg(sum("numberOfConnection").as("numberOfConnection"))
      .sort(col("numberOfConnection").desc)
      .first()

    val superHeroNameSchema = new StructType()
      .add("superHeroId", IntegerType)
      .add("superHeroName", StringType)

    val superHeroNames = spark.read
      .option("sep", " ")
      .schema(superHeroNameSchema)
      .csv("data/Marvel-names.txt").as[SuperHeroName]


    val mostPopularSuperHeroName = superHeroNames.filter($"superHeroId"===mostPopularSuperHeroEntry(0)).first()

    println(f"${mostPopularSuperHeroName.superHeroName} is the most popular Super Hero with ${mostPopularSuperHeroEntry(1)} co-appearances")

    spark.close()
  }
}
