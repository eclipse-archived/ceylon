interface Bug1064_A<out T> {}
interface Bug1064_B satisfies Bug1064_A<String?> {}
class Bug1064_C() satisfies Bug1064_B & Bug1064_A<String> {}