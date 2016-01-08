package com.augmentum.ot.remoteService.dataObject;

import java.util.List;

public class Screens {

    private String i10n;
    private String slideEffect;
   
    private List<Screen> screenList;
    
    public Screens(String i10n, String slideEffect, List<Screen> screenList) {
        this.i10n = i10n;
        this.slideEffect = slideEffect;
        this.screenList = screenList;
    }
    
    public Screens() {
        //empty
    }
    
    public String getI10n() {
        return i10n;
    }

    public void setI10n(String i10n) {
        this.i10n = i10n;
    }
    
    public String getSlideEffect() {
        return slideEffect;
    }
    
    public void setSlideEffect(String slideEffect) {
        this.slideEffect = slideEffect;
    }

    public List<Screen> getScreenList() {
        return screenList;
    }

    public void setScreenList(List<Screen> screenList) {
        this.screenList = screenList;
    }
    

}
