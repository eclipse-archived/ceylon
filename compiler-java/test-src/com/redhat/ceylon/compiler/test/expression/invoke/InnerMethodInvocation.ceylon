@nomodel
shared void innerMethodInvocation() {
    Natural fib(Natural n) {
        if (n==0) {
            return 1;
        }
        else {
            return n*fib(n-1);
        }
    }
    for (n in 0..50) {
        print("n! = " + fib(n).string);
    }
}
