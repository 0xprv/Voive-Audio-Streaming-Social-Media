package com.voive.android.sample;



import android.os.Build;

import androidx.annotation.RequiresApi;

import com.voive.android.media.AccessToken2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class AccessTokenInspector {
    public static void main(String[] args) {
        AccessTokenInspector inspector = new AccessTokenInspector();
        inspector.inspect(args[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void inspect(String input) {
        AccessToken2 token = new AccessToken2();
        System.out.printf("parsing token: %s\n\n", input);
        token.parse(input);
        System.out.printf("appId:%s\n", token.appId);
        System.out.printf("appCert:%s\n", token.appCert);
        System.out.printf("salt:%d\n", token.salt);
        System.out.printf("issueTs:%d\n", token.issueTs);
        System.out.printf("expire:%d\n", token.expire);
        System.out.printf("services:\n");

        for (AccessToken2.Service service : token.services.values()) {
            System.out.printf("\t{%s}\n", toServiceStr(service));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String toServiceStr(AccessToken2.Service service) {
        if (service.getServiceType() == AccessToken2.SERVICE_TYPE_RTC) {
            AccessToken2.ServiceRtc serviceRtc = (AccessToken2.ServiceRtc) service;
            return String.format("type:rtc, channel:%s, uid: %s, privileges: [%s]}", serviceRtc.getChannelName(),
                    serviceRtc.getUid(), toRtcPrivileges(serviceRtc.getPrivileges()));
        } else if (service.getServiceType() == AccessToken2.SERVICE_TYPE_RTM) {
            AccessToken2.ServiceRtm serviceRtm = (AccessToken2.ServiceRtm) service;
            return String.format("type:rtm, user_id:%s, privileges:[%s]", serviceRtm.getUserId(),
                    toRtmPrivileges(serviceRtm.getPrivileges()));
        } else if (service.getServiceType() == AccessToken2.SERVICE_TYPE_STREAMING) {
            AccessToken2.ServiceStreaming serviceStreaming = (AccessToken2.ServiceStreaming) service;
            return String.format("type:streaming, privileges:[%s]", toStreamingPrivileges(serviceStreaming.getPrivileges()));
        } else if (service.getServiceType() == AccessToken2.SERVICE_TYPE_CHAT) {
            AccessToken2.ServiceChat serviceChat = (AccessToken2.ServiceChat) service;
            return String.format("type:chat, user_id:%s, privileges:[%s]", serviceChat.getUserId(),
                    toChatPrivileges(serviceChat.getPrivileges()));
        }
        return "unknown";
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String toRtcPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_JOIN_CHANNEL.intValue)) {
            privilegeStrList.add(String.format("JOIN_CHANNEL(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_JOIN_CHANNEL.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_AUDIO_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_AUDIO_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_AUDIO_STREAM.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_VIDEO_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_VIDEO_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_VIDEO_STREAM.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_DATA_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_DATA_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_DATA_STREAM.intValue)));
        }
        return String.join(",", privilegeStrList);
    }

    private String toRtmPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeRtm.PRIVILEGE_JOIN_LOGIN.intValue)) {
            privilegeStrList.add(String.format("JOIN_LOGIN(%d)",
                    privileges.get(AccessToken2.PrivilegeRtm.PRIVILEGE_JOIN_LOGIN.intValue)));
        }
        return String.join(",", privilegeStrList);
    }

    private String toStreamingPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeStreaming.PRIVILEGE_PUBLISH_MIX_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_MIX_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeStreaming.PRIVILEGE_PUBLISH_MIX_STREAM.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeStreaming.PRIVILEGE_PUBLISH_RAW_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_RAW_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeStreaming.PRIVILEGE_PUBLISH_RAW_STREAM.intValue)));
        }
        return String.join(",", privilegeStrList);
    }

    private String toChatPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_USER.intValue)) {
            privilegeStrList.add(String.format("USER(%d)",
                    privileges.get(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_USER.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_APP.intValue)) {
            privilegeStrList.add(String.format("APP(%d)",
                    privileges.get(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_APP.intValue)));
        }
        return String.join(",", privilegeStrList);
    }
}
