shared extension class StringBuilder(Character[] this) {

    Character[] with(Character[] appendedStrings...) {
        variable OpenList<Character> list = ArrayList<Character>(this);
        list.append(string);
        for (Character[] s in appendedStrings) {
            list.append(s);
        }
    }

    Character[] forEach<X>(iterated Iterable<X> objects,
                           Character[] with(coordinated X x)) {
        variable OpenList<Character> list = ArrayList<Character>(this);
        for (X x in objects) {
            list.append(with(x));
        }
        return list;
    }

    Character[] forEach<X>(iterated Iterable<X> objects,
                           Character[][] with(coordinated X x)) {
        variable OpenList<Character> list = ArrayList<Character>(this);
        for (X x in objects) {
            for (Character[] s in with(x)) {
                list.append(s);
            }
        }
        return list;
    }

}