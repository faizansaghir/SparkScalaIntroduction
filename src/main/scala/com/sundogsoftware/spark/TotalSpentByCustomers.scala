package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object TotalSpentByCustomers {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "TotalSpentByCustomers")
    val lines = sc.textFile("data/customer-orders.csv")
    val customerData = lines.map(parseCustomerData)
    val spendPerCustomer = customerData.reduceByKey((x, y) => x+y)
    val spendPerCustomerSorted = spendPerCustomer.map(row => (row._2, row._1)).sortByKey().map(row => (row._2, row._1))
    spendPerCustomerSorted.foreach(println)
  }

  private def parseCustomerData(line: String): (Int, Float) = {
    val parts = line.split(",")
    (parts(0).toInt, parts(2).toFloat)
  }
}
