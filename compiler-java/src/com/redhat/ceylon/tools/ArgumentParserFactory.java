package com.redhat.ceylon.tools;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class ArgumentParserFactory {
    
    public ArgumentParserFactory() {
        super();
    }
    
    public <A> ArgumentParser<A> getParser(Class<A> cls) {
        if (cls.equals(String.class)) {
            return (ArgumentParser)new ArgumentParser<String>() {
                @Override
                public String parse(String argument) {
                    return argument;
                }
            };
        } else if (cls.equals(Boolean.class)
                || cls.equals(Boolean.TYPE)) {
            return (ArgumentParser)new BooleanArgumentParser();
        } else if (cls.equals(Integer.class)
                || cls.equals(Integer.TYPE)) {
            return (ArgumentParser)new IntegerArgumentParser();
        } else if (cls.equals(File.class)) {
            return new ConstructorArgumentParser<A>(cls);
        } else if (cls.equals(URL.class)) {
            return new ConstructorArgumentParser<A>(cls);
        } else if (cls.equals(URI.class)) {
            return new ConstructorArgumentParser<A>(cls);
        } else if (Enum.class.isAssignableFrom(cls)) {
            return new EnumArgumentParser(cls, true);
        }
        return null;
    }
    
}
