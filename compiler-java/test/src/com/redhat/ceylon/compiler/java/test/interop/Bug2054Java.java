package com.redhat.ceylon.compiler.java.test.interop;

interface Bug2054Java<T, U extends String> {}

class Bug2054Raw implements Bug2054Java {}

class Bug2054Free<U,V extends String> implements Bug2054Java<U,V> {}

class Bug2054Complete implements Bug2054Java<String,String> {}

class Bug2054Partial<U> implements Bug2054Java<U,String> {}

class Bug2054PartialUpper<U extends String> implements Bug2054Java<String,U> {}

class Bug2054IndirectUpper<U extends String, V extends U> implements Bug2054Java<U,V> {}