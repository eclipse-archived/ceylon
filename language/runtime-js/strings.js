function String$(/*{Character*}*/value,size) {
    if (value && value.getT$name && value.getT$name() == 'ceylon.language::String') {
        //if it's already a String just return it
        return value;
    }
    else if (typeof(value) === 'string') {
        var that = new String(value);
        that.codePoints = size;
        return that;
    }
    var _sb = StringBuilder();
    var _iter = value.iterator();
    var _c; while ((_c = _iter.next()) !== getFinished()) {
        _sb.appendCharacter(_c);
    }
    var that = _sb.string;
    if (size !== undefined) that.codePoints=size;
    return that;
}
String$.$$metamodel$$=function(){return{'super':{t:Object$},
  $ps:[{$nm:'characters',$t:{t:Iterable,a:{Element:{t:Character},Absent:{t:Null}}},$mt:'prm'}],
  'satisfies':[{t:List,a:{Element:{t:Character}}}, {t:Comparable,a:{Other:{t:String$}}},
    {t:Summable,a:{Other:{t:String$}}}, {t:Ranged,a:{Index:{t:Integer},Span:{t:String$}}}],
  $an:function(){return[shared(),native(),final()];},
  mod:$$METAMODEL$$,d:['ceylon.language','String']};};

initExistingType(String$, String, 'ceylon.language::String', Object$, Sequential, Comparable,
        Ranged, Summable);
var origStrToString = String.prototype.toString;
inheritProto(String$, Object$, Sequential, Comparable, Ranged, Summable);
var String$proto = String$.$$.prototype;
String$proto.$$targs$$={Element:{t:Character}, Absent:{t:Null}};
String$proto.getT$name = function() {
    return String$.$$.T$name;
}
String$proto.getT$all = function() {
    return String$.$$.T$all;
}
String$proto.toString = origStrToString;
defineAttr(String$proto, 'string', function(){ return this; },undefined,function(){return{mod:$$METAMODEL$$,
  $t:{t:String$},d:['ceylon.language','Object','$at','string']}});
String$proto.plus = function(other) {
    var size = this.codePoints + other.codePoints;
    return String$(this+other, isNaN(size)?undefined:size);
}
String$proto.plus.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','plus'],
  $ps:[{$nm:'other',$t:{t:String$},$mt:'prm'}]};}
String$proto.equals = function(other) {
    if (other.constructor===String) {
        return other.valueOf()===this.valueOf();
    } else if (isOfType(other, {t:Iterable, a:{Element:{t:Character}}})) {
        if (other.size===this.size) {
            for (var i=0;i<this.size;i++) {
                if (!this.$get(i).equals(other.$get(i))) return false;
            }
            return true;
        }
    }
    return false;
}
String$proto.equals.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','String','$m','equals'],
  $ps:[{$nm:'other',$t:{t:String$},$mt:'prm'}]};
String$proto.compare = function(other) {
    var cmp = this.localeCompare(other);
    return cmp===0 ? equal : (cmp<0 ? smaller:larger);
}
String$proto.compare.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Comparison},d:['ceylon.language','String','$m','compare'],
  $ps:[{$nm:'other',$t:{t:String$}}]};
