function for$(makeNextFunc, $a$, compr) {
    $i$for$();
    if (compr===undefined) {compr = new for$.$$;}
    Basic(compr);
    Iterable($a$,compr);
    compr.makeNextFunc = makeNextFunc;
    return compr;
}
for$.$m$={nm:'Comprehension',pa:1,mod:$M$,d:['$','Iterable']};
function $i$for$() {
    if (for$.$$===undefined) {
        initTypeProto(for$, 'ceylon.language::Comprehension', $i$Basic(), $i$Iterable());
    }
    return for$;
}
$i$for$();
var for$$proto = for$.$$.prototype;
for$$proto.iterator = function() {
    return for$iter(this.makeNextFunc(), this.$a$);
}
for$$proto.sequence = function() {
    return Iterable.$$.prototype.sequence.call(this);
}
for$$proto.sequence.$m$={pa:3,mod:$M$,d:['$','Iterable','$m','sequence']}
x$.for$=for$;

function for$iter(nextFunc, $a$, it) {
    $i$for$iter();
    if (it===undefined) {it = new for$iter.$$;}
    Basic(it);
    Iterator($a$,it);
    it.next = nextFunc;
    return it;
}
for$iter.$m$={nm:'ComprehensionIterator',pa:1,mod:$M$,d:['$','Iterator']};
function $i$for$iter() {
    if (for$iter.$$===undefined) {
        initTypeProto(for$iter, 'ceylon.language::ComprehensionIterator',
                $i$Basic(), $i$Iterator());
    }
    return for$iter;
}
$i$for$iter();
