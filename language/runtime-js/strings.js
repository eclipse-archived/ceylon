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
    var that = '';
    var _iter = value.iterator();
    var _c; while ((_c = _iter.next()) !== getFinished()) {
        that += _c.string;
    }
    if (size !== undefined) that.codePoints=size;
    return that;
}
String$.$crtmm$=function(){return{'super':{t:$_Object},
  $ps:[{$nm:'characters',$t:{t:Iterable,a:{Element$Iterable:{t:Character},Absent$Iterable:{t:Null}}},$mt:'prm'}],
  'satisfies':[{t:List,a:{Element$List:{t:Character}}}, {t:Comparable,a:{Other$Comparable:{t:String$}}},
    {t:Summable,a:{Other$Summable:{t:String$}}}, {t:Ranged,a:{Index$Ranged:{t:Integer},Span$Ranged:{t:String$}}}],
  $an:function(){return[shared(),$_native(),$_final()];},
  mod:$CCMM$,d:['$','String']};};

initExistingType(String$, String, 'ceylon.language::String', $_Object, List, Comparable,
        Ranged, Summable);
var origStrToString = String.prototype.toString;
inheritProto(String$, $_Object, Sequential, Comparable, Ranged, Summable);
var String$proto = String$.$$.prototype;
String$proto.$$targs$$={Element$Iterable:{t:Character}, Absent$Iterable:{t:Null},
  Element$List:{t:Character}, Other$Summable:{t:String$}, Other$Comparable:{t:String$},
  Index$Ranged:{t:Integer}, Element$Ranged:{t:Character}, Span$Ranged:{t:String$},
  Element$Collection:{t:Character}, Key$Correspondence:{t:Integer}, Item$Correspondence:{t:Character}};
String$proto.getT$name = function() {
    return String$.$$.T$name;
}
String$proto.getT$all = function() {
    return String$.$$.T$all;
}
String$proto.toString = origStrToString;
atr$(String$proto, 'string', function(){ return this; },undefined,function(){return{mod:$CCMM$,
  $t:{t:String$},d:['$','Object','$at','string']}});
String$proto.plus = function(other) {
    var size = this.codePoints + other.codePoints;
    return String$(this+other, isNaN(size)?undefined:size);
}
String$proto.plus.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','plus'],
  $ps:[{$nm:'other',$t:{t:String$},$mt:'prm'}]};}
String$proto.equals = function(other) {
    if (other.constructor===String) {
        return other.valueOf()===this.valueOf();
    } else if (is$(other, {t:String$})) {
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
String$proto.equals.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','String','$m','equals'],
  $ps:[{$nm:'other',$t:{t:String$},$mt:'prm'}]};}
String$proto.compare = function(other) {
    var cmp = this.localeCompare(other);
    return cmp===0 ? getEqual() : (cmp<0 ? getSmaller():getLarger());
}
String$proto.compare.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Comparison},d:['$','String','$m','compare'],
  $ps:[{$nm:'other',$t:{t:String$}}]};}
String$proto.smallerThan=function(o){
  return Comparable.$$.prototype.smallerThan.call(this,o);
}
String$proto.smallerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},
  $ps:[{$nm:'Other',$t:{t:String$},$mt:'prm'}],d:['$','String','$m','smallerThan']};};
String$proto.largerThan=function(o){
  return Comparable.$$.prototype.largerThan.call(this,o);
}
String$proto.largerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},
  $ps:[{$nm:'Other',$t:{t:String$},$mt:'prm'}],d:['$','String','$m','largerThan']};};
String$proto.notSmallerThan=function(o){
  return Comparable.$$.prototype.notSmallerThan.call(this,o);
}
String$proto.notSmallerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},
  $ps:[{$nm:'Other',$t:{t:String$},$mt:'prm'}],d:['$','String','$m','notSmallerThan']};};
String$proto.notLargerThan=function(o){
  return Comparable.$$.prototype.notLargerThan.call(this,o);
}
String$proto.notLargerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},
  $ps:[{$nm:'Other',$t:{t:String$},$mt:'prm'}],d:['$','String','$m','notLargerThan']};};
String$proto.sequence=function(){
  return this.length===0?getEmpty():ArraySequence(this,{Element$ArraySequence:{t:Character}});
}
String$proto.sequence.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Sequential,
  a:{Element$Sequential:{t:Character}}},$cont:String$,d:['$','String','$m','sequence'],
  $an:function(){return[actual(),shared()]}};};
atr$(String$proto, 'uppercased', function(){ return String$(this.toUpperCase()); },undefined,function(){return{
  mod:$CCMM$,$t:{t:String$},d:['$','String','$at','uppercased']}});
atr$(String$proto, 'lowercased', function(){ return String$(this.toLowerCase()); },undefined,function(){return{
  mod:$CCMM$,$t:{t:String$},d:['$','String','$at','lowercased']}});
