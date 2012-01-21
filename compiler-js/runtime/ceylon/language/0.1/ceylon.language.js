(function (define) {
    define('ceylon.language', function (require, exports) {

//the Ceylon language module
function print(line) { console.log(line.getString().value) }

CeylonObject=function CeylonObject() {}

CeylonObject.prototype.getString=function() { String$(Object.prototype.toString.apply(this)) };
CeylonObject.prototype.toString=function() { return this.getString().value };

//TODO: we need to distinguish between Objects and IdentifiableObjects
CeylonObject.prototype.equals = function(other) { return Boolean$(this===other) }

function $Integer() {}
function Integer(value) {
    var that = new $Integer;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$Integer.prototype[$]=CeylonObject.prototype[$]}
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

function $Float() {}
function Float(value) {
    var that = new $Float;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$Float.prototype[$]=CeylonObject.prototype[$]}
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

function $String() {}
function String$(value,size) {
    var that = new $String;
    that.value = value;
    that.codePoints = size;
    return that;
}
for(var $ in CeylonObject.prototype){$String.prototype[$]=CeylonObject.prototype[$]}
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
    var toIndex = to === getNull() ? lastIndex.value : smallest(to, lastIndex).value;
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
    //TODO does this work for unicode, etc?
    return Boolean$(this.value.indexOf(sub.value) >= 0);
}

function $StringIterator() {}
function StringIterator(string) {
    var that = new $StringIterator;
    that.string = string;
    that.index = 0;
    return that;
}
for(var $ in CeylonObject.prototype){$StringIterator.prototype[$]=CeylonObject.prototype[$]}
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

function $Character() {}
function Character(value) {
    var that = new $Character;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$Character.prototype[$]=CeylonObject.prototype[$]}
$Character.prototype.getString = function() { return String$(codepointToString(this.value)) }
$Character.prototype.equals = function(other) { return Boolean$(other && other.value===this.value) }
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
var $WS={}
$WS[0x9]=true;
$WS[0xa]=true;
$WS[0xb]=true;
$WS[0xc]=true;
$WS[0xd]=true;
$WS[0x20]=true;
$WS[0x85]=true;
$WS[0xa0]=true;
$WS[0x1680]=true;
$WS[0x180e]=true;
for (var i=0x2000; i<=0x200a; i++) { $WS[i]=true }
$WS[0x2028]=true;
$WS[0x2029]=true;
$WS[0x202f]=true;
$WS[0x205f]=true;
$WS[0x3000]=true;
$Character.prototype.getWhitespace = function() { return Boolean$(this.value in $WS) }
$Character.prototype.getControl = function() { return Boolean$(this.value<32 || this.value===127) }
$Character.prototype.getDigit = function() { return Boolean$(this.value>=48 && this.value<=57) }
$Character.prototype.getInteger = function() { return Integer(this.value); }
$Character.prototype.getUppercase = function() {
    var str = codepointToString(this.value);
    return Boolean$(str.toLowerCase()!==str);
}
$Character.prototype.getLowercase = function() {
    var str = codepointToString(this.value);
    return Boolean$(str.toUpperCase()!==str);
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

function $StringBuilder() {}
function StringBuilder() {
    var that = new $StringBuilder;
    that.value = "";
    return that;
}
for(var $ in CeylonObject.prototype){$StringBuilder.prototype[$]=CeylonObject.prototype[$]}
$StringBuilder.prototype.getString = function() { return String$(this.value); }
$StringBuilder.prototype.append = function(s) {
    this.value = this.value + s.value;
}
$StringBuilder.prototype.appendAll = function(strings) {
    for (var i = 0; i < strings.value.length; i++) {
        this.value = this.value + strings.value[i].value;
    }
    return this; //strictly speaking, this method should return void, but then string interpolation would be a big mess
}
$StringBuilder.prototype.appendCharacter = function(c) {
    this.append(c.getString());
}
$StringBuilder.prototype.appendNewline = function() { this.value = this.value + "\n"; }
$StringBuilder.prototype.appendSpace = function() { this.value = this.value + " "; }

function $Case() {}
function Case(caseName) {
    var that = new $Case;
    that.string = String$(caseName);
    return that;
}
for(var $ in CeylonObject.prototype){$Case.prototype[$]=CeylonObject.prototype[$]}
$Case.prototype.getString = function() { return this.string }

function getNull() { return null }
var $true = Case("true");
function getTrue() { return $true; }
var $false = Case("false");
function getFalse() { return $false; }
function Boolean$(value) {
    return value ? $true : $false;
}
var $finished = Case("Finished");
function getExhausted() { return $finished; }

//These are operators for handling nulls
function exists(value) { return value === getNull() ? getFalse() : getTrue(); }
function nonempty(value) { return Boolean$(value && value.value && value.value.length > 0); }

var larger = Case("larger");
function getLarger() { return larger }
var smaller = Case("smaller");
function getSmaller() { return smaller }
var equal = Case("equal");
function getEqual() { return equal }
function largest(x, y) { return x.compare(y) === larger ? x : y }
function smallest(x, y) { return x.compare(y) === smaller ? x : y }

function $Sequence() {}
function Sequence($$sequence) {
    // TODO: the following additional definition of getEmpty is necessary for closure style.
    //       We need to find a better solution.
    $$sequence.getEmpty = function() { return $false }
    return $$sequence;
}
$Sequence.prototype.getEmpty = function() { return $false }
$Sequence.prototype.getSize = function() { return Integer(this.getLastIndex()+1) }

function $ArraySequence() {}
function ArraySequence(value) {
    var that = new $ArraySequence;
    that.value = value;
    return that;
}
for(var $ in CeylonObject.prototype){$ArraySequence.prototype[$]=CeylonObject.prototype[$]}
for(var $ in $Sequence.prototype){$ArraySequence.prototype[$]=$Sequence.prototype[$]}
$ArraySequence.prototype.getString = function() { return String$("{" + this.value.toString() +"}") }
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
    var toIndex = to === getNull() ? this.getLastIndex().value : smallest(to, this.getLastIndex()).value;
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
        seq.push(this.item(keys.item(Integer(i))));
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
            if ((mine === null && theirs) || !(mine && mine.equals(theirs) === getTrue())) {
                return getFalse();
            }
        }
        return getTrue();
    }
    return getFalse();
}
$ArraySequence.prototype.getIterator = function() { return ArrayIterator(this.value); }
$ArraySequence.prototype.getKeys = function() { return IntCategory(this); }

function $IntCategory() {}
function IntCategory(seq) {
    var that = new $IntCategory;
    that.seq = seq;
    return that;
}
for(var $ in CeylonObject.prototype){$IntCategory.prototype[$]=CeylonObject.prototype[$]}
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

function $ArrayIterator() {}
function ArrayIterator(arr) {
    var that = new $ArrayIterator;
    that.array = arr;
    that.current = arr && arr.length ? arr[0] : $finished;
    that.idx = 0;
    return that;
}
for(var $ in CeylonObject.prototype){$ArrayIterator.prototype[$]=CeylonObject.prototype[$]}
$ArrayIterator.prototype.next = function() {
    if (this.current === $finished) {
        return $finished;
    }
    this.current = this.idx < this.array.length ? this.array[this.idx] : $finished;
    this.idx++;
    return this.current;
}

function $SequenceBuilder() {}
function SequenceBuilder() {
    var that = new $SequenceBuilder;
    that.seq = [];
    return that;
}
for(var $ in CeylonObject.prototype){$SequenceBuilder.prototype[$]=CeylonObject.prototype[$]}
$SequenceBuilder.prototype.getSequence = function() { return ArraySequence(this.seq); }
$SequenceBuilder.prototype.append = function(e) { this.seq.push(e); }
$SequenceBuilder.prototype.appendAll = function(arr) {
	if (arr && arr.value && arr.value.length)
    for (var i = 0; i < arr.value.length; i++) {
        this.seq.push(arr.value[i]);
    }
}
$SequenceBuilder.prototype.getSize = function() { return Integer(this.seq.length); }
$SequenceBuilder.prototype.getEmpty = function() { return Boolean$(this.seq.length == 0); }

function $SequenceAppender() {}
function SequenceAppender(other) {
	var that = new $SequenceAppender;
	that.seq = [];
	that.appendAll(other);
	return that;
}
for(var $ in CeylonObject.prototype){$SequenceAppender.prototype[$]=CeylonObject.prototype[$]}
for(var $ in $SequenceBuilder.prototype){$SequenceAppender.prototype[$]=$SequenceBuilder.prototype[$]}

function $Range() {}
function Range(first, last) {
    var that = new $Range;
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
for(var $ in CeylonObject.prototype){$Range.prototype[$]=CeylonObject.prototype[$]}
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
$Range.prototype.contains = $Range.prototype.includes;
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
    if (len.compare(Integer(0)) !== larger) return ArraySequence([])
    var x = this.first;
    for (var i=0; i < from.value; i++) { x = this.next(x); }
    //only positive length for now
    var y = x;
    for (var i=1; i < len.value; i++) { y = this.next(y); }
    if (this.includes(y) === getFalse()) { y = this.last; }
    return Range(x, y);
}
$Range.prototype.span = function(from, to) {
    from = largest(Integer(0),from);
    to = to === getNull() ? this.getLastIndex() : smallest(to, this.getLastIndex());
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

function $RangeIterator() {}
function RangeIterator(range) {
    var that = new $RangeIterator;
    that.range = range;
    that.current = range.getFirst();
    return that;
}
for(var $ in CeylonObject.prototype){$RangeIterator.prototype[$]=CeylonObject.prototype[$]}
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

function $Singleton() {}
function Singleton(elem) {
    var that = new $Singleton;
    that.value = [elem];
    that.elem = elem;
    return that;
}
for(var $ in CeylonObject.prototype){$Singleton.prototype[$]=CeylonObject.prototype[$]}
$Singleton.prototype.getString = function() { return String$("{ " + this.elem.getString().value + " }") }
$Singleton.prototype.item = function(index) {
    return index.value===0 ? this.value[0] : null;
}
$Singleton.prototype.getSize = function() { return Integer(1); }
$Singleton.prototype.getLastIndex = function() { return Integer(0); }
$Singleton.prototype.getFirst = function() { return this.elem; }
$Singleton.prototype.getLast = function() { return this.elem; }
$Singleton.prototype.getEmpty = function() { return $false; }
$Singleton.prototype.getRest = function() { return ArraySequence([]); }
$Singleton.prototype.defines = function(idx) { return idx.equals(Integer(0)); }
$Singleton.prototype.getKeys = function() { return IntCategory(this); }
$Singleton.prototype.span = function(from, to) {
	if (to === undefined || to === null) to = from;
    return (from.equals(Integer(0)) === getTrue() || to.equals(Integer(0)) === getTrue()) ? this : ArraySequence([])
}
$Singleton.prototype.segment = function(idx, len) {
    if (idx.equals(Integer(0)) === getTrue() && len.compare(Integer(0)) === larger) {
        return this;
    }
    return ArraySequence([]);
}
$Singleton.prototype.getIterator = function() { return SingletonIterator(this.elem); }

function $SingletonIterator() {}
function SingletonIterator(elem) {
    var that = new $SingletonIterator;
    that.elem = elem;
    that.done = false;
    return that;
}
for(var $ in CeylonObject.prototype){$SingletonIterator.prototype[$]=CeylonObject.prototype[$]}
$SingletonIterator.prototype.next = function() {
    if (this.done) {
        return $finished;
    }
    this.done = true;
    return this.elem;
}

function $Entry() {}
function Entry(key, item) {
    var that = new $Entry;
    that.key = key;
    that.item = item;
    return that;
}
for(var $ in CeylonObject.prototype){$Entry.prototype[$]=CeylonObject.prototype[$]}
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

exports.print=print;
exports.Integer=Integer;
exports.Float=Float;
exports.String=String$;
exports.StringBuilder=StringBuilder;
exports.Boolean=Boolean$;
exports.Character=Character;
exports.getNull=getNull;
exports.Case=Case;
exports.getTrue=getTrue;
exports.getFalse=getFalse;
exports.getLarger=getLarger;
exports.getSmaller=getSmaller;
exports.getEqual=getEqual;
exports.getExhausted=getExhausted;
exports.Sequence=Sequence;
exports.$Sequence=$Sequence;
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
exports.parseInteger=$parseInteger;
exports.parseFloat=$parseFloat;

    });
}(typeof define==='function' && define.amd ? 
    define : function (id, factory) {
    if (typeof exports!=='undefined') {
        factory(require, exports);
    } else {
        throw "no module loader";
    }
}));
