package com.augmentum.ot.remoteService.dataObject;

import java.util.List;

public class Row {

    private List<Column> columns;

    public Row(List<Column> columns) {
        this.columns = columns;
    }

    public Row() {
        //empty
    }
    
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
    

}
