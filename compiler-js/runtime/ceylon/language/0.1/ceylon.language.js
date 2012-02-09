(function(define) {
    define(function(require, exports, module) {

//the Ceylon language module
function print(line) { console.log(line.getString().value) }

function initType(type, typeName) {
    var cons = function() {}
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=2; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for ($ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
}
function inheritProto(type, superType, suffix) {
    var proto = type.$$.prototype;
    var superProto = superType.$$.prototype;
    for(var $ in superProto){
        var $m = superProto[$];
        proto[$] = $m;
        if(suffix!==undefined && $.charAt($.length-1)!=='$') {proto[$+suffix] = $m}
    }
}

// TODO: Equality will probably be removed
function Equality(wat) {
    return wat;
}
initType(Equality, 'ceylon.language.Equality');

function Void(wat) {
    return wat;
}
initType(Void, 'ceylon.language.Void');
function Object$(wat) {
    return wat;
}
initType(Object$, 'ceylon.language.Object', Void);
var $Object = Object$.$$;
$Object.prototype.getString=function() { String$(Object.prototype.toString.apply(this)) };
$Object.prototype.toString=function() { return this.getString().value };
$Object.prototype.equals = function(other) { return Boolean$(this===other) } //TODO: is this correct?
function IdentifiableObject(obj) {
    return obj;
}
initType(IdentifiableObject, 'ceylon.language.IdentifiableObject', Object$, Equality);
inheritProto(IdentifiableObject, Object$, '$Object$');

//INTERFACES
function Cloneable(wat) {
    return wat;
}
initType(Cloneable, 'ceylon.language.Cloneable');
function Callable(wat) {
    return wat;
}
initType(Castable, 'ceylon.language.Callable');
function Castable(wat) {
    return wat;
}
initType(Castable, 'ceylon.language.Castable');
function Closeable(wat) {
    return wat;
}
initType(Closeable, 'ceylon.language.Closeable');
function Comparable(wat) {
    return wat;
}
initType(Comparable, 'ceylon.language.Comparable', Equality);
function Container(wat) {
    return wat;
}
initType(Container, 'ceylon.language.Container');
function Correspondence(wat) {
    return wat;
}
initType(Correspondence, 'ceylon.language.Correspondence');
function Sized(wat) {
    return wat;
}
initType(Sized, 'ceylon.language.Sized', Container);
function Iterable(wat) {
    return wat;
}
initType(Iterable, 'ceylon.language.Iterable', Container);
function Category(wat) {
    return wat;
}
initType(Category, 'ceylon.language.Category');
function Iterator(wat) {
    return wat;
}
initType(Iterator, 'ceylon.language.Iterator');
function Collection(wat) {
    return wat;
}
initType(Collection, 'ceylon.language.Collection', Iterable, Sized, Category, Equality, Cloneable);
function FixedSized(wat) {
    return wat;
}
initType(FixedSized, 'ceylon.language.FixedSized', Collection);
function Some(wat) {
    return wat;
}
initType(Some, 'ceylon.language.Some', FixedSized);
function Summable(wat) {
    return wat;
}
initType(Summable, 'ceylon.language.Summable');
function Number(wat) {
    return wat;
}
initType(Number, 'ceylon.language.Number', Equality);
function Invertable(wat) {
    return wat;
}
initType(Invertable, 'ceylon.language.Invertable');
function Numeric(wat) {
    return wat;
}
initType(Numeric, 'ceylon.language.Numeric', Number, Comparable, Summable, Invertable);
function Ordinal(wat) {
    return wat;
}
initType(Ordinal, 'ceylon.language.Ordinal', Equality);
function Integral(wat) {
    return wat;
}
initType(Integral, 'ceylon.language.Integral', Numeric, Ordinal);
function Ranged(wat) {
    return wat;
}
initType(Ranged, 'ceylon.language.Ranged');
function List(wat) {
    return wat;
}
initType(List, 'ceylon.language.List', Collection, Correspondence, Ranged, Cloneable);
function Map(wat) {
    return wat;
}
initType(Map, 'ceylon.language.Map', Collection, Correspondence, Cloneable);
function None(wat) {
    return wat;
}
initType(None, 'ceylon.language.None', FixedSized);
function Set(wat) {
    return wat;
}
initType(Set, 'ceylon.language.Set', Collection, Cloneable);

//Interface methods
var $FixedSized = FixedSized.$$;
$FixedSized.prototype.getFirst = function() {
    var e = this.getIterator().next();
    return e === $finished ? null : e;
}

var $None = None.$$;
$None.prototype.getFirst = function() { return null; }
$None.prototype.getIterator = function() { return emptyIterator; }
$None.prototype.getSize = function() { return Integer(0); }
$None.prototype.getEmpty = function() { return $true; }

var $Some = Some.$$;
$Some.prototype.getFirst = function() {
    var e = this.getIterator().next();
    if (e === $finished) throw Exception();
    return e;
}
$Some.prototype.getEmpty = function() { return $false; }

function Exception(description, cause, wat) {
    if (wat===undefined) {wat=new $Exception}
    wat.description = description;
    wat.cause = cause;
    return wat;
}
initType(Exception, 'ceylon.language.Exception', IdentifiableObject);
inheritProto(Exception, IdentifiableObject, '$IdentifiableObject$');
var $Exception = Exception.$$;
$Exception.prototype.getCause = function() {return this.cause}
$Exception.prototype.getMessage = function() {
    return this.description!==null ? this.description
           : (this.cause!==null ? this.cause.getMessage() : String$("", 0));
}
$Exception.prototype.getString = function() {
    return String$('Exception "' + this.getMessage().value + '"');
}

function Integer(value) {
    var that = new Integer.$$;
    that.value = value;
    return that;
}
initType(Integer, 'ceylon.language.Integer', Object$, Castable, Integral, Numeric);
inheritProto(Integer, Object$, '$Object$');
var $Integer = Integer.$$;
$Integer.prototype.getString = function() { return String$(this.value.toString()) }
$Integer.prototype.plus = function(other) { return Integer(this.value+other.value) }
$Integer.prototype.minus = function(other) { return Integer(this.value-other.value) }
$Integer.prototype.times = function(other) { return Integer(this.value*other.value) }
$Integer.prototype.divided = function(other) {
    var exact = this.value/other.value;
    return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
$Integer.prototype.remainder = function(other) { return Integer(this.value%other.value) }
$Integer.prototype.power = function(other) {
    var exact = Math.pow(this.value, other.value);
    return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
$Integer.prototype.getNegativeValue = function() { return Integer(-this.value) }
$Integer.prototype.getPositiveValue = function() { return this }
$Integer.prototype.equals = function(other) { return Boolean$(other && other.value===this.value) }
$Integer.prototype.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Integer.prototype.getFloat = function() { return Float(this.value) }
$Integer.prototype.getInteger = function() { return this }
$Integer.prototype.getCharacter = function() { return Character(this.value); }
$Integer.prototype.getSuccessor = function() { return Integer(this.value+1) }
$Integer.prototype.getPredecessor = function() { return Integer(this.value-1) }
$Integer.prototype.getUnit = function() { return Boolean$(this.value === 1) }
$Integer.prototype.getZero = function() { return Boolean$(this.value === 0) }
$Integer.prototype.getFractionalPart = function() { return Integer(0); }
$Integer.prototype.getWholePart = function() { return this; }
$Integer.prototype.getSign = function() { return this.value > 0 ? Integer(1) : this.value < 0 ? Integer(-1) : Integer(0); }
$Integer.prototype.getHash = function() { return this; }

function $parseInteger(s) { return Integer(parseInt(s.value)); }
function $parseFloat(s) { return Float(parseFloat(s.value)); }

function Float(value) {
    var that = new Float.$$;
    that.value = value;
    return that;
}
initType(Float, 'ceylon.language.Float', Object$, Castable, Numeric);
inheritProto(Float, Object$, '$Object$');
var $Float = Float.$$;
$Float.prototype.getString = function() { return String$(this.value.toString()) }
$Float.prototype.plus = function(other) { return Float(this.value+other.value) }
$Float.prototype.minus = function(other) { return Float(this.value-other.value) }
$Float.prototype.times = function(other) { return Float(this.value*other.value) }
$Float.prototype.divided = function(other) { return Float(this.value/other.value) }
$Float.prototype.power = function(other) { return Float(Math.pow(this.value, other.value)) }
$Float.prototype.getNegativeValue = function() { return Float(-this.value) }
$Float.prototype.getPositiveValue = function() { return this }
$Float.prototype.equals = function(other) { return Boolean$(other && other.value===this.value) }
$Float.prototype.compare = function(other) {
    if (other === null || other === undefined) { return larger; }
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Float.prototype.getFloat = function() { return this }
$Float.prototype.getInteger = function() { return Integer(parseInt(this.value.toFixed())); }
$Float.prototype.getWholePart = function() {
    var _p = this.value.toPrecision();
    var dot = _p.indexOf('.');
    return dot >= 0 ? Float(parseFloat(_p.slice(0, dot))) : this;
}
$Float.prototype.getFractionalPart = function() {
    var _p = this.value.toPrecision();
    var dot = _p.indexOf('.');
    return dot >= 0 ? Float(parseFloat(_p.slice(dot))) : Float(0.0);
}
$Float.prototype.getSign = function() { return this.value > 0 ? Integer(1) : this.value < 0 ? Integer(-1) : Integer(0); }
$Float.prototype.getHash = function() { return String$(this.value.toPrecision()).getHash(); }
$Float.prototype.getUndefined = function() { return isNaN(this.value) ? $true : $false; }
$Float.prototype.getFinite = function() { return this.value!==Infinity && this.value!==-Infinity && !isNaN(this.value) ? $true : $false; }
$Float.prototype.getInfinite = function() { return this.value===Infinity || this.value===-Infinity ? $true : $false; }

function getInfinity() { return Float(Infinity); }
//function getNegativeInfinity() { return Float(-Infinity); }

function String$(value,size) {
    var that = new String$.$$;
    that.value = value;
    that.codePoints = size;
    return that;
}
initType(String$, 'ceylon.language.String', Object$, List, Comparable, Ranged, FixedSized,
    Summable, Castable, Cloneable);
inheritProto(String$, Object$, '$Object$');
var $String = String$.$$;
$String.prototype.getString = function() { return this }
$String.prototype.toString = function() { return this.value }
$String.prototype.plus = function(other) {
    var size = this.codePoints + other.codePoints;
    return String$(this.value+other.value, isNaN(size)?undefined:size);
}
$String.prototype.equals = function(other) { return Boolean$(other && other.value===this.value) }
$String.prototype.compare = function(other) {
    var cmp = this.value.localeCompare(other.value);
    return cmp===0 ? equal : (cmp<0 ? smaller:larger);
}
$String.prototype.getUppercased = function() { return String$(this.value.toUpperCase()) }
$String.prototype.getLowercased = function() { return String$(this.value.toLowerCase()) }
$String.prototype.getSize = function() {
    if (this.codePoints===undefined) {
        this.codePoints = countCodepoints(this.value);
    }
    return Integer(this.codePoints);
}
$String.prototype.getLastIndex = function() { return this.getSize().equals(Integer(0)) === $true ? null : this.getSize().getPredecessor(); }
$String.prototype.span = function(from, to) {
	var lastIndex = this.getLastIndex();
	if (!lastIndex) return this; //it's empty
    var fromIndex = largest(Integer(0),from).value;
    var toIndex = to === getNull() || to === undefined ? lastIndex.value : smallest(to, lastIndex).value;
    if (fromIndex === toIndex) {
		return this.item(from).getString();
    } else if (toIndex > fromIndex) {
		//TODO optimize this
		var s = String$("");
        for (var i = fromIndex; i <= toIndex; i++) {
			s = s.plus(this.item(Integer(i)).getString());
        }
		return s;
    } else {
        //Negative span, reverse seq returned
        //TODO optimize
        var s = String$("");
        for (var i = fromIndex; i >= toIndex; i--) {
            var x = this.item(Integer(i));
			if (x !== null) s = s.plus(x.getString());
        }
		return s;
    }
}
$String.prototype.segment = function(from, len) {
	//TODO optimize
    var s = String$("");
    if (len.compare(Integer(0)) === larger) {
        var stop = from.plus(len).value;
        for (var i=from.value; i < stop; i++) {
            var x = this.item(Integer(i));
            if (x !== getNull()) { s = s.plus(x.getString()); }
        }
    }
    return s;
}
$String.prototype.getEmpty = function() {
    return Boolean$(this.value.length===0);
}
$String.prototype.longerThan = function(length) {
    if (this.codePoints!==undefined) {return Boolean$(this.codePoints>length.value)}
    if (this.value.length <= length.value) {return $false}
    if (this.value.length<<1 > length.value) {return $true}
    this.codePoints = countCodepoints(this.value);
    return Boolean$(this.codePoints>length.value);
}
$String.prototype.shorterThan = function(length) {
    if (this.codePoints!==undefined) {return Boolean$(this.codePoints<length.value)}
    if (this.value.length < length.value) {return $true}
    if (this.value.length<<1 >= length.value) {return $false}
    this.codePoints = countCodepoints(this.value);
    return Boolean$(this.codePoints<length.value);
}
$String.prototype.getIterator = function() { return StringIterator(this.value) }
$String.prototype.item = function(index) {
    if (index<0 || index>=this.value.length) {return null}
    var i = 0;
    for (var count=0; count<index; count++) {
        if ((this.value.charCodeAt(i)&0xfc00) === 0xd800) {++i}
        if (++i >= this.value.length) {return null}
    }
    return Character(codepointFromString(this.value, i));
}
$String.prototype.getTrimmed = function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var from = 0;
    while (from<this.value.length && (this.value.charCodeAt(from) in $WS)) {++from}
    var to = this.value.length;
    if (from < to) {
        do {--to} while (from<to && (this.value.charCodeAt(to) in $WS));
        ++to;
    }
    if (from===0 && to===this.value.length) {return this}
    var result = String$(this.value.substring(from, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.value.length + to;
    }
    return result;
}
$String.prototype.initial = function(length) {
    if (length.value >= this.codePoints) {return this}
    var count = 0;
    var i = 0;
    for (; i<this.value.length && count<length.value; ++i, ++count) {
        if ((this.value.charCodeAt(i)&0xfc00) === 0xd800) {++i}
    }
    if (i >= this.value.length) {
        this.codePoints = count;
        return this;
    }
    return String$(this.value.substr(0, i), count);
}
$String.prototype.terminal = function(length) {
    if (length.value >= this.codePoints) {return this}
    var count = 0;
    var i = this.value.length;
    for (; i>0 && count<length.value; ++count) {
        if ((this.value.charCodeAt(--i)&0xfc00) === 0xdc00) {--i}
    }
    if (i <= 0) {
        this.codePoints = count;
        return this;
    }
    return String$(this.value.substr(i), count);
}
$String.prototype.getHash = function() {
    if (this._hash === undefined) {
        for (var i = 0; i < this.value.length; i++) {
          var c = this.value.charCodeAt(i);
          this._hash += c + (this._hash << 10);
          this._hash ^= this._hash >> 6;
    }

    this._hash += this._hash << 3;
    this._hash ^= this._hash >> 11;
    this._hash += this._hash << 15;
    this._hash = this._hash & ((1 << 29) - 1);
  }
  return Integer(this._hash);
}
$String.prototype.getFirst = function() {
    return this.item(Integer(0));
}

function cmpSubString(str, subStr, offset) {
    for (var i=0; i<subStr.length; ++i) {
        if (str.charCodeAt(offset+i)!==subStr.charCodeAt(i)) {return $false}
    }
    return $true;
}
$String.prototype.startsWith = function(str) {
    if (str.value.length > this.value.length) {return $false}
    return cmpSubString(this.value, str.value, 0);
}
$String.prototype.endsWith = function(str) {
    var start = this.value.length - str.value.length
    if (start < 0) {return $false}
    return cmpSubString(this.value, str.value, start);
}
$String.prototype.contains = function(sub) {
    var str;
    if (sub.constructor === $String) {str = sub.value}
    else if (sub.constructor !== $Character) {return $false}
    else {str = codepointToString(sub.value)}
    return Boolean$(this.value.indexOf(str) >= 0);
}
$String.prototype.getNormalized = function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var result = "";
    var len = 0;
    var first = true;
    var i1 = 0;
    while (i1 < this.value.length) {
        while (this.value.charCodeAt(i1) in $WS) {
            if (++i1 >= this.value.length) {return String$(result)}
        }
        var i2 = i1;
        var cc = this.value.charCodeAt(i2);
        do {
            ++i2;
            if ((cc&0xfc00) === 0xd800) {++i2}
            ++len;
            cc = this.value.charCodeAt(i2);
        } while (i2<this.value.length && !(cc in $WS));
        if (!first) {
            result += " ";
            ++len;
        }
        first = false;
        result += this.value.substring(i1, i2);
        i1 = i2+1;
    }
    return String$(result, len);
}
$String.prototype.firstOccurrence = function(sub) {
    if (sub.value.length == 0) {return Integer(0)}
    var bound = this.value.length - sub.value.length;
    for (var i=0, count=0; i<=bound; ++count) {
        if (cmpSubString(this.value, sub.value, i) === $true) {return Integer(count)}
        if ((this.value.charCodeAt(i++)&0xfc00) === 0xd800) {++i}
    }
    return null;
}
$String.prototype.lastOccurrence = function(sub) {
    if (sub.value.length == 0) {return Integer(this.value.length>0 ? this.value.length-1 : 0)}
    for (var i=this.value.length-sub.value.length; i>=0; --i) {
        if (cmpSubString(this.value, sub.value, i) === $true) {
            for (var count=0; i>0; ++count) {
                if ((this.value.charCodeAt(--i)&0xfc00) === 0xdc00) {--i}
            }
            return Integer(count);
        }
    }
    return null;
}
$String.prototype.firstCharacterOccurrence = function(subc) {
    for (var i=0, count=0; i<this.value.length; count++) {
        var cp = this.value.charCodeAt(i++);
        if (((cp&0xfc00) === 0xd800) && i<this.value.length) {
            cp = (cp<<10) + this.value.charCodeAt(i++) - 0x35fdc00;
        }
        if (cp === subc.value) {return Integer(count)}
    }
    this.codePoints = count;
    return null;
}
$String.prototype.lastCharacterOccurrence = function(subc) {
    for (var i=this.value.length-1, count=0; i>=0; count++) {
        var cp = this.value.charCodeAt(i--);
        if (((cp%0xfc00) === 0xdc00) && i>=0) {
           cp = (this.value.charCodeAt(i--)<<10) + cp - 0x35fdc00;
        }
        if (cp === subc.value) {
            if (this.codePoints === undefined) {this.codePoints = countCodepoints(this.value)}
            return Integer(this.codePoints - count - 1);
        }
    }
    this.codePoints = count;
    return null;
}
$String.prototype.getCharacters = function() {
    //we can cheat and add the required behavior to String, avoiding the need to create a Sequence...
    //TODO: this probably doesn't work completely because String doesn't satisfy
    //      all required interfaces, so "if(is ...)" will be false when it shouldn't.
    return this.value.length>0 ? this:$empty;
}
$String.prototype.getFirst = function() { return this.getSize().value>0?this.item(Integer(0)):null; }
$String.prototype.getLast = function() { return this.getSize().value>0?this.item(Integer(this.getSize().getPredecessor())):null; }
$String.prototype.getKeys = function() {
    //TODO implement!!!
    return this.getSize().value > 0 ? Range(Integer(0), this.getSize().getPredecessor()) : $empty;
}
$String.prototype.join = function(strings) {
    if (strings===undefined || strings.value.length===0) {return String$("", 0)}
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this.value)}
    var str = strings.value[0];
    var result = str.value;
    var len = str.codePoints;
    for (var i=1; i<strings.value.length; ++i) {
        str = strings.value[i];
        result += this.value;
        result += str.value;
        len += this.codePoints + str.codePoints;
    }
    return String$(result, isNaN(len)?undefined:len);
}
$String.prototype.split = function(seps, discard) {
    var sepChars = $WS;
    if (seps!==undefined && seps!==null) {
        sepChars = {}
        var it = seps.getIterator();
        var c; while ((c=it.next()) !== $finished) {sepChars[c.value] = true}
    }
    if (discard === undefined) {discard = false}
    
    //TODO: return an iterable which determines the next token on demand
    var tokens = [];
    var tokenBegin = 0;
    var tokenBeginCount = 0;
    for (var i=0, count=0; i<this.value.length;) {
        var j = i;
        var cp = this.value.charCodeAt(i++);
        if ((cp&0xfc00)===0xd800 && i<this.value.length) {
            cp = (cp<<10) + this.value.charCodeAt(i++) - 0x35fdc00;
        }
        ++count;
        if (cp in sepChars) {
            if (tokenBegin != j) {
                tokens.push(String$(this.value.substring(tokenBegin, j), count-tokenBeginCount-1))
            }
            if (!discard) {tokens.push(String$(this.value.substring(j, i), 1))}
            tokenBegin = i;
            tokenBeginCount = count;
        }
    }
    if (tokenBegin != i) {
        tokens.push(String$(this.value.substring(tokenBegin, i), count-tokenBeginCount))
    }
    this.codePoints = count;
    return ArraySequence(tokens);
}
$String.prototype.getReversed = function() {
    var result = "";
    for (var i=this.value.length; i>0;) {
        var cc = this.value.charCodeAt(--i);
        if ((cc&0xfc00)!==0xdc00 || i===0) {
            result += this.value.charAt(i);
        } else {
            result += this.value.substr(--i, 2);
        }
    }
    return String$(result);
}
$String.prototype.replace = function(sub, repl) {
    return String$(this.value.replace(new RegExp(sub.value, 'g'), repl.value));
}
$String.prototype.repeat = function(times) {
    var sb = StringBuilder();
    for (var i = 0; i < times.value; i++) {
        sb.append(this);
    }
    return sb.getString();
}

function StringIterator(string) {
    var that = new StringIterator.$$;
    that.string = string;
    that.index = 0;
    return that;
}
initType(StringIterator, 'ceylon.language.StringIterator', IdentifiableObject, Iterator);
inheritProto(StringIterator, IdentifiableObject, '$IdentifiableObject$');
var $StringIterator = StringIterator.$$;
$StringIterator.prototype.next = function() {
    if (this.index >= this.string.length) { return $finished }
    var first = this.string.charCodeAt(this.index++);
    if ((first&0xfc00) !== 0xd800 || this.index >= this.string.length) {
        return Character(first);
    }
    return Character((first<<10) + this.string.charCodeAt(this.index++) - 0x35fdc00);
}

function countCodepoints(str) {
    var count = 0;
    for (var i=0; i<str.length; ++i) {
        ++count;
        if ((str.charCodeAt(i)&0xfc00) === 0xd800) {++i}
    }
    return count;
}
function codepointToString(cp) {
    if (cp <= 0xffff) {
        return String.fromCharCode(cp);
    }
    return String.fromCharCode((cp>>10)+0xd7c0, (cp&0x3ff)+0xdc00);
}
function codepointFromString(str, index) {
    var first = str.charCodeAt(index);
    if ((first&0xfc00) !== 0xd800) {return first}
    var second = str.charCodeAt(index+1);
    return isNaN(second) ? first : ((first<<10) + second - 0x35fdc00);
}

function Character(value) {
    var that = new Character.$$;
    that.value = value;
    return that;
}
initType(Character, 'ceylon.language.Character', Object$, Comparable);
inheritProto(Character, Object$, '$Object$');
var $Character = Character.$$;
$Character.prototype.getString = function() { return String$(codepointToString(this.value)) }
$Character.prototype.equals = function(other) {
    return Boolean$(other.constructor===$Character && other.value===this.value);
}
$Character.prototype.getHash = function() {return Integer(this.value)}
$Character.prototype.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Character.prototype.getUppercased = function() {
    var ucstr = codepointToString(this.value).toUpperCase();
    return Character(codepointFromString(ucstr, 0));
}
$Character.prototype.getLowercased = function() {
    var lcstr = codepointToString(this.value).toLowerCase();
    return Character(codepointFromString(lcstr, 0));
}
$Character.prototype.getTitlecased = function() {
    var tc = $toTitlecase[this.value];
    return tc===undefined ? this.getUppercased() : Character(tc);
}
var $WS={0x9:true, 0xa:true, 0xb:true, 0xc:true, 0xd:true, 0x20:true, 0x85:true, 0xa0:true,
    0x1680:true, 0x180e:true, 0x2028:true, 0x2029:true, 0x202f:true, 0x205f:true, 0x3000:true}
for (var i=0x2000; i<=0x200a; i++) { $WS[i]=true }
var $digit={0x30:true, 0x660:true, 0x6f0:true, 0x7c0:true, 0x966:true, 0x9e6:true, 0xa66:true,
    0xae6:true, 0xb66:true, 0xbe6:true, 0xc66:true, 0xce6:true, 0xd66:true, 0xe50:true,
    0xed0:true, 0xf20:true, 0x1040:true, 0x1090:true, 0x17e0:true, 0x1810:true, 0x1946:true,
    0x19d0:true, 0x1a80:true, 0x1a90:true, 0x1b50:true, 0x1bb0:true, 0x1c40:true, 0x1c50:true,
    0xa620:true, 0xa8d0:true, 0xa900:true, 0xa9d0:true, 0xaa50:true, 0xabf0:true, 0xff10:true,
    0x104a0:true, 0x11066:true, 0x110f0:true, 0x11136:true, 0x111d0:true, 0x116c0:true}
var $titlecase={
    0x1c5: [0x1c4, 0x1c6], 0x1c8: [0x1c7, 0x1c9], 0x1cb: [0x1ca, 0x1cc], 0x1f2: [0x1f1, 0x1f3],
    0x1f88: [undefined, 0x1f80], 0x1f89: [undefined, 0x1f81], 0x1f8a: [undefined, 0x1f82],
    0x1f8b: [undefined, 0x1f83], 0x1f8c: [undefined, 0x1f84], 0x1f8d: [undefined, 0x1f85],
    0x1f8e: [undefined, 0x1f86], 0x1f8f: [undefined, 0x1f87], 0x1f98: [undefined, 0x1f90],
    0x1f99: [undefined, 0x1f91], 0x1f9a: [undefined, 0x1f92], 0x1f9b: [undefined, 0x1f93],
    0x1f9c: [undefined, 0x1f94], 0x1f9d: [undefined, 0x1f95], 0x1f9e: [undefined, 0x1f96],
    0x1f9f: [undefined, 0x1f97], 0x1fa8: [undefined, 0x1fa0], 0x1fa9: [undefined, 0x1fa1],
    0x1faa: [undefined, 0x1fa2], 0x1fab: [undefined, 0x1fa3], 0x1fac: [undefined, 0x1fa4],
    0x1fad: [undefined, 0x1fa5], 0x1fae: [undefined, 0x1fa6], 0x1faf: [undefined, 0x1fa7],
    0x1fbc: [undefined, 0x1fb3], 0x1fcc: [undefined, 0x1fc3], 0x1ffc: [undefined, 0x1ff3]
}
var $toTitlecase={
    0x1c6:0x1c5, 0x1c7:0x1c8, 0x1ca:0x1cb, 0x1f1:0x1f2,
    0x1c4:0x1c5, 0x1c9:0x1c8, 0x1cc:0x1cb, 0x1f3:0x1f2, 0x1f80:0x1f88, 0x1f81:0x1f89, 0x1f82:0x1f8a,
    0x1f83:0x1f8b, 0x1f84:0x1f8c, 0x1f85:0x1f8d, 0x1f86:0x1f8e, 0x1f87:0x1f8f, 0x1f90:0x1f98,
    0x1f91:0x1f99, 0x1f92:0x1f9a, 0x1f93:0x1f9b, 0x1f94:0x1f9c, 0x1f95:0x1f9d, 0x1f96:0x1f9e,
    0x1f97:0x1f9f, 0x1fa0:0x1fa8, 0x1fa1:0x1fa9, 0x1fa2:0x1faa, 0x1fa3:0x1fab, 0x1fa4:0x1fac,
    0x1fa5:0x1fad, 0x1fa6:0x1fae, 0x1fa7:0x1faf, 0x1fb3:0x1fbc, 0x1fc3:0x1fcc, 0x1ff3:0x1ffc
}
$Character.prototype.getWhitespace = function() { return Boolean$(this.value in $WS) }
$Character.prototype.getControl = function() { return Boolean$(this.value<32 || this.value===127) }
$Character.prototype.getDigit = function() {
    var check = this.value & 0xfffffff0;
    if (check in $digit) {
        return Boolean$((this.value&0xf) <= 9);
    }
    if ((check|6) in $digit) {
        return Boolean$((this.value&0xf) >= 6);
    }
    return Boolean$(this.value>=0x1d7ce && this.value<=0x1d7ff);
}
$Character.prototype.getInteger = function() { return Integer(this.value); }
$Character.prototype.getUppercase = function() {
    var str = codepointToString(this.value);
    return Boolean$(str.toLowerCase()!==str && !(this.value in $titlecase));
}
$Character.prototype.getLowercase = function() {
    var str = codepointToString(this.value);
    return Boolean$(str.toUpperCase()!==str && !(this.value in $titlecase));
}
$Character.prototype.getTitlecase = function() {return Boolean$(this.value in $titlecase)}
$Character.prototype.getLetter = function() {
    //TODO: this captures only letters that have case
    var str = codepointToString(this.value);
    return Boolean$(str.toUpperCase()!==str || str.toLowerCase()!==str || (this.value in $titlecase));
}
$Character.prototype.getSuccessor = function() {
    var succ = this.value+1;
    if ((succ&0xf800) === 0xd800) {return Character(0xe000)}
    return Character((succ<=0x10ffff) ? succ:0);
}
$Character.prototype.getPredecessor = function() {
    var succ = this.value-1;
    if ((succ&0xf800) === 0xd800) {return Character(0xd7ff)}
    return Character((succ>=0) ? succ:0x10ffff);
}

function StringBuilder() {
    var that = new StringBuilder.$$;
    that.value = "";
    return that;
}
initType(StringBuilder, 'ceylon.language.StringBuilder', IdentifiableObject);
inheritProto(StringBuilder, IdentifiableObject, '$IdentifiableObject$');
var $StringBuilder = StringBuilder.$$;
$StringBuilder.prototype.getString = function() { return String$(this.value); }
$StringBuilder.prototype.append = function(s) {
    this.value = this.value + s.value;
}
$StringBuilder.prototype.appendAll = function(strings) {
    if (strings === null || strings === undefined) { return this; }
    for (var i = 0; i < strings.value.length; i++) {
        var _s = strings.value[i];
        this.value += _s?_s.value:"null";
    }
    return this; //strictly speaking, this method should return void, but then string interpolation would be a big mess
}
$StringBuilder.prototype.appendCharacter = function(c) {
    this.append(c.getString());
}
$StringBuilder.prototype.appendNewline = function() { this.value = this.value + "\n"; }
$StringBuilder.prototype.appendSpace = function() { this.value = this.value + " "; }

function getNull() { return null }
function Boolean$(value) {
    return value ? $true : $false;
}
initType(Boolean$, 'ceylon.language.Boolean', IdentifiableObject);
inheritProto(Boolean$, IdentifiableObject, '$IdentifiableObject$');
var $true = new Boolean$.$$;
$true.string = String$("true");
$true.getString = function() {return this.string}
function getTrue() { return $true; }
var $false = new Boolean$.$$;
$false.string = String$("false");
$false.getString = function() {return this.string}
function getFalse() { return $false; }
function Finished() {}
initType(Finished, 'ceylon.language.Finished', IdentifiableObject);
inheritProto(Finished, IdentifiableObject, '$IdentifiableObject$');
var $finished = new Finished.$$;
$finished.string = String$("exhausted");
$finished.getString = function() {return this.string}
function getExhausted() { return $finished; }

//These are operators for handling nulls
function $nullsafe() { return null; }
function exists(value) { return value === getNull() || value === undefined ? $false : $true; }
function nonempty(value) { return value === null || value === undefined ? $false : Boolean$(value.getEmpty() === $false); }

function isOfType(obj, typeName) {
    return Boolean$((obj===null) ? (typeName==="ceylon.language.Nothing" || typeName==="ceylon.language.Void") : (typeName in obj.constructor.T$all));
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
    return String$(obj!==null ? obj.constructor.T$name : 'ceylon.language.Nothing');
}

function Comparison(name) {
    var that = new Comparison.$$;
    that.name = String$(name);
    return that;
}
initType(Comparison, 'ceylon.language.Comparison', IdentifiableObject);
inheritProto(Comparison, IdentifiableObject, '$IdentifiableObject$');
var $Comparison = Comparison.$$;
$Comparison.prototype.getString = function() { return this.name }

var larger = Comparison("larger");
function getLarger() { return larger }
var smaller = Comparison("smaller");
function getSmaller() { return smaller }
var equal = Comparison("equal");
function getEqual() { return equal }
function largest(x, y) { return x.compare(y) === larger ? x : y }
function smallest(x, y) { return x.compare(y) === smaller ? x : y }

function Sequence($$sequence) {
    return $$sequence;
}
initType(Sequence, 'ceylon.language.Sequence', List, Some, Cloneable, Ranged);
var $Sequence = Sequence.$$;
$Sequence.prototype.getEmpty = function() { return $false }
$Sequence.prototype.getSize = function() { return Integer(this.getLastIndex()+1) }
$Sequence.prototype.defines = function(index) { return Boolean$(index.value<=this.getLastIndex().value) }

function Empty() {
    var that = new Empty.$$;
    that.value = [];
    return that;
}
initType(Empty, 'ceylon.language.Empty', List, None, Ranged, Cloneable);
var $Empty = Empty.$$;
$Empty.prototype.getEmpty = function() { return $true; }
$Empty.prototype.defines = function(x) { return $false; }
$Empty.prototype.getKeys = function() { return IntCategory(this); }
$Empty.prototype.definesEvery = function(x) { return $false; }
$Empty.prototype.definesAny = function(x) { return $false; }
$Empty.prototype.items = function(x) { return this; }
$Empty.prototype.getSize = function() { return Integer(0); }
$Empty.prototype.item = function(x) { return null; }
$Empty.prototype.getFirst = function() { return null; }
$Empty.prototype.segment = function(a,b) { return this; }
$Empty.prototype.span = function(a,b) { return this; }
$Empty.prototype.getIterator = function() { return emptyIterator; }
$Empty.prototype.getString = function() { return String$("{}"); }
$Empty.prototype.contains = function(x) { return $false; }
$Empty.prototype.getLastIndex = function() { return null; }
$Empty.prototype.getClone = function() { return this; }
$Empty.prototype.count = function(x) { return Integer(0); }

$empty = Empty();

function EmptyIterator() {
    var that = new EmptyIterator.$$;
    return that;
}
initType(EmptyIterator, 'ceylon.language.EmptyIterator', IdentifiableObject, Iterator);
inheritProto(EmptyIterator, IdentifiableObject, '$IdentifiableObject$');
var $EmptyIterator = EmptyIterator.$$;
$EmptyIterator.prototype.next = function() { return $finished; }
emptyIterator=EmptyIterator();

function ArraySequence(value) {
    var that = new ArraySequence.$$;
    that.value = value;
    return that;
}
initType(ArraySequence, 'ceylon.language.ArraySequence', IdentifiableObject, Sequence);
inheritProto(ArraySequence, IdentifiableObject, '$IdentifiableObject$');
inheritProto(ArraySequence, Sequence, '$Sequence$');
var $ArraySequence = ArraySequence.$$;
$ArraySequence.prototype.getString = function() {
	if (this.value.length === 0) {
		return String$("{}");
	}
    var desc = "{ ";
    var first = true;
    for (var i = 0; i < this.value.length; i++) {
        if (first) {
            first = false;
		} else {
            desc += ", ";
        }
        var item = this.value[i];
        desc += exists(item) === $true ? item.getString().value : "null";
    }
    return String$(desc +" }");
}
$ArraySequence.prototype.item = function(index) {
    var result = this.value[index.value];
    return result!==undefined ? result:null;
}
$ArraySequence.prototype.getSize = function() { return Integer(this.value.length) }
$ArraySequence.prototype.getEmpty = function() { return this.value.length > 0 ? getFalse() : getTrue(); }
$ArraySequence.prototype.getLastIndex = function() { return this.getSize().getPredecessor(); }
$ArraySequence.prototype.getFirst = function() { return this.item(Integer(0)); }
$ArraySequence.prototype.getLast = function() { return this.item(this.getLastIndex()); }
$ArraySequence.prototype.defines = function(idx) { return Boolean$(idx.compare(this.getSize()) === smaller); }
$ArraySequence.prototype.segment = function(from, len) {
    var seq = [];
    if (len.compare(Integer(0)) === larger) {
        var stop = from.plus(len).value;
        for (var i=from.value; i < stop; i++) {
            var x = this.item(Integer(i));
            if (x !== getNull()) { seq.push(x); }
        }
    }
    return ArraySequence(seq);
}
$ArraySequence.prototype.span = function(from, to) {
    var fromIndex = largest(Integer(0),from).value;
    var toIndex = to === getNull() || to === undefined ? this.getLastIndex().value : smallest(to, this.getLastIndex()).value;
    var seq = [];
    if (fromIndex === toIndex) {
        return Singleton(this.item(from));
    } else if (toIndex > fromIndex) {
        for (var i = fromIndex; i <= toIndex && this.defines(Integer(i)) === $true; i++) {
            seq.push(this.item(Integer(i)));
        }
    } else {
        //Negative span, reverse seq returned
        for (var i = fromIndex; i >= toIndex && this.defines(Integer(i)) === $true; i--) {
            seq.push(this.item(Integer(i)));
        }
    }
    return ArraySequence(seq);
}
$ArraySequence.prototype.getRest = function() { return ArraySequence(this.value.slice(1)); }
$ArraySequence.prototype.items = function(keys) {
    var seq = [];
    for (var i = 0; i < keys.getSize().value; i++) {
        var key = keys.item(Integer(i));
        if (this.defines(key)) {
            seq.push(this.item(key));
        }
    }
    return ArraySequence(seq);
}
$ArraySequence.prototype.definesEvery = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getFalse()) {
            return getFalse();
        }
    }
    return getTrue();
}
$ArraySequence.prototype.definesAny = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getTrue()) {
            return getTrue();
        }
    }
    return getFalse();
}
$ArraySequence.prototype.equals = function(other) {
    if (other && other.getSize().equals(this.getSize()) === getTrue()) {
        for (var i = 0; i < this.getSize().value; i++) {
            var mine = this.item(Integer(i));
            var theirs = other.item(Integer(i));
            if (((mine === null) && theirs) || !(mine && mine.equals(theirs) === getTrue())) {
                return getFalse();
            }
        }
        return getTrue();
    }
    return getFalse();
}
$ArraySequence.prototype.getIterator = function() { return ArrayIterator(this.value); }
$ArraySequence.prototype.getKeys = function() { return IntCategory(this); }

