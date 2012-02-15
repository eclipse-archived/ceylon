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
        for (i = 1; i < seq.value.length; i++) {
            v = smallest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence, returns element 
function max(seq) {
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (i = 1; i < seq.value.length; i++) {
            v = largest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence of ArraySequences, returns flat ArraySequence
function join(seqs) {
    var builder = [];
    for (i = 0; i < seqs.value.length; i++) {
        builder = builder.concat(seqs.value[i].value);
    }
    return ArraySequence(builder);
}
//receives ArraySequences, returns ArraySequence
function zip(keys, items) {
    var entries = []
    var numEntries = Math.min(keys.value.length, items.value.length);
    for (i = 0; i < numEntries; i++) {
        entries[i] = Entry(keys.value[i], items.value[i]);
    }
    return ArraySequence(entries);
}
//receives and returns ArraySequence
function coalesce(seq) {
    var newseq = [];
    for (i = 0; i < seq.value.length; i++) {
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
    if (seq.getEmpty() === getTrue()) {
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
    for (i = 0; i < seq.value.length; i++) {
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
    return value === getNull() || value === undefined ? getFalse() : getTrue();
}
function nonempty(value) {
    return value === null || value === undefined ? getFalse() : Boolean$(value.getEmpty() === getFalse());
}

function isOfType(obj, typeName) {
    if (obj === null) return Boolean$(typeName==="ceylon.language.Nothing" || typeName==="ceylon.language.Void");
    var cons = obj.$$;
    if (cons === undefined) cons = obj.constructor;
    return Boolean$(typeName in cons.T$all);
}
function isOfTypes(obj, types) {
    if (obj===null) { //TODO check if this is right
        return types.l.indexOf('ceylon.language.Nothing')>=0 || types.l.indexOf('ceylon.language.Void')>=0;
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
            unions |= partial;
        } else {
            inters &= partial;
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

exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.isOfTypes=isOfTypes;
exports.className=className;
