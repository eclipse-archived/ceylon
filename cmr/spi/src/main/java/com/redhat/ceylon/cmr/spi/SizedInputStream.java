package com.redhat.ceylon.cmr.spi;

import java.io.InputStream;

/**
 * An InputStream holder which may know its size
 */
public class SizedInputStream {

    private final InputStream inputStream;
    
    private final long size;
    
    public SizedInputStream(InputStream inputStream, long size){
        this.inputStream = inputStream;
        this.size = size;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Can be -1 if we don't know its size
     */
    public long getSize() {
        return size;
    }
}