function IntCategory(seq) {
    var that = new IntCategory.$$;
    that.seq = seq;
    return that;
}
initType(IntCategory, 'ceylon.language.IntCategory', IdentifiableObject, Category);
inheritProto(IntCategory, IdentifiableObject, '$IdentifiableObject$');
var $IntCategory = IntCategory.$$;
$IntCategory.prototype.contains = function(k) {
    return this.seq.defines(k);
}
$IntCategory.prototype.containsEvery = function(keys) {
    var all = true;
    for (var i = 0; i < this.seq.value.length; i++) {
        all = all && this.seq.defines(keys.item(Integer(i))).value;
    }
    return Boolean$(all);
}
$IntCategory.prototype.containsAny = function(keys) {
    for (var i = 0; i < this.seq.value.length; i++) {
        if (this.seq.defines(keys.item(Integer(i))) == $true) {
            return $true;
        }
    }
    return $false;
}

function ArrayIterator(arr) {
    var that = new ArrayIterator.$$;
    that.array = arr;
    that.current = arr && arr.length ? arr[0] : $finished;
    that.idx = 0;
    return that;
}
initType(ArrayIterator, 'ceylon.language.ArrayIterator', IdentifiableObject, Iterator);
inheritProto(ArrayIterator, IdentifiableObject, '$IdentifiableObject$');
var $ArrayIterator = ArrayIterator.$$;
$ArrayIterator.prototype.next = function() {
    if (this.current === $finished) {
        return $finished;
    }
    this.current = this.idx < this.array.length ? this.array[this.idx] : $finished;
    this.idx++;
    return this.current;
}

