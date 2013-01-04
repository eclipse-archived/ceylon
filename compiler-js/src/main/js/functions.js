var exports,console,$finished,process$,empty;//IGNORE
function Comparison(x){}//IGNORE
function ArraySequence(x){}//IGNORE
function Entry(a,b){}//IGNORE
function Singleton(x){}//IGNORE
function SequenceBuilder(){}//IGNORE
function String$(f,x){}//IGNORE

var larger = Comparison("larger");
function getLarger() { return larger }
var smaller = Comparison("smaller");
function getSmaller() { return smaller }
var equal = Comparison("equal");
function getEqual() { return equal }

exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;

//These are operators for handling nulls
function exists(value) {
    return value !== null && value !== undefined;
}
function nonempty(value) {
    return value !== null && value !== undefined && !value.getEmpty();
}

function isOfType(obj, type) {
    if (type && type.t) {
        if (type.t == 'i' || type.t == 'u') {
            return isOfTypes(obj, type);
        }
        if (obj === null) {
            return type.t===Nothing || type.t===Void;
        }
        var typeName = type.t.$$.T$name;
        if (obj.getT$all && typeName in obj.getT$all()) {
            if (type.a) {
                for (var i=0; i<type.a.length; i++) {
                    if (!extendsType(obj.$$targs$$[i].t, type.a[i].t)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
    return false;
}
function isOfTypes(obj, types) {
    if (obj===null) {
        for (var i=0; i < types.l.length; i++) {
            if(types.l[i].t===Nothing || types.l[i].t===Void) return true;
        }
        return false;
    }
    var unions = false;
    var inters = true;
    var _ints=false;
    var objTypes = obj.getT$all();
    for (var i = 0; i < types.l.length; i++) {
        var t = types.l[i];
        var partial = isOfType(obj, t);
        if (types.t==='u') {
            unions = partial || unions;
        } else {
            inters = partial && inters;
            _ints=true;
        }
    }
    return _ints ? inters||unions : unions;
}
function extendsType(t1, t2) {
    //TODO deal with union/intersection types
    for (t in t1.$$.T$all) {
        if (t === t2.$$.T$name) {
            return true;
        }
    }
    return false;
}

function className(obj) {
    if (obj === null) return String$('ceylon.language::Nothing');
    return String$(obj.getT$name());
}

function identityHash(obj) {
    return obj.identifiableObjectID;
}

exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.className=className;
exports.identityHash=identityHash;
