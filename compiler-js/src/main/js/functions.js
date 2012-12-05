var exports,console,$finished,process$,empty;//IGNORE
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

function isOfType(obj, typeName) {
    if (obj === null) {
        return typeName==="ceylon.language::Nothing" || typeName==="ceylon.language::Void";
    }
    return obj.getT$all && typeName in obj.getT$all();
}
function isOfTypes(obj, types) {
    if (obj===null) {
        return types.l.indexOf('ceylon.language::Nothing')>=0 || types.l.indexOf('ceylon.language::Void')>=0;
    }
    var unions = false;
    var inters = true;
    var _ints=false;
    var objTypes = obj.getT$all();
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
    if (obj === null) return String$('ceylon.language::Nothing');
    return String$(obj.getT$name());
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
