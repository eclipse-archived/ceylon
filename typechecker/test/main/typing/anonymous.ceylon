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

class Promise<V>() {}
class Deferred<V>() {
    shared object promise extends Promise<V>() {}
}
shared void testAnonymousClassMemberOfGenericClass() {
    @type:"Promise<String>" 
    value promise1 = Deferred<String>().promise;
    Promise<String> promise2 = Deferred<String>().promise;
    Deferred<String>.\Ipromise promise3 = Deferred<String>().promise;
}


class SuperFoobarbaz() {
    @error print(foobarbaz.hash);
    @error print(Foobarbaz.foobarbaz.hash);
}

object foobarbaz extends SuperFoobarbaz() {
    @error print(foobarbaz.hash);
    @error print(this.hash);
    @error print(super.hash);
}

class Foobarbaz extends SuperFoobarbaz {
    shared new foobarbaz extends SuperFoobarbaz() {}
    @error print(foobarbaz.hash);
    @error print(Foobar.foobarbaz.hash);
}

