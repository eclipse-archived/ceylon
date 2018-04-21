//internal
function tpl$(elems,spread){
  if (spread!==undefined) {
    if (is$(spread,{t:Sequential})) {
      if (spread.size===0)spread=undefined;
    } else {
      var e;
      for (var iter=spread.iterator();(e=iter.next())!==finished();) {
        elems.push(e);
      }
      spread=undefined;
    }
  }
  if (elems.length===0&&(spread===undefined||spread.size===0))return empty();
  var types=[];
  for (var i=0; i < elems.length; i++){
    var e=elems[i];
    if (e===null) {
      types.push({t:$i$$_null()});
    } else if (e===undefined) {
      types.push({t:Anything});
    } else if (e.getT$all && e.getT$name) {
      var _et={t:e.getT$all()[e.getT$name()]};
      if (e.$a$)_et.a=e.$a$;
      types.push(_et);
    } else {
      console.log("Tuple: WTF do I use for the type of " + e);
      types.push({t:Anything});
    }
  }
  //Check if I need to do this
  //if (spread && spread.$a$) {
  //  types.push(spread.$a$.Element$Sequence);
  //}
  types={t:'T',l:types};
  $i$tpl$();
  var that=new tpl$.$$;
  that.$a$=types;
  $_Object(that);
  var _t=types.l.length===1?types.l[0]:{t:'u',l:types.l};
  Sequence({Element$Sequence:_t},that);
  elems.$a$={Element$Tuple:_t,First$Tuple:types.l[0],Element$Iterable:_t,
    Element$Sequential:_t,Element$List:_t,Element$Array:_t,Element$Sequence:_t,Absent$Iterable:{t:Nothing},
    Element$Collection:_t,Key$Correspondence:{t:Integer},Item$Correspondence:_t};
  set_type_args(that,elems.$a$,Tuple);
  that.$a$.Element$Tuple=_t;
  that.$a$.First$Tuple=types.l[0];
  if (spread!==undefined) {
    that.$a$.Rest$Tuple={t:Sequence,a:_t};
  } else if (types.l.length===1) {
    that.$a$.Rest$Tuple={t:Empty};
  } else {
    that.$a$.Rest$Tuple={t:'T',l:_t.l.slice(1)};
  }
  //This is awful as well. When you start getting weird undefined refs calling tuple.first, update this (check Tuple.first)
  that.$z_=elems[0];
  that.elem$=elems;
  if (spread!==undefined) {
    that.sp$=spread;
  }
  that.t$=_t;
  that.tps$=types;
  return that;
}
function $i$tpl$(){
  if (tpl$.$$===undefined) {
    initTypeProto(tpl$,'ceylon.language::Tuple',$i$Tuple());
    (function(tuple) {
      tuple.getFromFirst=function(i){
        if (this.sp$ && i>=this.elem$.length) {
          return this.sp$.getFromFirst(i-this.elem$.length);
        }
        return this.elem$[i];
      };
      tuple.getFromFirst.$m$=Tuple.$$.prototype.getFromFirst.$m$;
      tuple.iterator=function(){
        if (this.sp$) {
          return ChainedIterator($arr$(this.elem$,this.t$),this.sp$,{Element$ChainedIterator:this.tps$,Other$ChainedIterator:this.sp$.$a$.Element$Sequential});
        }
        var i=0,e=this.elem$;
        return for$iter(function(){
          if (i===e.length)return finished();
          var r=e[i++];
          return r===undefined?null:r;
        },{Element$Iterator:this.t$});
      }
      tuple.iterator.$m$=Tuple.$$.prototype.iterator.$m$;
      //That stupid last "||false" is needed otherwise this returns undefined
      tuple.contains=function(i) { return $arr$cnt(this.elem$,i) || (this.sp$&&this.sp$.contains(i)) || false; }
      tuple.contains.$m$=Tuple.$$.prototype.contains.$m$;
      tuple.count=function(f){
        var c=0;
        for (var i=0;i<this.elem$.length;i++) {
          if (f(this.elem$[i])) {
            c++;
          }
        }
        if (this.sp$)c+=this.sp$.count(f);
        return c;
      };
      tuple.count.$m$=Tuple.$$.prototype.count.$m$;
      tuple.select=function(f){
        var a=[];
        for (var i=0;i<this.elem$.length;i++) {
          if (f(this.elem$[i])) {
            a.push(this.elem$[i]);
          }
        }
        if (this.sp$)for (i=0;i<this.sp$.size;i++) {
          if (f(this.sp$.getFromFirst(i)))a.push(this.sp$.getFromFirst(i));
        }
        return $arr$sa$(a,this.t$);
      };
      tuple.select.$m$=Tuple.$$.prototype.select.$m$;
      tuple.$_filter=function(f){
        var elems=this.elem$;
        var spr=this.sp$;
        return for$(function(){
          var i=0;
          return function(){
            while (i<elems.length) {
              var e=elems[i];
              i++;
              if (f(e))return e;
            }
            if (spr) {
              while(i<elems.length+spr.size) {
                var e=spr.getFromFirst(i-elems.length-1);
                i++;
                if (f(e))return e;
              }
            }
            return finished();
          };
        },{Element$Iterable:this.t$,Absent$Iterable:{t:Null}});
      };
      tuple.$_filter.$m$=Tuple.$$.prototype.$_filter.$m$;
      tuple.collect=function(f,$m){
        var a=new Array(this.elem$.length+(this.sp$?this.sp$.size:0));
        var j=0;
        for (var i=0;i<this.elem$.length;i++) {
          a[j++]=f(this.elem$[i]);
        }
        if (this.sp$)for (i=0;i<this.sp$.size;i++) {
          a[j++]=f(this.sp$.getFromFirst(i));
        }
        return $arr$sa$(a,$m.Result$collect);
      };
      tuple.collect.$m$=Tuple.$$.prototype.collect.$m$;
      tuple.$_map=function(f,$m){
        var elems=this.elem$;
        var spr=this.sp$;
        return for$(function(){
          var i=0;
          return function(){
            if (i<elems.length) {
              i++;
              return f(elems[i-1]);
            }
            if (spr && elems.length+spr.size>i) {
              i++;
              return f(spr.getFromFirst(i-elems.length-1));
            }
            return finished();
          };
        },{Element$Iterable:$m.Result$map,Absent$Iterable:{t:Null}});
      };
      tuple.$_map.$m$=Tuple.$$.prototype.$_map.$m$;
      tuple.withLeading=function(a,b){
        var e2 = this.elem$.slice(0); e2.unshift(a);
        var t2 = this.tps$.l.slice(0); t2.unshift(b.Other$withLeading);
        return tpl$(e2,this.sp$);
      }
      tuple.withLeading.$m$=Tuple.$$.prototype.withLeading.$m$;
      tuple.span=function(a,b){//from,to
        if (this.sp$) {
          if (a>=this.elem$.length&&b>=this.elem$.length){
            return this.sp$.span(a-this.elem$.length,b-this.elem$.length).sequence();
          }
          if (b>=this.elem$.length) {
            var s1=this.elem$.slice(a);
            var s2=this.sp$.spanTo(b-this.elem$.length);
            return $arr$(s1,this.t$).chain(s2,
              {Other$chain:this.sp$.$a$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
          }
          if (a>=this.elem$.length) {
            var s1=this.sp$.span(a-this.elem$.length,0);
            var s2=$arr$(this.elem$,this.t$).span(this.elem$.length-1,b)
            return s1.chain(s2,{Other$chain:this.tps$,OtherAbsent$chain:{t:Nothing}}).sequence();
          }
        }
        return $arr$(this.elem$,this.t$).span(a,b).sequence();
      }
      tuple.span.$m$=Tuple.$$.prototype.span.$m$;
      tuple.spanTo=function(x){
        if (this.sp$) {
          if (x<0)return empty();
          return this.span(0,x);
        }
        return $arr$(this.elem$,this.t$).spanTo(x);
      }
      tuple.spanTo.$m$=Tuple.$$.prototype.spanTo.$m$;
      tuple.spanFrom=function(x){
        if (x<=0)return this;
        if (this.sp$) {
          if (x>=this.elem$.length) {
            return this.sp$.spanFrom(x-this.elem$.length);
          } else if (x<0) {
            return this;
          }
          return $arr$(this.elem$.slice(x),this.t$).chain(this.sp$,{Other$chain:this.sp$.$a$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
        }
        var r=this.elem$.slice(x);
        return r.size===0?empty():tpl$(r);
      }
      tuple.spanFrom.$m$=Tuple.$$.prototype.spanFrom.$m$;
      tuple.measure=function(a,b){//from,length
        if(b===0)return empty();
        if (this.sp$) {
          if (a>=this.elem$.length) {
            var m1=this.sp$.measure(a-this.elem$.length,b);
            if (b>0) {
              return m1.sequence();
            } else {
              console.log("missing tpl.measure with negative length");
            }
          }
          if (b>0 && a+b-1>=this.elem$.length) {
            var m1=$arr$(this.elem$,this.t$).measure(a,this.elem$.length-a);
            var m2=this.sp$.measure(0,b-m1.size);
            return m1.chain(m2,{Other$chain:this.sp$.$a$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
          }
        }
        return $arr$(this.elem$,this.t$).measure(a,b).sequence();
      }
      tuple.measure.$m$=Tuple.$$.prototype.measure.$m$;
      tuple.equals=function(o){
        if (this.sp$) {
          if (!is$(o,{t:Sequential}))return false;
          if (!o.size===this.size)return false;
          var oi=o.iterator();
          var ot=this.iterator();
          var ni=oi.next();
          var nt=ot.next();
          while (ni!==finished()) {
            if (!$eq$(ni,nt))return false;
            ni=oi.next();
            nt=ot.next();
          }
          return true;
        }
        return $arr$(this.elem$,this.t$).equals(o);
      }
      tuple.equals.$m$=List.$$.prototype.equals.$m$;
      tuple.withTrailing=function(a,b){
        if (this.sp$) {
          return tpl$(this.elem$,this.sp$.withTrailing(a,b));
        }
        var e2=this.elem$.slice(0);e2.push(a);
        var t2=this.tps$.l.slice(0);t2.push(b.Other$withTrailing);
        return tpl$(e2);
      }
      tuple.withTrailing.$m$=Sequential.$$.prototype.withTrailing.$m$;
      tuple.longerThan=function(i){return (this.sp$?this.size:this.elem$.length)>i;}
      tuple.longerThan.$m$=Iterable.$$.prototype.longerThan.$m$;
      tuple.shorterThan=function(i){return (this.sp$?this.size:this.elem$.length)<i;}
      tuple.shorterThan.$m$=Iterable.$$.prototype.shorterThan.$m$;
      atr$(tuple,'hash',function(){
        return $arr$(this.elem$,this.t$).hash+(this.sp$?this.sp$.hash:0);
      },undefined,List.$$.prototype.$prop$getHash.$m$);
      atr$(tuple,'rest',function(){
        return this.elem$.length===1?this.sp$||empty():tpl$(this.elem$.slice(1),this.sp$);
      },undefined,Tuple.$$.prototype.$prop$getRest.$m$);
      atr$(tuple,'size',function(){
        return this.elem$.length+(this.sp$?this.sp$.size:0);
      },undefined,Tuple.$$.prototype.$prop$getSize.$m$);
      atr$(tuple,'lastIndex',function(){
        return this.elem$.length-1+(this.sp$?this.sp$.size:0);
      },undefined,Tuple.$$.prototype.$prop$getLastIndex.$m$);
      atr$(tuple,'last',function(){
        return this.sp$?this.sp$.last:this.elem$[this.elem$.length-1];
      },undefined,Tuple.$$.prototype.$prop$getLast.$m$);
      atr$(tuple,'string',function(){
        return '['+commaList($arr$(this.elem$,this.t$))+(this.sp$?', '+commaList(this.sp$):'')+']';
      },undefined,Tuple.$$.prototype.$prop$getString.$m$);
      tuple.nativeArray=function(){
        if (this.sp$) {
          var e=new Array(this.elem$.length+this.sp$.size);
          for (var i=0;i<this.elem$.length;i++) {
            e[i]=this.elem$[i];
          }
          var elem;for(var iter=this.sp$.iterator();(elem=iter.next())!==finished();) {
            e[i++]=elem;
          }
          return e;
        }
        return this.elem$.slice(0);
      };
      tuple.append=function(e){
        var na=this.nativeArray();
        for (var i=0;i<e.size;i++) {
          na.push(e.$_get(i));
        }
        return tpl$(na);
      };
    })(tpl$.$$.prototype);
  }
}
$i$tpl$();
x$.tpl$=tpl$;

//Create a proper Tuple from a simplified tuple type description
function retpl$(t) { //receives {t:'T',l:[...]}
  if (t.t!=='T')return t;
  var e;
  var r={t:Empty};
  for (var i=t.l.length-1;i>=0;i--){
    var f=retpl$(t.l[i]);
    var e=(r.a&&r.a.Element$Tuple)||f;
    if (r.a&&r.a.Element$Tuple){
      if (e.l) {
        var l2=[];for(var j=0;j<e.l.length;j++)l2.push(e.l[j]);
        l2.unshift(f);
        e={t:'u',l:l2};
      } else {
        e = {t:'u',l:[f,e]};
      }
    }
    r={t:Tuple,a:{First$Tuple:f,Element$Tuple:e,Rest$Tuple:r}};
  }
  return r;
}
//Create a simplified Tuple {t:'T',l:[...]} from a proper {t:Tuple,a:...}
//based on Unit.getTupleElementTypes
function detpl$(t) {
  if (t.t==='T')return t;
  if (t.t===Empty)return t;
  if (t.t==='u') {
    for (var i=0;i<t.l.length;i++) {
      if (t.l[i].t==='T')return t.l[i];
    }
  }
  function stet(args,count) {
    if (t.t==='u') {
      if (t.l.length!==2)return null;
      var caseA=t.l[0];
      var caseB=t.l[1];
      if (caseA.t===Empty && caseB.t===Tuple)return stet(caseB,count);
      if (caseB.t===Empty && caseA.t===Tuple)return stet(caseA,count);
      return null;
    }
    if (args.t===Tuple) {
      var first=args.a.First$Tuple;
      var rest=args.a.Rest$Tuple;
      var ret=stet(rest,count+1);
      if (ret===null)return null;
      ret[count]=first;
      return ret;
    }
    if (args.t===Empty) {
      var ret=Array(count);
      for(var i=0;i<count;i++)ret[i]=null;
      return ret;
    }
    if (args.t===Sequential || args.t===Sequence || args.t===Range || args.t===Iterable) {
      var ret=Array(count+1);
      for (var i=0;i<count;i++)ret[i]=null;
      ret[count]=args;
      return ret;
    }
    return null;
  }
  var simpleResult=stet(t,0);
  if (simpleResult)return {t:'T',l:simpleResult};
  if (t.a && t.a.First$Tuple && t.a.Element$Tuple && t.a.Rest$Tuple) {
    var result = detpl$(t.a.Rest$Tuple);
    if (result.l===undefined)return result;
    var arg=t.a.First$Tuple || {t:Anything};
    result.l.unshift(arg);
    return result;
  } else if (t.a && (t.a.Element$Sequential || t.a.Element$Sequence)) {
    return t.a.Element$Sequential || t.a.Element$Sequence;
  }
  throw new TypeError("detpl$ requires a proper Tuple {t:Tuple,a:{First$Tuple:F,Element$Tuple:E,Rest$Tuple:R}}");
}
