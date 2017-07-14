function cmpSubString(str, subStr, offset) {
    for (var i=0; i<subStr.length; ++i) {
        if (str.charCodeAt(offset+i)!==subStr.charCodeAt(i)) {return false}
    }
    return true;
}
function StringIterator(string) {
    var that = new StringIterator.$$;
    that.str = string;
    that.index = 0;
    return that;
}
StringIterator.$crtmm$=function(){return{nm:'StringIterator',mt:'c',ps:[{nm:'string',$t:{t:$_String}}],pa:1,mod:$CCMM$,d:['$','Iterator']}};

initTypeProto(StringIterator, 'ceylon.language::StringIterator', $init$Basic(), Iterator);
var StringIterator$proto = StringIterator.$$.prototype;
StringIterator$proto.$$targs$$={Element$Iterator:{t:Character}, Absent$Iterator:{t:Null}};
StringIterator$proto.next = function() {
    if (this.index >= this.str.length) { return finished(); }
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
ex$.codepointFromString=codepointFromString;

function StringSublist(str,offset,from,to) {
  var that=new StringSublist.$$;
  BaseCharacterList$impl(that);
  that.str=str;
  if (from<=0) {
    that.start=offset;
    that.to=to;
  } else {
    that.to=to-from;
    if (offset<0||offset>str.size) {
      that.start=str.size;
    } else {
      that.start=offset+from;
    }
  }
  return that;
}
StringSublist.$crtmm$=function(){return{nm:'StringSublist',mt:'c',ps:[{nm:'str',$t:{t:$_String}},{nm:'from',$t:{t:Integer}},{nm:'to:',$t:{t:Integer}}],pa:1,mod:$CCMM$,d:['$','List']}};
initTypeProto(StringSublist,'ceylon.language::StringSublist', $init$BaseCharacterList$impl());
var ssubl$proto=StringSublist.$$.prototype;
ssubl$proto.getFromFirst=function(i) {
  if (i>this.to||i<0)return null;
  return this.str.getFromFirst(this.start+i);
}
atr$(ssubl$proto, 'empty', function(){return this.to<0||this.start>this.str.size;},
  undefined, function(){return{mod:$CCMM$,$t:{t:$_Boolean},pa:0,$cont:StringSublist,d:['$','List','$at','empty']};});
atr$(ssubl$proto, 'lastIndex', function(){
  var s=this.size;
  return s>0 ? s-1 : null;
  },undefined, function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Integer},{t:Null}]},pa:0,$cont:StringSublist,d:['$','List','$at','lastIndex']};});
atr$(ssubl$proto, 'size', function(){return Math.min(this.str.size-this.start,this.to+1);},
  undefined, function(){return{mod:$CCMM$,$t:{t:Integer},pa:0,$cont:StringSublist,d:['$','List','$at','size']};});
ssubl$proto.iterator=function() {
  $init$BaseIterator$impl();
  var that=new BaseIterator$impl.$$;
  BaseIterator$impl({Element$BaseIterator:{t:Character},Absent$BaseIterator:{t:Null}},that);
  that.offset=this.start;
  that.index=0;
  that.outer=this;
  that.next=function() {
    if (this.offset<this.outer.str.size && this.index<=this.outer.to) {
      var cp=this.outer.str.getFromFirst(this.offset);
      this.offset++;
      this.index++;
      return cp;
    }
    return finished();
  }
  return that;
}
ssubl$proto.contains=function contains(o) {
  if (is$(o,{t:$_String})) {
    if (o.size>(this.start-this.to))return false;
    var idx=this.str.indexOf(o,this.start);
    return idx>=this.start && idx<=1+this.start+this.to-o.size;
  } else if (is$(o,{t:Character})) {
    return this.str.occurs(o,this.start,this.to) >= 0;
  }
  return false;
}
ssubl$proto.sublist=function(f,t) {
  return StringSublist(this.str,this.start,f,Math.min(t,this.to));
}
ssubl$proto.sublistFrom=function(f) {
  return this.sublist(f, this.str.size);
}
