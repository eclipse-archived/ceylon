function for$(makeNextFunc, $$targs$$, compr) {
    $init$for$();
    if (compr===undefined) {compr = new for$.$$;}
    Basic(compr);
    Iterable($$targs$$,compr);
    compr.makeNextFunc = makeNextFunc;
    return compr;
}
for$.$crtmm$={nm:'Comprehension',pa:1,mod:$CCMM$,d:['$','Iterable']};
function $init$for$() {
    if (for$.$$===undefined) {
        initTypeProto(for$, 'ceylon.language::Comprehension', $init$Basic(), $init$Iterable());
    }
    return for$;
}
$init$for$();
var for$$proto = for$.$$.prototype;
for$$proto.iterator = function() {
    return for$iter(this.makeNextFunc(), this.$$targs$$);
}
for$$proto.sequence = function() {
    return Iterable.$$.prototype.sequence.call(this);
}
for$$proto.sequence.$crtmm$={pa:3,mod:$CCMM$,d:['$','Iterable','$m','sequence']}
ex$.for$=for$;

function for$iter(nextFunc, $$targs$$, it) {
    $init$for$iter();
    if (it===undefined) {it = new for$iter.$$;}
    Basic(it);
    Iterator($$targs$$,it);
    it.next = nextFunc;
    return it;
}
for$iter.$crtmm$={nm:'ComprehensionIterator',pa:1,mod:$CCMM$,d:['$','Iterator']};
function $init$for$iter() {
    if (for$iter.$$===undefined) {
        initTypeProto(for$iter, 'ceylon.language::ComprehensionIterator',
                $init$Basic(), $init$Iterator());
    }
    return for$iter;
}
$init$for$iter();
