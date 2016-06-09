package com.redhat.ceylon.compiler.java.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ceylon.language.Resource;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ceylon(major = 8)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes({
    "ceylon.language::Resource"
})
public class FileResource implements Resource {

    private final File file;

    public FileResource(File file) {
        this.file = file;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public java.lang.String getUri() {
        return file.toURI().toString();
    }

    @Override
    public java.lang.String textContent() {
        return textContent(textContent$encoding());
    }

    @Override
    public java.lang.String textContent(java.lang.String enc) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(fis, enc)) {
            char[] buf = new char[16384];
            while (reader.ready()) {
                int read = reader.read(buf);
                if (read > 0) {
                    sb.append(buf, 0, read);
                }
            }
            return sb.toString();
        } catch (IOException ex) {
            throw new ceylon.language.Exception(new ceylon.language.String(
                    "Reading file resource " + getUri()), ex);
        }
    }

}
