import ceylon.language.meta.model{ Function }

@noanno
shared void bug1926() {
    Function<Anything,Nothing> q = `bug1926`;
    value s = q.parameterTypes[0];
}