package jvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class Holder {
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface Annot{}
    
    @Annot
    public class One{}
    
    @Annot
    public static class Two{}
    }
