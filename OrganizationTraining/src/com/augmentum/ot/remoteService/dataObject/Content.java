package com.augmentum.ot.remoteService.dataObject;

import java.util.List;

public class Content {

    private List<Group> groups;

    public Content(List<Group> groups) {
        this.groups = groups;
    }
    
    public Content(){
        //empty
    }
    
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    


}
