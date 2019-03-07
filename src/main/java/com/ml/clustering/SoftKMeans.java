package com.ml.clustering;

import static com.ml.sevices.Utils.computeNorm1D;
import static com.ml.sevices.Utils.vectorization;

public class SoftKMeans {

    private Double[] clusterCentroids;
    private double stiffness;
    private String norm;

    public Double[] getClusterCentroids() {
        return clusterCentroids;
    }

    public static class Builder{

        private Double[] clusterCentroids = null;
        private double stiffness = 10e-3;
        private String  norm;

        public Builder setClusterCentroids(Double[] clusterCentroids){
            this.clusterCentroids = clusterCentroids;
            return this;
        }

        public Builder setStiffness(double stiffness){
            this.stiffness = stiffness;
            return this;
        }

        public Builder setNorm(String norm){
            this.norm = norm;
            return this;
        }

        public SoftKMeans build(){
            SoftKMeans softKMeans = new SoftKMeans();
            softKMeans.clusterCentroids = clusterCentroids;
            softKMeans.stiffness = stiffness;
            softKMeans.norm = norm;
            return softKMeans;
        }

    }

    public void clusterer(Double[] data){

        double[] responsibilities = new double[clusterCentroids.length]; // k values
        double[] totalResponsibilities = new double[clusterCentroids.length]; // k values
        double[] combining = new double[clusterCentroids.length]; // k values;
        double sumResponsabilities ;

        for (int n=0; n<data.length; n++){
            sumResponsabilities = 0;
            for(int k=0; k<responsibilities.length; k++){
                responsibilities[k] = Math.exp( -stiffness*computeNorm1D(norm, vectorization(data[n], clusterCentroids[k])));
                sumResponsabilities = sumResponsabilities + responsibilities[k];
            }
            for(int k=0; k<responsibilities.length; k++){
                responsibilities[k] = responsibilities[k]/sumResponsabilities;
                totalResponsibilities[k] = totalResponsibilities[k] + responsibilities[k];
                combining[k] = combining[k] + responsibilities[k]*data[n].doubleValue();
            }
            for(int k=0; k<responsibilities.length; k++){
                clusterCentroids[k]=combining[k]/totalResponsibilities[k];
            }
        }

    }

}
