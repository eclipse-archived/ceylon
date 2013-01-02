package com.redhat.ceylon.ant;

import org.apache.tools.ant.types.DataType;

public class Repo extends DataType {
    public String url;

    public void setUrl(String url){
        this.url = url;
    }
    
    @Override
    public String toString() {
        return url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Repo other = (Repo) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
    
    
}
