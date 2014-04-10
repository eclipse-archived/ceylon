package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class
@SatisfiedTypes(value="ceylon.language::Binary<ceylon.language::Boolean>")
@CaseTypes({"ceylon.language::true", "ceylon.language::false"})
@ValueType
public abstract class Boolean implements Binary<Boolean>, ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Boolean.class);
    @Ignore
    private final Binary$impl<Boolean> $ceylon$language$Binary$this = new Binary$impl<Boolean>($TypeDescriptor$, this);
    
    @Ignore
    @Override
    public Binary$impl<Boolean> $ceylon$language$Binary$impl() {
        return $ceylon$language$Binary$this;
    }
    
    @Ignore
    public static Boolean instance(boolean value) {
        return value ? true_.get_() : false_.get_();
    }

    @Ignore
    abstract public boolean booleanValue();
    
    @Override
    public Boolean getNot() {
        return instance(getNot(booleanValue()));
    }
    
    @Ignore
    public static boolean getNot(boolean instance) {
        return !instance;
    }
    
    @Override
    public Boolean and(@Name("other") Boolean other) {
        return instance(and(booleanValue(), other.booleanValue()));
    }
    
    @Ignore
    public static boolean and(boolean instance, boolean other) {
        return instance && other;
    }

    @Override
    public Boolean clear(@Name("index") long index) {
        return instance(clear(booleanValue(), index));
    }
    @Ignore
    public static boolean clear(boolean instance, long index) {
        if (index == 0) {
            return false;
        }
        return instance;
    }

    @Override
    public Boolean flip(@Name("index") long index) {
        return instance(flip(booleanValue(), index));
    }
    
    @Ignore
    public static boolean flip(boolean instance, long index) {
        if (index == 0) {
            return !instance;
        }
        return instance;
    }

    @Override
    public boolean get(@Name("index") long index) {
        return get(booleanValue(), index);
    }
    
    @Ignore
    public static boolean get(boolean instance, long index) {
        if (index == 0) {
            return instance;
        }
        return false;
    }

    @Override
    public long getSize() {
        return 1;
    }
    
    @Ignore
    public static long getSize(boolean instance) {
        return 1;
    }

    @Override
    public Boolean leftLogicalShift(@Name("shift") long arg0) {
        return this;
    }
    
    @Ignore
    public static boolean leftLogicalShift(boolean instance, long shift) {
        return instance;
    }

    @Override
    public Boolean or(@Name("other") Boolean other) {
        return instance(or(booleanValue(), other.booleanValue()));
    }
    
    @Ignore
    public static boolean or(boolean instance, boolean other) {
        return instance || other;
    }

    @Override
    public Boolean rightArithmeticShift(@Name("shift") long arg0) {
        return this;
    }
    
    @Ignore
    public static boolean rightArithmeticShift(boolean instance, long shift) {
        return instance;
    }

    @Override
    public Boolean rightLogicalShift(@Name("shift") long arg0) {
        return this;
    }
    
    @Ignore
    public static boolean rightLogicalShift(boolean instance, long shift) {
        return instance;
    }

    @Override
    @Ignore
    public Boolean set(@Name("index") long index) {
        return instance(set(booleanValue(), index));
    }
    
    @Ignore
    public static boolean set(boolean instance, long index) {
        return set(instance, index, true);
    }

    @Override
    public Boolean set(@Name("index") long index, @Name("big") @Defaulted boolean bit) {
        return instance(set(booleanValue(), index, bit));
    }
    
    @Ignore
    public static boolean set(boolean instance, long index, boolean bit) {
        if (index == 0) {
            return bit;
        }
        return instance;
    }

    @Ignore
    @Override
    public boolean set$bit(long index) {
        return true;
    }

    @Override
    public Boolean xor(@Name("other") Boolean other) {
        return instance(xor(booleanValue(), other.booleanValue()));
    }
    
    @Ignore
    public static boolean xor(boolean instance, boolean other) {
        return instance ^ other;
    }

    
    @Ignore
    public static java.lang.String toString(boolean value) {
        return value ? "true" : "false";
    }
    
    @Ignore
    public static boolean equals(boolean value, java.lang.Object that) {
        if (that instanceof Boolean) {
            return value == ((Boolean) that).booleanValue();
        }
        else {
            return false;
        }
    }

    @Override
    @Ignore
    public final int hashCode() {
        return hashCode(booleanValue());
    }

    @Ignore
    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    @Override
    @Ignore
    public final TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
}
