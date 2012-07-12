@nomodel
void anonymousArgument2<Y>(Y y) {
    void callFunction<X>(X f(Integer i)) {
    }
    callFunction<String>((Integer i) (i*3).string);
    callFunction<Y>((Integer i) y);
}
