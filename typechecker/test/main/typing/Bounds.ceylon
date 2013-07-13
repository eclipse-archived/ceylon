interface Super<T> {}
class Impl1<X,Y>(X x, Y y) satisfies Super<X&Y> {}
class Impl2<X,Y>() given X satisfies Super<X> {}
class Impl3<X,Y>() given X satisfies Super<Y> {}
class Impl4<X,Y>() given X satisfies Super<X&Y> {}
class Impl5<X,Y>() given X satisfies Super<X|Y> {}
class Impl6<X,Y>() @error given X satisfies Super<Super<X&Y>> {}
