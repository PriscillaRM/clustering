package sparkTest;
import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.clustering.GaussianMixture;
import org.apache.spark.ml.clustering.GaussianMixtureModel;
import org.apache.spark.ml.feature.MinHashLSH;
import org.apache.spark.ml.feature.MinHashLSHModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StandaloneTest {

    @Test
    public void test() {

        SparkConf conf = new SparkConf().setAppName("myFirstDF").setMaster("local[*]");

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaLogisticRegressionExample")
                .config(conf)
                .getOrCreate();

        SparkContext sc = spark.sparkContext();
        SQLContext sqlContext = spark.sqlContext();

        VectorUDT vectorUDT = new VectorUDT();

        StructType schema = new StructType()
                .add("Date", DataTypes.StringType, true)
                .add("DateLong", DataTypes.FloatType, true)
                .add("globalLasting", DataTypes.FloatType, true);

        Dataset<Row> dataSet = sqlContext.read()
                .option("inferSchema", false)
                .option("header", false)
                .schema(schema)
                .csv("../forge_clementine_per_hourv3.csv")
                .toDF(); //     .format("csv")       .load("../forge_clementine_per_hourv2.csv")    .schema(schema)

        dataSet.printSchema();
        dataSet.show();

        RDD rdd = dataSet.rdd();

        System.out.println(" <<<<<<<<<<<<<<<<<<<<<   ici  ");
        System.out.println(" first element >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+ rdd.first());

        org.apache.spark.mllib.clustering.GaussianMixture gmm = new org.apache.spark.mllib.clustering.GaussianMixture()
                .setMaxIterations(800)
                .setK(2);//                .setFeaturesCol("_c2")
        org.apache.spark.mllib.clustering.GaussianMixtureModel model = gmm.run(rdd);

    }


    @Test
    public void examplesSpark(){

        SparkConf sparkConf = new SparkConf().setAppName("JavaGaussianMixtureExample").setMaster("local[*]").set("spark.executor.memory","1g");

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaGaussianMixtureExample")
                .config(sparkConf)
                .getOrCreate();

        List<Row> dataA = Arrays.asList(
                RowFactory.create(0, Vectors.sparse(6, new int[]{0, 1, 2}, new double[]{1.0, 1.0, 1.0})),
                RowFactory.create(1, Vectors.sparse(6, new int[]{2, 3, 4}, new double[]{1.0, 1.0, 1.0})),
                RowFactory.create(2, Vectors.sparse(6, new int[]{0, 2, 4}, new double[]{1.0, 1.0, 1.0}))
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });
        Dataset<Row> dfA = spark.createDataFrame(dataA, schema);

        dfA.show();

        MinHashLSH mh = new MinHashLSH()
                .setNumHashTables(5)
                .setInputCol("features")
                .setOutputCol("hashes");

        MinHashLSHModel model = mh.fit(dfA);

        model.transform(dfA).show();

    }


    @Test
    public void testExample(){

        SparkConf sparkConf = new SparkConf().setAppName("JavaGaussianMixtureExample").setMaster("local[*]").set("spark.executor.memory","1g");

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaGaussianMixtureExample")
                .config(sparkConf)
                .getOrCreate();

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });

        // Loads data
        Dataset<Row> dataset = spark.read().schema(schema).text("../testDatas.txt");

        dataset.show();

        // Trains a GaussianMixture model

        GaussianMixture gmm = new GaussianMixture()
                .setFeaturesCol("features")
                .setK(2);
        //GaussianMixtureModel model = gmm.fit(dataset);


        /*
        // Output the parameters of the mixture model
        for (int i = 0; i < model.getK(); i++) {
            System.out.printf("Gaussian %d:\nweight=%f\nmu=%s\nsigma=\n%s\n\n",
                    i, model.weights()[i], model.gaussians()[i].mean(), model.gaussians()[i].cov());
        }*/
    }





    @Test
    public void withExtraction(){
        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;

        ExtractSample extractSample = new ExtractSample.Builder(rscPath, lineToReadSample).sampleSize(240).build();

        SampleEntity sampleEntity = extractSample.sampleFactory();

        int[] integer = new int[sampleEntity.getSample().length];
        double[] hour = new double[sampleEntity.getSample().length];
        double[] data = new double[sampleEntity.getSample().length];

        for(int i=0; i<data.length; i++){
            integer[i] = i;
            hour[i] = (double) i%24;
            data[i] = sampleEntity.getSample()[i].doubleValue();
            System.out.println("integer i = " + integer[i] + " data i = " + data[i]);
        }


        List<Row> dataSet = Arrays.asList(
                RowFactory.create(0, Vectors.sparse(2*data.length, integer, hour) ),
                RowFactory.create(1, Vectors.sparse(2*data.length, integer, data) )
        );

        SparkConf conf = new SparkConf().setAppName("MinHashLSHWithClementineData").setMaster("local[*]");

        SparkSession spark = SparkSession
                .builder()
                .appName("MinHashLSHWithClementineData")
                .config(conf)
                .getOrCreate();

        SparkContext sc = spark.sparkContext();
        SQLContext sqlContext = spark.sqlContext();

        VectorUDT vectorUDT = new VectorUDT();
        //vectorUDT.sqlType().add("features",DataTypes.FloatType);

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", vectorUDT, false, Metadata.empty())
        });

        Dataset<Row> dfA = spark.createDataFrame(dataSet, schema);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + vectorUDT.sqlType().typeName());
        dfA.printSchema();

        dfA.show();

        MinHashLSH mh = new MinHashLSH()
                .setNumHashTables(5)
                .setInputCol("features")
                .setOutputCol("hashes");

        MinHashLSHModel model = mh.fit(dfA);

        model.transform(dfA).show();

        ///////// pb with GaussianMixture

        /*GaussianMixture gmm = new GaussianMixture()
                .setK(2)
                .setFeaturesCol("features");

        GaussianMixtureModel model = gmm.fit(dfA);

        /*model.transform(dfA).show();*/


    }




}
