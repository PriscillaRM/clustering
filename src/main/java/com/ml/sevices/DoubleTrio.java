package com.ml.sevices;

public class DoubleTrio {

    Double mean;
    Double stDev;
    Double weight;

    public DoubleTrio(){
        this.mean = null;
        this.stDev = null;
        this.weight = null;
    }

    public DoubleTrio(Double mean, Double stDev, Double weight){
        this.mean = mean;
        this.stDev = stDev;
        this.weight = weight;
    }

    public Double getMean() {
        return mean;
    }

    public Double getStDev() {
        return stDev;
    }

    public Double getWeight() {
        return weight;
    }
}
