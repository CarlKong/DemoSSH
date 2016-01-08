package com.augmentum.ot.remoteService.dataObject;

import java.util.List;

public class Group {

    private List<Row> rows;

    public Group(List<Row> rows) {
        this.rows = rows;
    }
    
    public Group() {
        //empty
    }
    
    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
    

}
