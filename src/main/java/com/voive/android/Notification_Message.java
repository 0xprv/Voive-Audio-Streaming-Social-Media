package com.voive.android;

public class Notification_Message {

    public CharSequence sender;
    public long time;
    public CharSequence text;

    public Notification_Message(CharSequence sender, CharSequence text) {
        this.sender = sender;
        this.time = System.currentTimeMillis();
        this.text = text;
    }

    public CharSequence getText(){

        return text;

    }

    public long getTime(){

        return time;
    }
    public CharSequence getSender(){

        return sender;
    }
}
