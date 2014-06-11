function $_String(/*{Character*}*/value,size) {
    if (value && value.getT$name && value.getT$name() == 'ceylon.language::String') {
        //if it's already a String just return it
        return value;
    }
    else if (typeof(value) === 'string') {
        var that = new String(value);
        that.codePoints = size;
        return that;
    }
    var that = '';
    var _iter = value.iterator();
    var _c; while ((_c = _iter.next()) !== getFinished()) {
        that += _c.string;
    }
    if (size !== undefined) that.codePoints=size;
    return that;
}
$_String.$crtmm$=function(){return{'super':{t:$_Object},
  $ps:[{$nm:'characters',$t:{t:Iterable,a:{Element$Iterable:{t:Character},Absent$Iterable:{t:Null}}},$mt:'prm'}],
  'satisfies':[{t:List,a:{Element$List:{t:Character}}}, {t:Comparable,a:{Other$Comparable:{t:$_String}}},
    {t:Summable,a:{Other$Summable:{t:$_String}}}, {t:Ranged,a:{Index$Ranged:{t:Integer},Span$Ranged:{t:$_String}}}],
  $an:function(){return[shared(),$_native(),$_final()];},
  mod:$CCMM$,d:['$','String']};};

initExistingType($_String, String, 'ceylon.language::String', $_Object, List, Comparable,
        Ranged, Summable);
var origStrToString = String.prototype.toString;
inheritProto($_String, $_Object, Sequential, Comparable, Ranged, Summable);
var str$proto = $_String.$$.prototype;
str$proto.$$targs$$={Element$Iterable:{t:Character}, Absent$Iterable:{t:Null},
  Element$List:{t:Character}, Other$Summable:{t:$_String}, Other$Comparable:{t:$_String},
  Index$Ranged:{t:Integer}, Element$Ranged:{t:Character}, Span$Ranged:{t:$_String},
  Element$Collection:{t:Character}, Key$Correspondence:{t:Integer}, Item$Correspondence:{t:Character}};
str$proto.getT$name = function() {
    return $_String.$$.T$name;
}
str$proto.getT$all = function() {
    return $_String.$$.T$all;
}
str$proto.toString = origStrToString;
atr$(str$proto, 'string', function(){ return this; },undefined,function(){return{mod:$CCMM$,
  $t:{t:$_String},d:['$','Object','$at','string']}});
str$proto.plus = function(other) {
    var size = this.codePoints + other.codePoints;
    return $_String(this+other, isNaN(size)?undefined:size);
}
str$proto.plus.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','plus'],
  $ps:[{$nm:'other',$t:{t:$_String},$mt:'prm'}]};}
str$proto.equals = function(other) {
    if (other.constructor===String) {
        return other.valueOf()===this.valueOf();
    } else if (is$(other, {t:$_String})) {
        if (other.size===this.size) {
            var oi=other.iterator();
            var ti=this.iterator();
            var oc=oi.next(); var tc;
            while((tc=ti.next())!=getFinished()){
                if (!tc.equals(oc))return false;
                oc=oi.next();
            }
            return true;
        }
    }
    return false;
}
str$proto.equals.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','String','$m','equals'],
  $ps:[{$nm:'other',$t:{t:$_String},$mt:'prm'}]};}
str$proto.compare = function(other) {
    var cmp = this.localeCompare(other);
    return cmp===0 ? getEqual() : (cmp<0 ? getSmaller():getLarger());
}
str$proto.compare.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Comparison},d:['$','String','$m','compare'],
  $ps:[{$nm:'other',$t:{t:$_String}}]};}
str$proto.smallerThan=function(o){
  return Comparable.$$.prototype.smallerThan.call(this,o);
}
str$proto.smallerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},
  $ps:[{$nm:'Other',$t:{t:$_String},$mt:'prm'}],d:['$','String','$m','smallerThan']};};
