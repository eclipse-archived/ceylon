(function(define) {
    define(function(require, exports, module) {

//the Ceylon language module
//#METAMODEL

//Hand-written implementations
function exists(x){}//IGNORE
function String$(x,y){}//IGNORE
function ArraySequence(x){}//IGNORE
function nonempty(x){}//IGNORE
function className(x){}//IGNORE
function isOfType(a,b){}//IGNORE
var larger,smaller,Sequence,Category,empty,equal; //IGNORE

function getT$name() {return this.constructor.T$name;}
function getT$all() {return this.constructor.T$all;}
function initType(type, typeName) {
    var cons = function() {}
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=2; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for (var $ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
    cons.prototype.getT$name = getT$name;
    cons.prototype.getT$all = getT$all;
}
function initTypeProto(type, typeName) {
    initType.apply(this, arguments);
    var args = [].slice.call(arguments, 2);
    args.unshift(type);
    inheritProto.apply(this, args);
}
var initTypeProtoI = initTypeProto;
function initExistingType(type, cons, typeName) {
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=3; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for (var $ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
    var proto = cons.prototype;
    if (cons !== undefined) {
        try {
            cons.prototype.getT$name = getT$name;
            cons.prototype.getT$all = getT$all;
        } catch (exc) {
            // browser probably prevented access to the prototype
        }
    }
}
function initExistingTypeProto(type, cons, typeName) {
    var args = [].slice.call(arguments, 0);
    args.push(Basic);
    initExistingType.apply(this, args);
    var proto = cons.prototype;
    if ((proto !== undefined) && (proto.getHash === undefined)) {
    	var origToString = proto.toString;
        try {
            inheritProto(type, Basic);
            proto.toString = origToString;
        } catch (exc) {
            // browser probably prevented access to the prototype
        }
    }
}
function inheritProto(type) {
    var proto = type.$$.prototype;
    for (var i=1; i<arguments.length; ++i) {
        var superProto = arguments[i].$$.prototype;
        for (var $ in superProto) {proto[$] = superProto[$]}
    }
}
function reify(obj, params) {
    if (obj) {
        obj.$$targs$$=params;
    }
    return obj;
}
var inheritProtoI = inheritProto;
exports.initType=initType;
exports.initTypeProto=initTypeProto;
exports.initTypeProtoI=initTypeProtoI;
exports.initExistingType=initExistingType;
exports.initExistingTypeProto=initExistingTypeProto;
exports.inheritProto=inheritProto;
exports.inheritProtoI=inheritProtoI;
exports.reify=reify;

function Anything(wat) {
    return wat;
}
initType(Anything, 'ceylon.language::Anything');
function Null(wat) {
    return null;
}
initType(Null, 'ceylon.language::Null', Anything);
function Nothing(wat) {
    throw "Nothing";
}
initType(Nothing, 'ceylon.language::Nothing');

function Object$(wat) {
    return wat;
}
initTypeProto(Object$, 'ceylon.language::Object', Anything);
var Object$proto = Object$.$$.prototype;
Object$proto.getString = function() { return String$(className(this) + "@" + this.getHash()); }
//Object$proto.getString=function() { String$(Object.prototype.toString.apply(this)) };
Object$proto.toString=function() { return this.getString().valueOf(); }

var BasicID=1;
function $identityHash(x) {
    var hash = x.BasicID;
    return (hash !== undefined)
            ? hash : (x.BasicID = BasicID++);
}

function Identifiable(obj) {}
initType(Identifiable, "ceylon.language::Identifiable");
var Identifiable$proto = Identifiable.$$.prototype;
Identifiable$proto.equals = function(that) {
    return isOfType(that, {t:Identifiable}) && (that===this);
}
Identifiable$proto.getHash = function() { return $identityHash(this); }

function Basic(obj) {
    return obj;
}
initTypeProto(Basic, 'ceylon.language::Basic', Object$, Identifiable);

//INTERFACES
//#include callable.js

function Correspondence(wat) {
    return wat;
}
initType(Correspondence, 'ceylon.language::Correspondence');
function $init$Correspondence() { return Correspondence; }
var Correspondence$proto=Correspondence.$$.prototype;
Correspondence$proto.defines = function(key) {
    return exists(this.item(key));
}
Correspondence$proto.definesEvery = function(keys) {
	if (keys === undefined) return true;
    for (var i=0; i<keys.length; i++) {
        if (!this.defines(keys[i])) {
            return false;
        }
    }
    return true;
}
Correspondence$proto.definesAny = function(keys) {
	if (keys === undefined) return true;
    for (var i=0; i<keys.length; i++) {
        if (this.defines(keys[i])) {
            return true;
        }
    }
    return false;
}
Correspondence$proto.items = function(keys) {
    if (nonempty(keys)) {
        var r=[];
        for (var i = 0; i < keys.length; i++) {
            r.push(this.item(keys[i]));
        }
        return ArraySequence(r);
    }
    return empty;
}
Correspondence$proto.keys = function() {
    return TypeCategory(this, {t:Integer});
}
exports.Correspondence=Correspondence;

//#include iterable.js
//#include collections.js
//Compiled from Ceylon sources
//#COMPILED
//Ends compiled from Ceylon sources

/****************************************************************
 * Overwriting of some methods not yet working in compiled code *
 ****************************************************************/

Singleton.$$.prototype.equals = function(other) {
    if (isOfType(other, {t:List})) {
        if (other.getSize() !== 1) {
            return false;
        }
        var o = other.item(0);
        return o !== null && o.equals(this.getFirst());
    }
    return false;
}
Singleton.$$.prototype.getKeys = function() { return TypeCategory(this, {t:Integer}); }

//#include maps.js

function Number$(wat) {
    return wat;
}
initType(Number$, 'ceylon.language::Number');
exports.Number=Number$;
function $init$Number$() {
    if (Number$.$$===undefined) {
        initType(Number$, 'ceylon.language::Number');
    }
    return Number$;
}

//#include numbers.js
//#include strings.js

function getNull() { return null }
function Boolean$(value) {return Boolean(value)}
initExistingTypeProto(Boolean$, Boolean, 'ceylon.language::Boolean');
function trueClass() {}
initType(trueClass, "ceylon.language::true", Boolean$);
function falseClass() {}
initType(falseClass, "ceylon.language::false", Boolean$);
Boolean.prototype.getT$name = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$name;
}
Boolean.prototype.getT$all = function() {
    return (this.valueOf()?trueClass:falseClass).$$.T$all;
}
Boolean.prototype.equals = function(other) {return other.constructor===Boolean && other==this;}
Boolean.prototype.getHash = function() {return this.valueOf()?1:0;}
var trueString = String$("true", 4);
var falseString = String$("false", 5);
Boolean.prototype.getString = function() {return this.valueOf()?trueString:falseString;}
function getTrue() {return true}
function getFalse() {return false}
var $true = true;
var $false = false;
function Finished() {}
initTypeProto(Finished, 'ceylon.language::Finished', Basic);
var $finished = new Finished.$$;
$finished.string = String$("exhausted", 9);
$finished.getString = function() { return this.string; }
function getFinished() { return $finished; }

function Comparison(name) {
    var that = new Comparison.$$;
    that.name = String$(name);
    return that;
}
initTypeProto(Comparison, 'ceylon.language::Comparison', Basic);
var Comparison$proto = Comparison.$$.prototype;
Comparison$proto.getString = function() { return this.name; }

//#include functions.js
//#include functions2.js
//#include sequences.js
//#include process.js
//#include range.js


//#include annotations.js

exports.Identifiable=Identifiable;
exports.identityHash=$identityHash;
exports.Basic=Basic;
exports.Object=Object$;
exports.Anything=Anything;
exports.Null=Null;
exports.Nothing=Nothing;
exports.Boolean=Boolean$;
exports.Comparison=Comparison;
exports.getNull=getNull;
exports.getTrue=getTrue;
exports.getFalse=getFalse;
exports.Finished=Finished;
exports.getFinished=getFinished;
    });
}(typeof define==='function' && define.amd ? 
    define : function (factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports, module);
    } else {
        throw "no module loader";
    }
}));
