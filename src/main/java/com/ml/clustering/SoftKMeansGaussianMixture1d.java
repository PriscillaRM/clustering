package com.ml.clustering;

import com.ml.enums.norms;
import com.ml.sevices.DoubleTrio;
import com.ml.sevices.mixtureGaussian.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ml.sevices.mixtureGaussian.SoftKMeansGaussianMixtureServices.gaussDensity;
import static com.ml.sevices.mixtureGaussian.SoftKMeansGaussianMixtureServices.initializeClusters;

public class SoftKMeansGaussianMixture1d {

    Logger logger = LoggerFactory.getLogger(SoftKMeansGaussianMixture1d.class);

    private List<Cluster> clusters;
    private String norm;

    public List<Cluster> getClusters() {
        return clusters;
    }

    public static class Builder{

        Logger logger2 = LoggerFactory.getLogger(Builder.class);

        private List<Cluster> clusters;
        private String norm = norms.L2.toString();

        public Builder (Double[] means, Double[] stDevs, Double[] weights){
            if (means.length!= stDevs.length || means.length!=weights.length){
                logger2.info(" size error ");
            }
            else{
                clusters = new ArrayList<Cluster>();
                initializeClusters(this.clusters, means, stDevs, weights);
            }
        }

        public Builder setNorm(String norm){
            this.norm = norm;
            return this;
        }

        public SoftKMeansGaussianMixture1d build(){
            SoftKMeansGaussianMixture1d softKMeans = new SoftKMeansGaussianMixture1d();
            softKMeans.clusters = this.clusters;
            softKMeans.norm = this.norm;
            return softKMeans;
        }
    }


    public void clusterer(Double[] datas){

        double[] responsibilities = new double[clusters.size()];
        double[] totalResponsibilities = new double[clusters.size()];

        double[] weightTMP = new double[clusters.size()];
        double[] meanTMP = new double[clusters.size()];
        double[] stDevTMP = new double[clusters.size()];

        double sumResponsibilitiesK = 0.0;
        double[] sumResponsibilitiesNProductMean = new double[clusters.size()];
        double[] sumResponsibilitiesNProductVar = new double[clusters.size()];
        double sumTotalResponsibilitiesK = 0.0;

        for(int k=0; k<clusters.size(); k++) {
            weightTMP[k] = clusters.get(k).getDoubleTrio().getWeight().doubleValue();
            meanTMP[k] = clusters.get(k).getDoubleTrio().getMean().doubleValue();
            stDevTMP[k] = clusters.get(k).getDoubleTrio().getStDev().doubleValue();
        }

        for(int n=0; n<datas.length; n++) {

            //// Assignement step
            for (int k = 0; k < clusters.size(); k++) {

                responsibilities[k] = weightTMP[k] * gaussDensity(norm, datas[n].doubleValue(), weightTMP[k], meanTMP[k], stDevTMP[k]);
                sumResponsibilitiesK = sumResponsibilitiesK + responsibilities[k];

            }

            for (int k = 0; k < clusters.size(); k++) {

                responsibilities[k] = responsibilities[k] / sumResponsibilitiesK;
                sumResponsibilitiesNProductMean[k] = sumResponsibilitiesNProductMean[k] + responsibilities[k] * datas[n].doubleValue();
                sumResponsibilitiesNProductVar[k] = sumResponsibilitiesNProductVar[k] + responsibilities[k] * (datas[n].doubleValue() - meanTMP[k])*(datas[n].doubleValue() - meanTMP[k]);
                totalResponsibilities[k] = totalResponsibilities[k] + responsibilities[k];
                sumTotalResponsibilitiesK = sumTotalResponsibilitiesK + totalResponsibilities[k];

                meanTMP[k] = sumResponsibilitiesNProductMean[k] / totalResponsibilities[k];
                stDevTMP[k] = Math.sqrt(sumResponsibilitiesNProductVar[k]) / Math.sqrt(totalResponsibilities[k]);

            }

            for (int k = 0; k < clusters.size(); k++) {
                weightTMP[k] = totalResponsibilities[k] / sumTotalResponsibilitiesK;
                clusters.get(k).setDoubleTrio(new DoubleTrio(meanTMP[k], stDevTMP[k], weightTMP[k]));
            }

            sumResponsibilitiesK =0.0;
            sumTotalResponsibilitiesK =0.0;
        }

    }


}
