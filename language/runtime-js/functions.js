var exports,console,process$,empty;//IGNORE
function Comparison(x){}//IGNORE
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
    return value !== null && value !== undefined && !value.empty;
}

function isOfType(obj, type) {
    if (type && type.t) {
        if (type.t == 'i' || type.t == 'u') {
            return isOfTypes(obj, type);
        }
        if (obj === null || obj === undefined) {
            return type.t===Null || type.t===Anything;
        }
        if (obj.getT$all === undefined) { return false; }
        if (type.t.$$.T$name in obj.getT$all()) {
            if (type.a && obj.$$targs$$) {
                for (var i in type.a) {
                    var cmptype = type.a[i];
                    var tmpobj = obj;
                    var iance = null;
                    if (type.t.$$.$$metamodel$$ && type.t.$$.$$metamodel$$.$tp && type.t.$$.$$metamodel$$.$tp[i]) iance=type.t.$$.$$metamodel$$.$tp[i]['var'];
                    if (iance === null) {
                        //Type parameter may be in the outer type
                        while (iance===null && tmpobj.$$outer !== undefined) {
                            tmpobj=tmpobj.$$outer;
                            if (tmpobj.constructor && tmpobj.constructor.$$metamodel$$ && tmpobj.constructor.$$metamodel$$.$tp && tmpobj.constructor.$$metamodel$$.$tp[i]) {
                                iance=tmpobj.constructor.$$metamodel$$.$tp[i]['var'];
                            }
                        }
                    }
                    if (iance === 'out') {
                        if (!extendsType(tmpobj.$$targs$$[i], cmptype)) {
                            return false;
                        }
                    } else if (iance === 'in') {
                        if (!extendsType(cmptype, tmpobj.$$targs$$[i])) {
                            return false;
                        }
                    } else if (iance === undefined) {
                        if (!(tmpobj.$$targs$$[i] && tmpobj.$$targs$$[i].t.$$ && tmpobj.$$targs$$[i].t.$$.T$name && cmptype && cmptype.t.$$ && cmptype.t.$$.T$name && tmpobj.$$targs$$[i].t.$$.T$name === cmptype.t.$$.T$name)) {
                            return false;
                        }
                    } else if (iance === null) {
                        console.log("Possible missing metamodel for " + type.t.$$.T$name + "<" + i + ">");
                    } else {
                        console.log("Don't know what to do about variance '" + iance + "'");
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
            if(types.l[i].t===Null || types.l[i].t===Anything) return true;
            else if (types.l[i].t==='u') {
                if (isOfTypes(null, types.l[i])) return true;
            }
        }
        return false;
    }
    if (obj === undefined || obj.getT$all === undefined) { return false; }
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
    if (t1 === undefined) {
        return true;//t2 === undefined;
    } else if (t1 === null) {
        return t2.t === Null;
    }
    if (t1.t === 'u' || t1.t === 'i') {
        if (t1.t==='i')removeSupertypes(t1.l);
        var unions = false;
        var inters = true;
        var _ints = false;
        for (var i = 0; i < t1.l.length; i++) {
            var partial = extendsType(t1.l[i], t2);
            if (t1.t==='u') {
                unions = partial||unions;
            } else {
                inters = partial&&inters;
                _ints=true;
            }
        }
        return _ints ? inters||unions : unions;
    }
    if (t2.t === 'u' || t2.t === 'i') {
        if (t2.t==='i') removeSupertypes(t2.l);
        var unions = false;
        var inters = true;
        var _ints = false;
        for (var i = 0; i < t2.l.length; i++) {
            var partial = extendsType(t1, t2.l[i]);
            if (t2.t==='u') {
                unions = partial||unions;
            } else {
                inters = partial&&inters;
                _ints=true;
            }
        }
        return _ints ? inters||unions : unions;
    }
    for (t in t1.t.$$.T$all) {
        if (t === t2.t.$$.T$name || t === 'ceylon.language::Nothing') {
            if (t1.a && t2.a) {
                //Compare type arguments
                for (ta in t1.a) {
                    if (!extendsType(t1.a[ta], t2.a[ta])) return false;
                }
            }
            return true;
        }
    }
    return false;
}
function removeSupertypes(list) {
    for (var i=0; i < list.length; i++) {
        for (var j=i; i < list.length; i++) {
            if (i!==j) {
                if (extendsType(list[i],list[j])) {
                    list[j]=list[i];
                } else if (extendsType(list[j],list[i])) {
                    list[i]=list[j];
                }
            }
        }
    }
}

function className(obj) {
    function _typename(t) {
        if (t.t==='i' || t.t==='u') {
            var _sep = t.t==='i'?'&':'|';
            var ct = '';
            for (var i=0; i < t.l.length; i++) {
                if (i>0) { ct+=_sep; }
                ct += _typename(t.l[i]);
            }
            return String$(ct);
        } else {
            var tn = t.t.$$.T$name;
            if (t.a) {
                tn += '<';
                for (var i = 0; i < t.a.length; i++) {
                    if (i>0) { tn += ','; }
                    tn += _typename(t.a[i]);
                }
                tn += '>';
            }
            return tn;
        }
    }
    if (obj === null) return String$('ceylon.language::Null');
    var tn = obj.getT$name === undefined ? 'UNKNOWN' : obj.getT$name();
    if (tn === 'UNKNOWN') {
        if (typeof obj === 'function') {
            tn = 'ceylon.language::Callable';
        }
    }
    else if (obj.$$targs$$) {
        /*tn += '<';
        for (var i=0; i < obj.$$targs$$.length; i++) {
            if (i>0) { tn += ','; }
            tn += _typename(obj.$$targs$$[i]);
        }
        tn += '>';*/
    }
    return String$(tn);
}

function identityHash(obj) {
    return obj.BasicID;
}

function set_type_args(obj, targs) {
    if (obj.$$targs$$ === undefined) {
        obj.$$targs$$=targs;
    } else {
        for (x in targs) {
            obj.$$targs$$[x] = targs[x];
        }
    }
}
function add_type_arg(obj, name, type) {
    if (obj.$$targs$$ === undefined) {
        obj.$$targs$$={};
    }
    obj.$$targs$$[name]=type;
}
function throwexc(msg) {
    throw Exception(msg.getT$all?msg:String$(msg));
}
exports.set_type_args=set_type_args;
exports.add_type_arg=add_type_arg;
exports.exists=exists;
exports.nonempty=nonempty;
exports.isOfType=isOfType;
exports.className=className;
exports.identityHash=identityHash;
exports.throwexc=throwexc;
