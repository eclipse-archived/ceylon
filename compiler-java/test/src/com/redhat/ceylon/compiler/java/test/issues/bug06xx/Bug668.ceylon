@nomodel
shared interface Bug668_X<out Element, out Null>
        given Null satisfies Nothing {
    shared formal Null|Element first;
}

@nomodel
shared interface Bug668_Y<out Element> 
        satisfies Bug668_X<Element,Nothing> {
    shared actual default Element? first {
        return null;
    }

}

@nomodel
void bug668_method<Null>(Null n) given Null satisfies Nothing { 
    object obj satisfies Bug668_Y<Bottom> & Bug668_X<Bottom,Nothing> {}
}
