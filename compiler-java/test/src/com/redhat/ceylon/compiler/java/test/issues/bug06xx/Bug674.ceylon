@nomodel
void bug674() {

    interface X<T> {}
    interface Y{}
    X<Y> y = bottom;

}