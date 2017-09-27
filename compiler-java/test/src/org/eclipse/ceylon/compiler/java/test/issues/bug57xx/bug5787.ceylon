@noanno
annotation Bug5787Fancy bug5787fancy() => Bug5787Fancy();

@noanno
final annotation class Bug5787Fancy() 
        satisfies OptionalAnnotation<Bug5787Fancy> {}

@noanno
class Bug5787Sender {
    Integer a; 
    shared  new (Integer a) {
        this.a = a;
        value f = () => a;
    }
}

@noanno
class Bug5787Sender2 {
    Integer a; 
    shared  new (bug5787fancy Integer a) {
        this.a = a;
    }
}
