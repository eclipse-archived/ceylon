var arrprot$=Array.prototype;
arrprot$.ser$$=function(d){
  var targ=this._elemTarg();
  d.putValue(OpenValue$jsint(lmp$(ex$,'$'),this.$prop$getSize),this.length,{Instance$putValue:{t:Integer}});
  var elemtarg={Instance$putElement:targ};
  for (var i=0; i < this.length; i++) {
    d.putElement(i,this[i],elemtarg);
  }
}
var origArrToString = arrprot$.toString;
arrprot$.toString = origArrToString;
arrprot$.rt$=function(t,ne) {
    if (t===null || t===undefined)t={t:Anything};
    if (this.$$targs$$===undefined)this.$$targs$$=={};
    add_type_arg(this,'Element$Iterable',t);
    add_type_arg(this,'Element$Array',t);
    add_type_arg(this,'Element$List',t);
    add_type_arg(this,'Element$Sequential',t);
    add_type_arg(this,'Absent$Iterable',ne?{t:Nothing}:{t:Null});
    return this;
}
arrprot$._elemTarg=function(){
  var t = this.$$targs$$ && this.$$targs$$.Element$Array;
  if (t===undefined)t = this.$$targs$$ && this.$$targs$$.Element$Iterable;
  if (t===undefined)t = this.$$targs$$ && this.$$targs$$.Element$List;
  if (t===undefined)t = {t:Anything};
  if (this.$$targs$$===undefined)this.$$targs$$={
    Element$Array:t, Element$List:t, Element$Iterable:t,
    Element$Collection:t, Item$Correspondence:t,Element$Ranged:t,
    Absent$Iterable:Null, Key$Correspondence:{t:Integer},
    Index$Ranged:{t:Integer}, Subrange$Ranged:{t:$_Array,a:{Element$Array:'Element$Array'}}
  };
  return t;
}
arrprot$.getT$name = function() {
    return $_Array.$$.T$name;
}
arrprot$.getT$all = function() {
    return $_Array.$$.T$all;
}

atr$(arrprot$,'reversed', function() {
  this._elemTarg();
  return this.Reversed$List();
},undefined,List.$$.prototype.$prop$getReversed.$crtmm$);
atr$(arrprot$,'rest', function() {
  this._elemTarg();
  return this.Rest$List(1);
},undefined,List.$$.prototype.$prop$getRest.$crtmm$);

//for sequenced enumerations
function sarg$(elems,spread,$$targs$$){
  $init$sarg();
  that=new sarg$.$$;
  Iterable($$targs$$,that);
  that.e=elems;
  that.s=spread;
  return that;
}
sarg$.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},ps:[],tp:{T$LazyIterable:{dv:'out'}},sts:[{t:Iterable,a:{Element$Iterable:'T$LazyIterable',Absent$Iterable:{t:Null}}}],pa:1,d:['$','LazyIterable']};};
ex$.sarg$=sarg$;
function $init$sarg(){if(sarg$.$$===undefined){
  initTypeProto(sarg$,'ceylon.language::LazyIterable',Basic,Iterable);
  (function(that){
    function iter(sarg){
      var $$4=new iter.$$;
      $$4.outer$=sarg;
      $$4.$$targs$$={Element$Iterator:sarg.$$targs$$.Element$Iterable};
      Iterator($$4.$$targs$$,$$4);
      $$4.i=0;
      return $$4;
    };
    iter.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$cont:iterator,sts:[{t:Iterator,a:{Element$Iterator:'T$LazyIterable'}}],d:['$','LazyIterable','$m','iterator','$o','it']};};
    if(iter.$$===undefined){
      initTypeProto(iter,'LazyIterable.iterator',Basic,Iterator);
      iter.$$.prototype.next=function(){
        if (this.sp)return this.sp.next();
        var e=this.outer$.e(this.i);
        if (e===finished() && this.outer$.s) {
          this.sp=this.outer$.s().iterator();
          e=this.sp.next();
        } else {
          this.i++;
        }
        return e;
      };
      iter.$$.prototype.next.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:['T$LazyIterable',{t:Finished}]},ps:[],$cont:iter,an:function(){return[shared(),actual()];},d:['$','LazyIterable','$m','iterator','$o','it','$m','next']};};
    }
    that.iterator=function(){
      return iter(this);
    };
    that.iterator.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Iterator,a:{Element$Iterator:'T$LazyIterable'}},ps:[],$cont:$sarg,an:function(){return[shared(),actual()];},d:['$','LazyIterable','$m','iterator']};};
  })(sarg$.$$.prototype);
}
return sarg$;
}
$init$sarg();
ex$.$init$sarg=$init$sarg;
$init$sarg();
//Turn a comprehension into a native array
function nfor$(c) {
  var a = [];
  var item;
  for (var iter = c.iterator();(item=iter.next())!==finished();){
    a.push(item);
  }
  return a;
}
ex$.nfor$=nfor$;

function variadicness(t) {
  if (extendsType(t,{t:Tuple})) {
    if (t.t==='T' && t.l.length>0) {
      return t.l[t.l.length-1].seq || 0;
    }
  } else if (extendsType(t,{t:Sequence})) {
    return 2;
  } else if (extendsType(t,{t:Sequential})) {
    return 1;
  }
  return 0;
}