str$proto.largerThan=function(o){
  return Comparable.$$.prototype.largerThan.call(this,o);
}
str$proto.largerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},
  $ps:[{$nm:'Other',$t:{t:$_String},$mt:'prm'}],d:['$','String','$m','largerThan']};};
str$proto.notSmallerThan=function(o){
  return Comparable.$$.prototype.notSmallerThan.call(this,o);
}
str$proto.notSmallerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},
  $ps:[{$nm:'Other',$t:{t:$_String},$mt:'prm'}],d:['$','String','$m','notSmallerThan']};};
str$proto.notLargerThan=function(o){
  return Comparable.$$.prototype.notLargerThan.call(this,o);
}
str$proto.notLargerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},
  $ps:[{$nm:'Other',$t:{t:$_String},$mt:'prm'}],d:['$','String','$m','notLargerThan']};};
str$proto.sequence=function(){
  return this.length===0?getEmpty():ArraySequence(this,{Element$ArraySequence:{t:Character}});
}
str$proto.sequence.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,
  a:{Element$Sequential:{t:Character}}},$cont:$_String,d:['$','String','$m','sequence'],
  $an:function(){return[actual(),shared()]}};};
atr$(str$proto, 'uppercased', function(){ return $_String(this.toUpperCase()); },undefined,function(){return{
  mod:$CCMM$,$t:{t:$_String},d:['$','String','$at','uppercased']}});
atr$(str$proto, 'lowercased', function(){ return $_String(this.toLowerCase()); },undefined,function(){return{
  mod:$CCMM$,$t:{t:$_String},d:['$','String','$at','lowercased']}});
atr$(str$proto, 'size', function() {
  if (this.codePoints===undefined) {
    this.codePoints = countCodepoints(this);
  }
  return this.codePoints;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},d:['$','Iterable','$at','size']}});
atr$(str$proto, 'lastIndex', function(){ return this.size.equals(0) ? null : this.size.predecessor; },undefined,function(){return{
  mod:$CCMM$,$t:{t:Integer},d:['$','List','$at','lastIndex']}});
atr$(str$proto, 'rest', function(){
  return $_String(this.Rest$List(1));
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$at','rest']}});
str$proto.span = function(from, to) {
    if (from > to) {
        return this.segment(to, from-to+1).reversed;
    }
    return this.segment(from, to-from+1);
}
str$proto.span.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','span'],
  $ps:[{$nm:'from',$t:{t:Integer}},{$nm:'to',$t:{t:Integer}}]};}
str$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
str$proto.spanFrom.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','spanFrom'],
  $ps:[{$nm:'from',$t:{t:Integer}}]};}
str$proto.spanTo = function(to) {
    return to < 0 ? '' : this.span(0, to);
}
str$proto.spanTo.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','spanTo'],
  $ps:[{$nm:'to',$t:{t:Integer}}]};}
str$proto.segment = function(from, len) {
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
    return $_String(this.substring(i1, i2), count-fromIndex);
}
str$proto.segment.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','segment'],
  $ps:[{$nm:'from',$t:{t:Integer}},{$nm:'length',$t:{t:Integer}}]};}
