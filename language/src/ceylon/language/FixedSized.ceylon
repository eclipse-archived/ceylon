doc "Represents a fixed-size collection which may or may not 
     be empty."
shared interface FixedSized<out Element> 
        of None<Element> | Some<Element>
        satisfies Collection<Element> {}