function SequenceBuilder() {
    var that = new SequenceBuilder.$$;
    that.seq = [];
    return that;
}
initType(SequenceBuilder, 'ceylon.language.SequenceBuilder', IdentifiableObject, Sized);
inheritProto(SequenceBuilder, IdentifiableObject, '$IdentifiableObject$');
var $SequenceBuilder = SequenceBuilder.$$;
$SequenceBuilder.prototype.getSequence = function() { return ArraySequence(this.seq); }
$SequenceBuilder.prototype.append = function(e) { this.seq.push(e); }
$SequenceBuilder.prototype.appendAll = function(arr) {
	if (arr && arr.value && arr.value.length) {
        for (var i = 0; i < arr.value.length; i++) {
            this.seq.push(arr.value[i]);
        }
    }
}
$SequenceBuilder.prototype.getSize = function() { return Integer(this.seq.length); }
$SequenceBuilder.prototype.getEmpty = function() { return Boolean$(this.seq.length === 0); }

function SequenceAppender(other) {
	var that = new SequenceAppender.$$;
	that.seq = [];
	that.appendAll(other);
	return that;
}
initType(SequenceAppender, 'ceylon.language.SequenceAppender', SequenceBuilder);
inheritProto(SequenceAppender, SequenceBuilder, '$SequenceBuilder$');

