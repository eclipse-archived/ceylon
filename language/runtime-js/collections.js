function Comprehension(makeNextFunc, $$targs$$, compr) {
    $init$Comprehension();
    if (compr===undefined) {compr = new Comprehension.$$;}
    Basic(compr);
    compr.makeNextFunc = makeNextFunc;
    compr.$$targs$$=$$targs$$;
    return compr;
}
Comprehension.$crtmm$={$nm:'Comprehension',$an:function(){return[shared()];},mod:$CCMM$,d:['$','Iterable']};
function $init$Comprehension() {
    if (Comprehension.$$===undefined) {
        initTypeProto(Comprehension, 'ceylon.language::Comprehension', $init$Basic(), $init$Iterable());
    }
    return Comprehension;
}
$init$Comprehension();
var Comprehension$proto = Comprehension.$$.prototype;
Comprehension$proto.iterator = function() {
    return ComprehensionIterator(this.makeNextFunc(), this.$$targs$$);
}
atr$(Comprehension$proto, 'sequence', function() {
    var sb = SequenceBuilder({Element$SequenceBuilder:this.$$targs$$.Element$Iterable});
    sb.appendAll(this);
    return sb.sequence;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Iterable','$at','sequence']});
ex$.Comprehension=Comprehension;

function ComprehensionIterator(nextFunc, $$targs$$, it) {
    $init$ComprehensionIterator();
    if (it===undefined) {it = new ComprehensionIterator.$$;}
    it.$$targs$$=$$targs$$;
    Basic(it);
    it.next = nextFunc;
    return it;
}
ComprehensionIterator.$crtmm$={$nm:'ComprehensionIterator',$an:function(){return[shared()];},mod:$CCMM$,d:['$','Iterator']};
function $init$ComprehensionIterator() {
    if (ComprehensionIterator.$$===undefined) {
        initTypeProto(ComprehensionIterator, 'ceylon.language::ComprehensionIterator',
                $init$Basic(), $init$Iterator());
    }
    return ComprehensionIterator;
}
$init$ComprehensionIterator();
