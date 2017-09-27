package ceylon.language;

import org.eclipse.ceylon.compiler.java.ArrayBuilderTest;
import org.eclipse.ceylon.compiler.java.TypeDescriptorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

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
