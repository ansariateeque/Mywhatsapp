package com.example.mywhatsapp.Models;

public class GroupMessageModels {
    String userId,name,msg;
    long time;

    public GroupMessageModels(String userId, String name, String msg, long time) {
        this.userId = userId;
        this.name = name;
        this.msg = msg;
        this.time = time;
    }

    public GroupMessageModels(String userId, String name, String msg) {
        this.userId = userId;
        this.name = name;
        this.msg = msg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
