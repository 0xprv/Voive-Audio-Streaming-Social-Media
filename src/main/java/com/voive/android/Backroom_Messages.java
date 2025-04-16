package com.voive.android;

import com.giphy.sdk.core.models.Media;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("backroom_messages")
public class Backroom_Messages extends ParseObject {

    public String sender_ID="sender";
    public String text="text";
    public String animated="animated";
    public String typee="type";
    public String audio="audio";
    public String image="image";
    public String file="file";
    public String file_extension="extension";
    public String file_size="file_size";
    public String file_name="file_name";
    public String receiver="Receiver";
    public String IS_READ="IS_READ";
    public String MENTIONS_LIST="MENTIONS";
    public String OTHER_TEXT="OTHER_TEXT";
    public String VIDEO="VIDEO";
    public String CLIP="CLIP";


    public Media getCLIP(){

        return (Media) get(CLIP);
    }
    public void setCLIP(Media mddxx){

        put(CLIP,mddxx);
    }

    public String getReceiver() {
        return getString(receiver);
    }

    public void setReceiver(String receiverX) {
        put(receiver,receiverX);
    }

    public String id_of_message="ATTACHED_MESSAGE_ID";


    public String getId_of_message() {
        return getString("ATTACHED_MESSAGE_ID");
    }

    public void setId_of_message(String id_of_message_x) {
        put(id_of_message,id_of_message_x);
    }

    public void set_OTHER_TEXT(String xcd){

        put(OTHER_TEXT,xcd);
    }

    public String get_OTHER_TEXT(){

        return getString("OTHER_TEXT");
    }

    public void set_mentions(List<String> ss){

        put(MENTIONS_LIST,ss);

    }
    public List<String> get_mention(){

        return getList("MENTIONS");

    }


    public String get_file_name(){

        return getString("file_name");
    }

    public void setFile_name(String namex){

        put(file_name,namex);
    }


    public boolean get_read(){

        return getBoolean("IS_READ");

    }

    public void setIS_READ(boolean stateX){

        put(IS_READ,stateX);
    }
    public ParseFile getFile() {
        return getParseFile("file");
    }

    public void setFile(ParseFile filex) {
        put(file,filex);
    }


    public void SET_VIDEo(ParseFile VDX){

        put(VIDEO,VDX);
    }

    public ParseFile get_VIDEO(){

        return getParseFile("VIDEO");
    }

    public String get_extension() {
        return getString("extension");
    }

    public void set_extension(String extesnionx) {
        put(file_extension,extesnionx);
    }
    public String get_File_size() {
        return getString("file_size");
    }

    public void set_File_size(String sizex) {
        put(file_size,sizex);
    }


    public ParseFile getAudio(){

        return getParseFile("Audio");

    }
    public void setAudio(ParseFile parseFile){

        put(audio,parseFile);
    }



    public String getAnimated() {
        return getString(animated);
    }

    public void setAnimated(String emoji) {
        put(animated,emoji);
    }

    public ParseFile getimage() {
        return getParseFile("image");
    }

    public void setimage(ParseFile file) {
        put(image,file);
    }

    public String getType() {
        return getString(typee);
    }

    public void setType(String type) {
        put(typee,type);
    }

    public String getsender() {
        return getString(sender_ID);
    }

    public void setsender(String sender) {
        put(sender_ID,sender);
    }

    public String getText() {
        return getString(text);
    }

    public void setText(String message) {
        put(text,message);
    }
}
