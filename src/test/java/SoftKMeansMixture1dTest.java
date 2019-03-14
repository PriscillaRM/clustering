import com.ml.clustering.SoftKMeans;
import com.ml.clustering.SoftKMeansGaussianMixture1d;
import com.ml.enums.norms;
import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftKMeansMixture1dTest {

    Logger logger = LoggerFactory.getLogger(SoftKMeansMixture1dTest.class);

    @Test
    public void dataBaseClementineSimpleTest(){

        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;

        ExtractSample extractSample = new ExtractSample.Builder(rscPath, lineToReadSample).sampleSize(240).build();

        SampleEntity sampleEntity = extractSample.sampleFactory();

        Double[] data = new Double[100];//[sampleEntity.getSample().length];
        Double[] centroids = new Double[2];//[4];
        Double[] stDevs = new Double[2];//[4];
        Double[] weights = new Double[2];//[4];

        /*for(int i=0; i<sampleEntity.getSample().length;i++) {
            data[i] = sampleEntity.getSample()[i].doubleValue();
        }*/

        for(int i=0; i<100;i++) {
            data[i] = sampleEntity.getSample()[i%24].doubleValue();
            logger.info(" data " + i + " = "+data[i]);
        }

        centroids[0] = 1.0;
        centroids[1] = 100.0;
        //centroids[2] = 50.0;
        //centroids[3] = 150.0;

        stDevs[0] = 5.0;
        stDevs[1] = 10.0;
        //stDevs[2] = 10.0;
        //stDevs[3] = 10.0;

        weights[0] = 0.5;
        weights[1] = 0.5;
        //weights[2] = 0.25;
        //weights[3] = 0.25;

        SoftKMeansGaussianMixture1d softKMeans = new SoftKMeansGaussianMixture1d.Builder(centroids, stDevs, weights)
                .setNorm(norms.L2.toString())
                .build();

        softKMeans.clusterer(data);

        for(int i=0; i<2; i++){
            logger.info(" i = " +i);
            logger.info(" controid = " + softKMeans.getClusters().get(i).getDoubleTrio().getMean());
            logger.info(" stDev = " + softKMeans.getClusters().get(i).getDoubleTrio().getStDev());
            logger.info(" weight = " + softKMeans.getClusters().get(i).getDoubleTrio().getWeight());
        }
    }


}
