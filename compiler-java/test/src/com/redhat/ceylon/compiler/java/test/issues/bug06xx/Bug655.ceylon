@nomodel
class Bug655()  {
    Bug655 select(String() predicates) {
        return this;
    }
    Bug655 where() {
        return this;
    }
    String nome() {
        return "";
    }
    void get() {
        select(nome).where();
    } 
}