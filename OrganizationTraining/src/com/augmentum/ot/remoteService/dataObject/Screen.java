package com.augmentum.ot.remoteService.dataObject;

public class Screen {

    private String header;
    private Content content;
    private String pattern;
    
    public Screen(String header, Content content, String pattern) {
        this.header = header;
        this.content = content;
        this.pattern = pattern;
    }
    
    public Screen() {
        //empty
    }
    
    public String getHeader() {
        return header;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }
    
    public Content getContent() {
        return content;
    }
    
    public void setContent(Content content) {
        this.content = content;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    

}
