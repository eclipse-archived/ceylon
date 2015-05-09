void anonymousTypes(Integer i) {
    \Iprocess p = process;
    {\Iprocess+} it = {p, process};
    [\Iprocess,\Iprocess] list = [p, process];
    \Iprocess->\Iprocess entry = p->process;
    \Iprocess? proc = list[i];
    
    @type:"Tuple<Basic,Basic,Tuple<Basic,Basic,Empty>>" 
    value tup = [p,process];
    @type:"Basic" 
    value xxx = process;
    @type:"Entry<Basic,Basic>"
    value e = p->process;
    @type:"Iterable<Basic,Nothing>"
    value iter = {p, process};
    @type:"Null|Basic"
    value pr = list[i];
    
    @type:"Callable<Callable<Boolean,Tuple<String,String,Empty>>,Tuple<process,process,Empty>>"
    value pna = \Iprocess.namedArgumentPresent;
    
}