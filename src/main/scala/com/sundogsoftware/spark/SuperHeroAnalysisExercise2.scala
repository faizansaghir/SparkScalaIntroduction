package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, size, split, sum}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object SuperHeroAnalysisExercise2 {

  private def getCharacterToNumberOfCoApprearances(line: String): (Int, Int) = {
    val parts = line.split(" ")
    (parts(0).toInt, parts.length-1)
  }

  private def parseMarvelNameLine(line: String): Option[(Int, String)] = {
    val parts = line.split(" ",2)
    if(parts.length<2) {
      None
    } else{
      val name = parts(1).stripPrefix("\"").stripSuffix("\"")
      Some(parts(0).toInt, name)
    }

  }

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .appName("SuperHeroAnalysisExercise")
      .master("local[*]")
      .getOrCreate()

    val characterIdToNumberOfCoAppearancesRdd = spark.sparkContext
      .textFile("data/Marvel-graph.txt")
      .map(getCharacterToNumberOfCoApprearances)

    val characterIdToTotalNumberOfCoAppearancesRdd = characterIdToNumberOfCoAppearancesRdd
      .reduceByKey((x,y) => x+y)

    val flipped = characterIdToTotalNumberOfCoAppearancesRdd.map(row => (row._2, row._1))

    val mostPopularSuperheroEntry = flipped.sortByKey(ascending = false).first()

    val mostPopularSuperheroId = mostPopularSuperheroEntry._2

    val superheroIdToNameRdd = spark.sparkContext.textFile("data/Marvel-names.txt")
      .flatMap(parseMarvelNameLine)

    val mostPopularSuperheroName = superheroIdToNameRdd
      .filter(entry => (entry._1 == mostPopularSuperheroId))
      .first()._2

    println(f"${mostPopularSuperheroName} is the most popular superhero with ${mostPopularSuperheroEntry._1} appearances")

    spark.close()
  }
}
