shared class Whatever<T>(shared T t) {}

shared Whatever<String> m1(Whatever<Integer> w)
    => Whatever(w.string);

shared Whatever<out String> m2(Whatever<in Singleton<String>> t)
    => Whatever(t.string);
