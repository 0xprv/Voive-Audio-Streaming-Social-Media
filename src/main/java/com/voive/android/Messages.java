package com.voive.android;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("session_messages")

public class Messages extends ParseObject {

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
   public String section_id="Section_ID";
    public String MENTIONS_LIST="MENTIONS";
    public String ATTACH_USERNAME="ATTACH_USERNAME";
   public String id_of_message="ATTACHED_MESSAGE_ID";
   public String id_of_message_sender="ATTACHED_MESSAGE_ID_SENDER";
    public String VIDEO="VIDEO";


    public String getId_of_message() {

        return getString("ATTACHED_MESSAGE_ID");
    }

    public void setId_of_message(String id_of_message_x) {

        put(id_of_message,id_of_message_x);

    }



    public void SET_VIDEo(ParseFile VDX){

        put(VIDEO,VDX);
    }

    public ParseFile get_VIDEO(){

        return getParseFile("VIDEO");
    }





    public String getId_of_message_sender() {

        return getString("ATTACHED_MESSAGE_ID_SENDER");
    }

    public void setId_of_message_sender(String id_of_message_x) {

        put(id_of_message_sender,id_of_message_x);

    }










    public String get_file_name(){

       return getString("file_name");
   }

   public void setFile_name(String namex){
       put(file_name,namex);}

    public void set_mentions(List<String> ss){
        put(MENTIONS_LIST,ss); }
    public List<String> get_mention(){ return getList("MENTIONS"); }

    public void set_ATTACH_USERNAME(String nme){

        put(ATTACH_USERNAME,nme);

    }

    public String get_ATTACH_USERNAME(){

        return getString("ATTACH_USERNAME");
    }

    public ParseFile getFile() {
        return getParseFile("file");
    }

    public void setFile(ParseFile filex) {
       put(file,filex);
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


    public String getSection_id(){return getString(section_id);}

    public void setSectionId(String S_id){put(section_id,S_id);}


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
