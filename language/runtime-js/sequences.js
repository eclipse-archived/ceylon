var arrprot$=$_Array.$$.prototype;
function $arr$(a,t,seq) {
  if (t===null || t===undefined)t={t:Anything};
  var a=$_Array$$c(a,{Element$Array:t});
  if (seq)a.$a$.Absent$Iterable={t:Nothing};
  return a;
}
x$.$arr$=$arr$;

function $arr$sa$(arr,t,ne) {
    if (arr.length===0)return empty();
    if (t===null || t===undefined)t={t:Anything};
    return ArraySequence($arr$(arr,t,ne),{Element$ArraySequence:t});
}
x$.$arr$sa$=$arr$sa$;

function $arr$eq(a,o) {
  if (o===undefined||o===null||typeof(o.length)!='number')return false;
  if (o.length===a.length) {
    for (var i=0;i<a.length;i++) {
      if (a[i]===undefined) {
        if (o[i]!==undefined)return false;
      } else if (a[i]===null) {
        if (o[i]!==null)return false;
      } else if (!$eq$(a[i],o[i]))return false;
    }
  }
  return false;
}
x$.$arr$eq=$arr$eq;
//Ceylon Iterable to Native Array
function $ci2na$(ci){
  if (ci===undefined||ci===empty())return [];
  var a=Array(ci.size),i=0,iter=ci.iterator(),next;
  while ((next=iter.next())!==finished()) {
    a[i++]=next;
  }
  return a;
}
function $arr$cnt(a,o){
  if (o===null) {
    for (var i=0;i<a.length;i++) {
      if (a[i]===null)return true;
    }
  } else if (o===undefined) {
    for (var i=0;i<a.length;i++) {
      if (a[i]===undefined)return true;
    }
  } else {
    for (var i=0;i<a.length;i++) {
      if ($eq$(a[i],o)) return true;
    }
  }
  return false;
}
x$.$arr$cnt=$arr$cnt;
empty().nativeArray=function(){return [];}

atr$(arrprot$,'string', function() {
  if (this.size===0)return '{}';
  var s="{ ";
  for (var i=0; i<this.size;i++) {
    var e=this.arr$[i];
    if (i>0)s+=", ";
    if (e===null)s+="<null>";
    else if (e===undefined)s+="<undefined>";
    else s+= e===this?"<CIRCULAR>":e.string;
  }
  s+=" }";
  return s;
},undefined,$_Object.$$.prototype.$prop$getString.$m$);
atr$(arrprot$,'reversed', function() {
  return this.Reversed$List();
},undefined,List.$$.prototype.$prop$getReversed.$m$);
atr$(arrprot$,'rest', function() {
  return this.Sublist$List(1, this.size-1);
},undefined,List.$$.prototype.$prop$getRest.$m$);

//for sequenced enumerations
function sarg$(elems,spread,$a$){
  $i$sarg();
  var that=new sarg$.$$;
  set_type_args(that,{T$LazyIterable:$a$.T$LazyIterable||$a$.Element$Iterable},sarg$);
  Iterable($a$,that);
  that.e=elems;
  that.s=spread;
  return that;
}
sarg$.$m$=function(){return{mod:$M$,'super':{t:Basic},ps:[],tp:{T$LazyIterable:{dv:'out'}},sts:[{t:Iterable,a:{Element$Iterable:'T$LazyIterable',Absent$Iterable:{t:Null}}}],pa:1,d:['$','LazyIterable']};};
x$.sarg$=sarg$;
function $i$sarg(){if(sarg$.$$===undefined){
  initTypeProto(sarg$,'ceylon.language::LazyIterable',Basic,Iterable);
  (function(that){
    function iter(sarg){
      var $$4=new iter.$$;
      $$4.outer$=sarg;
      $$4.$a$={Element$Iterator:sarg.$a$.Element$Iterable};
      Iterator($$4.$a$,$$4);
      $$4.i=0;
      $$4.e=sarg.e;
      $$4.s=sarg.s;
      return $$4;
    };
    iter.$m$=function(){return{mod:$M$,'super':{t:Basic},$cont:iterator,sts:[{t:Iterator,a:{Element$Iterator:'T$LazyIterable'}}],d:['$','LazyIterable','$m','iterator','$o','it']};};
    if(iter.$$===undefined){
      initTypeProto(iter,'LazyIterable.iterator',Basic,Iterator);
      iter.$$.prototype.next=function(){
        if (this.sp)return this.sp.next();
        var e=this.e(this.i);
        if (e===finished() && this.s) {
          var a=this.s();
          //If spread is also LazyIter, get its elements and spread and continue iterating over those
          if (a.getT$name && a.getT$name()==='ceylon.language::LazyIterable') {
            this.i=0;
            this.s=a.s;
            this.e=a.e;
            return this.next();
          }
          this.sp=(Array.isArray(a)?$arr$(a,{t:Anything}):a).iterator();
          e=this.sp.next();
        } else {
          this.i++;
        }
        return e;
      };
      iter.$$.prototype.next.$m$=function(){return{mod:$M$,$t:{t:'u',l:['T$LazyIterable',{t:Finished}]},ps:[],$cont:iter,an:function(){return[shared(),actual()];},d:['$','LazyIterable','$m','iterator','$o','it','$m','next']};};
    }
    that.iterator=function(){
      return iter(this);
    };
    that.iterator.$m$=function(){return{mod:$M$,$t:{t:Iterator,a:{Element$Iterator:'T$LazyIterable'}},ps:[],$cont:$sarg,an:function(){return[shared(),actual()];},d:['$','LazyIterable','$m','iterator']};};
  })(sarg$.$$.prototype);
}
return sarg$;
}
$i$sarg();
x$.$i$sarg=$i$sarg;
$i$sarg();
//Turn a comprehension into a native array
function nfor$(c) {
  var a = [];
  var item;
  for (var iter = c.iterator();(item=iter.next())!==finished();){
    a.push(item);
  }
  return a;
}
x$.nfor$=nfor$;

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
//For lazy iterables from an array of literals
//"literal array for iterable"
function $lai$(a){
  return function(i){
    return i<a.length?a[i]:finished();
  }
}
x$.$lai$=$lai$;
