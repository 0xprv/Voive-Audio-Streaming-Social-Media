package com.voive.android.sample;


import com.voive.android.rtm.RtmTokenBuilder;

public class RtmTokenBuilderSample {
    private static String appId = "970CA35de60c44645bbae8a215061b33";
    private static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    private static String userId = "2882341273";
    private static int expireTimestamp = 0;

    public static void main(String[] args) throws Exception {
    	RtmTokenBuilder token = new RtmTokenBuilder();
        String result = token.buildToken(appId, appCertificate, userId, RtmTokenBuilder.Role.Rtm_User, expireTimestamp);
        System.out.println(result);
    }
}
