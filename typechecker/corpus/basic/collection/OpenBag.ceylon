public mutable interface OpenBag<X> 
        satisfies Bag<X>, OpenCollection<X>, OpenCorrespondence<Object, Natural> {
        
    override public OpenMap<X,Natural> map;
    
}