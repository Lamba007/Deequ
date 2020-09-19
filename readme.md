# Deequ-Scala

Deequ is being used internally at Amazon for verifying the quality of many large production datasets. Dataset producers can add and edit data quality constraints. The system computes data quality metrics on a regular basis (with every new version of a dataset), verifies constraints defined by dataset producers, and publishes datasets to consumers in case of success. In error cases, dataset publication can be stopped, and producers are notified to take action. Data quality issues do not propagate to consumer data pipelines, reducing their blast radius.

This project includes functions to insert your data set in the form of CSV or via database table.

## Basic Usage

Basic usage
Currently PyDeequ only implements the basic functionality of Deequ but hopefully it still brings some value to a python based data science projects. In the followings we demonstrate the basic usage of these four functionalities. There are also example files available in src\pydeequ\examples.

The main components of Deequ are

Analyzers: main metrics computation engine of Deequ, they output descriptive statistics on tabular input data,
Constrain verification: predefined data quality checks with threshold values as parameters, they based on metrics computed by analyzers,
Constrain suggestions: automated constrain generation based on a set of rules, which profile the data first to come up with useful constrains.


## Constraint Suggestion

```
val suggestionResult = 

{ ConstraintSuggestionRunner()
      // data to suggest constraints for
.onData(df2)
      // default set of rules for constraint suggestion
      .addConstraintRules(Rules.DEFAULT)
      // run data profiling and constraint suggestion
      .run()
    }
```

## Analyser

```def putAnalysers(df2:sql.DataFrame, spark:SparkSession):Unit={
    val analysisResult: AnalyzerContext = {
      AnalysisRunner
        // data to run the analysis on
        .onData(df2)
        //define analyzers that compute metrics
        .addAnalyzer(Size())
        .addAnalyzer(Completeness("registration"))
        .addAnalyzer(Completeness("date"))
        .addAnalyzer(ApproxCountDistinct("review_id"))
        .addAnalyzer(Mean("all_users"))
        .addAnalyzer(Completeness("market"))
        .addAnalyzer(Correlation("all_users", "active_users"))
        // compute metrics
        .run()
    }
````

## Verification Result

```
val verificationResult: VerificationResult = {
      VerificationSuite()
        // data to run the verification on
        .onData(df2)
        // define a data quality check
        .addCheck(
          Check(CheckLevel.Error, "Review Check")
            .hasSize(_ >= 50) // at least 50 rows
            .isComplete("market") // should never be NULL
            .isComplete("date") // should never be NULL
            .isUnique("primekey") // should not contain duplicates
            .hasMin("diff",_ == 1206)
            //          // contains only the listed values
            .isContainedIn("currency", Array("US", "UK", "DE", "JP", "FR"))
            .isNonNegative("diff")) // should not contain negative values


        // compute metrics and verify check conditions
        .run()
    }
```


## Installation

1. Open git bash 
   * **Git clone https://github.com/Lamba007/Deequ.git**
2. Open project in Intellij
3. Sync the build.sbt file (to download the dependency)
4. Add the config to the Application
   * VM options: -Xms1024m
5. Build the project
6. Run project 




## Usage


Use these methods as a baseline to perform data validation on the data. Just provide the dataset either in the CSV format or DB connection details

#### tools

Intellij
Scala
SBT
Git

#### sbt

libraryDependencies ++= Seq(
  
// https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.11
  "org.apache.spark" %% "spark-core" % "2.3.2",
  
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.11
  "org.apache.spark" %% "spark-sql" % "2.3.2",
    
// https://mvnrepository.com/artifact/com.amazon.deequ/deequ
  "com.amazon.deequ" % "deequ" % "1.0.1",
  
// https://mvnrepository.com/artifact/mysql/mysql-connector-java
  "mysql" % "mysql-connector-java" % "8.0.19"

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