defineAttr(String$proto, 'uppercased', function(){ return String$(this.toUpperCase()); },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$at','uppercased']}});
defineAttr(String$proto, 'lowercased', function(){ return String$(this.toLowerCase()); },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$at','lowercased']}});
defineAttr(String$proto, 'size', function() {
  if (this.codePoints===undefined) {
    this.codePoints = countCodepoints(this);
  }
  return this.codePoints;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Iterable','$at','size']}});
defineAttr(String$proto, 'lastIndex', function(){ return this.size.equals(0) ? null : this.size.predecessor; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','List','$at','lastIndex']}});
String$proto.span = function(from, to) {
    if (from > to) {
        return this.segment(to, from-to+1).reversed;
    }
    return this.segment(from, to-from+1);
}
String$proto.span.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','span'],
  $ps:[{$nm:'from',$t:{t:Integer}},{$nm:'to',$t:{t:Integer}}]};
String$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
String$proto.spanFrom.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','spanFrom'],
  $ps:[{$nm:'from',$t:{t:Integer}}]};
String$proto.spanTo = function(to) {
    return to < 0 ? String$('', 0) : this.span(0, to);
}
String$proto.spanTo.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','spanTo'],
  $ps:[{$nm:'to',$t:{t:Integer}}]};
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
String$proto.segment.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','segment'],
  $ps:[{$nm:'from',$t:{t:Integer}},{$nm:'length',$t:{t:Integer}}]};
defineAttr(String$proto, 'empty', function() {
    return this.length===0;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Sequential','$at','empty']}});
String$proto.longerThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints>length}
    if (this.length <= length) {return false}
    if (this.length<<1 > length) {return true}
    this.codePoints = countCodepoints(this);
    return this.codePoints>length;
}
String$proto.longerThan.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','String','$m','longerThan'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};
String$proto.shorterThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints<length}
    if (this.length < length) {return true}
    if (this.length<<1 >= length) {return false}
    this.codePoints = countCodepoints(this);
    return this.codePoints<length;
}
String$proto.shorterThan.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','String','$m','shorterThan'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};
String$proto.iterator= function() {
	return this.length === 0 ? getEmptyIterator() : StringIterator(this);
}
String$proto.iterator.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Iterator,a:{Element:{t:Character}}},d:['ceylon.language','String','$m','iterator']};
String$proto.$get = function(index) {
    if (index<0 || index>=this.length) {return null}
    var i = 0;
    for (var count=0; count<index; count++) {
        if ((this.charCodeAt(i)&0xfc00) === 0xd800) {++i}
        if (++i >= this.length) {return null}
    }
    return Character(codepointFromString(this, i));
}
String$proto.$get.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['ceylon.language','String','$m','get'],
  $ps:[{$nm:'index',$t:{t:Integer}}]};
defineAttr(String$proto, 'trimmed', function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var from = 0;
    while (from<this.length && (this.charCodeAt(from) in $WS)) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && (this.charCodeAt(to) in $WS));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = String$(this.substring(from, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$at','trimmed']}});
String$proto.trim = function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$get(from))) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && chars(this.$get(to)));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = String$(this.substring(from, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
}
String$proto.trim.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','trim']};
String$proto.trimLeading = function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$get(from))) {++from}
    if (from===0) {return this;}
    var result = String$(this.substring(from, this.length));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from;
    }
    return result;
}
String$proto.trimLeading.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','trimLeading']};
String$proto.trimTrailing = function(/*Category*/chars) {
    var to = this.length;
    if (to > 0) {
        do {--to} while (to>=0 && chars(this.$get(to)));
        ++to;
    }
    if (to===this.length) {return this;}
    else if (to===0) { return String$("",0); }
    var result = String$(this.substring(0, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - this.length + to;
    }
    return result;
}
String$proto.trimTrailing.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','trimTrailing']};

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
String$proto.initial.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','initial'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};
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
String$proto.terminal.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','terminal'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};
defineAttr(String$proto, 'hash', function() {
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
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Object','$at','hash']}});

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
String$proto.startsWith.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','String','$m','startsWith'],
  $ps:[{$nm:'string',$t:{t:String$}}]};
String$proto.endsWith = function(str) {
    var start = this.length - str.length
    if (start < 0) {return false}
    return cmpSubString(this, str, start);
}
String$proto.endsWith.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','String','$m','endsWith'],
  $ps:[{$nm:'string',$t:{t:String$}}]};
String$proto.contains = function(sub) {
    var str;
    if (sub.constructor === String) {str = sub}
    else if (sub.constructor !== Character.$$) {return false}
    else {str = codepointToString(sub.value)}
    return this.indexOf(str) >= 0;
}
String$proto.contains.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','String','$m','contains'],
  $ps:[{$nm:'substring',$t:{t:String$}}]};