function Range(first, last) {
    var that = new Range.$$;
    that.first = first;
    that.last = last;
    var index = 0;
    var x = first;
    var dec = first.compare(last) === larger;
    while (x.equals(last) === getFalse()) { //some replicated code because we don't yet have the functions here
        index++;
        x = dec ? x.getPredecessor() : x.getSuccessor();
    }
    that.size = Integer(index+1);
    return that;
}
initType(Range, 'ceylon.language.Range', Object$, Sequence, Category, Equality);
inheritProto(Range, Object$, '$Object$');
inheritProto(Range, Sequence, '$Sequence$');
var $Range = Range.$$;
$Range.prototype.getFirst = function() { return this.first; }
$Range.prototype.getLast = function() { return this.last; }
$Range.prototype.getEmpty = function() { return getFalse(); }
$Range.prototype.getDecreasing = function() {
    return Boolean$(this.first.compare(this.last) === larger);
}
$Range.prototype.next = function(x) {
    return this.getDecreasing() === getTrue() ? x.getPredecessor() : x.getSuccessor();
}
$Range.prototype.getSize = function() { return this.size; }
$Range.prototype.getLastIndex = function() { return Integer(this.size-1); }
$Range.prototype.item = function(index) {
    var idx = 0;
    var x = this.first;
    while (idx < index.value) {
        if (x.equals(this.last) === getTrue()) { return getNull(); }
        else {
            idx++;
            x = this.next(x);
        }
    }
    return x;
}
$Range.prototype.includes = function(x) {
    var compf = x.compare(this.first);
    var compl = x.compare(this.last);
    var rval = this.getDecreasing() === getTrue() ? ((compf === equal || compf === smaller) && (compl === equal || compl === larger)) : ((compf === equal || compf === larger) && (compl === equal || compl === smaller));
    return Boolean$(rval);
}
$Range.prototype.contains = function(x) {
    if (typeof x.compare==='function' || (x.prototype && typeof x.prototype.compare==='function')) {
        return this.includes(x);
    }
    return $false;
}
$Range.prototype.getRest = function() {
    var n = this.next(this.first);
    return (n.equals(this.last) === getTrue()) ? ArraySequence([]) : Range(n, this.last);
}
$Range.prototype.by = function(step) {
    if (step.compare(Integer(0)) !== larger) {
        //throw
    }
    if (this.first.equals(this.last) === getTrue() || step.equals(Integer(1)) === getTrue()) {
        return this;
    }
    var seq = [];
    var x = this.first;
    while (this.includes(x) === getTrue()) {
        seq.push(x);
        for (var i = 0; i < step.value; i++) { x = this.next(x); }
    }
    return ArraySequence(seq);
}
$Range.prototype.segment = function(from, len) {
    //only positive length for now
    if (len.compare(Integer(0)) !== larger) return $empty;
    if (this.defines(from) === $false) return $empty;
    var x = this.first;
    for (var i=0; i < from.value; i++) { x = this.next(x); }
    var y = x;
    for (var i=1; i < len.value; i++) { y = this.next(y); }
    if (this.includes(y) === getFalse()) { y = this.last; }
    return Range(x, y);
}
$Range.prototype.span = function(from, to) {
    from = largest(Integer(0),from);
    if (to === getNull() || to === undefined) {
        to = this.getLastIndex();
    }
    if (this.defines(from) === $false) {
        //If it's an inverse range, adjust the "from" (upper bound)
        if (from.compare(to) === larger && this.defines(to) === $true) {
            //Decrease the upper bound
            while (!this.defines(from)) {
                from = from.getPredecessor();
            }
        } else {
            return $empty;
        }
    } else while (this.defines(to) === $false) {
        //decrease the upper bound
        to = to.getPredecessor();
    }
    return Range(this.item(from), this.item(to));
}
$Range.prototype.definesEvery = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getFalse()) {
            return getFalse();
        }
    }
    return getTrue();
}
$Range.prototype.definesAny = function(keys) {
    for (var i = 0; i < keys.getSize().value; i++) {
        if (this.defines(keys.item(Integer(i))) === getTrue()) {
            return getTrue();
        }
    }
    return getFalse();
}
$Range.prototype.defines = function(idx) { return Boolean$(idx.compare(this.getSize()) === smaller); }
$Range.prototype.getString = function() { return String$(this.first.getString().value + ".." + this.last.getString().value); }
$Range.prototype.equals = function(other) {
    if (!other) { return getFalse(); }
    var eqf = this.first.equals(other.getFirst());
    var eql = this.last.equals(other.getLast());
    return Boolean$(eqf === getTrue() && eql === getTrue());
}
$Range.prototype.getIterator = function() { return RangeIterator(this); }

