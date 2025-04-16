package com.voive.android;

public class REPORT_MODAL_TABLE {

    String TBL_ID;
    String TBL_NAME;
    String CAUSE;
    String text;
    String DATE;

    public String getTBL_ID() {
        return TBL_ID;
    }

    public void setTBL_ID(String TBL_ID) {
        this.TBL_ID = TBL_ID;
    }

    public String getTBL_NAME() {
        return TBL_NAME;
    }

    public void setTBL_NAME(String TBL_NAME) {
        this.TBL_NAME = TBL_NAME;
    }

    public String getCAUSE() {
        return CAUSE;
    }

    public void setCAUSE(String CAUSE) {
        this.CAUSE = CAUSE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public void setEdittext(String text){
        this.text=text;
    }
    public String getEdittext(){
        return text;
    }
}