defineAttr(String$proto, 'normalized', function() {
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
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$at','normalized']}});
String$proto.firstOccurrence = function(subc) {
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
String$proto.firstOccurrence.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:'u',l:[{t:Null},{t:Integer}]},d:['ceylon.language','String','$m','firstOccurrence']};
String$proto.lastOccurrence = function(subc) {
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
String$proto.lastOccurrence.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:'u',l:[{t:Null},{t:Integer}]},d:['ceylon.language','String','$m','lastOccurrence']};
defineAttr(String$proto, 'characters', function() {
    return this.size>0 ? this:getEmpty();
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Iterable,a:{Absent:{t:Null},Element:{t:Character}}},d:['ceylon.language','String','$at','characters']}});
defineAttr(String$proto, 'first', function() { return this.$get(0); },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['ceylon.language','Iterable','$at','first']}});
defineAttr(String$proto, 'last', function(){ return this.size>0?this.$get(this.size.predecessor):null; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['ceylon.language','Iterable','$at','last']}});
defineAttr(String$proto, 'keys', function() {
    //TODO implement!!!
    return this.size > 0 ? Range(0, this.size.predecessor, {Element:{t:Integer}}) : getEmpty();
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Category},d:['ceylon.language','Category','$at','keys']}});
String$proto.join = function(strings) {
    var it = strings.iterator();
    var str = it.next();
    if (str === getFinished()) {return String$("", 0);}
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this)}
    var result = str;
    var len = str.codePoints;
    while ((str = it.next()) !== getFinished()) {
        result += this;
        result += str;
        len += this.codePoints + str.codePoints;
    }
    return String$(result, isNaN(len)?undefined:len);
}
String$proto.join.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','join'],
  $ps:[{$nm:'strings',$t:{t:Iterable,a:{Absent:{t:Null},Element:{t:String$}}}}]};
function isWhitespace(c) { return c.value in $WS; }
String$proto.$split = function(sep, discard, group) {
    // shortcut for empty input
    if (this.length === 0) {return Singleton(this, {Element:{t:String$}}); }

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
    if (isOfType(sep, {t:Iterable})) {
        var sepChars = {}
        var it = sep.iterator();
        var c; while ((c=it.next()) !== getFinished()) {sepChars[c.value] = true}
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
    return ArraySequence(tokens, {Element:{t:String$}});
}
String$proto.$split.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Iterable,a:{Absent:{t:Null},Element:{t:String$}}},d:['ceylon.language','String','$m','split']};
defineAttr(String$proto, 'reversed', function() {
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
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$at','reversed']}});
String$proto.$replace = function(sub, repl) {
    return String$(this.replace(new RegExp(sub, 'g'), repl));
}
String$proto.$replace.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','replace'],
  $ps:[{$nm:'substring',$t:{t:String$}},{$nm:'replacement',$t:{t:String$}}]};
String$proto.repeat = function(times) {
    var sb = StringBuilder();
    for (var i = 0; i < times; i++) {
        sb.append(this);
    }
    return sb.string;
}
String$proto.repeat.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','repeat'],
  $ps:[{$nm:'times',$t:{t:Integer}}]};
function isNewline(c) { return c.value===10; }
defineAttr(String$proto, 'lines', function() {
    return this.$split(isNewline, true);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Iterable,a:{Absent:{t:Null},Element:{t:String$}}},d:['ceylon.language','String','$at','lines']}});
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
    return ocs.length > 0 ? ocs : getEmpty();
}
String$proto.occurrences.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Iterable,a:{Absent:{t:Null},Element:{t:Integer}}},d:['ceylon.language','List','$m','occurrences']};
String$proto.$filter = function(f) {
    var r = Iterable.$$.prototype.$filter.call(this, f);
    return String$(r);
}
String$proto.following=function(o,$mpt) {
  return Iterable.$$.prototype.following.call(this,o,$mpt);
}
String$proto.$filter.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','filter']};
String$proto.skipping = function(skip) {
    if (skip==0) return this;
    return this.segment(skip, this.size);
}
String$proto.skipping.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','skipping'],
  $ps:[{$nm:'skip',$t:{t:Integer}}]};
String$proto.taking = function(take) {
    if (take==0) return getEmpty();
    return this.segment(0, take);
}
String$proto.taking.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','taking'],
  $ps:[{$nm:'take',$t:{t:Integer}}]};
