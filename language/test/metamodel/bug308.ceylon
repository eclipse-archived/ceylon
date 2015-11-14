import ceylon.language.meta { type }

@test
shared void bug308() {
    Integer|String test = "Diego Coronel";
    String s = type(type(test)).string;
}