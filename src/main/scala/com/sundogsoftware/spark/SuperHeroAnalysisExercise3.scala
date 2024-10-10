package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object SuperHeroAnalysisExercise3 {

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

    val mostObscureSuperheroAppearanceCount = flipped.min()._1

    val mostObscureSuperheroAppearanceEntries = flipped.filter(row => row._1==mostObscureSuperheroAppearanceCount)

    val mostObscureSuperheroIds = mostObscureSuperheroAppearanceEntries.values.collect().toSet

    val superheroIdToNameRdd = spark.sparkContext.textFile("data/Marvel-names.txt")
      .flatMap(parseMarvelNameLine)

    val mostObscureSuperheroNames = superheroIdToNameRdd
      .filter(entry => mostObscureSuperheroIds.contains(entry._1))
      .map(_._2).collect()

    println(f"Most obscure superheros are ${mostObscureSuperheroNames.mkString("", ", ", "")} with ${mostObscureSuperheroAppearanceCount} co-appearances")

    spark.close()
  }
}
