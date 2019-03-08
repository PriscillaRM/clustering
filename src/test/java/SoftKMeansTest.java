import com.ml.clustering.SoftKMeans;
import com.ml.enums.norms;
import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftKMeansTest {

    Logger logger = LoggerFactory.getLogger(SoftKMeans.class);

    @Test
    public void dataBaseClementineSimpleTest(){

        String rscPath = "../test.csv";
        int lineToReadSample = 26;

        ExtractSample extractSample = new ExtractSample.Builder(rscPath, lineToReadSample).sampleSize(240).build();

        SampleEntity sampleEntity = extractSample.sampleFactory();

        Double[] data = new Double[sampleEntity.getSample().length];
        Double[] centroids = new Double[4];

        for(int i=0; i<sampleEntity.getSample().length;i++) {
            data[i] = sampleEntity.getSample()[i].doubleValue();
        }

        centroids[0] = 0.0;
        centroids[1] = 10.0;
        centroids[2] = 50.0;
        centroids[3] = 150.0;

        SoftKMeans softKMeans = new SoftKMeans.Builder().setNorm(norms.L2.toString())
                                                        .setStiffness(10e-2)
                                                        .setClusterCentroids(centroids)
                                                        .build();

        softKMeans.clusterer(data);

        for(int i=0; i<4; i++){
            logger.info(" controid i = " + centroids[i].doubleValue());
        }
    }

    @Test
    public void dataBaseClementineExtractTest(){

        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;
        int nbOfCluster = 3;

        ExtractSample extractSample = new ExtractSample.Builder(rscPath, lineToReadSample).sampleSize(2400).build();

        SampleEntity sampleEntity = extractSample.sampleFactory();

        Double[] data = new Double[50];
        Double[] centroids = new Double[nbOfCluster];

        for(int i=0; i<50;i++) {
            data[i] = sampleEntity.getSample()[i*8+24].doubleValue();
            logger.info(" data " + i + " = " + data[i].doubleValue());
        }

        centroids[0] = 0.0;
        centroids[1] = 10.0;
        centroids[2] = 100.0;
        //centroids[3] = 150.0;

        SoftKMeans softKMeans = new SoftKMeans.Builder().setNorm(norms.L2.toString())
                .setStiffness(10e-2)
                .setClusterCentroids(centroids)
                .build();

        softKMeans.clusterer(data);

        for(int i=0; i<nbOfCluster; i++){
            logger.info(" controid "+ i + " = " + centroids[i].doubleValue());
        }

    }


}
