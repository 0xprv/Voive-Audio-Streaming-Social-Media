package com.voive.android.sample;


import com.voive.android.media.RtcTokenBuilder;

public class RtcTokenBuilderSample {
    static String appId = "970CA35de60c44645bbae8a215061b33";
    static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    static String channelName = "7d72365eb983485397e3e3f9d460bdda";
    static String userAccount = "2082341273";
    static int uid = 2082341273;
    static int expirationTimeInSeconds = 3600; 


    public static void main(String[] args) throws Exception {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(appId, appCertificate,  
        		 channelName, userAccount, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);
        
        result = token.buildTokenWithUid(appId, appCertificate,  
       		 channelName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);
        result = token.buildTokenWithUid(appId, appCertificate, channelName, uid,
                timestamp, timestamp, timestamp,
                timestamp);
        System.out.println(result);
        result = token.buildTokenWithUserAccount(appId, appCertificate, channelName,
                userAccount, timestamp, timestamp, timestamp,
                timestamp);
        System.out.println(result);
    }
}
