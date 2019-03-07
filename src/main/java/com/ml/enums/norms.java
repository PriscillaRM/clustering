package com.ml.enums;

public enum norms {

    L1("normL1"),
    L2("normL2"),
    INF("normInf");

    private String name = "";

    norms(String name){ this.name = name;}

    public String toString(){return name;}



}