String$proto.by = function(step) {
    var r = Iterable.$$.prototype.by.call(this, step);
    return String$(r);
}
String$proto.by.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','by'],
  $ps:[{$nm:'step',$t:{t:Integer}}]};
String$proto.$sort = function(f) {
    var r = Iterable.$$.prototype.$sort.call(this, f);
    return String$(r);
}
String$proto.$sort.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$m','sort']};
defineAttr(String$proto, 'coalesced', function(){ return this; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','String','$at','coalesced']}});

function StringIterator(string) {
    var that = new StringIterator.$$;
    that.str = string;
    that.index = 0;
    return that;
}
StringIterator.$$metamodel$$=function(){return{$nm:'StringIterator',$mt:'cls',$ps:[{$nm:'string',$t:{t:String$}}],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','Iterator']}};

initTypeProto(StringIterator, 'ceylon.language::StringIterator', $init$Basic(), Iterator);
var StringIterator$proto = StringIterator.$$.prototype;
StringIterator$proto.$$targs$$={Element:{t:Character}, Absent:{t:Null}};
StringIterator$proto.next = function() {
    if (this.index >= this.str.length) { return getFinished(); }
    var first = this.str.charCodeAt(this.index++);
    if ((first&0xfc00) !== 0xd800 || this.index >= this.str.length) {
        return Character(first);
    }
    return Character((first<<10) + this.str.charCodeAt(this.index++) - 0x35fdc00);
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
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Character') {
        return value;
    }
    var that = new Character.$$;
    that.value = value;
    return that;
}
Character.$$metamodel$$={'super':{t:Object$}, 'satisfies':[
    {t:Comparable,a:{Other:{t:Character}}}, {t:Enumerable,a:{Other:{t:Character}}}],
  $an:function(){return[shared(),native(),final()];},mod:$$METAMODEL$$,d:['ceylon.language','Character']};

initTypeProto(Character, 'ceylon.language::Character', Object$, Comparable, $init$Enumerable());
var Character$proto = Character.$$.prototype;
defineAttr(Character$proto, 'string', function(){ return String$(codepointToString(this.value)); },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','Character','$at','string']}});
Character$proto.equals = function(other) {
    return other.constructor===Character.$$ && other.value===this.value;
}
defineAttr(Character$proto, 'hash', function(){ return this.value; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Character','$at','hash']}});
Character$proto.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
defineAttr(Character$proto, 'uppercased', function() {
    var ucstr = codepointToString(this.value).toUpperCase();
    return Character(codepointFromString(ucstr, 0));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Character},d:['ceylon.language','Character','$at','uppercased']}});
defineAttr(Character$proto, 'lowercased', function() {
    var lcstr = codepointToString(this.value).toLowerCase();
    return Character(codepointFromString(lcstr, 0));
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Character},d:['ceylon.language','Character','$at','lowercased']}});
defineAttr(Character$proto, 'titlecased', function() {
    var tc = $toTitlecase[this.value];
    return tc===undefined ? this.uppercased : Character(tc);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Character},d:['ceylon.language','Character','$at','titlecased']}});
var $WS={0x9:true, 0xa:true, 0xb:true, 0xc:true, 0xd:true, 0x20:true, 0x85:true,
    0x1680:true, 0x180e:true, 0x2028:true, 0x2029:true, 0x205f:true, 0x3000:true,
    0x1c:true, 0x1d:true, 0x1e:true, 0x1f:true};
