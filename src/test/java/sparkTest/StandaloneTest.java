package sparkTest;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
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

        //StructType file = sqlContext.csvFile("../../forge_clementine_per_hour.csv", true, ';');
        Dataset<Row> dataSet = sqlContext.read().format("csv").load("../forge_clementine_per_hour.csv");
        dataSet.show();

    }

}
