doc "Check that overloads and extensions work together"
public void multitest(Process process) {
    Multi().test(process);
    Multi(4).test(process);
}
