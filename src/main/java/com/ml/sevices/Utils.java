package com.ml.sevices;

import com.ml.enums.norms;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;

public class Utils {

    public static Vector1D vectorization(Double data, Double mean){

        Vector1D vector1D = new Vector1D(data.doubleValue()-mean.doubleValue());

        return vector1D;
    }

    public static double computeNorm1D( String norm, Vector1D vector1D){

        if (norm.equals(norms.L1)==true){
            return vector1D.getNorm1();
        }
        else if (norm.equals(norms.INF)==true){
            return vector1D.getNormInf();
        }
        else{
            return vector1D.getNorm();
        }
    }

    public static int maximum(Double[] datas){
        int maximum = datas[0].intValue();

        for(int i=1; i<datas.length; i++){
            if(maximum<datas[i].intValue()){
                maximum = datas[i].intValue();
            }
        }

        return maximum;
    }

    public static double[] comptorTable(Double[] datas){
        double[] comptor = new double[maximum(datas)];

        for(int i=0; i<datas.length; i++){
            for(int j=0; j<comptor.length; j++){
                if(datas[i].intValue()==j){
                    System.out.println(" data to int " + datas[i].intValue());
                    comptor[j] = comptor[j]+1.0;
                }
            }
        }
        return comptor;
    }

}
