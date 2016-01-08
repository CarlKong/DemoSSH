package com.augmentum.ot.remoteService.dataObject;

public class Display {

    private Meta meta;
    private Screens screens;
    
    public Display(Meta meta, Screens screens) {
        this.meta = meta;
        this.screens = screens;
    }
    
    public Display(Meta meta) {
        this.meta = meta;
    }
    
    public Display() {
        //empty
    }
    
    public Meta getMeta() {
        return meta;
    }
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Screens getScreens() {
        return screens;
    }

    public void setScreens(Screens screens) {
        this.screens = screens;
    }
    
 
}
