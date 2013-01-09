interface SharedParameters {

    class WithSharedParam<T>(shared T t) {
        print(t);
    }

    class ExtendsWithSharedParam() 
            extends WithSharedParam<String>("") {}

    void check() {
        WithSharedParam<String> wsp = ExtendsWithSharedParam();
        String s1 = wsp.t;
        String s2 = ExtendsWithSharedParam().t;
    }

}