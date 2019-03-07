package com.ml.sevices.mixtureGaussian;

import com.ml.sevices.DoubleTrio;

import java.util.List;

import static com.ml.sevices.Utils.computeNorm1D;
import static com.ml.sevices.Utils.vectorization;

public class SoftKMeansGaussianMixtureServices {

    public static Cluster createCluster(int id, Double mean, Double stDev, Double weight){
        DoubleTrio doubleTrio = new DoubleTrio(mean, stDev, weight);
        Cluster cluster = new Cluster(id);
        cluster.setDoubleTrio(doubleTrio);
        return cluster;
    }

    public static void initializeClusters(List<Cluster> clusters, Double[] means, Double[] stDevs, Double[] weights){

        for (int i=0; i<means.length; i++){

            clusters.add(createCluster(i, means[i], stDevs[i], weights[i]));

        }

    }

    public static double gaussDensity( String norm, double x, double weight, double mean, double stDev ){
        return (  weight * ( 1.0/(Math.sqrt(2)*stDev) ) * Math.exp( -( computeNorm1D(norm, vectorization(x, mean) )/(2*stDev*stDev) ) ) );
    }



}
