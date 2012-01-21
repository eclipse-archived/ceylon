shared interface Set<out Element> 
        satisfies Collection<Element> &
                  Slots<Set<Equality>> &
                  Cloneable<Set<Element>>
        given Element satisfies Equality {}