atr$(String$proto, 'size', function() {
  if (this.codePoints===undefined) {
    this.codePoints = countCodepoints(this);
  }
  return this.codePoints;
},undefined,function(){return{mod:$CCMM$,$t:{t:Integer},d:['$','Iterable','$at','size']}});
atr$(String$proto, 'lastIndex', function(){ return this.size.equals(0) ? null : this.size.predecessor; },undefined,function(){return{
  mod:$CCMM$,$t:{t:Integer},d:['$','List','$at','lastIndex']}});
atr$(String$proto, 'rest', function(){
  return String$(this.Rest$List(1));
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$at','rest']}});
String$proto.span = function(from, to) {
    if (from > to) {
        return this.segment(to, from-to+1).reversed;
    }
    return this.segment(from, to-from+1);
}
String$proto.span.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','span'],
  $ps:[{$nm:'from',$t:{t:Integer}},{$nm:'to',$t:{t:Integer}}]};}
String$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
String$proto.spanFrom.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','spanFrom'],
  $ps:[{$nm:'from',$t:{t:Integer}}]};}
String$proto.spanTo = function(to) {
    return to < 0 ? String$('', 0) : this.span(0, to);
}
String$proto.spanTo.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','spanTo'],
  $ps:[{$nm:'to',$t:{t:Integer}}]};}
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
String$proto.segment.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','segment'],
  $ps:[{$nm:'from',$t:{t:Integer}},{$nm:'length',$t:{t:Integer}}]};}
atr$(String$proto, 'empty', function() {
    return this.length===0;
},undefined,function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','Sequential','$at','empty']}});
String$proto.longerThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints>length}
    if (this.length <= length) {return false}
    if (this.length<<1 > length) {return true}
    this.codePoints = countCodepoints(this);
    return this.codePoints>length;
}
String$proto.longerThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','String','$m','longerThan'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
String$proto.shorterThan = function(length) {
    if (this.codePoints!==undefined) {return this.codePoints<length}
    if (this.length < length) {return true}
    if (this.length<<1 >= length) {return false}
    this.codePoints = countCodepoints(this);
    return this.codePoints<length;
}
String$proto.shorterThan.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','String','$m','shorterThan'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
String$proto.iterator= function() {
	return this.length === 0 ? getEmptyIterator() : StringIterator(this);
}
String$proto.iterator.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterator,a:{Element$Iterator:{t:Character}}},d:['$','String','$m','iterator']};}
String$proto.elementAt=function(index){
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
String$proto.elementAt.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['$','String','$m','get'],
  $ps:[{$nm:'index',$t:{t:Integer}}]};}
atr$(String$proto, 'trimmed', function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var from = 0;
    while (from<this.length && (this.charCodeAt(from) in Character.WS$)) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && (this.charCodeAt(to) in Character.WS$));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = String$(this.substring(from, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$at','trimmed']}});
String$proto.trim = function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$_get(from))) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && chars(this.$_get(to)));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = String$(this.substring(from, to));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
}
String$proto.trim.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','trim']};}
String$proto.trimLeading = function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$_get(from))) {++from}
    if (from===0) {return this;}
    var result = String$(this.substring(from, this.length));
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from;
    }
    return result;
}
String$proto.trimLeading.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','trimLeading']};}
String$proto.trimTrailing = function(/*Category*/chars) {
    var to = this.length;
    if (to > 0) {
        do {--to} while (to>=0 && chars(this.$_get(to)));
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
String$proto.trimTrailing.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','trimTrailing']};}

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
String$proto.initial.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','initial'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
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
String$proto.terminal.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','terminal'],
  $ps:[{$nm:'length',$t:{t:Integer}}]};}
atr$(String$proto, 'hash', function() {
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
String$proto.startsWith = function(str) {
    if (str.length > this.length) {return false}
    return cmpSubString(this, str, 0);
}
String$proto.startsWith.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','String','$m','startsWith'],
  $ps:[{$nm:'string',$t:{t:String$}}]};}
String$proto.endsWith = function(str) {
    var start = this.length - str.length
    if (start < 0) {return false}
    return cmpSubString(this, str, start);
}
String$proto.endsWith.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','String','$m','endsWith'],
  $ps:[{$nm:'string',$t:{t:String$}}]};}
String$proto.contains = function(sub) {
    var str;
    if (sub.constructor === String) {str = sub}
    else if (sub.constructor !== Character.$$) {return false}
    else {str = codepointToString(sub.value)}
    return this.indexOf(str) >= 0;
}
String$proto.contains.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Boolean$},d:['$','String','$m','contains'],
  $ps:[{$nm:'substring',$t:{t:String$}}]};}
atr$(String$proto, 'normalized', function() {
    // make use of the fact that all WS characters are single UTF-16 code units
    var result = "";
    var len = 0;
    var first = true;
    var i1 = 0;
    while (i1 < this.length) {
        while (this.charCodeAt(i1) in Character.WS$) {
            if (++i1 >= this.length) {return String$(result)}
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
    return String$(result, len);
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$at','normalized']}});
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
String$proto.firstOccurrence.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Integer}]},d:['$','String','$m','firstOccurrence']};}
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
String$proto.lastOccurrence.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Integer}]},d:['$','String','$m','lastOccurrence']};}
atr$(String$proto, 'first', function() { return this.$_get(0); },undefined,function(){return{
  mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['$','Iterable','$at','first']}});
