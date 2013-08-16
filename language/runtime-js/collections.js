function Comprehension(makeNextFunc, $$targs$$, compr) {
    $init$Comprehension();
    if (compr===undefined) {compr = new Comprehension.$$;}
    Basic(compr);
    compr.makeNextFunc = makeNextFunc;
    compr.$$targs$$=$$targs$$;
    return compr;
}
Comprehension.$$metamodel$$={$nm:'Comprehension',$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','Iterable']};
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
defineAttr(Comprehension$proto, 'sequence', function() {
    var sb = SequenceBuilder(this.$$targs$$);
    sb.appendAll(this);
    return sb.sequence;
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Iterable','$at','sequence']});
exports.Comprehension=Comprehension;

function ComprehensionIterator(nextFunc, $$targs$$, it) {
    $init$ComprehensionIterator();
    if (it===undefined) {it = new ComprehensionIterator.$$;}
    it.$$targs$$=$$targs$$;
    Basic(it);
    it.next = nextFunc;
    return it;
}
ComprehensionIterator.$$metamodel$$={$nm:'ComprehensionIterator',$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','Iterator']};
function $init$ComprehensionIterator() {
    if (ComprehensionIterator.$$===undefined) {
        initTypeProto(ComprehensionIterator, 'ceylon.language::ComprehensionIterator',
                $init$Basic(), $init$Iterator());
    }
    return ComprehensionIterator;
}
$init$ComprehensionIterator();
