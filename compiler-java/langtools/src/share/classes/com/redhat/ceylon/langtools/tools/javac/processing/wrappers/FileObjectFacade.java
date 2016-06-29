package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import com.redhat.ceylon.javax.tools.FileObject;

public class FileObjectFacade implements javax.tools.FileObject {

    protected FileObject f;

    public FileObjectFacade(FileObject f) {
        this.f = f;
    }

    @Override
    public boolean delete() {
        return f.delete();
    }

    @Override
    public CharSequence getCharContent(boolean arg0) throws IOException {
        return f.getCharContent(arg0);
    }

    @Override
    public long getLastModified() {
        return f.getLastModified();
    }

    @Override
    public String getName() {
        return f.getName();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return f.openInputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return f.openOutputStream();
    }

    @Override
    public Reader openReader(boolean arg0) throws IOException {
        return f.openReader(arg0);
    }

    @Override
    public Writer openWriter() throws IOException {
        return f.openWriter();
    }

    @Override
    public URI toUri() {
        return f.toUri();
    }

}
