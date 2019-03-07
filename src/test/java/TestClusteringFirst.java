import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestClusteringFirst {

    @Test
    public void test1() {
        int n = 5;
        double[] init = new double[2];
        Collection<DoublePoint> x = new ArrayList<DoublePoint>();
        Cluster cluster = new Cluster();


        FuzzyKMeansClusterer fuzzyKMeansClusterer = new FuzzyKMeansClusterer(2,10);

        for (int i = 0; i < n; i++) {
            init[0]=(double) i;
            init[1]=i*2.0;
            if(i==2){init[1]=100.0;}
            x.add(new DoublePoint(init));
            //Cluster.addPoint(new DoublePoint(init));
            System.out.println("in collection" + ((ArrayList<DoublePoint>) x).get(i).getPoint()[1]);
            //System.out.println("in Cluster" + Cluster.getPoints().get(i));
        }

        fuzzyKMeansClusterer.cluster(x);
        System.out.println(fuzzyKMeansClusterer.getDataPoints());
        System.out.println(fuzzyKMeansClusterer.getClusters());


    }


}
