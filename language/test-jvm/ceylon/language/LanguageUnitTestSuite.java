package ceylon.language;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.java.ArrayBuilderTest;
import com.redhat.ceylon.compiler.java.TypeDescriptorTest;

@RunWith(Suite.class) 
@SuiteClasses({
    FloatTest.class,
    IntegerTest.class,
    ArrayBuilderTest.class,
    TypeDescriptorTest.class,
    PrimitiveArrayIterableTest.class
})
public class LanguageUnitTestSuite {

}