function RangeIterator(range) {
    var that = new RangeIterator.$$;
    that.range = range;
    that.current = range.getFirst();
    return that;
}
initType(RangeIterator, 'ceylon.language.RangeIterator', IdentifiableObject, Iterator);
inheritProto(RangeIterator, IdentifiableObject, '$IdentifiableObject$');
var $RangeIterator = RangeIterator.$$;
$RangeIterator.prototype.next = function() {
    var rval = this.current;
    if (rval.equals($finished) === getTrue()) {
        return rval;
    } else if (rval.equals(this.range.getLast()) === getTrue()) {
        this.current = $finished;
    } else {
        this.current = this.range.next(this.current);
    }
    return rval;
}

function Singleton(elem) {
    var that = new Singleton.$$;
    that.value = [elem];
    that.elem = elem;
    return that;
}
initType(Singleton, 'ceylon.language.Singleton', Object$, Sequence);
inheritProto(Singleton, Object$, '$Object$');
inheritProto(Singleton, Sequence, '$Sequence$');
var $Singleton = Singleton.$$;
$Singleton.prototype.getString = function() { return String$("{ " + this.elem.getString().value + " }") }
$Singleton.prototype.item = function(index) {
    return index.value===0 ? this.value[0] : null;
}
$Singleton.prototype.getSize = function() { return Integer(1); }
$Singleton.prototype.getLastIndex = function() { return Integer(0); }
$Singleton.prototype.getFirst = function() { return this.elem; }
$Singleton.prototype.getLast = function() { return this.elem; }
$Singleton.prototype.getEmpty = function() { return $false; }
$Singleton.prototype.getRest = function() { return $empty; }
$Singleton.prototype.defines = function(idx) { return idx.equals(Integer(0)); }
$Singleton.prototype.getKeys = function() { return IntCategory(this); }
$Singleton.prototype.span = function(from, to) {
	if (to === undefined || to === null) to = from;
    return (from.equals(Integer(0)) === getTrue() || to.equals(Integer(0)) === getTrue()) ? this : $empty;
}
$Singleton.prototype.segment = function(idx, len) {
    if (idx.equals(Integer(0)) === getTrue() && len.compare(Integer(0)) === larger) {
        return this;
    }
    return $empty;
}
$Singleton.prototype.getIterator = function() { return SingletonIterator(this.elem); }

