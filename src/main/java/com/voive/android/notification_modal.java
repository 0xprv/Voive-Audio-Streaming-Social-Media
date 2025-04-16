package com.voive.android;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Notifications")
public class notification_modal extends ParseObject {

    public String SENDER="SENDER_ID";
    public String RECEIVER="USER_ID";
    public String CONTENT="Content";
    public String READ="READ";
    public String MESSAGE_ID="Message_ID";
    public String TYPE="TYPE";
    public String TABLE_ID="TABLE_ID";


    public void setREAD(boolean rdd){

        put(READ,rdd);
    }

    public boolean getRead(){

        return getBoolean("READ");
    }


    public void setTABLE(String rdd){

        put(TABLE_ID,rdd);
    }

    public String getTABLE(){

        return getString("TABLE_ID");
    }


    public String getSENDER() {
        return getString("SENDER_ID");
    }

    public void setSENDER(String SENDERX) {
       put(SENDER,SENDERX);
    }

    public String getRECEIVER() {
        return getString("USER_ID");
    }

    public void setRECEIVER(String RECEIVERX) {
        put(RECEIVER,RECEIVERX);
    }

    public String getCONTENT() {
        return getString("Content");
    }

    public void setCONTENT(String CONTENTX) {
        put(CONTENT,CONTENTX);
    }

    public String getMESSAGE_ID() {
        return getString("Messag_ID");
    }

    public void setMESSAGE_ID(String MESSAGE_IDX) {
        put(MESSAGE_ID,MESSAGE_IDX);
    }

    public String getTYPE() {
        return getString("Type");
    }

    public void setTYPE(String TYPEX) {
       put(TYPE,TYPEX);
    }
}
