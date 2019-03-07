package csvfile;

import com.ml.sevices.SampleEntity;
import csvfile.dao.SampleFromDataBase;
import csvfile.dao.UnitSample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExtractSample {

    private String pathFile;
    private int lineNbToReadSample;
    private SampleFromDataBase sampleFromDataBase;
    private int sampleSize;

    public String getPathFile() {
        return pathFile;
    }

    public int getLineNbToReadSample() {
        return lineNbToReadSample;
    }

    public SampleFromDataBase getSampleFromDataBase() {
        return sampleFromDataBase;
    }

    public static class Builder{

        private String pathFile;
        private int lineNbToReadSample;
        private int sampleSize = 100;

        public Builder(String pathFile, int lineNbToReadSample) {
            this.pathFile = pathFile;
            this.lineNbToReadSample = lineNbToReadSample;
        }

        public Builder sampleSize(int sampleSize){
            this.sampleSize = sampleSize;
            return this;
        }

        public ExtractSample build(){
            ExtractSample extractSample = new ExtractSample();
            extractSample.pathFile = this.pathFile;
            extractSample.lineNbToReadSample = this.lineNbToReadSample;
            extractSample.sampleSize = this.sampleSize;
            extractSample.load();
            return extractSample;
        }
    }



    public void load() {
        Path rscPath = Paths.get(pathFile); //("../forge_clementine_per_hour.csv");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(rscPath);
        } catch (IOException e) {
            System.out.println("Impossible de lire le fichier des commandes");
        }
        if (lines.size() < 2) {
            System.out.println("Il n'y a pas de commande dans le fichier");
            return;
        }

        sampleFromDataBase = new SampleFromDataBase.Builder()
                .source(lines.get(0))
                .context(lines.get(1))
                .role(lines.get(5))
                .build();

        int i=lineNbToReadSample;
        while (lines.get(i) !=null && sampleFromDataBase.getSampleList().size()!=sampleSize) {
            String eachLine = lines.get(i);
            String[] split = eachLine.split(",");
            UnitSample unitSample = new UnitSample.Builder()
                    .setDate(split[0])
                    .setNumDate(split[1])
                    .setGlobalLasting(split[2])
                    .build();
            sampleFromDataBase.addToList(unitSample);
            i++;
        }
    }

    public SampleEntity sampleFactory(){

        List<Double> sampleList = new ArrayList<>();
        Long[] timeTable =new Long[this.sampleFromDataBase.getSampleList().size()];
        for(int i=0; i<this.sampleFromDataBase.getSampleList().size();i++){
            Double smp = sampleFromDataBase.getSampleList().get(i).getGlobalLasting().doubleValue();
            sampleList.add(smp);
            timeTable[i] = sampleFromDataBase.getSampleList().get(i).getGlobalLasting();
        }

        SampleEntity sampleEntity = new SampleEntity.Builder().setSample(sampleList).setTimeTable(timeTable).build();

        return sampleEntity;
    }



}
