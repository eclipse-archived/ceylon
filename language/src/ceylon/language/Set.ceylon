shared interface Set<out Element> 
        satisfies Collection<Element> 
        given Element satisfies Equality {}