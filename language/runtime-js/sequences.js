function $_Array(elems,$$targs$$) {
    var e=[];
    if (!(elems === null || elems === undefined)) {
        var iter=elems.iterator();
        var item;while((item=iter.next())!==getFinished()) {
            e.push(item);
        }
    }
    e.$$targs$$=$$targs$$;
    List({Element$List:$$targs$$.Element$Array}, e);
    return e;
}
$_Array.$crtmm$={$ps:[{$nm:'elements',$mt:'prm',$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:'Element$Array'}}}],$an:function(){return[shared(),$_final(),$_native()];},mod:$CCMM$,d:['$','Array'],
  'super':{t:$_Object}, $tp:{Element$Array:{}}, satisfies:[{t:List,a:{Element$List:'Element$Array'}},
    {t:Ranged,a:{Index$Ranged:{t:Integer},Span$Ranged:{t:$_Array,a:{Element$Array:'Element$Array'}}}}]};

initExistingType($_Array, Array, 'ceylon.language::Array', $_Object,
        Ranged, $init$List());
var $_Arrayproto = Array.prototype;
var origArrToString = $_Arrayproto.toString;
inheritProto($_Array, $_Object, Ranged, $init$List());
$_Arrayproto.toString = origArrToString;
$_Arrayproto.elementAt=function elementAt(i){
  var e=this[i];
  if (e===undefined)return null;
  return e;
}
$_Arrayproto.reifyCeylonType=function(t,ne) {
    if (t===null)t={t:Anything};
    this.$$targs$$ = {Element$Iterable:t, Element$Array:t, Element$List:t, Element$Sequential:t,
      Absent$Iterable:ne?{t:Nothing}:{t:Null}};
    return this;
}
ex$.$_Array=$_Array;

$_Arrayproto.getT$name = function() {
    return (this.seq$ ? ArraySequence : $_Array).$$.T$name;
}
$_Arrayproto.getT$all = function() {
    return (this.seq$ ? ArraySequence : $_Array).$$.T$all;
}

atr$($_Arrayproto, 'size', function(){ return this.length; },undefined,function(){return{mod:$CCMM$,d:['$','Iterable','$at','size'],$cont:$_Arrayproto,$t:{t:Integer}};});
atr$($_Arrayproto,'string',function(){
    return (this.seq$?Sequential:Iterable).$$.prototype.$prop$getString.get.call(this);
},undefined,function(){return{mod:$CCMM$,d:['$','Iterable','$at','string'],$t:{t:String$},$cont:$_Arrayproto};});
$_Arrayproto.set = function(idx,elem) {
    if (idx >= 0 && idx < this.length) {
        this[idx] = elem;
    }
}
$_Arrayproto.set.$crtmm$=function(){return{mod:$CCMM$,d:['$','Array','$m','set'],$t:{t:Anything},$ps:[{$nm:'index',$t:{t:Integer},$mt:'prm'},{$nm:'element',$mt:'prm',$t:'Element$Array'}]};}
$_Arrayproto.$_get = function(idx) {
    var result = this[idx];
    return result!==undefined ? result:null;
}
$_Arrayproto.$_get.$crtmm$=function(){
  return{mod:$CCMM$,d:['$','List','$m','get'],$t:{t:'u',l:[{t:Null},'Element$Array']},$ps:[{$nm:'index',$t:{t:Integer},$mt:'prm'}]};
}
atr$($_Arrayproto, 'lastIndex', function() {
    return this.length>0 ? (this.length-1) : null;
},undefined,function(){return{mod:$CCMM$,d:['$','List','$at','lastIndex'],$t:{t:'u',l:[{t:Null},{t:Integer}]}};});
atr$($_Arrayproto, 'reversed', function() {
  var r=this.Reversed$List();
  return this.seq$?r.sequence():r;
},undefined,function(){return{mod:$CCMM$,d:['$','List','$at','reversed'],$t:{t:List,a:{Element$List:'Element$Array'}}};});
$_Arrayproto.chain = function(other, $$$mptypes) {
    if (this.length === 0) { return other; }
    return Iterable.$$.prototype.chain.call(this, other, $$$mptypes);
}
$_Arrayproto.sequence = function() {
    return this.seq$?this:ArraySequence(this,this.$$targs$$);
}
$_Arrayproto.sequence.$crtmm$ = function(){return{mod:$CCMM$,d:['$','Iterable','$m','sequence']};};
atr$($_Arrayproto, 'first', function(){ return this.length>0 ? this[0] : null; },
  undefined,function(){return{mod:$CCMM$,d:['$','Iterable','$at','first'],$t:{t:'u',l:[{t:Null},'Element$Array']}};});
atr$($_Arrayproto, 'last', function() { return this.length>0 ? this[this.length-1] : null; },
  undefined,function(){return{mod:$CCMM$,d:['$','List','$at','last'],$t:{t:'u',l:[{t:Null},'Element$Array']}};});
