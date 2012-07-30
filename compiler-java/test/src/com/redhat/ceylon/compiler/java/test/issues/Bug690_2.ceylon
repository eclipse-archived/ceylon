shared void bug690_2(Object obj) {
    if (is Bug690&Identifiable tst = obj) {
        print(tst.x);
    }
}