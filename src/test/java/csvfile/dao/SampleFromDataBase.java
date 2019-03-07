package csvfile.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleFromDataBase {

    Logger logger = LoggerFactory.getLogger(SampleFromDataBase.class);

    private List<UnitSample> sampleList = null;
    private String source = null;
    private String  Context = null;
    private Date dateStart = null;
    private String role = null;

    public List<UnitSample> getSampleList() {
        return sampleList;
    }

    public String getSource() {
        return source;
    }

    public String getContext() {
        return Context;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public String getRole() {
        return role;
    }

    public static class Builder{
        private List<UnitSample> sampleList = new ArrayList<>();
        private String source;
        private String Context;
        private Date dateStart;
        private Date dateEnd;
        private String role;

        public Builder sampleList(List<UnitSample> sampleList) {
            this.sampleList = sampleList;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder context(String context) {
            Context = context;
            return this;
        }

        public Builder dateStart(Date dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public SampleFromDataBase build(){
            SampleFromDataBase sampleFromDataBase = new SampleFromDataBase();
            sampleFromDataBase.sampleList = this.sampleList;
            sampleFromDataBase.Context = this.Context;
            sampleFromDataBase.source = this.source;
            sampleFromDataBase.dateStart = this.dateStart;
            sampleFromDataBase.role =this.role;
            return sampleFromDataBase;
        }
    }

    public void addToList(UnitSample unitSample){
        sampleList.add(unitSample);
    }

}
