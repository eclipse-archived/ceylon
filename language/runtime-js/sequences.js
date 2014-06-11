var arrprot$=Array.prototype;
var origArrToString = arrprot$.toString;
arrprot$.toString = origArrToString;
arrprot$.elementAt=$_Array.$$.prototype.elementAt;
arrprot$.reifyCeylonType=function(t,ne) {
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
  if (this.$$targs$$===undefined)this.$$targs$$={};
  if (this.$$targs$$.Element$Array===undefined)this.$$targs$$.Element$Array=t;
  return t;
}
arrprot$.getT$name = function() {
    return $_Array.$$.T$name;
}
arrprot$.getT$all = function() {
    return $_Array.$$.T$all;
}

arrprot$.set=$_Array.$$.prototype.set;
arrprot$.chain=Iterable.$$.prototype.chain;
arrprot$.sequence=$_Array.$$.prototype.sequence;
arrprot$.segment=$_Array.$$.prototype.segment;
arrprot$.span=$_Array.$$.prototype.span;
arrprot$.take=$_Array.$$.prototype.take;
arrprot$.skip=$_Array.$$.prototype.skip;
arrprot$.by=$_Array.$$.prototype.by;
arrprot$.spanTo=$_Array.$$.prototype.spanTo;
arrprot$.spanFrom=$_Array.$$.prototype.spanFrom;
arrprot$.items=List.$$.prototype.items;
arrprot$.contains=$_Array.$$.prototype.contains;
arrprot$.equals=List.$$.prototype.equals;
arrprot$.iterator=$_Array.$$.prototype.iterator;
arrprot$.copyTo=$_Array.$$.prototype.copyTo;

atr$(arrprot$,'size',$_Array.$$.prototype.$prop$getSize.get,undefined,$_Array.$$.prototype.$prop$getSize.$crtmm$);
atr$(arrprot$,'string',Iterable.$$.prototype.$prop$getString.get,undefined,Iterable.$$.prototype.$prop$getString.$crtmm$);
atr$(arrprot$,'lastIndex',$_Array.$$.prototype.$prop$getLastIndex.get,undefined,$_Array.$$.prototype.$prop$getLastIndex.$crtmm$);
atr$(arrprot$,'first',$_Array.$$.prototype.$prop$getFirst.get,undefined,$_Array.$$.prototype.$prop$getFirst.$crtmm$);
atr$(arrprot$,'last',$_Array.$$.prototype.$prop$getLast.get,undefined,$_Array.$$.prototype.$prop$getLast.$crtmm$);
atr$(arrprot$,'keys',List.$$.prototype.$prop$getKeys.get,undefined,List.$$.prototype.$prop$getKeys.$crtmm$);
atr$(arrprot$,'empty',$_Array.$$.prototype.$prop$getEmpty.get,undefined,$_Array.$$.prototype.$prop$getEmpty.$crtmm$);
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
