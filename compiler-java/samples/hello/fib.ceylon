Natural fib(Natural n) {
    if (n==0) {
        return 1;
    }
    else {
        return n*fib(n-1);
    }
}
