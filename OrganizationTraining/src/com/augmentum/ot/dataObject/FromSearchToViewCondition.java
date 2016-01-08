package com.augmentum.ot.dataObject;

public class FromSearchToViewCondition{

    private String backupId = "";

    private int nowId = 0;

    private int totalPageNum=0;
    
    private int isNoSelectedflag=0;
    
    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    public int getNowId() {
        return nowId;
    }

    public void setNowId(int nowId) {
        this.nowId = nowId;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getIsNoSelectedflag() {
        return isNoSelectedflag;
    }

    public void setIsNoSelectedflag(int isNoSelectedflag) {
        this.isNoSelectedflag = isNoSelectedflag;
    }
    
}
