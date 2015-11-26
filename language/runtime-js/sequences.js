var arrprot$=$_Array.$$.prototype;
function $arr$(a,t,seq) {
  if (t===null || t===undefined)t={t:Anything};
  return $_Array$$c(a,{Element$Array:t});
}
ex$.$arr$=$arr$;

Array.prototype.$sa$=function $sa$(t,ne) {
    if (this.length===0)return empty();
    if (t===null || t===undefined)t={t:Anything};
    return ArraySequence($arr$(this,t),{Element$ArraySequence:t});
}
Array.prototype.equals=function arrayEquals(o) {
  if (o===undefined||o===null||typeof(o.length)!='number')return false;
  if (o.length===this.length) {
    for (var i=0;i<this.length;i++) {
      if (this[i]===undefined) {
        if (o[i]!==undefined)return false;
      } else if (this[i]===null) {
        if (o[i]!==null)return false;
      } else if (!this[i].equals(o[i]))return false;
    }
  }
  return false;
}
Array.prototype.contains=function arrayContains(o){
  for (var i=0;i<this.length;i++) {
    if (o===null) {
      if (this[i]===null)return true;
    } else if (o===undefined) {
      if (this[i]===undefined)return true;
    } else if (this[i].equals(o)) return true;
  }
  return false;
}

atr$(arrprot$,'string', function() {
  if (this.length===0)return '{}';
  var s="{ ";
  for (var i=0; i<this.length;i++) {
    var e=this[i];
    if (i>0)s+=", ";
    if (e===null)s+="<null>";
    else if (e===undefined)s+="<undefined>";
    else s+= e===this?"<CIRCULAR>":e.string;
  }
  s+=" }";
  return s;
},undefined,$_Object.$$.prototype.$prop$getString.$crtmm$);
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
  var that=new sarg$.$$;
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
