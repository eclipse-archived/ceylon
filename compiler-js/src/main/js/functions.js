var exports,console,$true,$false;//IGNORE
function Comparison(x){}//IGNORE
function ArraySequence(x){}//IGNORE
function Entry(a,b){}//IGNORE
function Singleton(x){}//IGNORE
function SequenceBuilder(){}//IGNORE
function Integer(x){}//IGNORE
function Boolean$(f){}//IGNORE
function String$(f,x){}//IGNORE

function print(line) { console.log(line.getString().value) }
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
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (var i = 1; i < seq.value.length; i++) {
            v = smallest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence, returns element 
function max(seq) {
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (var i = 1; i < seq.value.length; i++) {
            v = largest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence of ArraySequences, returns flat ArraySequence
function join(seqs) {
    var builder = [];
    for (var i = 0; i < seqs.value.length; i++) {
        builder = builder.concat(seqs.value[i].value);
    }
    return ArraySequence(builder);
}
//receives ArraySequences, returns ArraySequence
function zip(keys, items) {
    var entries = []
    var numEntries = Math.min(keys.value.length, items.value.length);
    for (var i = 0; i < numEntries; i++) {
        entries[i] = Entry(keys.value[i], items.value[i]);
    }
    return ArraySequence(entries);
}
//receives and returns ArraySequence
function coalesce(seq) {
    var newseq = [];
    for (var i = 0; i < seq.value.length; i++) {
        if (seq.value[i]) {
            newseq = newseq.concat(seq.value[i]);
        }
    }
    return ArraySequence(newseq);
}

//receives ArraySequence and CeylonObject, returns new ArraySequence
function append(seq, elem) {
    return ArraySequence(seq.value.concat(elem));
}
function prepend(seq, elem) {
    if (seq.getEmpty() === $true) {
        return Singleton(elem);
    } else {
        var sb = SequenceBuilder();
        sb.append(elem);
        sb.appendAll(seq);
        return sb.getSequence();
    }
}

//Receives ArraySequence, returns ArraySequence (with Entries)
function entries(seq) {
    var e = [];
    for (var i = 0; i < seq.value.length; i++) {
        e.push(Entry(Integer(i), seq.value[i]));
    }
    return ArraySequence(e);
}

exports.min=min;
exports.max=max;
exports.join=join;
exports.zip=zip;
exports.coalesce=coalesce;
exports.append=append;
exports.prepend=prepend;
exports.entries=entries;

//These are operators for handling nulls
function exists(value) {
    return value === null || value === undefined ? $false : $true;
}
function nonempty(value) {
    return value === null || value === undefined ? $false : Boolean$(value.getEmpty() === $false);
}

function isOfType(obj, typeName) {
    if (obj === null) return Boolean$(typeName==="ceylon.language.Nothing" || typeName==="ceylon.language.Void");
    if (typeof obj === 'function') {
        return Boolean$(typeName === 'ceylon.language.Callable');
    }
    var cons = obj.$$;
    if (cons === undefined) cons = obj.constructor;
    return Boolean$(typeName in cons.T$all);
}
function isOfTypes(obj, types) {
    if (obj===null) { //TODO check if this is right
        return types.l.indexOf('ceylon.language.Nothing')>=0 || types.l.indexOf('ceylon.language.Void')>=0;
    }
    if (typeof obj === 'function') {
        return Boolean$(typeName === 'ceylon.language.Callable');
    }
    var unions = false;
    var inters = true;
    var _ints=false;
    var cons = obj.$$;
    if (cons === undefined) cons = obj.constructor;
    for (var i = 0; i < types.l.length; i++) {
        var t = types.l[i];
        var partial = false;
        if (typeof t === 'string') {
            partial = t in cons.T$all;
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
    var cons = obj.$$;
    if (cons === undefined) cons = obj.constructor;
    return String$(cons.T$name);
}

function identityHash(obj) {
    return obj.identifiableObjectID;
}

exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.isOfTypes=isOfTypes;
exports.className=className;
exports.identityHash=identityHash;
