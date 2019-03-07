package com.ml.sevices.mixtureGaussian;

import com.ml.sevices.DoubleTrio;

public class Cluster {

    private int id;
    private DoubleTrio doubleTrio;

    public int getId() {
        return id;
    }

    public DoubleTrio getDoubleTrio() {
        return doubleTrio;
    }

    public void setDoubleTrio(DoubleTrio doubleTrio) {
        this.doubleTrio = doubleTrio;
    }

    public Cluster(int id) {
        this.id = id;
        //this.doubleTrio = null;
    }
}