atr$(str$proto, 'empty', function() {
    return this.length===0;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','Sequential','$at','empty']}});
str$proto.longerThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints>length}
    if (this.length <= length) {return false}
    if (this.length<<1 > length) {return true}
    this.codePoints = countCodepoints(this);
    return this.codePoints>length;
}
str$proto.longerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','String','$m','longerThan'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
str$proto.shorterThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints<length}
    if (this.length < length) {return true}
    if (this.length<<1 >= length) {return false}
    this.codePoints = countCodepoints(this);
    return this.codePoints<length;
}
str$proto.shorterThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','String','$m','shorterThan'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
str$proto.iterator= function() {
	return this.length === 0 ? getEmptyIterator() : StringIterator(this);
}
str$proto.iterator.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterator,a:{Element$Iterator:{t:Character}}},d:['$','String','$m','iterator']};}
str$proto.elementAt=function(index){
  if (index<0 || index>=this.length) {return getFinished();}
  if (this._bumps===undefined)this._bumps=[];
  var cnt=0;
  var mb=0;
  for (var i=0;i<this._bumps.length;i++) {
    mb=this._bumps[i];
    if (mb<index)cnt++;
  }
  if (index<=this._maxidx) {
    index+=cnt;
    return Character(codepointFromString(this, index));
  }
  if (this._maxidx>mb)mb=this._maxidx;
  for (cnt=mb; cnt<index; cnt++) {
    if ((this.charCodeAt(mb)&0xfc00) === 0xd800) {
      this._bumps.push(mb);
      ++mb;
    }
    if (++mb >= this.length) {return getFinished();}
  }
  if (this._maxidx===undefined || mb>this._maxidx)this._maxidx=mb;
  return Character(codepointFromString(this, mb));
}
str$proto.elementAt.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['$','String','$m','get'],
  $ps:[{$nm:'index',$t:{t:Integer}}]};}
atr$(str$proto, 'trimmed', function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var from = 0;
    while (from<this.length && (this.charCodeAt(from) in Character.WS$)) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && (this.charCodeAt(to) in Character.WS$));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = this.substring(from, to);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$at','trimmed']}});
str$proto.trim = function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$_get(from))) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && chars(this.$_get(to)));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = this.substring(from, to);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
}
str$proto.trim.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','trim']};}
str$proto.trimLeading = function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$_get(from))) {++from}
    if (from===0) {return this;}
    var result = this.substring(from, this.length);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from;
    }
    return result;
}
str$proto.trimLeading.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','trimLeading']};}
str$proto.trimTrailing = function(/*Category*/chars) {
    var to = this.length;
    if (to > 0) {
        do {--to} while (to>=0 && chars(this.$_get(to)));
        ++to;
    }
    if (to===this.length) {return this;}
    else if (to===0) { return ""; }
    var result = this.substring(0, to);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - this.length + to;
    }
    return result;
}
str$proto.trimTrailing.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','trimTrailing']};}

str$proto.initial = function(length) {
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
    return $_String(this.substr(0, i), count);
}
str$proto.initial.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','initial'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
str$proto.terminal = function(length) {
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
    return $_String(this.substr(i), count);
}
str$proto.terminal.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','terminal'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
atr$(str$proto, 'hash', function() {
  if (this._hash === undefined) {
    var h=0;
    for (var i = 0; i < this.length; i++) {
      var c = this.charCodeAt(i);
      h=(31*h+c)&0xffffffff;
    }
    this._hash=h;
  }
  return this._hash;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},$an:function(){return[shared(),actual()];},d:['$','Object','$at','hash']}});

function cmpSubString(str, subStr, offset) {
    for (var i=0; i<subStr.length; ++i) {
        if (str.charCodeAt(offset+i)!==subStr.charCodeAt(i)) {return false}
    }
    return true;
}
str$proto.startsWith = function(str) {
    if (str.length > this.length) {return false}
    return cmpSubString(this, str, 0);
}
str$proto.startsWith.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','String','$m','startsWith'],
  $ps:[{$nm:'string',$t:{t:$_String}}]};}
str$proto.endsWith = function(str) {
    var start = this.length - str.length
    if (start < 0) {return false}
    return cmpSubString(this, str, start);
}
str$proto.endsWith.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','String','$m','endsWith'],
  $ps:[{$nm:'string',$t:{t:$_String}}]};}
str$proto.contains = function(sub) {
    var str;
    if (sub.constructor === String) {str = sub}
    else if (sub.constructor !== Character.$$) {return false}
    else {str = codepointToString(sub.value)}
    return this.indexOf(str) >= 0;
}
str$proto.contains.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},d:['$','String','$m','contains'],
  $ps:[{$nm:'substring',$t:{t:$_String}}]};}
