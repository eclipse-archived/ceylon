    interface Set<Element,Other> of Other
            //satisfies Iterable<Element> 
            given Other satisfies Set<Element,Other> {
        shared formal void add(Element elem);
        shared formal Boolean contains1(Element elem);
        shared formal Boolean equals1(Other that);
        shared formal void addAll(Other set);
    }


    class TreeSet<Element>()
            satisfies Set<Element,TreeSet<Element>> 
            given Element satisfies Object {
        //shared actual Iterator<Element> iterator() => nothing;
        {Element*} elements => nothing;
        actual shared void add(Element elem) {}
        actual shared Boolean contains1(Element elem) { 
            for (e in elements) {
                if (elem==e) {
                    return true;
                }
            }
            else {
                return false;
            } 
        }
        actual shared Boolean equals1(TreeSet<Element> that) {
            return this==that;
        }
        shared actual void addAll(TreeSet<Element> set) {
            for (e in set.elements) {
                add(e);
            }
        }
    }
