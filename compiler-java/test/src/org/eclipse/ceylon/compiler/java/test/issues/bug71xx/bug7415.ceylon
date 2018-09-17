void bug7415(Bug7415<String> c) {
    String s = c of String;
}

interface Bug7415<T> of T given T satisfies Bug7415<T> {}
