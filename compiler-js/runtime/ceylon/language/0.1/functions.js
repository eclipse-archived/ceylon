(function(define) {
    define(function(require, exports, module) {

var clang = require('ceylon/language/0.1/ceylon.language');

//receives ArraySequence, returns element
function min(seq) {
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (i = 1; i < seq.value.length; i++) {
            v = clang.smallest(v, seq.value[i]);
        }
    }
    return v;
}
//receives ArraySequence, returns element 
function max(seq) {
    var v = seq.value[0];
    if (seq.value.length > 1) {
        for (i = 1; i < seq.value.length; i++) {
            v = clang.largest(v, seq.value[i]);
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
    return clang.ArraySequence(builder);
}
//receives ArraySequences, returns ArraySequence
function zip(keys, items) {
    var entries = []
    var numEntries = Math.min(keys.value.length, items.value.length);
    for (i = 0; i < numEntries; i++) {
        entries[i] = clang.Entry(keys.value[i], items.value[i]);
    }
    return clang.ArraySequence(entries);
}
//receives and returns ArraySequence
function coalesce(seq) {
    var newseq = [];
    for (i = 0; i < seq.value.length; i++) {
        if (seq.value[i]) {
            newseq = newseq.concat(seq.value[i]);
        }
    }
    return clang.ArraySequence(newseq);
}

//receives ArraySequence and CeylonObject, returns new ArraySequence
function append(seq, elem) {
    return clang.ArraySequence(seq.value.concat(elem));
}
function prepend(seq, elem) {
    if (seq.getEmpty() === clang.getTrue()) {
        return clang.Singleton(elem);
    } else {
        var sb = clang.SequenceBuilder();
        sb.append(elem);
        sb.appendAll(seq);
        return sb.getSequence();
    }
}

//Receives ArraySequence, returns ArraySequence (with Entries)
function entries(seq) {
    var e = [];
    for (i = 0; i < seq.value.length; i++) {
        e.push(clang.Entry(clang.Integer(i), seq.value[i]));
    }
    return clang.ArraySequence(e);
}

//These are operators for handling nulls
function $nullsafe() { return null; }
function exists(value) {
    return value === clang.getNull() || value === undefined ? clang.getFalse() : clang.getTrue();
}
function nonempty(value) {
    return value === null || value === undefined ? clang.getFalse() : clang.Boolean(value.getEmpty() === clang.getFalse());
}

function isOfType(obj, typeName) {
    return clang.Boolean((obj===null) ? (typeName==="ceylon.language.Nothing" || typeName==="ceylon.language.Void") : (typeName in obj.constructor.T$all));
}
function isOfTypes(obj, types) {
    if (obj===null) { //TODO check if this is right
        return types.l.indexOf('ceylon.language.Nothing')>=0 || types.l.indexOf('ceylon.language.Void')>=0;
    }
    var unions = false;
    var inters = true;
    var _ints=false;
    for (var i = 0; i < types.l.length; i++) {
        var t = types.l[i];
        var partial = false;
        if (typeof t === 'string') {
            partial = t in obj.constructor.T$all;
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
    return clang.String(obj!==null ? obj.constructor.T$name : 'ceylon.language.Nothing');
}

exports.min=min;
exports.max=max;
exports.join=join;
exports.zip=zip;
exports.coalesce=coalesce;
exports.append=append;
exports.prepend=prepend;
exports.entries=entries;
exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.isOfTypes=isOfTypes;
exports.nullsafe=$nullsafe;
exports.className=className;

    });
}(typeof define==='function' && define.amd ? 
    define : function (factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports, module);
    } else {
        throw "no module loader";
    }
}));
