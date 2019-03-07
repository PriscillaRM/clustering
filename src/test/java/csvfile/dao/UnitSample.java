package csvfile.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnitSample {

    Logger logger = LoggerFactory.getLogger(UnitSample.class);

    private Date date;
    private Long numDate;
    private Long globalLasting;

    public Date getDate() {
        return date;
    }

    public Long getNumDate() {
        return numDate;
    }

    public Long getGlobalLasting() {
        return globalLasting;
    }

    public static class Builder{
        private Date date;
        private Long numDate;
        private Long globalLasting;

        public Builder setDate(String stringDate){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            ParsePosition parsePosition = new ParsePosition(0);
            this.date = dateFormat.parse(stringDate,parsePosition);
            return this;
        }

        public Builder setNumDate(String stringNumDate){
            stringNumDate = stringNumDate.substring(1);
            this.numDate = Long.parseLong(stringNumDate);
            return this;
        }

        public Builder setGlobalLasting(String stringGlobalLasting){
            stringGlobalLasting = stringGlobalLasting.substring(1);
            Long tmp = Long.parseLong(stringGlobalLasting);
            double maximum = Math.max(tmp.doubleValue(),0.0);
            Double maximumDb = maximum;
            this.globalLasting = new Long(maximumDb.longValue());
            return this;
        }

        public UnitSample build(){
            UnitSample unitSample = new UnitSample();
            unitSample.date = this.date;
            unitSample.numDate = this.numDate;
            unitSample.globalLasting = this.globalLasting;
            return unitSample;
        }
    }


}
