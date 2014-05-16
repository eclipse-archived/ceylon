import java.util { List, ArrayList }


@test
shared void bug200() {
    Object l = ArrayList<Integer>();
    assert(!is ArrayList<Integer> l);
    //Object l2 = JavaList();
    //assert(is ArrayList<Integer> l2);
}