package sparkTest;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;

public class AlgoPoubelle {

    private SparkSession sparkSession(String appName, String masterName){
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(masterName);

        SparkSession spark = SparkSession
                .builder()
                .appName(appName)
                .config(conf)
                .getOrCreate();

        return spark;
    }

    @Test
    public void dclMLLIBTest(){
        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;

        //List<Row> dataset = dataSet(rscPath, lineToReadSample);

        SparkSession spark=sparkSession("dclTest", "local[*]");

        SparkContext sc = spark.sparkContext();
        SQLContext sqlContext = spark.sqlContext();

        StructType schema = new StructType()
                .add("Date", DataTypes.StringType, true)
                .add("DateLong", DataTypes.FloatType, true)
                .add("globalLasting", DataTypes.FloatType, true);

        Dataset<Row> dataSet = sqlContext.read()
                .option("inferSchema", false)
                .option("header", false)
                .schema(schema)
                .csv("../forge_clementine_per_hourv3.csv")
                .toDF();

        RDD rdd = dataSet.rdd();

        org.apache.spark.mllib.clustering.KMeans kMeans = new org.apache.spark.mllib.clustering.KMeans()
                .setK(2);

        //org.apache.spark.mllib.clustering.KMeansModel model = new kMeans.run(rdd);
    }


}
