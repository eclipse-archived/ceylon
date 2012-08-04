package com.redhat.ceylon.common;

import java.io.IOException;

public interface ConfigReaderListener {

    void onSection(String section, String text) throws IOException;

    void onOption(String name, String value, String text) throws IOException;

    void onComment(String text) throws IOException;

    void onWhitespace(String text) throws IOException;

}