$_Arrayproto.segment = function(from, len) {
    if (len <= 0) { return getEmpty(); }
    var stop = from + len;
    var seq = this.slice((from>=0)?from:0, (stop>=0)?stop:0);
    return this.seq$?ArraySequence(seq,this.$$targs$$):seq.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
$_Arrayproto.span = function(from, to) {
    if (from > to) {
        var arr = this.segment(to, from-to+1);
        arr.reverse();
        return this.seq$?ArraySequence(arr,this.$$targs$$):arr.reifyCeylonType(this.$$targs$$.Element$Iterable);
    }
    return this.segment(from, to-from+1);
}
$_Arrayproto.take=function(c) {
  if (c<=0)return [].reifyCeylonType(this.$$targs$$.Element$Iterable);
  var r=this.slice(0,c);
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
$_Arrayproto.skip=function(c){
  if (c<=0)return this;
  var r=this.slice(c);
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
$_Arrayproto.by=function(c){
  if (c<=0)throw AssertionError("Step must be > 0");
  if (c===1)return this;
  var r=[],idx=0;
  while (idx<this.length) {
    r.push(this[idx]);
    idx+=c;
  }
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
$_Arrayproto.spanTo = function(to) {
    return to < 0 ? getEmpty() : this.span(0, to);
}
$_Arrayproto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
atr$($_Arrayproto, 'rest', function() {
    return this.Rest$List(1);
});
$_Arrayproto.items = function(keys) {
    if (keys === undefined) return getEmpty();
    var seq = [];
    for (var i = 0; i < keys.size; i++) {
        var key = keys.$_get(i);
        seq.push(this.$_get(key));
    }
    return this.seq$?ArraySequence(seq,this.$$targs$$||{Element$Iterable:{t:Anything}}):seq.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
atr$($_Arrayproto, 'keys', function(){ 
  return this.Indexes$List();
});
$_Arrayproto.contains = function(elem) {
    for (var i=0; i<this.length; i++) {
        if (elem.equals(this[i])) {
            return true;
        }
    }
    return false;
}
$_Arrayproto.equals=function(o) {
  return List.$$.prototype.equals.call(this,o);
}
$_Arrayproto.iterator = function() {
    var $idx$=0;
    var $arr$=this;
    return new ComprehensionIterator(function() {
        return ($idx$===$arr$.length) ? getFinished() : $arr$[$idx$++];
    }, this.$$targs$$);
}
$_Arrayproto.copyTo = function(other,srcpos,dstpos,length){
    if(srcpos===undefined)srcpos=0;
    if(length===undefined)length=this.size-srcpos;
    if (length===0)return;
    if(dstpos===undefined)dstpos=0;
    var endpos=srcpos+length-1;
    if (srcpos<0||srcpos>=this.size||length<1||dstpos+length>other.size||endpos>=this.size)throw Exception("Array index out of bounds");
    if (other===this && dstpos>srcpos) {
      dstpos+=length-1;
      for (var i=endpos; i>=srcpos; i--) {
        other[dstpos--]=this[i];
      }
    } else {
      for (var i=srcpos;i<=endpos;i++) {
        other[dstpos++]=this[i];
      }
    }
}
$_Arrayproto.shorterThan = function(len) {
  return this.size < len;
}
$_Arrayproto.shorterThan.$crtmm$={mod:$CCMM$,d:['$','Iterable','$m','shorterThan']};
$_Arrayproto.longerThan = function(len) {
  return this.size > len;
}
$_Arrayproto.longerThan.$crtmm$={mod:$CCMM$,d:['$','Iterable','$m','longerThan']};

ex$.ArraySequence=ArraySequence;

//for sequenced enumerations
function sarg$(elems,spread,$$targs$$){
  $init$sarg();
  that=new sarg$.$$;
  Iterable($$targs$$,that);
  that.e=elems;
  that.s=spread;
  return that;
}
sarg$.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[],$tp:{T$LazyIterable:{'var':'out'}},satisfies:[{t:Iterable,a:{Element$Iterable:'T$LazyIterable',Absent$Iterable:{t:Null}}}],$an:function(){return[shared()];},d:['$','LazyIterable']};};
ex$.sarg$=sarg$;
function $init$sarg(){if(sarg$.$$===undefined){
  initTypeProto(sarg$,'ceylon.language::LazyIterable',Basic,Iterable);
  (function(that){
    that.iterator=function (){
      var sarg=this;
      //ObjectDef it at caca.ceylon (13:4-15:4)
      function iter($$targs$$){
        var $$4=new iter.$$;
        $$4.outer$=sarg;
        $$4.$$targs$$=$$targs$$;
        Iterator({Element$Iterator:sarg.$$targs$$.Element$Iterable},$$4);
        $$4.i=0;
        return $$4;
      };iter.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$cont:iterator,satisfies:[{t:Iterator,a:{Element$Iterator:'T$LazyIterable'}}],d:['$','LazyIterable','$m','iterator','$o','it']};};
      if(iter.$$===undefined){
        initTypeProto(iter,'LazyIterable.it',Basic,Iterator);
        iter.$$.prototype.next=function(){
          if (this.sp)return this.sp.next();
          var e=sarg.e(this.i);
          if (e===getFinished() && sarg.s) {
            this.sp=sarg.s().iterator();
            e=this.sp.next();
          } else {
            this.i++;
          }
          return e;
        };
        iter.$$.prototype.next.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:['T$LazyIterable',{t:Finished}]},$ps:[],$cont:iter,$an:function(){return[shared(),actual()];},d:['$','LazyIterable','$m','iterator','$o','it','$m','next']};};
      }
      return iter({Element$Iterator:sarg.$$targs$$.T$LazyIterable});
    };
    that.iterator.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterator,a:{Element$Iterator:'T$LazyIterable'}},$ps:[],$cont:$sarg,$an:function(){return[shared(),actual()];},d:['$','LazyIterable','$m','iterator']};};
  })(sarg$.$$.prototype);
}
return sarg$;
}
$init$sarg();
ex$.$init$sarg=$init$sarg;
$init$sarg();
