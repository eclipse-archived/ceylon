import com.redhat.ceylon.compiler.test.dump;

doc "Test \"this\""
void test(Process process) {
    Test(17, "xyz?").test1();
    dump(Test(23, "hello!").test2());
    Test(99, "!").test3();
}
 