atr$(str$proto, 'normalized', function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var result = "";
    var len = 0;
    var first = true;
    var i1 = 0;
    while (i1 < this.length) {
        while (this.charCodeAt(i1) in Character.WS$) {
            if (++i1 >= this.length) {return result;}
        }
        var i2 = i1;
        var cc = this.charCodeAt(i2);
        do {
            ++i2;
            if ((cc&0xfc00) === 0xd800) {++i2}
            ++len;
            cc = this.charCodeAt(i2);
        } while (i2<this.length && !(cc in Character.WS$));
        if (!first) {
            result += " ";
            ++len;
        }
        first = false;
        result += this.substring(i1, i2);
        i1 = i2+1;
    }
    return $_String(result, len);
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$at','normalized']}});
str$proto.firstOccurrence = function(subc) {
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
str$proto.firstOccurrence.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Integer}]},d:['$','String','$m','firstOccurrence']};}
str$proto.lastOccurrence = function(subc) {
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
str$proto.lastOccurrence.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Integer}]},d:['$','String','$m','lastOccurrence']};}
atr$(str$proto, 'first', function() { return this.$_get(0); },undefined,function(){return{
  mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['$','Iterable','$at','first']}});
atr$(str$proto, 'last', function(){ return this.size>0?this.$_get(this.size.predecessor):null; },undefined,function(){return{
  mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['$','Iterable','$at','last']}});
atr$(str$proto, 'keys', function() {
    return this.size > 0 ? Range(0, this.size.predecessor, {Element$Range:{t:Integer}}) : getEmpty();
},undefined,function(){return{mod:$CCMM$,$t:{t:Category},d:['$','Correspondence','$at','keys']}});
str$proto.join = function(objects) {
    var it = objects.iterator();
    var obj = it.next();
    if (obj === getFinished()) {return "";}
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this)}
    var str = obj.string;
    var result = str;
    var len = str.codePoints;
    while ((obj = it.next()) !== getFinished()) {
        result += this;
        str = obj.string;
        result += str;
        len += this.codePoints + str.codePoints;
    }
    return $_String(result, isNaN(len)?undefined:len);
}
str$proto.join.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','join'],
  $ps:[{$nm:'strings',$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:$_String}}}}]};}
function isWhitespace(c) { return c.value in Character.WS$; }
str$proto.$_split = function(sep, discard, group) {
    // shortcut for empty input
    if (this.length === 0) {return Singleton(this, {Element$Singleton:{t:$_String}}); }

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
        tokens.push($_String(value.substring(tokenBegin, tokenEnd), count-tokenBeginCount));
    }
    if (is$(sep, {t:Iterable})) {
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
                        tokens.push($_String(this.substring(j, i), 1));
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
                        tokens.push($_String(this.substring(j, i), 1));
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
            tokens.push("");
        }
    }

    this.codePoints = count;
    return tokens.reifyCeylonType({t:$_String});
}
str$proto.$_split.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:$_String}}},d:['$','String','$m','split']};}
atr$(str$proto, 'reversed', function() {
    var result = "";
    for (var i=this.length; i>0;) {
        var cc = this.charCodeAt(--i);
        if ((cc&0xfc00)!==0xdc00 || i===0) {
            result += this.charAt(i);
        } else {
            result += this.substr(--i, 2);
        }
    }
    return result;
},undefined,function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$at','reversed']}});
str$proto.$_replace = function(sub, repl) {
    if (this.indexOf(sub) < 0) {
      return this;
    }
    var ns = this.replace(sub, repl);
    while (ns.indexOf(sub) >= 0) {
      ns = ns.replace(sub, repl);
    }
    return ns;
}
str$proto.$_replace.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','replace'],
  $ps:[{$nm:'substring',$t:{t:$_String}},{$nm:'replacement',$t:{t:$_String}}]};}
