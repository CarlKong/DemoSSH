package com.augmentum.ot.remoteService.dataObject;

public class Meta {

    private String name;
    private Integer notice;
    
    public Meta(String name, Integer notice) {
        this.name = name;
        this.notice = notice;
    }
    
    public Meta() {
        //empty
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getNotice() {
        return notice;
    }
    
    public void setNotice(Integer notice) {
        this.notice = notice;
    }
    
    
}
