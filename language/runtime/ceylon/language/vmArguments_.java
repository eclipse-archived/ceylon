package ceylon.language;

import com.redhat.ceylon.compiler.java.Util;

@com.redhat.ceylon.compiler.java.metadata.Ceylon(
        major = 8,
        minor = 1)
@com.redhat.ceylon.compiler.java.metadata.Attribute
@com.redhat.ceylon.compiler.java.metadata.Name("vmArguments")
final class vmArguments_ {
    
    private vmArguments_() {
    }
    
    @NativeAnnotation$annotation$(backends = "jvm")
    @com.redhat.ceylon.common.NonNull
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Array<ceylon.language::String>")
    @com.redhat.ceylon.compiler.java.metadata.Transient
    static Array<String> get_() {
        java.lang.String[] args = Util.checkNull(Util.getArgs());
        String[] strings = new String[args.length];
        for (int i=0; i<args.length; i++) {
            java.lang.String arg = args[i];
            strings[i] = String.instance(arg==null ? "" : arg);
        }
        return Array.instance(strings);
    }
}
