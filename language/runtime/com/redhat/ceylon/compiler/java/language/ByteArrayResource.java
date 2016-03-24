package com.redhat.ceylon.compiler.java.language;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

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
public class ByteArrayResource implements Resource {
    
    @Ignore
    protected final ceylon.language.Resource$impl $ceylon$language$Resource$this;
    private final byte[] contents;
    private URI uri;

    public ByteArrayResource(byte[] contents, URI uri) {
        this.contents = contents;
        this.uri = uri;
        $ceylon$language$Resource$this = new ceylon.language.Resource$impl(this);
    }

    @Ignore @Override
    public ceylon.language.Resource$impl $ceylon$language$Resource$impl() {
        return $ceylon$language$Resource$this;
    }

    @Override
    public java.lang.String getName() {
        String name = $ceylon$language$Resource$this.getName();
        int p = name.lastIndexOf('!');
        if (p >= 0) {
            name = name.substring(p + 1);
        }
        return name;
    }

    @Override
    public long getSize() {
        return contents.length;
    }

    @Override
    public java.lang.String getUri() {
        return uri.toString();
    }

    @Override
    public java.lang.String textContent() {
        return textContent(textContent$encoding());
    }

    @Override
    public java.lang.String textContent(java.lang.String enc) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        try (ByteArrayInputStream fis = new ByteArrayInputStream(contents);
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

    @Override
    public java.lang.String textContent$encoding() {
        return $ceylon$language$Resource$this.textContent$encoding();
    }
    
    @Override
    public String toString() {
        return $ceylon$language$Resource$this.toString();
    }
}
