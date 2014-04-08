package ceylon.language;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.java.ArrayBuilderTest;
import com.redhat.ceylon.compiler.java.TypeDescriptorTest;

@RunWith(Suite.class) 
@SuiteClasses({
    ArraySequenceTest.class,
    FloatTest.class,
    IntegerTest.class,
    SequenceBuilderTest.class,
    ArrayBuilderTest.class,
    TypeDescriptorTest.class,
})
public class LanguageUnitTestSuite {

}
