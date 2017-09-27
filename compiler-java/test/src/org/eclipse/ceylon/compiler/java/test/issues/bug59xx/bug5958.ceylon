@noanno
void bug5958() {
    variable C? foo = null;
    class C {
        shared new instance {}
        shared C theInstance() => instance;
        void f(){
            print(foo);
        }
    }
    print(C.instance);
    print(C.instance.theInstance());
}
