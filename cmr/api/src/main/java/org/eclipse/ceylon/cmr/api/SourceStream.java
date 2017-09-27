package org.eclipse.ceylon.cmr.api;

import java.io.IOException;
import java.io.InputStream;

public abstract class SourceStream {
    public abstract String getSourceRelativePath();
    public abstract InputStream getInputStream() throws IOException;
    
    @Override
    public String toString() {
        return getSourceRelativePath();
    }
    
    @Override
    public int hashCode() {
        return getSourceRelativePath().hashCode();
    }    
}