package com.ml.sevices;

import java.util.List;

public class SampleEntity {
    private Double[] sample;
    private Long time;
    private Long[] timeTable;

    public static class Builder{

        private Double[] sample;
        private Long time;
        private Long[] timeTable;

        public Builder setSample(Double[] sample) {
            this.sample = sample;
            return this;
        }

        public Builder setSample(List<Double> sample) {
            this.sample = ListToTable(sample);
            return  this;
        }

        public Builder setTime(Long time){
            this.time = time;
            return this;
        }

        public Builder setTimeTable(Long[] timeTable){
            this.timeTable = timeTable;
            return this;
        }

        public  SampleEntity build(){
            SampleEntity sampleEntity = new SampleEntity();
            sampleEntity.sample = this.sample;
            sampleEntity.time = this.time;
            sampleEntity.timeTable =this.timeTable;
            return sampleEntity;
        }
    }

    public Double[] getSample() {
        return sample;
    }

    public Long getTime(){return time;}

    public Long[] getTimeTable(){return timeTable;}

    public static Double[] ListToTable(List<Double> sampleL){
        Double[] sampleTmp = new Double[sampleL.size()];
        for(int i=0; i<sampleL.size(); i++){
            sampleTmp[i] = sampleL.get(i);
        }

        return sampleTmp;
    }
}
