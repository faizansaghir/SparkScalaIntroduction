# Objective

Summarize learning of Spark using Scala

## Prerequisites

1. Make sure Java version for the project is below Java 18
2. Make sure Scala plugin is installed for the IDE
3. For windows, create a hadoop folder and a bin sub-folder
    <pre>To simulate Hadoop environment in Windows, in environment variable
    a. Add HADOOP_HOME as newly created hadoop folder
    b. Add %HADOOP_HOME\bin to Path variable
    c. Add winutils.exe to hadoop\bin folder</pre>

## RDD(Resilient Distributed Dataset)

1. This is the original API Spark came with. <br><br>
2. We now have higher level APIs but some work are more efficiently done by RDDs <br><br>
3. RDDs are rows of data hence called dataset which are distributed across different nodes thus it is distributed <br><br>
4. Even if a node fails, Spark has the lineage of RDD and knows how to recreate it thus make it resilient <br><br>
5. There are different ways of creating an RDD
   <pre> Examples
   a. val nums = parallelize(List(1,2,3,4))
   
   b. sc.textFile("file:///c:/users/example/someTextFile.txt")
      This creates an RDD with each line of text file as a separate row
      We can also use distributed file system like s3n:// or hdfs://
   
   c. hiveCtx = HiveContext(sc) 
      rows = hiveCtx.sql("SELECT name, age FROM users")
      We can also load data from JDBC, Cassandra, HBase, 
         ElasticSearch, JSON, CSV,sequence files, 
         object files, and many other file formats </pre> <br>
6. If a RDD has tuple of 2 elements, then it is called and acts as key-value RDD <br>
   These RDDs enable additional functionalities like <br>
   mapValues, reduceByKey, etc <br><br>
7. Some important functions and their use <br>
   a. <strong>map</strong>: Creates 1 to 1 mapping <br>
   b. <strong>flatmap</strong>: Maps the rows and then applies flatten method which separates each element of resultant array into a row
   i.e. if the row is array of elements then it separates each element into a separate row <br>
   c. <strong>mapValues</strong>: Applied to key-value RDD where the mapping function passes the value part of the tuple
   and returns a tuple or a single value back <br>
   d. <strong>reduceByKey</strong>: Applied to key-value RDD where the reducing function passes value part of the tuple
   and we need to return a single value back from the reducing function <br>
   d. <strong>countByValue</strong>: Gets count of each distinct value in RDD as a map or dictionary
   e. <strong>sortBykey</strong>: Applied on key-value RDD, this sorts the RDD by key i.e. the first element of tuple
8. RDDs contain rows but the value of these rows are not separated out into columns 
   and each row acts as having a single simple or complex object as its value   

## DataFrame

1. These are similar to RDDs but apart from containing dataset of rows, it also has a schema associated to the row <br><br>
2. DatFrame is a DataSet of Row object and this Row object has a schema <br><br>
2. A defined schema allows it to be stored more efficiently <br><br>
3. Having a schema allows to execute SQl queries on them <br><br>
4. It can read and write from JSON, Hive, Parquet, etc. <br><br>
5. It can communicate with JDBC/ODBC, etc. <br><br>

## Dataset

1. These are DataFrame where instead of Row, we can have a defined structure i.e. a class or struct <br><br>
2. Due to having a defined structure, it can detect issues during compile time related to data types <br><br>
3. Dataset is only available in languages that support compile time check, thus, Python does not support Dataset <br><br>
4. For using Spark SQl and higher level APIs like DatFrame and Dataset, we need SparkSession instead of SparkContext <br><br>
5. We can convert an RDD into a Dataset using toDS() function of RDD <br><br>
6. We can convert Dataset to RDD using rdd() function of Dataset <br><br>
7. We can have UDFs in Dataset like we have in DataBases <br><br>
8. When the case class is simple, spark can implicitly infer the schema and encode each row to the given case class object <br>
   For this we need to add <code>import spark.implicits._</code> before we convert DataFrame to Dataset
   <pre>Example
      import spark.implicits._
      val ds = df.as[Person]
   </pre>
   

## Some spark related questions

1. The script that controls your Spark job is called... <br>
   &emsp;The Driver Program
   <br><br>