str$proto.replaceFirst = function(sub, repl) {
    return this.replace(sub, repl);
}
str$proto.replaceFirst.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','replaceFirst'],
  $ps:[{$nm:'substring',$t:{t:$_String}},{$nm:'replacement',$t:{t:$_String}}]};}
str$proto.repeat = function(times) {
    var sb = StringBuilder();
    for (var i = 0; i < times; i++) {
        sb.append(this);
    }
    return sb.string;
}
str$proto.repeat.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','repeat'],
  $ps:[{$nm:'times',$t:{t:Integer}}]};}
function isNewline(c) { return c.value===10; }
atr$(str$proto, 'lines', function() {
    return this.$_split(isNewline, true);
},undefined,function(){return{mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:$_String}}},d:['$','String','$at','lines']}});
atr$(str$proto, 'coalesced', function(){return this;},undefined,function(){return{
  mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:$_String}}},d:['$','String','$at','coalesced'],
  $an:[shared(),actual()]
};});
str$proto.occurrences = function(sub) {
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
str$proto.occurrences.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Integer}}},d:['$','List','$m','occurrences']};}
str$proto.$_filter = function(f) {
    var r = Iterable.$$.prototype.$_filter.call(this, f);
    return $_String(r);
}
str$proto.$_filter.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','filter']};}
str$proto.following=function(o,$mpt) {
  return Iterable.$$.prototype.following.call(this,o,$mpt);
}
str$proto.skip = function(skip) {
    if (skip==0) return this;
    return this.segment(skip, this.size);
}
str$proto.skip.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','skip'],
  $ps:[{$nm:'skip',$t:{t:Integer}}]};}
str$proto.take = function(take) {
    if (take==0) return getEmpty();
    return this.segment(0, take);
}
str$proto.take.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','take'],
  $ps:[{$nm:'take',$t:{t:Integer}}]};}
str$proto.by = function(step) {
    var r = Iterable.$$.prototype.by.call(this, step);
    return $_String(r);
}
str$proto.by.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},d:['$','String','$m','by'],
  $ps:[{$nm:'step',$t:{t:Integer}}]};}
str$proto.$_slice=function(idx) {
  var s1 = idx>0 ? this.segment(0,idx) : '';
  if (idx<0)idx=0;
  var s2 = idx<this.size ? this.segment(idx,this.size) : '';
  return tpl$([s1,s2],{t:'T',l:[{t:$_String},{t:$_String}]});
}
str$proto.$_slice.$crtmm$=function(){return{mod:$CCMM$,d:['$','String','$m','slice'],cont:$_String,
  $ps:[{$nm:'index',$mt:'prm',$t:{t:Integer}}],
  $t:{t:'T',l:[{t:$_String},{t:$_String}]}};}

function StringIterator(string) {
    var that = new StringIterator.$$;
    that.str = string;
    that.index = 0;
    return that;
}
StringIterator.$crtmm$=function(){return{$nm:'StringIterator',$mt:'c',$ps:[{$nm:'string',$t:{t:$_String}}],$an:function(){return[shared()];},mod:$CCMM$,d:['$','Iterator']}};

initTypeProto(StringIterator, 'ceylon.language::StringIterator', $init$Basic(), Iterator);
var StringIterator$proto = StringIterator.$$.prototype;
StringIterator$proto.$$targs$$={Element$Iterator:{t:Character}, Absent$Iterator:{t:Null}};
StringIterator$proto.next = function() {
    if (this.index >= this.str.length) { return getFinished(); }
    var first = this.str.charCodeAt(this.index++);
    if ((first&0xfc00) !== 0xd800 || this.index >= this.str.length) {
        return Character(first);
    }
    return Character((first<<10) + this.str.charCodeAt(this.index++) - 0x35fdc00);
}
ex$.$_String=$_String;

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
ex$.codepointFromString=codepointFromString;