atr$(String$proto, 'last', function(){ return this.size>0?this.$_get(this.size.predecessor):null; },undefined,function(){return{
  mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Character}]},d:['$','Iterable','$at','last']}});
atr$(String$proto, 'keys', function() {
    return this.size > 0 ? Range(0, this.size.predecessor, {Element$Range:{t:Integer}}) : getEmpty();
},undefined,function(){return{mod:$CCMM$,$t:{t:Category},d:['$','Correspondence','$at','keys']}});
String$proto.join = function(objects) {
    var it = objects.iterator();
    var obj = it.next();
    if (obj === getFinished()) {return String$("", 0);}
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
    return String$(result, isNaN(len)?undefined:len);
}
String$proto.join.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','join'],
  $ps:[{$nm:'strings',$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:String$}}}}]};}
function isWhitespace(c) { return c.value in Character.WS$; }
String$proto.$_split = function(sep, discard, group) {
    // shortcut for empty input
    if (this.length === 0) {return Singleton(this, {Element$Singleton:{t:String$}}); }

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
    return tokens.reifyCeylonType({t:String$});
}
String$proto.$_split.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:String$}}},d:['$','String','$m','split']};}
atr$(String$proto, 'reversed', function() {
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
},undefined,function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$at','reversed']}});
String$proto.$_replace = function(sub, repl) {
    if (this.indexOf(sub) < 0) {
      return this;
    }
    var ns = this.replace(sub, repl);
    while (ns.indexOf(sub) >= 0) {
      ns = ns.replace(sub, repl);
    }
    return String$(ns);
}
String$proto.$_replace.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','replace'],
  $ps:[{$nm:'substring',$t:{t:String$}},{$nm:'replacement',$t:{t:String$}}]};}
String$proto.replaceFirst = function(sub, repl) {
    return String$(this.replace(sub, repl));
}
String$proto.replaceFirst.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','replaceFirst'],
  $ps:[{$nm:'substring',$t:{t:String$}},{$nm:'replacement',$t:{t:String$}}]};}
String$proto.repeat = function(times) {
    var sb = StringBuilder();
    for (var i = 0; i < times; i++) {
        sb.append(this);
    }
    return sb.string;
}
String$proto.repeat.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','repeat'],
  $ps:[{$nm:'times',$t:{t:Integer}}]};}
function isNewline(c) { return c.value===10; }
atr$(String$proto, 'lines', function() {
    return this.$_split(isNewline, true);
},undefined,function(){return{mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:String$}}},d:['$','String','$at','lines']}});
atr$(String$proto, 'coalesced', function(){return this;},undefined,function(){return{
  mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:String$}}},d:['$','String','$at','coalesced'],
  $an:[shared(),actual()]
};});
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
String$proto.occurrences.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Integer}}},d:['$','List','$m','occurrences']};}
String$proto.$_filter = function(f) {
    var r = Iterable.$$.prototype.$_filter.call(this, f);
    return String$(r);
}
String$proto.$_filter.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','filter']};}
String$proto.following=function(o,$mpt) {
  return Iterable.$$.prototype.following.call(this,o,$mpt);
}
String$proto.skip = function(skip) {
    if (skip==0) return this;
    return this.segment(skip, this.size);
}
String$proto.skip.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','skip'],
  $ps:[{$nm:'skip',$t:{t:Integer}}]};}
String$proto.take = function(take) {
    if (take==0) return getEmpty();
    return this.segment(0, take);
}
String$proto.take.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','take'],
  $ps:[{$nm:'take',$t:{t:Integer}}]};}
String$proto.by = function(step) {
    var r = Iterable.$$.prototype.by.call(this, step);
    return String$(r);
}
String$proto.by.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},d:['$','String','$m','by'],
  $ps:[{$nm:'step',$t:{t:Integer}}]};}
String$proto.$_slice=function(idx) {
  var s1 = idx>0 ? this.segment(0,idx) : '';
  if (idx<0)idx=0;
  var s2 = idx<this.size ? this.segment(idx,this.size) : '';
  return tpl$([s1,s2],{t:'T',l:[{t:String$},{t:String$}]});
}
String$proto.$_slice.$crtmm$=function(){return{mod:$CCMM$,d:['$','String','$m','slice'],cont:String$,
  $ps:[{$nm:'index',$mt:'prm',$t:{t:Integer}}],
  $t:{t:'T',l:[{t:String$},{t:String$}]}};}

function StringIterator(string) {
    var that = new StringIterator.$$;
    that.str = string;
    that.index = 0;
    return that;
}
StringIterator.$crtmm$=function(){return{$nm:'StringIterator',$mt:'c',$ps:[{$nm:'string',$t:{t:String$}}],$an:function(){return[shared()];},mod:$CCMM$,d:['$','Iterator']}};

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
ex$.String=String$;

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

