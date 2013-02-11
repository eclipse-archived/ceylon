shared interface X<in T> {}
shared interface Y<out T> {}
@error shared X<Other> foo<out Other>(X<Other> o) => nothing;
shared Y<Other> bar<out Other>(X<Other> o) => nothing;
shared X<Other> baz<in Other>(X<Other> o) => nothing;
@error shared Y<Other> qux<in Other>(X<Other> o) => nothing;
