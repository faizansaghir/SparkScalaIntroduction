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
   &emsp;Scala