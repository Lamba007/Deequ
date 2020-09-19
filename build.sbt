name := "untitled"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.11
  "org.apache.spark" %% "spark-core" % "2.3.2",
  // https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.11
  "org.apache.spark" %% "spark-sql" % "2.3.2",
    // https://mvnrepository.com/artifact/com.amazon.deequ/deequ
  "com.amazon.deequ" % "deequ" % "1.0.1",
  // https://mvnrepository.com/artifact/mysql/mysql-connector-java
  "mysql" % "mysql-connector-java" % "8.0.19"


)