package com.redhat.ceylon.ant;

import java.io.File;
import java.util.List;

/**
 * Contract for things using {@link LazyHelper}. 
 * @author tom
 */
public interface Lazy {

    void log(String string, int msgDebug);

    boolean getNoMtimeCheck();

    List<File> getSrc();

    void log(String string);

    String getOut();

}