for (var i=0x2000; i<0x2007; i++) { $WS[i]=true; }
for (var i=0x2008; i<=0x200a; i++) { $WS[i]=true; }
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
defineAttr(Character$proto, 'whitespace', function(){ return this.value in $WS; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','whitespace']}});
defineAttr(Character$proto, 'control', function(){ return this.value<32 || this.value===127; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','control']}});
defineAttr(Character$proto, 'digit', function() {
    var check = this.value & 0xfffffff0;
    if (check in $digit) {
        return (this.value&0xf) <= 9;
    }
    if ((check|6) in $digit) {
        return (this.value&0xf) >= 6;
    }
    return this.value>=0x1d7ce && this.value<=0x1d7ff;
},undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','digit']}});
defineAttr(Character$proto, 'integerValue', function(){ return this.value; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Enumerable','$at','integerValue']}});
defineAttr(Character$proto, 'integer',function(){ return this.value; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','Character','$at','integer']}});
defineAttr(Character$proto, 'uppercase', function() {
    var str = codepointToString(this.value);
    return str.toLowerCase()!==str && !(this.value in $titlecase);
},undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','uppercase']}});
defineAttr(Character$proto, 'lowercase', function() {
    var str = codepointToString(this.value);
    return str.toUpperCase()!==str && !(this.value in $titlecase);
},undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','lowercase']}});
defineAttr(Character$proto, 'titlecase', function(){ return this.value in $titlecase; },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','titlecase']}});
defineAttr(Character$proto, 'letter', function() {
    //TODO: this captures only letters that have case
    var str = codepointToString(this.value);
    return str.toUpperCase()!==str || str.toLowerCase()!==str || (this.value in $titlecase);
},undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:Boolean$},d:['ceylon.language','Character','$at','letter']}});
defineAttr(Character$proto, 'successor', function() {
    var succ = this.value+1;
    if ((succ&0xf800) === 0xd800) {return Character(0xe000)}
    return Character((succ<=0x10ffff) ? succ:0);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Character},d:['ceylon.language','Ordinal','$at','successor']}});
defineAttr(Character$proto, 'predecessor', function() {
    var succ = this.value-1;
    if ((succ&0xf800) === 0xd800) {return Character(0xd7ff)}
    return Character((succ>=0) ? succ:0x10ffff);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Character},d:['ceylon.language','Ordinal','$at','predecessor']}});
Character$proto.distanceFrom = function(other) {
    return this.value - other.value;
}

function StringBuilder(/*String...*/comps) {
    var that = new StringBuilder.$$;
    that.value = "";
    if (comps !== undefined) {
        that.appendAll(comps);
    }
    return that;
}
StringBuilder.$$metamodel$$={'super':{t:Basic},$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','StringBuilder']};

initTypeProto(StringBuilder, 'ceylon.language::StringBuilder', $init$Basic());
var StringBuilder$proto = StringBuilder.$$.prototype;
defineAttr(StringBuilder$proto, 'string', function(){ return String$(this.value); },undefined,function(){return{
  mod:$$METAMODEL$$,$t:{t:String$},d:['ceylon.language','StringBuilder','$at','string']}});
StringBuilder$proto.append = function(s) {
    this.value = this.value + s;
    return this;
}
StringBuilder$proto.appendAll = function(strings) {
    var iter = strings.iterator();
    var _s; while ((_s = iter.next()) !== getFinished()) {
        this.value += _s?_s:"null";
    }
    return this;
}
StringBuilder$proto.appendCharacter = function(c) {
    this.append(c.string);
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
defineAttr(StringBuilder$proto, 'size', function() {
    return countCodepoints(this.value);
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},d:['ceylon.language','StringBuilder','$at','size']}});
StringBuilder$proto.reset = function() {
    this.value = "";
    return this;
}
StringBuilder$proto.insert = function(pos, content) {
    if (pos <= 0) {
        this.value = content + this.value;
    } else if (pos >= this.size) {
        this.value = this.value + content;
    } else {
        this.value = this.value.slice(0, pos) + content + this.value.slice(pos);
    }
    return this;
}
StringBuilder$proto.insertCharacter = function(pos, c) {
    if (pos <= 0) {
        this.value = c.string + this.value;
    } else if (pos >= this.size) {
        this.value = this.value + c.string;
    } else {
        this.value = this.value.slice(0, pos) + c.string + this.value.slice(pos);
    }
    return this;
}
StringBuilder$proto.$delete = function(pos, count) {
    if (pos < 0) pos=0; else if (pos>this.size) return this;
    if (count > 0) {
        this.value = this.value.slice(0, pos) + this.value.slice(pos+count);
    }
    return this;
}

exports.String=String$;
exports.Character=Character;
exports.StringBuilder=StringBuilder;
