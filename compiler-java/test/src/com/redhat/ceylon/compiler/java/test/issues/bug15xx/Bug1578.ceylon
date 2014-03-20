@noanno
interface Buffer<X> {
}
@noanno
interface OutStream<T> {
    shared void writeFrom<BT>() 
        given BT satisfies Buffer<T> {
    } 
}
@noanno
class OpenFileImpl() {
    class UnbufferedOutstreamImpl() satisfies OutStream<Integer> {
    } 
}