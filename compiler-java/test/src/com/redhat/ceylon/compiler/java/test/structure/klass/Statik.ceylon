class StaticMembers<T> {
    shared static String attribute => "";
    shared static T method<U>(T t, U u) => t;
    shared static class MemberClass<U>(T t, U u) {
        shared T attribute => nothing;
        shared T method(T t) => t;
    }
    //shared static interface MemberInterface {
    //    shared T attribute => nothing;
    //    shared T method(T t) => t;
    //}
    shared new (){}
}