function SingletonIterator(elem) {
    var that = new SingletonIterator.$$;
    that.elem = elem;
    that.done = false;
    return that;
}
initType(SingletonIterator, 'ceylon.language.SingletonIterator', IdentifiableObject, Iterator);
inheritProto(SingletonIterator, IdentifiableObject, '$IdentifiableObject$');
var $SingletonIterator = SingletonIterator.$$;
$SingletonIterator.prototype.next = function() {
    if (this.done) {
        return $finished;
    }
    this.done = true;
    return this.elem;
}

function Entry(key, item) {
    var that = new Entry.$$;
    Object$(that);
    Equality(that);
    Void(that);
    that.key = key;
    that.item = item;
    return that;
}
initType(Entry, 'ceylon.language.Entry', Object$, Equality);
inheritProto(Entry, Object$, '$Object$');
var $Entry = Entry.$$;
$Entry.prototype.getString = function() {
    return String$(this.key.getString().value + "->" + this.item.getString().value)
}
$Entry.prototype.getKey = function() { return this.key }
$Entry.prototype.getItem = function() { return this.item }
$Entry.prototype.equals = function(other) {
    return Boolean$(other && this.key.equals(other.key) === getTrue() && this.item.equals(other.item) === getTrue());
}
$Entry.prototype.getHash = function() { Integer(this.key.getHash().value ^ this.item.getHash().value) }

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
    for (i = 0; i < seq.value.length; i++) {
        e.push(Entry(Integer(i), seq.value[i]));
    }
    return ArraySequence(e);
}

