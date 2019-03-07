import com.ml.clustering.SoftKMeansGaussianMixture1d;
import com.ml.enums.norms;
import com.ml.sevices.SampleEntity;
import csvfile.ExtractSample;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ml.sevices.Utils.comptorTable;
import static com.ml.sevices.Utils.maximum;

public class DataValidationHistogram extends ApplicationFrame {

    Logger logger = LoggerFactory.getLogger(DataValidationHistogram.class);

    public DataValidationHistogram(final String title){
        super(title);

        String rscPath = "../forge_clementine_per_hour.csv";
        int lineToReadSample = 26;
        XYSeries series  = new XYSeries("realDatas");
        XYSeries series2 = new XYSeries("gaussianApproximation");

        ExtractSample extractSample = new ExtractSample.Builder(rscPath, lineToReadSample).sampleSize(240).build();

        SampleEntity sampleEntity = extractSample.sampleFactory();

        Double[] data = new Double[100];
        Double[] centroids = new Double[2];
        Double[] stDevs = new Double[2];
        Double[] weights = new Double[2];


        for(int i=0; i<100;i++) {
            data[i] = sampleEntity.getSample()[i%24 +14].doubleValue();
            logger.info(" data " + i + " = "+data[i]);

        }

        double[] compteur = comptorTable(data);

        centroids[0] = 1.0;
        centroids[1] = 100.0;
        //centroids[2] = 500.0;

        stDevs[0] = 5.0;
        stDevs[1] = 10.0;
        //stDevs[2] = 10.0;

        weights[0] = 0.5;
        weights[1] = 0.3;
        //weights[2] = 0.2;

        SoftKMeansGaussianMixture1d softKMeans = new SoftKMeansGaussianMixture1d.Builder(centroids, stDevs, weights)
                .setNorm(norms.L2.toString())
                .build();

        softKMeans.clusterer(data);

        double pi0;
        double pi1;
        double pi2;

        double somme = 0.0;

        for(int i=0; i<maximum(data); i++){ somme = somme + compteur[i];}

        NormalDistribution gauss0 = new NormalDistribution(softKMeans.getClusters().get(0).getDoubleTrio().getMean(),
                softKMeans.getClusters().get(0).getDoubleTrio().getStDev());

        pi0 = softKMeans.getClusters().get(0).getDoubleTrio().getWeight();

        NormalDistribution gauss1 = new NormalDistribution(softKMeans.getClusters().get(1).getDoubleTrio().getMean(),
                softKMeans.getClusters().get(1).getDoubleTrio().getStDev());

        pi1 = softKMeans.getClusters().get(1).getDoubleTrio().getWeight();

        /*NormalDistribution gauss2 = new NormalDistribution(softKMeans.getClusters().get(2).getDoubleTrio().getMean(),
                softKMeans.getClusters().get(2).getDoubleTrio().getStDev());

        pi2 = softKMeans.getClusters().get(2).getDoubleTrio().getWeight();*/

        for(int i=0; i<maximum(data); i++){

            series.add(i,compteur[i]/somme);
            //series.add(i, pi1*gauss1.density((double) i)); //+ pi2*gauss2.density((double) i)); pi0*gauss0.density((double) i) +
        }

        logger.info("pi0 = " + pi0 + " mean = " + softKMeans.getClusters().get(0).getDoubleTrio().getMean() + "stDev = " + softKMeans.getClusters().get(0).getDoubleTrio().getStDev() );
        logger.info("pi1 = " + pi1 + " mean = " + softKMeans.getClusters().get(1).getDoubleTrio().getMean() + "stDev = " + softKMeans.getClusters().get(1).getDoubleTrio().getStDev());
        //logger.info("pi2 = " + pi2 + " mean = " + softKMeans.getClusters().get(2).getDoubleTrio().getMean() + "stDev = " + softKMeans.getClusters().get(2).getDoubleTrio().getStDev());

        final XYSeriesCollection seriesCollection = new XYSeriesCollection(series);
        //seriesCollection.addSeries(series2);

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "GlobalLasting Evolution",
                "X",
                "Y",
                seriesCollection,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel2 = new ChartPanel(chart);
        chartPanel2.setPreferredSize(new java.awt.Dimension(1000, 500));
        setContentPane(chartPanel2);
    }

    public static void main(final String[] args) {

        final DataValidationHistogram demoDecomposition = new DataValidationHistogram("Global Evol");
        demoDecomposition.pack();
        RefineryUtilities.centerFrameOnScreen(demoDecomposition);
        demoDecomposition.setVisible(true);

    }

}