2. Apache Spark replaces Hadoop. <br>
   &emsp;Spark is in most cases a better alternative to Hadoop's MapReduce component,
   but Spark can run on top of a Hadoop cluster, taking advantage of its distributed file system and YARN cluster manager.
   Spark also has its own built-in cluster manager allowing you to use it outside of Hadoop,
   but it is not a complete replacement for Hadoop. Spark and Hadoop can co-exist.
   <br><br>
3. How does Spark Core optimize its workflows? <br>
   &emsp;Using DAG engine
   A DAG allows Spark to find the most efficient order to run the tasks needed to accomplish your desired output.
   <br><br>
4. What are the four main components of Spark, that sit above Spark Core in an architectural diagram? <br>
&emsp;Spark Streaming, Spark SQL, MLLib and GraphX
   <br><br>
5. What language is Spark itself written in? <br>
   &emsp;Scala <br><br>
6. What is Lazy Evaluation? <br>
   &emsp;Spark does not evaluate until you call an action no matter how many transformations you have applied <br>
   Once you call an action, Spark does the optimizations and then executes the operations <br><br>
7. What does it mean when we pass "local[\*]" as master while instantiating SparkContext? <br>
   &emsp;local[\*] passed as master parameter to SparkContext constructor means 
   we will be using all available threads of local system <br><br>
8. What is Transformation in Spark? <br>
   &emsp;Transformation functions when applied to a dataset, gives back a dataset where the given function
   is applied to each row of the original dataset <br>
   Some important transformation supported by RDD: <br>
   &emsp;map, flatmap, filter, distinct, sample, union, intersection, subtract, cartesian, etc. <br><br>
9. What is Action in Spark? <br>
   &emsp;Action functions are functions that gives back result to the Driver. These return concern from Spark back to Scala,
   thus we no longer have Spark features and optimizations <br>
   Some important action supported by RDD: <br>
   collect, count, countByValue, take, top, reduce, etc. <br><br>
10. What is Job, Stage and Task? <br>
   &emsp;Each Spark code submitted to run on a cluster(even local) is called a Job <br>
   Each job is divided into Stages, Stages are created each time data needs to be shuffled among the executors 
   due to some transformation <br>
   Each stage is divided into tasks based on transformations to be applied, where each task can be processed in 
   parallel in some executor node. <br><br>
11. How can we create a SparkSession? <br>
   <pre>Example
   val spark = SparkSession.builder.appName("SomeAppName").master("local[\*]").getOrCreate()</pre> <br>
12. Show an example of DataFrame and Dataset
   <pre>Example
   val df = spark.read
               .option("header","true")
               .option("inferSchema","true")
               .csv("pathToCSV.csv")
   import spark.implicits._
   val ds = df.as[SomeCaseClass]
   /*
   * Here each entry of DataFrame will be of type Row, as schema will be infered when we read the csv file 
      the schema will be known during execution but there is no compile time check
      While each entry of Dataset will be of type Person,
      Any operation on the Dataset will be checked at compile time
   */
   </pre> <br>
13. What is case class in scala?
   &emsp;A case class in scala is a short hand notation to define a class that is kind of a POJO
   <pre>Example
   case class Person(id:Int, name:String, age:Int, friends:Int)</pre> <br><br>
14. How to create a temporary view using Dataset and how to query it using spark? <br>
   &emsp;To create a view, use createOrReplaceTempView(viewName) function of Dataset or DataFrame object <br>
   To query from the view, use spark.sql(query) and query from the view
   <pre>Example
   dfOrDs.createOrReplaceTempView("people")
   val teenagers = spark.sql("SELECT * FROM people WHERE age>=13 AND age<=19")
   </pre> <br><br>
15. When can RDD be more useful than DataFrame and Dataset? <br>
   &emsp;Weh we are working with data that do not have a schema, eg: text file with lines of text like a book. <br>
   In such cases, RDD can be used as these data do not have a schema <br><br>
16. What is the default column name when we do not specify a schema or column name? <br>
    &emsp; Default column name in Spark is "value" <br><br>
17. How to handle cases when CSV file does not have header? <br>
   &emsp;We should create a Schema and pass it to DataFrameReader
   <pre>Example
      val temperatureSchema = new StructType()
      .add("stationID", StringType, nullable = true)
      .add("date", IntegerType, nullable = true)
      .add("measure_type", StringType, nullable = true)
      .add("temperature", FloatType, nullable = true)

      import spark.implicits._
      val ds = spark.read
      .schema(temperatureSchema)
      .csv("data/1800.csv")
      .as[Temperature]
</pre>