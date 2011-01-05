doc "Check that overloads and extensions work together"
public void multitest(Process process) {
    Multi(4).test(process);
    Multi().test(process);
}
