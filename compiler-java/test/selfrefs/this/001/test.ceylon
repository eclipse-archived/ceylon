import com.redhat.ceylon.compiler.test.dump;

doc "Test \"this\""
void test(Process process) {
    TestClass(17, "xyz?").test1();
    dump(TestClass(23, "hello!").test2());
    TestClass(99, "!").test3();
}
 
