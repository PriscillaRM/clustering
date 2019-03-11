package sparkTest;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.*;
import org.junit.Test;

public class StandaloneTest {

    @Test
    public void test(){

        SparkConf conf = new SparkConf().setAppName("myFirstDF").setMaster("local[*]");

        SparkContext sc = new SparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        StructType schema = new StructType()
                .add("Date",DataTypes.StringType,true)
                .add("LongDate",DataTypes.LongType,true)
                .add("globalLasting",DataTypes.LongType,true);

        //StructType file = sqlContext.csvFile("../../forge_clementine_per_hour.csv", true, ';');
        Dataset<Row> dataSet = sqlContext.read().format("csv")
                .schema(schema)
                .load("../forge_clementine_per_hourv2.csv");

        dataSet.show();

        Dataset<Row> dataCol = dataSet.select("globalLasting");

        dataCol.show();

        LogisticRegression lr = new LogisticRegression().setMaxIter(100);

        LogisticRegressionModel model = lr.fit(dataCol);

        System.out.println("passe2");

        /*// Inspect the model: get the feature weights.
        Vector weights = (Vector) model.coefficients();

        // Given a dataset, predict each point's label, and show the results.
        model.transform(dataCol).show();*/




    }

}
