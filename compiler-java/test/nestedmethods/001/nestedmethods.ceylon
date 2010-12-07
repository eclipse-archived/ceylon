public void nestedmethods(Process p) {
    Integer poo = 99;

    void inner(Process p) {
        void innersInner() {
            p.writeLine(poo);
        }
        innersInner();
    }

    class Inner(Integer bar) {
        public Integer value = poo + bar;
    }

    Inner instance = Inner(22);

    inner(p);
    p.writeLine(instance.value);
}
