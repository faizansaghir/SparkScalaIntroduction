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
   parallel in some executor node. 
   