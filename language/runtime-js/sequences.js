function Array$(elems,$$targs$$) {
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
Array$.$crtmm$={$ps:[{$nm:'elements',$mt:'prm',$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:'Element$Array'}}}],$an:function(){return[shared(),$_final(),$_native()];},mod:$CCMM$,d:['$','Array'],
  'super':{t:Object$}, $tp:{Element$Array:{}}, satisfies:[{t:List,a:{Element$List:'Element$Array'}},
    {t:Ranged,a:{Index$Ranged:{t:Integer},Span$Ranged:{t:Array$,a:{Element$Array:'Element$Array'}}}}]};

initExistingType(Array$, Array, 'ceylon.language::Array', Object$,
        Ranged, $init$List());
var Array$proto = Array.prototype;
var origArrToString = Array$proto.toString;
inheritProto(Array$, Object$, Ranged, $init$List());
Array$proto.toString = origArrToString;
Array$proto.reifyCeylonType=function(t,ne) {
    if (t===null)t={t:Anything};
    this.$$targs$$ = {Element$Iterable:t, Element$Array:t, Element$List:t, Element$Sequential:t,
      Absent$Iterable:ne?{t:Nothing}:{t:Null}};
    return this;
}
ex$.$_Array=Array$;

function ArraySequence(/* {Element+} */items, $$targs$$) {
  var value = [];
  var iter=items.iterator();
  var item;while((item=iter.next())!==getFinished())value.push(item);
  value.seq$ = true;
  var t=$$targs$$.Element$Iterable;
  if (t===undefined)t=$$targs$$.Element$ArraySequence;
  if (t===undefined)t=$$targs$$.Element$Array;
  if (t===undefined)t=$$targs$$.Element$Sequence;
  if (t===undefined)t=$$targs$$.Element$Sequential;
  if (t===undefined)t=$$targs$$.Element$List;
  if (t===undefined)throw new Error("Invalid type arguments for ArraySequence: "+/*require('util').inspect(*/$$targs$$);
  Sequence({Element$Sequence:t},value);
  value.seq$ = true;
  value.$$targs$$.Element$ArraySequence=t;
  value.$$targs$$.Element$Array=t;
  return value;
}
ArraySequence.$crtmm$=function(){return{mod:$CCMM$,d:['$','ArraySequence'],$ps:[{$nm:'elements',$t:{t:Iterable,a:{Element$Iterable:'Element$ArraySequence',Absent$Iterable:{t:Nothing}}}}],$tp:{Element$ArraySequence:{'var':'out'}},satisfies:[{t:Sequence,a:{Element$Sequence:'Element$ArraySequence'}}]};};
initTypeProto(ArraySequence, 'ceylon.language::ArraySequence', $init$Basic(), $init$Sequence());
Array$proto.getT$name = function() {
    return (this.seq$ ? ArraySequence : Array$).$$.T$name;
}
Array$proto.getT$all = function() {
    return (this.seq$ ? ArraySequence : Array$).$$.T$all;
}

atr$(Array$proto, 'size', function(){ return this.length; },undefined,function(){return{mod:$CCMM$,d:['$','Iterable','$at','size'],$cont:Array$proto,$t:{t:Integer}};});
atr$(Array$proto,'string',function(){
    return (this.seq$?Sequential:Iterable).$$.prototype.$prop$getString.get.call(this);
},undefined,function(){return{mod:$CCMM$,d:['$','Iterable','$at','string'],$t:{t:String$},$cont:Array$proto};});
Array$proto.set = function(idx,elem) {
    if (idx >= 0 && idx < this.length) {
        this[idx] = elem;
    }
}
Array$proto.set.$crtmm$=function(){return{mod:$CCMM$,d:['$','Array','$m','set'],$t:{t:Anything},$ps:[{$nm:'index',$t:{t:Integer},$mt:'prm'},{$nm:'element',$mt:'prm',$t:'Element$Array'}]};}
Array$proto.$_get = function(idx) {
    var result = this[idx];
    return result!==undefined ? result:null;
}
Array$proto.$_get.$crtmm$=function(){
  return{mod:$CCMM$,d:['$','List','$m','get'],$t:{t:'u',l:[{t:Null},'Element$Array']},$ps:[{$nm:'index',$t:{t:Integer},$mt:'prm'}]};
}
atr$(Array$proto, 'lastIndex', function() {
    return this.length>0 ? (this.length-1) : null;
},undefined,function(){return{mod:$CCMM$,d:['$','List','$at','lastIndex'],$t:{t:'u',l:[{t:Null},{t:Integer}]}};});
atr$(Array$proto, 'reversed', function() {
    if (this.length === 0) { return this; }
    var arr = this.slice(0);
    arr.reverse();
    return this.seq$ ? ArraySequence(arr,this.$$targs$$||{Element$Iterable:{t:Anything}}) : arr.reifyCeylonType(this.$$targs$$.Element$Iterable);
},undefined,function(){return{mod:$CCMM$,d:['$','List','$at','reversed'],$t:{t:List,a:{Element$List:'Element$Array'}}};});
Array$proto.chain = function(other, $$$mptypes) {
    if (this.length === 0) { return other; }
    return Iterable.$$.prototype.chain.call(this, other, $$$mptypes);
}
atr$(Array$proto,'sequence',function(){return this.seq$?this:ArraySequence(this,this.$$targs$$);},undefined,function(){return{
  mod:$CCMM$,d:['$','Iterable','$at','sequence']};});
