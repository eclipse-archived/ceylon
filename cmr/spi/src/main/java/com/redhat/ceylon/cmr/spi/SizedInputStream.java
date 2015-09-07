package com.redhat.ceylon.cmr.spi;

import java.io.InputStream;

/**
 * An InputStream holder which may know its size
 */
public class SizedInputStream {

    public final InputStream inputStream;
    /**
     * Can be -1 if we don't know its size
     */
    public final long size;
    
    public SizedInputStream(InputStream inputStream, long size){
        this.inputStream = inputStream;
        this.size = size;
    }
}
