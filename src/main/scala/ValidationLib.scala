import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import com.amazon.deequ.analyzers._
import com.amazon.deequ.analyzers.runners.AnalyzerContext.successMetricsAsDataFrame
import com.amazon.deequ.analyzers.runners.{AnalysisRunner, AnalyzerContext}
import com.amazon.deequ.checks.{Check, CheckLevel}
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}
import com.amazon.deequ.{VerificationResult, VerificationSuite}
import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, SparkSession}
//import spark.implicits._

object ValidationLib {

  def testAnalysers(df2: DataFrame, spark: SparkSession): Unit = {

    // Add tests on the data
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

    // convert check results to a Spark data frame
    val resultDataFrame = checkResultsAsDataFrame(spark, verificationResult)

    resultDataFrame.show(truncate = false);




  }

  def suggestAnalysers(df2: DataFrame, spark: SparkSession): Unit = {

    val suggestionResult = { ConstraintSuggestionRunner()
      // data to suggest constraints for
      .onData(df2)
      // default set of rules for constraint suggestion
      .addConstraintRules(Rules.DEFAULT)
      // run data profiling and constraint suggestion
      .run()
    }



    // We can now investigate the constraints that Deequ suggested.
    val suggestionDataFrame = suggestionResult.constraintSuggestions.flatMap {
      case (column, suggestions) =>
        suggestions.map { constraint =>
          (column, constraint.description, constraint.codeForConstraint)
        }
    }.toStream

    println(suggestionDataFrame.print())
  }

  def putAnalysers(df2:sql.DataFrame, spark:SparkSession):Unit={
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

    // retrieve successfully computed metrics as a Spark data frame
    val metrics = successMetricsAsDataFrame(spark, analysisResult)
    metrics.show()



  }
}
