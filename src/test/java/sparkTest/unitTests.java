package sparkTest;

import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.GaussianMixture;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.eclipse.jetty.websocket.common.frames.DataFrame;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ml.sevices.Utils.comptorTable;

public class unitTests {

    Logger logger = LoggerFactory.getLogger(unitTests.class);

    @Test
    public void test1() {

        SparkConf conf = new SparkConf().setAppName("JavaGaussianMixtureExample").setMaster("local[*]").set("spark.executor.memory","1g");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        String path = "../dataGMM.txt";
        JavaRDD<String> data = jsc.textFile(path);
        JavaRDD<Vector> parsedData = data.map(s -> {
            String[] sarray = s.trim().split(" ");
            double[] values = new double[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                values[i] = Double.parseDouble(sarray[i]);
            }
            return Vectors.dense(values);
        });
        parsedData.cache();
        System.out.println(" here >>>>>>>>>>>>>>>>>>>>  "+parsedData.rdd().first());

        // Cluster the data into two classes using GaussianMixture
        //GaussianMixtureModel gmm = new GaussianMixture().setK(2).run(parsedData.rdd());

        // Save and load GaussianMixtureModel
       /* gmm.save(jsc.sc(), "target/org/apache/spark/JavaGaussianMixtureExample/GaussianMixtureModel");
        GaussianMixtureModel sameModel = GaussianMixtureModel.load(jsc.sc(),
                "target/org.apache.spark.JavaGaussianMixtureExample/GaussianMixtureModel");

        // Output the parameters of the mixture model
        for (int j = 0; j < gmm.k(); j++) {
            System.out.printf("weight=%f\nmu=%s\nsigma=\n%s\n",
                    gmm.weights()[j], gmm.gaussians()[j].mu(), gmm.gaussians()[j].sigma());


        }*/
    }

    @Test
    public void test2(){
        //SparkConf conf = new SparkConf().setAppName("SparkGaussianMixtureExample").setMaster("local[*]");

        SparkSession spark = SparkSession
                .builder()
                .appName("SparkGaussianMixtureExample")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> dataset = spark.read().option("inferSchema", true).text("../dataGMM.txt");

        // Trains a GaussianMixture model
        org.apache.spark.ml.clustering.GaussianMixture gmm = new org.apache.spark.ml.clustering.GaussianMixture()
                .setK(2);
        org.apache.spark.ml.clustering.GaussianMixtureModel model = gmm.fit(dataset);

        // Output the parameters of the mixture model
        for (int i = 0; i < model.getK(); i++) {
            System.out.printf("Gaussian %d:\nweight=%f\nmu=%s\nsigma=\n%s\n\n",
                    i, model.weights()[i], model.gaussians()[i].mean(), model.gaussians()[i].cov());
        }
    }


}
