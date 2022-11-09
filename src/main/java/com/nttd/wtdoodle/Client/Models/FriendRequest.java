package com.nttd.wtdoodle.Client.Models;

import java.util.ArrayList;

public class FriendRequest {
    public static FriendRequest instance = new FriendRequest();
    ArrayList<FriendRequestData> requestData = new ArrayList<FriendRequestData>();
    public FriendRequest(){}
    public static FriendRequest getInstance(){
        return instance;
    }
    public ArrayList<FriendRequestData> getRequestData(){
        return requestData;
    }
}
