package com.augmentum.ot.remoteService.dataObject;

public class Column {

    private String type;
    private String information;
    
    public Column(String type, String information) {
        this.type = type;
        this.information = information;
    }
    
    public Column() {
        //empty
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
    
    
   
}