exports.initType=initType;
exports.inheritProto=inheritProto;
exports.Cloneable=Cloneable; //TODO just to let the compiler finish
exports.Callable=Callable;
exports.Castable=Castable;
exports.Sized=Sized;
exports.Closeable=Closeable;
exports.Container=Container;
exports.Correspondence=Correspondence;
exports.Collection=Collection;
exports.FixedSized=FixedSized;
exports.Some=Some;
exports.Number=Number;
exports.Invertable=Invertable;
exports.Numeric=Numeric;
exports.Ordinal=Ordinal;
exports.Integral=Integral;
exports.Ranged=Ranged;
exports.List=List;
exports.Map=Map;
exports.None=None;
exports.Set=Set;
exports.Equality=Equality; //TODO just to let the compiler finish
exports.Iterable=Iterable; //TODO just to let the compiler finish
exports.Iterator=Iterator; //TODO just to let the compiler finish
exports.Exception=Exception; //TODO just to let the compiler finish
exports.Comparable=Comparable; //TODO just to let the compiler finish
exports.Summable=Summable;
exports.IdentifiableObject=IdentifiableObject;
exports.Object=Object$; //TODO just to let the compiler finish
exports.print=print;
exports.Integer=Integer;
exports.Float=Float;
exports.String=String$;
exports.StringBuilder=StringBuilder;
exports.Boolean=Boolean$;
exports.Character=Character;
exports.Comparison=Comparison;
exports.getNull=getNull;
exports.getTrue=getTrue;
exports.getFalse=getFalse;
exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;
exports.getExhausted=getExhausted;
exports.Sequence=Sequence;
exports.ArraySequence=ArraySequence;
exports.SequenceBuilder=SequenceBuilder;
exports.SequenceAppender=SequenceAppender;
exports.Range=Range;
exports.Singleton=Singleton;
exports.Entry=Entry;
exports.largest=largest;
exports.smallest=smallest;
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
exports.parseInteger=$parseInteger;
exports.parseFloat=$parseFloat;
exports.empty=$empty;
exports.nullsafe=$nullsafe;
exports.getInfinity=getInfinity;
exports.className=className;
exports.emptyIterator=emptyIterator;

    });
}(typeof define==='function' && define.amd ? 
    define : function (factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports, module);
    } else {
        throw "no module loader";
    }
}));
