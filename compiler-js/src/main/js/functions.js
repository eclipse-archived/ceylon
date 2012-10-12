var exports,console,$finished,process$,$empty;//IGNORE
function Comparison(x){}//IGNORE
function ArraySequence(x){}//IGNORE
function Entry(a,b){}//IGNORE
function Singleton(x){}//IGNORE
function SequenceBuilder(){}//IGNORE
function String$(f,x){}//IGNORE

function print(line) { process$.writeLine(line===null?"«null»":line.getString()); }
exports.print=print;

var larger = Comparison("larger");
function getLarger() { return larger }
var smaller = Comparison("smaller");
function getSmaller() { return smaller }
var equal = Comparison("equal");
function getEqual() { return equal }
function largest(x, y) { return x.compare(y) === larger ? x : y }
function smallest(x, y) { return x.compare(y) === smaller ? x : y }

exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;
exports.largest=largest;
exports.smallest=smallest;

//receives ArraySequence, returns element
function min(seq) {
    var v = seq.getFirst();
    if (v === null) return null;
    var iter = seq.getRest().getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        v = smallest(v, e);
    }
    return v;
}
//receives ArraySequence, returns element 
function max(/*ContainerWithFirstElement*/seq) {
    var v = seq.getFirst();
    if (v === null) return null;
    var iter = seq.getRest().getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        v = largest(v, e);
    }
    return v;
}
function sum(seq) {
    var v = seq.getFirst();
    var iter = seq.getRest().getIterator();
    var e; while ((e = iter.next()) !== $finished) {
        v = v.plus(e);
    }
    return v;
}

//receives ArraySequence of ArraySequences, returns flat ArraySequence
function join(seqs) {
    if (seqs === undefined) return $empty;
    var builder = [];
    var it = seqs.getIterator();
    var seq;
    while ((seq = it.next()) !== $finished) {
        var it2 = seq.getIterator();
        var elem;
        while ((elem = it2.next()) != $finished) {builder.push(elem);}
    }
    return ArraySequence(builder);
}
//receives ArraySequences, returns ArraySequence
function zip(keys, items) {
    var entries = []
    var numEntries = Math.min(keys.length, items.length);
    for (var i = 0; i < numEntries; i++) {
        entries[i] = Entry(keys[i], items[i]);
    }
    return ArraySequence(entries);
}
//receives and returns ArraySequence
function coalesce(seq) {
    if (seq === undefined) {return $empty}
    return seq.getCoalesced();
}

//Receives Iterable, returns ArraySequence (with Entries)
function entries(seq) {
    if (seq === undefined) return $empty;
    return seq.getIndexed();
}

function any(/*Boolean...*/ values) {
    if (values === undefined) return false;
    var it = values.getIterator();
    var v;
    while ((v = it.next()) !== $finished) {
        if (v) {return true;}
    }
    return false;
}
function every(/*Boolean...*/ values) {
    if (values === undefined) return false;
    var it = values.getIterator();
    var v;
    while ((v = it.next()) !== $finished) {
        if (!v) {return false;}
    }
    return true;
}

function first(/*Element...*/ elements) {
    if (elements === undefined) return null;
    var e = elements.getIterator().next();
    return (e !== $finished) ? e : null;
}

exports.min=min;
exports.max=max;
exports.sum=sum;
exports.join=join;
exports.zip=zip;
exports.coalesce=coalesce;
exports.entries=entries;
exports.any=any;
exports.every=every;
exports.first=first;

//These are operators for handling nulls
function exists(value) {
    return value !== null && value !== undefined;
}
function nonempty(value) {
    return value !== null && value !== undefined && !value.getEmpty();
}

function isOfType(obj, typeName) {
    if (obj === null) {
        return typeName==="ceylon.language.Nothing" || typeName==="ceylon.language.Void";
    }
    return obj.getT$all$ && typeName in obj.getT$all$();
}
function isOfTypes(obj, types) {
    if (obj===null) {
        return types.l.indexOf('ceylon.language.Nothing')>=0 || types.l.indexOf('ceylon.language.Void')>=0;
    }
    var unions = false;
    var inters = true;
    var _ints=false;
    var objTypes = obj.getT$all$();
    for (var i = 0; i < types.l.length; i++) {
        var t = types.l[i];
        var partial = false;
        if (typeof t === 'string') {
            partial = t in objTypes;
        } else {
            partial = isOfTypes(obj, t);
        }
        if (types.t==='u') {
            unions = partial || unions;
        } else {
            inters = partial && inters;
            _ints=true;
        }
    }
    return _ints ? inters||unions : unions;
}

function className(obj) {
    if (obj === null) return String$('ceylon.language.Nothing');
    return String$(obj.getT$name$());
}

function identityHash(obj) {
    return obj.identifiableObjectID;
}

//This is just so that you can pass a comprehension and return it as iterable
function elements(iter) {
    return iter;
}
exports.elements=elements;
exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.isOfTypes=isOfTypes;
exports.className=className;
exports.identityHash=identityHash;
