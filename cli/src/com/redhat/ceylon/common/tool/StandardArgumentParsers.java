package com.redhat.ceylon.common.tool;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

public class StandardArgumentParsers {

    public static final ArgumentParser<String> CHAR_SEQUENCE_PARSER = new ArgumentParser<String>() {
        @Override
        public String parse(String argument, Tool tool) {
            return argument != null ? argument : null;
        }
    };
    
    public static final ArgumentParser<Boolean> BOOLEAN_PARSER = new ArgumentParser<Boolean>() {
        @Override
        public Boolean parse(String argument, Tool tool) {
            return argument.matches("1|yes|true");
        }
    };
    
    public static final ArgumentParser<Integer> INTEGER_PARSER = new ArgumentParser<Integer>() {
        @Override
        public Integer parse(String argument, Tool tool) {
            return Integer.valueOf(argument);
        }
    };
    
    public static final ArgumentParser<URI> URI_PARSER = new ArgumentParser<URI>() {
        @Override
        public URI parse(String argument, Tool tool) {
            try {
                return new URI(argument);
            } catch (URISyntaxException e) {
                try {
                    return new URI(argument.replace('\\', '/'));
                } catch (URISyntaxException e2) {
                    File f = new File(argument);
                    return f.toURI();
                }
            }
        }
    };

    public static class PathArgumentParser implements ArgumentParser<List<File>> {
        @Override
        public List<File> parse(String argument, Tool tool) {
            String[] dirs = argument.split(Pattern.quote(File.pathSeparator));
            ArrayList<File> result = new ArrayList<File>(dirs.length); 
            for (String dir : dirs) {
                result.add(new File(dir));
            }
            return result;
        }
    };
    public static final PathArgumentParser PATH_PARSER = new PathArgumentParser();

    public static class ConstructorArgumentParser<T> implements ArgumentParser<T> {

        private Constructor<T> ctor;

        public ConstructorArgumentParser(Class<T> clazz) {
            try {
                this.ctor = clazz.getConstructor(String.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public T parse(String argument, Tool tool) {
            try {
                return ctor.newInstance(argument);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                } else if (cause instanceof Error) {
                    throw (Error)cause;
                } else {
                    throw new RuntimeException(e);
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    public abstract static class EnumParserBase<A, E extends Enum<E>> implements EnumerableParser<A> {

        protected final boolean denormalize;
        protected final Class<E> enumClass;
        
        public EnumParserBase(Class<E> enumClass, boolean denormalize) {
            this.enumClass = enumClass;
            this.denormalize = denormalize;
        }
        
        protected String denormalize(String argument) {
            argument = argument.replace('-', '_');
            for (String nm : possibilities()) {
                if (nm.equalsIgnoreCase(argument)) {
                    argument = nm;
                }
            }
            return argument;
        }

        protected E valueOf(String name) {
            return EnumUtil.valueOf(enumClass, name);
        }
        
        @Override
        public Iterable<String> possibilities() {
            return EnumUtil.possibilities(enumClass);
        }
    }
    
    public static class EnumArgumentParser<E extends Enum<E>> extends EnumParserBase<E, E> {

        public EnumArgumentParser(Class<E> enumClass, boolean denormalize) {
            super(enumClass, denormalize);
        }

        @Override
        public E parse(String argument, Tool tool) {
            if (denormalize) {
                argument = denormalize(argument);
            }
            return valueOf(argument);
        }
    }
    
    public static class EnumArgumentsParser<E extends Enum<E>> extends EnumParserBase<List<E>, E> {

        public EnumArgumentsParser(Class<E> enumClass, boolean denormalize) {
            super(enumClass, denormalize);
        }

        @Override
        public List<E> parse(String argument, Tool tool) {
            if (!argument.isEmpty()) {
                String[] elems = argument.split(",");
                ArrayList<E> result = new ArrayList<E>(elems.length);
                for (String elem : elems) {
                    elem = elem.trim();
                    if (denormalize) {
                        elem = denormalize(elem);
                    }
                    result.add(valueOf(elem));
                }
                return result;
            } else {
                return new ArrayList<>(EnumSet.allOf(enumClass));
            }
        }
    }
    
    public static ArgumentParser<?> forClass(Class<?> setterType, ToolLoader toolLoader, boolean isSimpleType) {
        if (CharSequence.class.isAssignableFrom(setterType)) {
            return CHAR_SEQUENCE_PARSER;
        } else if (Integer.class.isAssignableFrom(setterType)
                || Integer.TYPE.isAssignableFrom(setterType)) {
            return INTEGER_PARSER;
        } else if (Boolean.class.isAssignableFrom(setterType)
                || Boolean.TYPE.isAssignableFrom(setterType)) {
            return BOOLEAN_PARSER;
        } else if (File.class.isAssignableFrom(setterType)) {
            return new ConstructorArgumentParser<>(File.class);
        } else if (URI.class.isAssignableFrom(setterType)) {
            return URI_PARSER;
        } else if (URL.class.isAssignableFrom(setterType)) {
            return new ConstructorArgumentParser<>(URL.class);
        } else if (Enum.class.isAssignableFrom(setterType)) {
            if (isSimpleType) {
                return new EnumArgumentParser(setterType, true);
            } else {
                return new EnumArgumentsParser(setterType, true);
            }
        } else if (ToolModel.class.isAssignableFrom(setterType)) {
            return new ToolModelArgumentParser(toolLoader);
        } /*else if (Tool.class.isAssignableFrom(setterType)) {
            return new ToolArgumentParser(toolLoader);
        }*/
        return null;
    }
    
}