atr$(Array$proto, 'first', function(){ return this.length>0 ? this[0] : null; },
  undefined,function(){return{mod:$CCMM$,d:['$','Iterable','$at','first'],$t:{t:'u',l:[{t:Null},'Element$Array']}};});
atr$(Array$proto, 'last', function() { return this.length>0 ? this[this.length-1] : null; },
  undefined,function(){return{mod:$CCMM$,d:['$','List','$at','last'],$t:{t:'u',l:[{t:Null},'Element$Array']}};});
Array$proto.segment = function(from, len) {
    if (len <= 0) { return getEmpty(); }
    var stop = from + len;
    var seq = this.slice((from>=0)?from:0, (stop>=0)?stop:0);
    return this.seq$?ArraySequence(seq,this.$$targs$$):seq.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
Array$proto.span = function(from, to) {
    if (from > to) {
        var arr = this.segment(to, from-to+1);
        arr.reverse();
        return this.seq$?ArraySequence(arr,this.$$targs$$):arr.reifyCeylonType(this.$$targs$$.Element$Iterable);
    }
    return this.segment(from, to-from+1);
}
Array$proto.take=function(c) {
  if (c<=0)return [].reifyCeylonType(this.$$targs$$.Element$Iterable);
  var r=this.slice(0,c);
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
Array$proto.skip=function(c){
  if (c<=0)return this;
  var r=this.slice(c);
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
Array$proto.by=function(c){
  if (c<=0)throw AssertionError("Step must be > 0");
  if (c===1)return this;
  var r=[],idx=0;
  while (idx<this.length) {
    r.push(this[idx]);
    idx+=c;
  }
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
Array$proto.spanTo = function(to) {
    return to < 0 ? getEmpty() : this.span(0, to);
}
Array$proto.spanFrom = function(from) {
    return this.span(from, 0x7fffffff);
}
atr$(Array$proto, 'rest', function() {
    return this.length>1?ArraySequence(this.slice(1),this.$$targs$$):getEmpty();
});
Array$proto.items = function(keys) {
    if (keys === undefined) return getEmpty();
    var seq = [];
    for (var i = 0; i < keys.size; i++) {
        var key = keys.$_get(i);
        seq.push(this.$_get(key));
    }
    return this.seq$?ArraySequence(seq,this.$$targs$$||{Element$Iterable:{t:Anything}}):seq.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
atr$(Array$proto, 'keys', function(){ return TypeCategory(this, {t:Integer}); });
Array$proto.contains = function(elem) {
    for (var i=0; i<this.length; i++) {
        if (elem.equals(this[i])) {
            return true;
        }
    }
    return false;
}
Array$proto.equals=function(o) {
  return List.$$.prototype.equals.call(this,o);
}
Array$proto.iterator = function() {
    var $idx$=0;
    var $arr$=this;
    return new ComprehensionIterator(function() {
        return ($idx$===$arr$.length) ? getFinished() : $arr$[$idx$++];
    }, this.$$targs$$);
}
Array$proto.copyTo = function(other,srcpos,dstpos,length){
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
Array$proto.shorterThan = function(len) {
  return this.size < len;
}
Array$proto.shorterThan.$crtmm$={mod:$CCMM$,d:['$','Iterable','$m','shorterThan']};
Array$proto.longerThan = function(len) {
  return this.size > len;
}
Array$proto.longerThan.$crtmm$={mod:$CCMM$,d:['$','Iterable','$m','longerThan']};

function arrayOfSize(size, elem, $$$mptypes) {
    var elems = [];
    if (size>0) {
        for (var i=0; i<size; i++) {
            elems.push(elem);
        }
    }
    return elems.reifyCeylonType($$$mptypes.Element$arrayOfSize);
}
arrayOfSize.$crtmm$=function(){return{$an:function(){return[shared()];},mod:$CCMM$,d:['$','arrayOfSize']};};
ex$.arrayOfSize=arrayOfSize;

function TypeCategory(seq, type) {
    var that = new TypeCategory.$$;
    that.type = type;
    that.seq = seq;
    return that;
}
initTypeProto(TypeCategory, 'ceylon.language::TypeCategory', $init$Basic(), Category);
var TypeCategory$proto = TypeCategory.$$.prototype;
TypeCategory$proto.contains = function(k) {
    return is$(k, this.type) && this.seq.defines(k);
}

function SequenceBuilder($$targs$$,that) {
    if(that===undefined)that=new SequenceBuilder.$$;
    that.seq = [];
    that.$$targs$$=$$targs$$;
    return that;
}
SequenceBuilder.$crtmm$=function(){return{$ps:[],$an:function(){return[shared()];},
  $tp:{Element$SequenceBuilder:{}}, mod:$CCMM$,d:['$','SequenceBuilder']};}

initTypeProto(SequenceBuilder, 'ceylon.language::SequenceBuilder', $init$Basic());
var SequenceBuilder$proto = SequenceBuilder.$$.prototype;
atr$(SequenceBuilder$proto, 'sequence', function() {
    return (this.seq.length > 0) ? ArraySequence(this.seq,{Element$Iterable:this.$$targs$$.Element$SequenceBuilder}) : getEmpty();
},undefined,function(){return{
  $t:{t:Sequential,a:{Element$Sequential:'Element$SequenceBuilder'}},mod:$CCMM$,d:['$','SequenceBuilder','$at','sequence']};});
SequenceBuilder$proto.append=function(e){
  this.seq.push(e);
  return this;
}
SequenceBuilder$proto.appendAll = function(/*Iterable*/arr) {
    if (arr === undefined) return;
    var iter = arr.iterator();
    var e; while ((e = iter.next()) !== getFinished()) {
        this.seq.push(e);
    }
    return this;
}
atr$(SequenceBuilder$proto, 'size', function(){ return this.seq.length; },undefined,function(){return{
  $t:{t:Integer},mod:$CCMM$,d:['$','SequenceBuilder','$at','size']
};});
atr$(SequenceBuilder$proto, 'empty', function() { return this.seq.length===0 },function(){return{
  $t:{t:Boolean$},mod:$CCMM$,d:['$','SequenceBuilder','$at','empty']
};});

function SequenceAppender(other, $$targs$$,that) {
    if (that===undefined)that=new SequenceAppender.$$;
    SequenceBuilder({Element$SequenceBuilder:$$targs$$.Element$SequenceAppender},that);
    that.appendAll(other);
    return that;
}
SequenceAppender.$crtmm$=function(){return{$ps:[{$nm:'elements',$t:{t:Sequence,a:{Element$Sequence:'Element$SequenceAppender'}},$mt:'prm'}],$an:function(){return[shared()];},
  'super':{t:SequenceBuilder,a:{Element$SequenceBuilder:'Element$SequenceAppender'}},$tp:{Element$SequenceAppender:{}},mod:$CCMM$,d:['$','SequenceAppender']};}

initTypeProto(SequenceAppender, 'ceylon.language::SequenceAppender', SequenceBuilder);

ex$.Sequence=Sequence;
//ex$.SequenceBuilder=SequenceBuilder;
//ex$.SequenceAppender=SequenceAppender;
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
        $$4.$$outer=sarg;
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
