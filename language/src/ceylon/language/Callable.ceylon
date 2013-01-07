doc "A reference to a method or function."
shared interface Callable<out Return, in Arguments> 
        given Arguments satisfies Anything[] {} 