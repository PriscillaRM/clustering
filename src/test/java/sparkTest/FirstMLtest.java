package sparkTest;

import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.graphx.Graph;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FirstMLtest {


    // On développe ici un test de d'algo, utilisant la librairie Spark (ou éventuellement quelques algos que j'ai dev)
    // Pour le moment c'est du static, mais on va l'appliquer dans une seconde version à des pbs dynamiques

    // On va tenter de rechercher des comportements types pour un process donné, selon l'heure et le jour de la semaine

    private List<Row> dataSet(String rscPath, int lineToReadSample){
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

        return dataSet;
    }

    private SparkSession sparkSession(String appName, String masterName){
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(masterName);

        SparkSession spark = SparkSession
                .builder()
                .appName(appName)
                .config(conf)
                .getOrCreate();

        return spark;
    }

    /// Pour le tracer de graphe


    @Test
    public void dclMLTest(){
        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;

        List<Row> dataset = dataSet(rscPath, lineToReadSample);

        SparkSession spark=sparkSession("dclTest", "local[*]");

        VectorUDT vectorUDT = new VectorUDT();
        //vectorUDT.sqlType().add("features",DataTypes.FloatType);

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", vectorUDT, false, Metadata.empty())
        });

        Dataset<Row> dfA = spark.createDataFrame(dataset, schema);

        dfA.printSchema();

        org.apache.spark.ml.clustering.KMeans kMeans = new org.apache.spark.ml.clustering.KMeans()
                .setK(2)
                .setFeaturesCol("features");


        org.apache.spark.ml.clustering.KMeansModel model = kMeans.fit(dfA);
        Vector[] vectors = model.clusterCenters();


    }


    @Test
    public void drsxTest(){
        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;

        List<Row> dataset = dataSet(rscPath, lineToReadSample);

        SparkSession spark=sparkSession("dsrxTest", "local[*]");

        VectorUDT vectorUDT = new VectorUDT();
        //vectorUDT.sqlType().add("features",DataTypes.FloatType);

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", vectorUDT, false, Metadata.empty())
        });

        Dataset<Row> dfA = spark.createDataFrame(dataset, schema);

        org.apache.spark.ml.classification.MultilayerPerceptronClassifier perceptronClassifier = new org.apache.spark.ml.classification.MultilayerPerceptronClassifier();

    }


    @Test
    public void drsxTest2(){

        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;

        List<Row> dataset = dataSet(rscPath, lineToReadSample);

        SparkSession spark=sparkSession("dsrxTest", "local[*]");

        VectorUDT vectorUDT = new VectorUDT();
        //vectorUDT.sqlType().add("features",DataTypes.FloatType);

        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", vectorUDT, false, Metadata.empty())
        });

        Dataset<Row> dfA = spark.createDataFrame(dataset, schema);

        Dataset<Row>[] splits  = dfA.randomSplit(new double[]{0.6,0.4}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

        int[] layers = new int[]{4, 5, 4, 3};

        org.apache.spark.ml.classification.MultilayerPerceptronClassifier trainer = new org.apache.spark.ml.classification.MultilayerPerceptronClassifier()
                .setLabelCol("label")
                .setLayers(layers)
                .setBlockSize(240)
                .setSeed(1234L)
                .setMaxIter(100);


        //org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel model = trainer.fit(train);



    }


}
