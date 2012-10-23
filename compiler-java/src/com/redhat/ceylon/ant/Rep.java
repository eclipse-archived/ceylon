package com.redhat.ceylon.ant;

public class Rep {
    public String url;

    public void setUrl(String url){
        this.url = url;
    }
    
    @Override
    public String toString() {
        return url;
    }
}
