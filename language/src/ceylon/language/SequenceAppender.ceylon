"Builder utility for constructing nonempty 
 [[sequences|Sequential]] by incrementally appending 
 elements. A newly-instantiated `SequenceAppender` produces
 a nonempty sequence containing the given initial 
 [[elements]]."
see (`class SequenceBuilder`)
shared native class SequenceAppender<Element>({Element+} elements) 
        extends SequenceBuilder<Element>() {
    
    "The resulting nonempty sequence. If no elements have 
     been appended, a nonempty sequence containing the given 
     initial [[elements]]."
    shared actual native [Element+] sequence;
    
    shared actual SequenceAppender<Element> append(Element element) {
        super.append(element);
        return this;
    }
    
    shared actual SequenceAppender<Element> appendAll({Element*} elements) {
        super.appendAll(elements);
        return this;
    }
    
}