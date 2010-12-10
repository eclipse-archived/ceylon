import com.redhat.ceylon.compiler.test.dump;

doc "Test the exists operator"
void t2(Process process, String? arg) {
    if (exists arg) {
        process.writeLine("arg exists:");
        dump(arg);
    }
    else {
        process.writeLine("arg does not exist");
    }
}
