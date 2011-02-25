shared interface ConcreteClass<out X, P...>
        satisfies Class<X> & Callable<X,P...> {}