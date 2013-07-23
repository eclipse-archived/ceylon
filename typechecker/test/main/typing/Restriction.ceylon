interface Interface<in T> {}
@error class Bong() satisfies Interface<Interface<Bong>> {}
@error class Bang<T>() satisfies Interface<Interface<Bang<Bang<T>>>> {}
//Interface<Bong> bong = Bong();
//Interface<Bang<String>> bang = Bang<String>();

interface Inty<T> => Interface<T>;
@error class Bongy() satisfies Inty<Inty<Bongy>> {}
@error class Bangy<T>() satisfies Inty<Inty<Bangy<Bangy<T>>>> {}
