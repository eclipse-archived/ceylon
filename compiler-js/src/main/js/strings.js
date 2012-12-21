function initType(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function inheritProto(a,b,c,d,e,f,g,h,j,i,k,l);//IGNORE
function ArraySequence(x){}//IGNORE
function Singleton(x){}//IGNORE
function largest(a,b){}//IGNORE
function smallest(a,b){}//IGNORE
var Object$,List,Comparable,Ranged,Summable,Castable,Cloneable,smaller,larger,equal;//IGNORE
var empty,$finished,IdentifiableObject,Iterator,exports;//IGNORE

function String$(value,size) {
    var that = new String(value);
    that.codePoints = size;
    return that;
}
initExistingType(String$, String, 'ceylon.language::String', Object$, Sequential, Comparable,
        Ranged, Summable, Castable, Cloneable);
var origStrToString = String.prototype.toString;
inheritProtoI(String$, Object$, Sequential, Comparable, Ranged, Summable, Castable,
        Cloneable);
function SequenceString() {}
initType(SequenceString, "ceylon.language::SequenceString", String$, Sequence);
function EmptyString() {}
initType(EmptyString, "ceylon.language::EmptyString", String$, Empty);
var String$proto = String$.$$.prototype;
String$proto.getT$name = function() {
    return ((this.length!==0)?SequenceString:EmptyString).$$.T$name;
}
String$proto.getT$all = function() {
    return ((this.length!==0)?SequenceString:EmptyString).$$.T$all;
}
String$proto.toString = origStrToString;
String$proto.getString = function() { return this }
String$proto.plus = function(other) {
    var size = this.codePoints + other.codePoints;
    return String$(this+other, isNaN(size)?undefined:size);
}
String$proto.equals = function(other) { return other.constructor===String && other.valueOf()===this.valueOf(); }
String$proto.compare = function(other) {
    var cmp = this.localeCompare(other);
    return cmp===0 ? equal : (cmp<0 ? smaller:larger);
}
String$proto.getUppercased = function() { return String$(this.toUpperCase()) }
String$proto.getLowercased = function() { return String$(this.toLowerCase()) }
String$proto.getSize = function() {
    if (this.codePoints===undefined) {
        this.codePoints = countCodepoints(this);
    }
    return this.codePoints;
}
String$proto.getLastIndex = function() { return this.getSize().equals(0) ? null : this.getSize().getPredecessor(); }
String$proto.span = function(from, to) {
    if (from > to) {
        return this.segment(to, from-to+1).getReversed();
    }
    return this.segment(from, to-from+1);
}
String$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
String$proto.spanTo = function(to) {
    return to < 0 ? String$('') : this.span(0, to);
}
String$proto.segment = function(from, len) {
    var fromIndex = from;
    var maxCount = len + fromIndex;
    if (fromIndex < 0) {fromIndex = 0;}
    var i1 = 0;
    var count = 0;
    for (; i1<this.length && count<fromIndex; ++i1, ++count) {
        if ((this.charCodeAt(i1)&0xfc00) === 0xd800) {++i1}
    }
    var i2 = i1;
    for (; i2<this.length && count<maxCount; ++i2, ++count) {
        if ((this.charCodeAt(i2)&0xfc00) === 0xd800) {++i2}
    }
    if (i2 >= this.length) {
        this.codePoints = count;
        if (fromIndex === 0) {return this;}
    }
    return String$(this.substring(i1, i2), count-fromIndex);
}
String$proto.getEmpty = function() {
    return this.length===0;
}
String$proto.longerThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints>length}
    if (this.length <= length) {return false}
    if (this.length<<1 > length) {return true}
    this.codePoints = countCodepoints(this);
    return this.codePoints>length;
}
String$proto.shorterThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints<length}
    if (this.length < length) {return true}
    if (this.length<<1 >= length) {return false}
    this.codePoints = countCodepoints(this);
    return this.codePoints<length;
}
String$proto.getIterator = function() {
	return this.length === 0 ? getEmptyIterator() : StringIterator(this);
}
String$proto.item = function(index) {
    if (index<0 || index>=this.length) {return null}
    var i = 0;
    for (var count=0; count<index; count++) {
        if ((this.charCodeAt(i)&0xfc00) === 0xd800) {++i}
        if (++i >= this.length) {return null}
    }
    return Character(codepointFromString(this, i));
}
String$proto.getTrimmed = function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var from = 0;
    while (from<this.length && (this.charCodeAt(from) in $WS)) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && (this.charCodeAt(to) in $WS));
        ++to;
    }
    if (from===0 && to===this.length) {return this}
    var result = String$(this.substring(from, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
}
String$proto.initial = function(length) {
    if (length >= this.codePoints) {return this}
    var count = 0;
    var i = 0;
    for (; i<this.length && count<length; ++i, ++count) {
        if ((this.charCodeAt(i)&0xfc00) === 0xd800) {++i}
    }
    if (i >= this.length) {
        this.codePoints = count;
        return this;
    }
    return String$(this.substr(0, i), count);
}
String$proto.terminal = function(length) {
    if (length >= this.codePoints) {return this}
    var count = 0;
    var i = this.length;
    for (; i>0 && count<length; ++count) {
        if ((this.charCodeAt(--i)&0xfc00) === 0xdc00) {--i}
    }
    if (i <= 0) {
        this.codePoints = count;
        return this;
    }
    return String$(this.substr(i), count);
}
String$proto.getHash = function() {
    if (this._hash === undefined) {
        for (var i = 0; i < this.length; i++) {
          var c = this.charCodeAt(i);
          this._hash += c + (this._hash << 10);
          this._hash ^= this._hash >> 6;
    }

    this._hash += this._hash << 3;
    this._hash ^= this._hash >> 11;
    this._hash += this._hash << 15;
    this._hash = this._hash & ((1 << 29) - 1);
  }
  return this._hash;
}
String$proto.getFirst = function() {
    return this.item(0);
}

function cmpSubString(str, subStr, offset) {
    for (var i=0; i<subStr.length; ++i) {
        if (str.charCodeAt(offset+i)!==subStr.charCodeAt(i)) {return false}
    }
    return true;
}
String$proto.startsWith = function(str) {
    if (str.length > this.length) {return false}
    return cmpSubString(this, str, 0);
}
String$proto.endsWith = function(str) {
    var start = this.length - str.length
    if (start < 0) {return false}
    return cmpSubString(this, str, start);
}
String$proto.contains = function(sub) {
    var str;
    if (sub.constructor === String) {str = sub}
    else if (sub.constructor !== Character.$$) {return false}
    else {str = codepointToString(sub.value)}
    return this.indexOf(str) >= 0;
}
String$proto.getNormalized = function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var result = "";
    var len = 0;
    var first = true;
    var i1 = 0;
    while (i1 < this.length) {
        while (this.charCodeAt(i1) in $WS) {
            if (++i1 >= this.length) {return String$(result)}
        }
        var i2 = i1;
        var cc = this.charCodeAt(i2);
        do {
            ++i2;
            if ((cc&0xfc00) === 0xd800) {++i2}
            ++len;
            cc = this.charCodeAt(i2);
        } while (i2<this.length && !(cc in $WS));
        if (!first) {
            result += " ";
            ++len;
        }
        first = false;
        result += this.substring(i1, i2);
        i1 = i2+1;
    }
    return String$(result, len);
}
String$proto.firstOccurrence = function(sub) {
    if (sub.length == 0) {return 0}
    var bound = this.length - sub.length;
    for (var i=0, count=0; i<=bound; ++count) {
        if (cmpSubString(this, sub, i)) {return count}
        if ((this.charCodeAt(i++)&0xfc00) === 0xd800) {++i}
    }
    return null;
}
String$proto.lastOccurrence = function(sub) {
    if (sub.length == 0) {return this.length>0 ? this.length-1 : 0}
    for (var i=this.length-sub.length; i>=0; --i) {
        if (cmpSubString(this, sub, i)) {
            for (var count=0; i>0; ++count) {
                if ((this.charCodeAt(--i)&0xfc00) === 0xdc00) {--i}
            }
            return count;
        }
    }
    return null;
}
String$proto.firstCharacterOccurrence = function(subc) {
    for (var i=0, count=0; i<this.length; count++) {
        var cp = this.charCodeAt(i++);
        if (((cp&0xfc00) === 0xd800) && i<this.length) {
            cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
        }
        if (cp === subc.value) {return count;}
    }
    this.codePoints = count;
    return null;
}
String$proto.lastCharacterOccurrence = function(subc) {
    for (var i=this.length-1, count=0; i>=0; count++) {
        var cp = this.charCodeAt(i--);
        if (((cp%0xfc00) === 0xdc00) && i>=0) {
           cp = (this.charCodeAt(i--)<<10) + cp - 0x35fdc00;
        }
        if (cp === subc.value) {
            if (this.codePoints === undefined) {this.codePoints = countCodepoints(this);}
            return this.codePoints - count - 1;
        }
    }
    this.codePoints = count;
    return null;
}
String$proto.getCharacters = function() {
    //we can cheat and add the required behavior to String, avoiding the need to create a Sequence...
    //TODO: this probably doesn't work completely because String doesn't satisfy
    //      all required interfaces, so "if(is ...)" will be false when it shouldn't.
    return this.length>0 ? this:empty;
}
String$proto.getFirst = function() { return this.getSize()>0?this.item(0):null; }
String$proto.getLast = function() { return this.getSize()>0?this.item(this.getSize().getPredecessor()):null; }
String$proto.getKeys = function() {
    //TODO implement!!!
    return this.getSize() > 0 ? Range(0, this.getSize().getPredecessor()) : empty;
}
String$proto.join = function(strings) {
    if (strings === undefined) {return String$("", 0)}
    var it = strings.getIterator();
    var str = it.next();
    if (str === $finished) {return String$("", 0);}
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this)}
    var result = str;
    var len = str.codePoints;
    while ((str = it.next()) !== $finished) {
        result += this;
        result += str;
        len += this.codePoints + str.codePoints;
    }
    return String$(result, isNaN(len)?undefined:len);
}
function isWhitespace(c) { return c.value in $WS; }
String$proto.$split = function(sep, discard, group) {
    // shortcut for empty input
    if (this.length === 0) {return Singleton(this);}

    if (sep === undefined) {sep = isWhitespace}
    if (discard === undefined) {discard = true}
    if (group === undefined) {group = true}

    //TODO: return an iterable which determines the next token on demand
    var tokens = [];
    var tokenBegin = 0;
    var tokenBeginCount = 0;
    var count = 0;
    var value = this;
    var separator = true;

    function pushToken(tokenEnd) {
        tokens.push(String$(value.substring(tokenBegin, tokenEnd), count-tokenBeginCount));
    }
    if (isOfType(sep, 'ceylon.language::Iterable')) {
        var sepChars = {}
        var it = sep.getIterator();
        var c; while ((c=it.next()) !== $finished) {sepChars[c.value] = true}
        for (var i=0; i<this.length;) {
            var j = i;
            var cp = this.charCodeAt(i++);
            if ((cp&0xfc00)===0xd800 && i<this.length) {
                cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
            }
            if (cp in sepChars) {
                if (!group) {
                    // ungrouped separator: store preceding token
                    pushToken(j);
                    if (!discard) {
                        // store separator as token
                        tokens.push(String$(this.substring(j, i), 1));
                    }
                    // next token begins after this character
                    tokenBegin = i;
                    tokenBeginCount = count + 1;
                } else if (!separator || (j == 0)) {
                    // begin of grouped separator: store preceding token
                    pushToken(j);
                    // separator token begins at this character
                    tokenBegin = j;
                    tokenBeginCount = count;
                }
                separator = true;

            } else if (separator) {
                // first non-separator after separators or at beginning
                if (!discard && (tokenBegin != j)) {
                    // store preceding grouped separator (if group=false then tokenBegin=j)
                    pushToken(j);
                }
                // non-separator token begins at this character
                tokenBegin = j;
                tokenBeginCount = count;
                separator = false;
            }
        }
        if (tokenBegin != i) {
            pushToken(i);
        }
    } else {
        for (var i=0; i<this.length; ++count) {
            var j = i;
            var cp = this.charCodeAt(i++);
            if ((cp&0xfc00)===0xd800 && i<this.length) {
                cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
            }

            if (sep(Character(cp))) {
                if (!group) {
                    // ungrouped separator: store preceding token
                    pushToken(j);
                    if (!discard) {
                        // store separator as token
                        tokens.push(String$(this.substring(j, i), 1));
                    }
                    // next token begins after this character
                    tokenBegin = i;
                    tokenBeginCount = count + 1;
                } else if (!separator || (j == 0)) {
                    // begin of grouped separator: store preceding token
                    pushToken(j);
                    // separator token begins at this character
                    tokenBegin = j;
                    tokenBeginCount = count;
                }
                separator = true;

            } else if (separator) {
                // first non-separator after separators or at beginning
                if (!discard && (tokenBegin != j)) {
                    // store preceding grouped separator (if group=false then tokenBegin=j)
                    pushToken(j);
                }
                // non-separator token begins at this character
                tokenBegin = j;
                tokenBeginCount = count;
                separator = false;
            }
        }

        if ((tokenBegin != i) && !(separator && discard)) {
            // store preceding token (may be a grouped separator)
            pushToken(i);
        }
        if (separator) {
            // if last character was a separator then there's another empty token
            tokens.push(String$("", 0));
        }
    }

    this.codePoints = count;
    return ArraySequence(tokens);
}
String$proto.getReversed = function() {
    var result = "";
    for (var i=this.length; i>0;) {
        var cc = this.charCodeAt(--i);
        if ((cc&0xfc00)!==0xdc00 || i===0) {
            result += this.charAt(i);
        } else {
            result += this.substr(--i, 2);
        }
    }
    return String$(result);
}
String$proto.$replace = function(sub, repl) {
    return String$(this.replace(new RegExp(sub, 'g'), repl));
}
String$proto.repeat = function(times) {
    var sb = StringBuilder();
    for (var i = 0; i < times; i++) {
        sb.append(this);
    }
    return sb.getString();
}
function isNewline(c) { return c.value===10; }
String$proto.getLines = function() {
    return this.$split(isNewline, true);
}
String$proto.occurrences = function(sub) {
    if (sub.length == 0) {return 0}
    var ocs = [];
    var bound = this.length - sub.length;
    for (var i=0, count=0; i<=bound; ++count) {
        if (cmpSubString(this, sub, i)) {
            ocs.push(count);
            i+=sub.length;
        } else if ((this.charCodeAt(i++)&0xfc00) === 0xd800) {++i;}
    }
    return ocs.length > 0 ? ocs : empty;
}
String$proto.$filter = function(f) {
    var r = Iterable.$$.prototype.$filter.apply(this, [f]);
    return string(r);
}
String$proto.skipping = function(skip) {
    if (skip==0) return this;
    return this.segment(skip, this.getSize());
}
String$proto.taking = function(take) {
    if (take==0) return empty;
    return this.segment(0, take);
}
String$proto.by = function(step) {
    var r = Iterable.$$.prototype.by.apply(this, [step]);
    return string(r);
}
String$proto.$sort = function(f) {
    var r = Iterable.$$.prototype.$sort.apply(this, [f]);
    return string(r);
}
String$proto.getCoalesced = function() { return this; }

