package com.voive.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class handlig_broadcast_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String ACTION=intent.getAction();

     /* if(ACTION.equals("Accept")){
          
          ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
          parseObjectParseQuery.getInBackground(intent.getStringExtra("TABLE_ID"), new GetCallback<ParseObject>() {
              @Override
              public void done(ParseObject object, ParseException e) {

                  List<String> REQQ=object.getList("REQUESTS");
                  REQQ.remove(intent.getStringExtra("USER_ID"));
                  object.put("REQUESTS",REQQ);

                  List<String> subscriber = object.getList("table_subscribers");
                  subscriber.add(intent.getStringExtra("USER_ID"));
                  object.put("table_subscribers", subscriber);


                  List<String> notifyr = object.getList("NOTIFY");
                  notifyr.add(intent.getStringExtra("USER_ID"));
                  object.put("NOTIFY", notifyr);


                  if(object.getBoolean("EVERYONE_CAN_TALK")){

                      List<String> SPK = object.getList("WHO_SPEAK");
                      SPK.add(intent.getStringExtra("USER_ID"));
                      object.put("WHO_SPEAK", SPK);

                      List<String> listner = object.getList("table_listners");
                      listner.add(intent.getStringExtra("USER_ID"));
                      object.put("table_listners", listner);


                      object.saveInBackground(new SaveCallback() {
                          @Override
                          public void done(ParseException e) {


                              ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                              parseInstallationParseQuery.whereEqualTo("UserId", intent.getStringExtra("USER_ID"));
                              parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                  @Override
                                  public void done(List<ParseInstallation> objects, ParseException e) {


                                      JSONObject data = new JSONObject();
// Put data in the JSON object
                                      try {
                                          data.put("alert", ParseUser.getCurrentUser().getUsername() + " Accept Your Subscription Request 🎉🎉");
                                          data.put("title", object.getString("table_name"));
                                          data.put("TYPE", Constant.TABLE_OTHER_NOTIFICATION);
                                      } catch (JSONException te) {
                                          throw new IllegalArgumentException("unexpected parsing error", te);
                                      }
// Configure the push
                                      ParsePush push = new ParsePush();
                                      push.setQuery(parseInstallationParseQuery);
                                      push.setData(data);
                                      push.sendInBackground();
                                  }
                              });


                              notification_modal notification_modal = new notification_modal();
                              notification_modal.setTYPE(Constant.TABLE_OTHER_NOTIFICATION);
                              notification_modal.setREAD(false);
                              notification_modal.setCONTENT(" Accept Your Subscription Request 🎉🎉");
                              notification_modal.setSENDER(ParseUser.getCurrentUser().getObjectId());
                              notification_modal.setRECEIVER(intent.getStringExtra("USER_ID"));
                              notification_modal.saveInBackground();


                          }
                      });

                  }
                  else {

                      List<String> listner = object.getList("ONLINE");
                      listner.add(intent.getStringExtra("USER_ID"));
                      object.put("ONLINE", listner);


                      object.saveInBackground(new SaveCallback() {
                          @Override
                          public void done(ParseException e) {


                              ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                              parseInstallationParseQuery.whereEqualTo("UserId", intent.getStringExtra("USER_ID"));
                              parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                  @Override
                                  public void done(List<ParseInstallation> objects, ParseException e) {


                                      JSONObject data = new JSONObject();
// Put data in the JSON object
                                      try {
                                          data.put("alert", ParseUser.getCurrentUser().getUsername() + " Accept Your Subscription Request 🎉🎉");
                                          data.put("title", object.getString("table_name"));
                                          data.put("TYPE", Constant.TABLE_OTHER_NOTIFICATION);
                                      } catch (JSONException te) {
                                          throw new IllegalArgumentException("unexpected parsing error", te);
                                      }
// Configure the push
                                      ParsePush push = new ParsePush();
                                      push.setQuery(parseInstallationParseQuery);
                                      push.setData(data);
                                      push.sendInBackground();
                                  }
                              });



                              notification_modal notification_modal = new notification_modal();
                              notification_modal.setTYPE(Constant.TABLE_OTHER_NOTIFICATION);
                              notification_modal.setREAD(false);
                              notification_modal.setCONTENT(" Accept Your Subscription Request 🎉🎉");
                              notification_modal.setSENDER(ParseUser.getCurrentUser().getObjectId());
                              notification_modal.setRECEIVER(intent.getStringExtra("USER_ID"));
                              notification_modal.saveInBackground();


                          }
                      });
                  }

              }
          });

      

      }
      else {

          ParseQuery<ParseObject> parseObjectParseQuery=new ParseQuery<ParseObject>("live_tables");
          parseObjectParseQuery.getInBackground(intent.getStringExtra("TABLE_ID"), new GetCallback<ParseObject>() {
              @Override
              public void done(ParseObject object, ParseException e) {
                  List<String> REQQ=object.getList("REQUESTS");
                  REQQ.remove(intent.getStringExtra("USER_ID"));
                  object.put("REQUESTS",REQQ);
                  object.saveInBackground();
              }
          });
          ParseQuery<ParseUser> FOR_NOTIFY=ParseUser.getQuery();
          FOR_NOTIFY.getInBackground(intent.getStringExtra("USER_ID"), new GetCallback<ParseUser>() {
              @Override
              public void done(ParseUser object, ParseException e) {



                  ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
                  parseInstallationParseQuery.whereEqualTo("UserId",intent.getStringExtra("USER_ID"));
                  parseInstallationParseQuery.findInBackground(new FindCallback<ParseInstallation>() {
                      @Override
                      public void done(List<ParseInstallation> objects, ParseException e) {


                          JSONObject data = new JSONObject();
// Put data in the JSON object
                          try {
                              data.put("alert", object.getString("table_name")+" Rejected your request to be part of the table");
                              data.put("title",object.getString("table_name"));
                              data.put("Type",Constant.CASUAL);
                          } catch ( JSONException eT) {

                              Log.i("ERROR", eT.getLocalizedMessage());
                              // should not happen
                          }
                          ParsePush push = new ParsePush();
                          push.setQuery(parseInstallationParseQuery);
                          push.setData(data);
                          push.sendInBackground();

                      }
                  });

                  if(object.getBoolean("In_App_updates")){



                      notification_modal notification_modal = new notification_modal();
                      notification_modal.setTYPE(Constant.TABLE_OTHER_NOTIFICATION);
                      notification_modal.setREAD(false);
                      notification_modal.setCONTENT(" Rejected your request to be part of the table");
                      notification_modal.setSENDER(ParseUser.getCurrentUser().getObjectId());
                      notification_modal.setRECEIVER(intent.getStringExtra("USER_ID"));
                      notification_modal.saveInBackground();



                  }


              }
          });
      }*/



    }
}
