import phase1 { m1, m2, Whatever }

shared Whatever<String> m3(Whatever<Integer> w)
    => m1(w);

shared Whatever<out Object> m4(Whatever<in Object> t)
    => m2(t);
