package com.redhat.ceylon.common.config;

import java.io.IOException;

public interface ConfigReaderListener {

    void setup() throws IOException;

    void onSection(String section, String text) throws IOException;

    void onOption(String name, String value, String text) throws IOException;

    void onComment(String text) throws IOException;

    void onWhitespace(String text) throws IOException;

    void cleanup() throws IOException;

}
