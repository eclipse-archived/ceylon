doc "Applies a function to each element of two `Iterable`s
     and returns an `Iterable` with the results."
by "Gavin" "Enrique Zamudio"
shared Iterable<Result> combine<Result,Element,OtherElement>(
        Result combination(Element element, OtherElement otherElement), 
        Iterable<Element> elements, 
        Iterable<OtherElement> otherElements) {
    //Eventually we'll just do this
    //return elements {
    //    let (oi=otherElements.iterator)
    //    for (e in elements)
    //        let (o=oi.next())
    //        if (is OtherElement o)
    //            combination(e, o)
    //};
    throw;
}