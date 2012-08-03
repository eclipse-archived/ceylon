package com.redhat.ceylon.common;

public interface ConfigReaderListener {

    void onSection(String section, String text);

    void onOption(String name, String value, String text);

    void onComment(String text);

    void onWhitespace(String text);

}
