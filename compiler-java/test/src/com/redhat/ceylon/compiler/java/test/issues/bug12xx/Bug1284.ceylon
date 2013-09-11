@noanno
void bug1284() {

    Consumer<Object> times1 = nothing;
    Consumer<String> objects1 = times1;
    interface Consumer<in E> {
        shared void add(E e) {}
    }

}