function StringIterator(string) {
    var that = new StringIterator.$$;
    that.string = string;
    that.index = 0;
    return that;
}
initTypeProto(StringIterator, 'ceylon.language::StringIterator', IdentifiableObject, Iterator);
var StringIterator$proto = StringIterator.$$.prototype;
StringIterator$proto.next = function() {
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
exports.codepointFromString=codepointFromString;

function Character(value) {
    var that = new Character.$$;
    that.value = value;
    return that;
}
initTypeProto(Character, 'ceylon.language::Character', Object$, Comparable);
var Character$proto = Character.$$.prototype;
Character$proto.getString = function() { return String$(codepointToString(this.value)) }
Character$proto.equals = function(other) {
    return other.constructor===Character.$$ && other.value===this.value;
}
Character$proto.getHash = function() {return this.value}
Character$proto.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
Character$proto.getUppercased = function() {
    var ucstr = codepointToString(this.value).toUpperCase();
    return Character(codepointFromString(ucstr, 0));
}
Character$proto.getLowercased = function() {
    var lcstr = codepointToString(this.value).toLowerCase();
    return Character(codepointFromString(lcstr, 0));
}
Character$proto.getTitlecased = function() {
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
Character$proto.getWhitespace = function() { return this.value in $WS }
Character$proto.getControl = function() { return this.value<32 || this.value===127 }
Character$proto.getDigit = function() {
    var check = this.value & 0xfffffff0;
    if (check in $digit) {
        return (this.value&0xf) <= 9;
    }
    if ((check|6) in $digit) {
        return (this.value&0xf) >= 6;
    }
    return this.value>=0x1d7ce && this.value<=0x1d7ff;
}
Character$proto.getInteger = function() { return this.value; }
Character$proto.getUppercase = function() {
    var str = codepointToString(this.value);
    return str.toLowerCase()!==str && !(this.value in $titlecase);
}
Character$proto.getLowercase = function() {
    var str = codepointToString(this.value);
    return str.toUpperCase()!==str && !(this.value in $titlecase);
}
Character$proto.getTitlecase = function() {return this.value in $titlecase}
Character$proto.getLetter = function() {
    //TODO: this captures only letters that have case
    var str = codepointToString(this.value);
    return str.toUpperCase()!==str || str.toLowerCase()!==str || (this.value in $titlecase);
}
Character$proto.getSuccessor = function() {
    var succ = this.value+1;
    if ((succ&0xf800) === 0xd800) {return Character(0xe000)}
    return Character((succ<=0x10ffff) ? succ:0);
}
Character$proto.getPredecessor = function() {
    var succ = this.value-1;
    if ((succ&0xf800) === 0xd800) {return Character(0xd7ff)}
    return Character((succ>=0) ? succ:0x10ffff);
}
Character$proto.distanceFrom = function(other) {
    return this.value - other.value;
}

function StringBuilder() {
    var that = new StringBuilder.$$;
    that.value = "";
    return that;
}
initTypeProto(StringBuilder, 'ceylon.language::StringBuilder', IdentifiableObject);
var StringBuilder$proto = StringBuilder.$$.prototype;
StringBuilder$proto.getString = function() { return String$(this.value); }
StringBuilder$proto.append = function(s) {
    this.value = this.value + s;
    return this;
}
StringBuilder$proto.appendAll = function(strings) {
    if (strings === null || strings === undefined) { return this; }
    for (var i = 0; i < strings.length; i++) {
        var _s = strings[i];
        this.value += _s?_s:"null";
    }
    return this;
}
StringBuilder$proto.appendCharacter = function(c) {
    this.append(c.getString());
    return this;
}
StringBuilder$proto.appendNewline = function() {
    this.value = this.value + "\n";
    return this;
}
StringBuilder$proto.appendSpace = function() {
    this.value = this.value + " ";
    return this;
}
StringBuilder$proto.getSize = function() {
    return countCodepoints(this.value);
}
StringBuilder$proto.reset = function() {
    this.value = "";
    return this;
}
StringBuilder$proto.insert = function(pos, content) {
    if (pos <= 0) {
        this.value = content + this.value;
    } else if (pos >= this.getSize()) {
        this.value = this.value + content;
    } else {
        this.value = this.value.slice(0, pos) + content + this.value.slice(pos);
    }
    return this;
}
StringBuilder$proto.$delete = function(pos, count) {
    if (pos < 0) pos=0; else if (pos>this.getSize()) return this;
    if (count > 0) {
        this.value = this.value.slice(0, pos) + this.value.slice(pos+count);
    }
    return this;
}

exports.String=String$;
exports.Character=Character;
exports.StringBuilder=StringBuilder;
