@noanno
shared interface Bug1923 {
    shared default void f() { }
}

@noanno
shared object bug1923 satisfies Bug1923 {
    void priv() {
         
    }
    T? optional<T>(T t) => t;
    T[] plural<T>(T t) => [t];
    shared void bug1923Usesite() {
        value x = this;
        value fRef = x.f;
        value y = optional(this);
        value fRefY = y?.f;
        value zs = plural(this);
        value fRefZ = zs*.f;
        value fRef2 = this.f;
        //value privRef = x.priv;
        value privRef2 = this.priv;
    }
}

