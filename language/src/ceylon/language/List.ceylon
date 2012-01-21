shared interface List<out Element> 
        satisfies Collection<Element> & 
                  Correspondence<Integer, Element> &
                  Ranged<Integer, List<Element>>
        /*given Element satisfies Equality?*/ {}