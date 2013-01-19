package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Member;
import com.redhat.ceylon.compiler.java.metadata.Members;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 4)
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.IN,
            satisfies="ceylon.language::Object"),
    @TypeParameter(value = "Item", variance = Variance.OUT)
})
@Members(@Member(name = "Items", packageName = "ceylon.language", 
    javaClass = "ceylon.language.Correspondence$impl.Items"))
public interface Correspondence<Key,Item> {

    @Annotations(@Annotation("formal"))
    @TypeInfo("Item|ceylon.language::Null")
    public Item item(@Name("key") Key key);

    @Annotations(@Annotation("default"))
    public boolean defines(@Name("key") Key key);

    @Annotations(@Annotation("default"))
    public Category getKeys();

    @Annotations(@Annotation("default"))
    public boolean definesEvery(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<Key,ceylon.language::Null>")
    Iterable<? extends Key, ? extends java.lang.Object> keys);
//    @Ignore
//    public boolean definesEvery();
    //@Ignore
    //public Iterable<? extends Key, ? extends java.lang.Object> definesEvery$keys();

    @Annotations(@Annotation("default"))
    public boolean definesAny(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<Key,ceylon.language::Null>")
    Iterable<? extends Key, ? extends java.lang.Object> keys);
//    @Ignore
//    public boolean definesAny();
    //@Ignore
    //public Iterable<? extends Key, ? extends java.lang.Object> definesAny$keys();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Item|ceylon.language::Null>")
    public Sequential<? extends Item> items(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<Key,ceylon.language::Null>")
    Iterable<? extends Key, ? extends java.lang.Object> keys);
//    @Ignore
//    public Sequential<? extends Item> items();
    //@Ignore
    //public Iterable<? extends Key, ? extends java.lang.Object> items$keys();

    @Ignore
    public ceylon.language.Correspondence$impl<? super Key, ? extends Item> $ceylon$language$Correspondence$impl();

    @Ignore
    public Correspondence$impl<? super Key, ? extends Item>.Items Items$new(final ceylon.language.Sequence<? extends Key> keys);
}
