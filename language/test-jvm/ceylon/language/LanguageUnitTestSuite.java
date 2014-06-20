package ceylon.language;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.java.ArrayBuilderTest;
import com.redhat.ceylon.compiler.java.TypeDescriptorTest;
import com.redhat.ceylon.compiler.java.runtime.MainTest;

@RunWith(Suite.class) 
@SuiteClasses({
    FloatTest.class,
    IntegerTest.class,
    ArrayBuilderTest.class,
    TypeDescriptorTest.class,
    PrimitiveArrayIterableTest.class,
    MainTest.class
})
public class LanguageUnitTestSuite {

}
