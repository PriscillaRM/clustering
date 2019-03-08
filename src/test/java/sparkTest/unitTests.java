package sparkTest;

import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ml.sevices.Utils.comptorTable;

public class unitTests {

    Logger logger = LoggerFactory.getLogger(unitTests.class);

    @Test
    public void test1(){
        String rscPath = "../test.csv";
        int lineToReadSample = 26;

        ExtractSample extractSample = new ExtractSample.Builder(rscPath, lineToReadSample).sampleSize(240).build();

        SampleEntity sampleEntity = extractSample.sampleFactory();

        List<Double> data = new ArrayList<>();


        for(int i=0; i<100;i++) {
            data.add( sampleEntity.getSample()[i%24 +14].doubleValue());
            logger.info(" data " + i + " = "+data.get(i));

        }

        SparkSession sparkSession = SparkSession.builder().getOrCreate();

        //Dataset dataset = sparkSession.createDataFrame(data, )


    }


}
