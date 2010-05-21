public class Enumeration<out X>(X... values)
        satisfies List<X>, Case<Object> {}