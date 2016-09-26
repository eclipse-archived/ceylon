import ceylon.language.meta{typeLiteral}
@test
shared void bug5771() {
    typeLiteral<Tuple<Boolean|Integer,Boolean,Range<Integer>>>();
    typeLiteral<Tuple<Boolean|Integer,Boolean,Integer[1]|String[2]>>();
}