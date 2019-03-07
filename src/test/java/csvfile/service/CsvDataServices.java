package csvfile.service;

public class CsvDataServices {

    public static double[] oneSubCycle(double[] data, int subCycleSize, int periodicity, int seasonNb){
        double[] tab = new double[subCycleSize];
        for(int i = 0; i<subCycleSize; i++ ){
            tab[i]=data[periodicity*i+seasonNb];
        }
        return tab;
    }

}
