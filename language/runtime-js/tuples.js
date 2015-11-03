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
  if (elems.size===0&&(spread===undefined||spread.size===0))return empty();
  var types=[];
  for (var i=0; i < elems.size; i++){
    var e=elems.$_get(i,1);
    if (e===null) {
      types.push({t:Null});
    } else if (e===undefined) {
      types.push({t:Anything});
    } else if (e.getT$all && e.getT$name) {
      var _et={t:e.getT$all()[e.getT$name()]};
      if (e.$$targs$$)_et.a=e.$$targs$$;
      types.push(_et);
    } else {
      console.log("Tuple: WTF do I use for the type of " + e);
      types.push({t:Anything});
    }
  }
  //Check if I need to do this
  //if (spread && spread.$$targs$$) {
  //  types.push(spread.$$targs$$.Element$Sequence);
  //}
  types={t:'T',l:types};
  $init$tpl$();
  var that=new tpl$.$$;
  that.$$targs$$=types;
  $_Object(that);
  var _t=types.l.length===1?types.l[0]:{t:'u',l:types.l};
  Sequence({Element$Sequence:_t},that);
  elems.$$targs$$={Element$Tuple:_t,First$Tuple:types.l[0],Element$Iterable:_t,
    Element$Sequential:_t,Element$List:_t,Element$Array:_t,Element$Sequence:_t,Absent$Iterable:{t:Nothing},
    Element$Collection:_t,Key$Correspondence:{t:Integer},Item$Correspondence:_t};
  set_type_args(that,elems.$$targs$$,Tuple);
  that.$$targs$$.Element$Tuple=_t;
  that.$$targs$$.First$Tuple=types.l[0];
  if (spread!==undefined) {
    that.$$targs$$.Rest$Tuple={t:Sequence,a:_t};
  } else if (types.l.length===1) {
    that.$$targs$$.Rest$Tuple={t:Empty};
  } else {
    that.$$targs$$.Rest$Tuple={t:'T',l:_t.l.spanFrom(1)};
  }
  that.first_=elems[0];
  that.elem$=elems;
  if (spread!==undefined) {
    that.sp$=spread;
  }
  that.t$=_t;
  that.tps$=types;
  return that;
}
function $init$tpl$(){
  if (tpl$.$$===undefined) {
    initTypeProto(tpl$,'ceylon.language::Tuple',$init$Tuple());
    (function(tuple) {
      tuple.getFromFirst=function(i){
        if (this.sp$ && i>=this.elem$.size) {
          return this.sp$.getFromFirst(i-this.elem$.size);
        }
        return this.elem$[i];
      };
      tuple.getFromFirst.$crtmm$=Tuple.$$.prototype.getFromFirst.$crtmm$;
      tuple.iterator=function(){
        if (this.sp$) {
          return ChainedIterator(this.elem$,this.sp$,{Element$ChainedIterator:this.tps$,Other$ChainedIterator:this.sp$.$$targs$$.Element$Sequential});
        }
        var i=0,e=this.elem$;
        return for$iter(function(){
          if (i===e.size)return finished();
          var r=e.$_get(i++);
          return r===undefined?null:r;
        },{Element$Iterator:this.t$});
      }
      tuple.iterator.$crtmm$=Tuple.$$.prototype.iterator.$crtmm$;
      //That stupid last "||false" is needed otherwise this returns undefined
      tuple.contains=function(i) { return this.elem$.contains(i) || (this.sp$&&this.sp$.contains(i)) || false; }
      tuple.contains.$crtmm$=Tuple.$$.prototype.contains.$crtmm$;
      tuple.count=function(f){
        var c=0;
        for (var i=0;i<this.elem$.size;i++) {
          if (f(this.elem$[i])) {
            c++;
          }
        }
        if (this.sp$)c+=this.sp$.count(f);
        return c;
      };
      tuple.count.$crtmm$=Tuple.$$.prototype.count.$crtmm$;
      tuple.select=function(f){
        var a=[];
        for (var i=0;i<this.elem$.size;i++) {
          if (f(this.elem$[i])) {
            a.push(this.elem$[i]);
          }
        }
        if (this.sp$)for (i=0;i<this.sp$.size;i++) {
          if (f(this.sp$.getFromFirst(i)))a.push(this.sp$.getFromFirst(i));
        }
        return a.length>0?ArraySequence(a,{Element$ArraySequence:this.t$}):empty();
      };
      tuple.select.$crtmm$=Tuple.$$.prototype.select.$crtmm$;
      tuple.$_filter=function(f){
        var elems=this.elem$;
        var spr=this.sp$;
        return for$(function(){
          var i=0;
          return function(){
            while (i<elems.size) {
              var e=elems[i];
              i++;
              if (f(e))return e;
            }
            if (spr) {
              while(i<elems.size+spr.size) {
                var e=spr.getFromFirst(i-elems.size-1);
                i++;
                if (f(e))return e;
              }
            }
            return finished();
          };
        },{Element$Iterable:this.t$,Absent$Iterable:{t:Null}});
      };
      tuple.$_filter.$crtmm$=Tuple.$$.prototype.$_filter.$crtmm$;
      tuple.collect=function(f,$m){
        var a=new Array(this.elem$.size+(this.sp$?this.sp$.size:0));
        var j=0;
        for (var i=0;i<this.elem$.size;i++) {
          a[j++]=f(this.elem$[i]);
        }
        if (this.sp$)for (i=0;i<this.sp$.size;i++) {
          a[j++]=f(this.sp$.getFromFirst(i));
        }
        return ArraySequence(a,{Element$ArraySequence:$m.Result$collect});
      };
      tuple.collect.$crtmm$=Tuple.$$.prototype.collect.$crtmm$;
      tuple.$_map=function(f,$m){
        var elems=this.elem$;
        var spr=this.sp$;
        return for$(function(){
          var i=0;
          return function(){
            if (i<elems.size) {
              i++;
              return f(elems[i-1]);
            }
            if (spr && elems.size+spr.size>i) {
              i++;
              return f(spr.getFromFirst(i-elems.size-1));
            }
            return finished();
          };
        },{Element$Iterable:$m.Result$map,Absent$Iterable:{t:Null}});
        for (var i=0;i<this.elem$.size;i++) {
          a[j++]=f(this.elem$[i]);
        }
        if (this.sp$)for (i=0;i<this.sp$.size;i++) {
          a[j++]=f(this.sp$.getFromFirst(i));
        }
      };
      tuple.$_map.$crtmm$=Tuple.$$.prototype.$_map.$crtmm$;
      tuple.withLeading=function(a,b){
        var e2 = this.elem$.slice(0); e2.unshift(a);
        var t2 = this.tps$.l.slice(0); t2.unshift(b.Other$withLeading);
        return tpl$(e2,this.sp$);
      }
      tuple.withLeading.$crtmm$=Tuple.$$.prototype.withLeading.$crtmm$;
      tuple.span=function(a,b){//from,to
        if (this.sp$) {
          if (a>=this.elem$.size&&b>=this.elem$.size){
            return this.sp$.span(a-this.elem$.size,b-this.elem$.size);
          }
          if (b>=this.elem$.size) {
            var s1=this.elem$.spanFrom(a);
            var s2=this.sp$.spanTo(b-this.elem$.size);
            return s1.chain(s2,{Other$chain:this.sp$.$$targs$$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
          }
          if (a>=this.elem$.size) {
            var s1=this.sp$.span(a-this.elem$.size,0);
            var s2=this.elem$.span(this.elem$.size-1,b)
            return s1.chain(s2,{Other$chain:this.tps$,OtherAbsent$chain:{t:Nothing}}).sequence();
          }
        }
        var r=this.elem$.span(a,b);
        return r.size===0?empty():ArraySequence(r,{Element$ArraySequence:this.t$});
      }
      tuple.span.$crtmm$=Tuple.$$.prototype.span.$crtmm$;
      tuple.spanTo=function(x){
        if (this.sp$) {
          if (x<0)return empty();
          return this.span(0,x);
        }
        var r=this.elem$.spanTo(x);
        return r.size===0?empty():ArraySequence(r,{Element$ArraySequence:this.t$});
      }
      tuple.spanTo.$crtmm$=Tuple.$$.prototype.spanTo.$crtmm$;
      tuple.spanFrom=function(x){
        if (x===0)return this;
        if (this.sp$) {
          if (x>=this.elem$.size) {
            return this.sp$.spanFrom(x-this.elem$.size);
          } else if (x<0) {
            return this;
          }
          return this.elem$.spanFrom(x).chain(this.sp$,{Other$chain:this.sp$.$$targs$$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
        }
        var r=this.elem$.spanFrom(x);
        return r.size===0?empty():tpl$(r);
      }
      tuple.spanFrom.$crtmm$=Tuple.$$.prototype.spanFrom.$crtmm$;
      tuple.measure=function(a,b){//from,length
        if(b===0)return empty();
        if (this.sp$) {
          if (a>=this.elem$.size) {
              var m1=this.sp$.measure(a-this.elem$.size,b);
            if (b>0) {
              return m1;
            } else {
              console.log("missing tpl.measure with negative length");
            }
          }
          if (b>0 && a+b-1>=this.elem$.size) {
            var m1=this.elem$.measure(a,this.elem$.size-a);
            var m2=this.sp$.measure(0,b-m1.size);
            return m1.chain(m2,{Other$chain:this.sp$.$$targs$$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
          }
        }
        var r=this.elem$.measure(a,b);
        return r.size===0?empty():ArraySequence(r,{Element$ArraySequence:this.t$});
      }
      tuple.measure.$crtmm$=Tuple.$$.prototype.measure.$crtmm$;
      tuple.equals=function(o){
        if (this.sp$) {
          if (!is$(o,{t:Sequential}))return false;
          if (!o.size===this.size)return false;
          var oi=o.iterator();
          var ot=this.iterator();
          var ni=oi.next();
          var nt=ot.next();
          while (ni!==finished()) {
            if (!ni.equals(nt))return false;
            ni=oi.next();
            nt=ot.next();
          }
          return true;
        }
        return this.elem$.equals(o);
      }
      tuple.equals.$crtmm$=List.$$.prototype.equals.$crtmm$;
      tuple.withTrailing=function(a,b){
        if (this.sp$) {
          return tpl$(this.elem$,this.sp$.withTrailing(a,b));
        }
        var e2=this.elem$.slice(0);e2.push(a);
        var t2=this.tps$.l.slice(0);t2.push(b.Other$withTrailing);
        return tpl$(e2);
      }
      tuple.withTrailing.$crtmm$=Sequential.$$.prototype.withTrailing.$crtmm$;
      tuple.longerThan=function(i){return this.elem$.longerThan(i);}
      tuple.longerThan.$crtmm$=Iterable.$$.prototype.longerThan.$crtmm$;
      tuple.shorterThan=function(i){return this.elem$.shorterThan(i);}
      tuple.shorterThan.$crtmm$=Iterable.$$.prototype.shorterThan.$crtmm$;
      atr$(tuple,'hash',function(){
        return this.elem$.hash+(this.sp$?this.sp$.hash:0);
      },undefined,List.$$.prototype.$prop$getHash.$crtmm$);
      atr$(tuple,'rest',function(){
        return this.elem$.size===1?this.sp$||empty():tpl$(this.elem$.slice(1),this.sp$);
      },undefined,Tuple.$$.prototype.$prop$getRest.$crtmm$);
      atr$(tuple,'size',function(){
        return this.elem$.size+(this.sp$?this.sp$.size:0);
      },undefined,Tuple.$$.prototype.$prop$getSize.$crtmm$);
      atr$(tuple,'lastIndex',function(){
        return this.elem$.size-1+(this.sp$?this.sp$.size:0);
      },undefined,Tuple.$$.prototype.$prop$getLastIndex.$crtmm$);
      atr$(tuple,'last',function(){
        return this.sp$?this.sp$.last:this.elem$[this.elem$.size-1];
      },undefined,Tuple.$$.prototype.$prop$getLast.$crtmm$);
      atr$(tuple,'string',function(){
        return '['+commaList(this.elem$)+(this.sp$?', '+commaList(this.sp$):'')+']';
      },undefined,Tuple.$$.prototype.$prop$getString.$crtmm$);
      tuple.nativeArray=function(){
        if (this.sp$) {
          var e=new Array(this.elem$.size+this.sp$.size);
          for (var i=0;i<this.elem$.size;i++) {
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
$init$tpl$();
ex$.tpl$=tpl$;

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
    if (args.t===Sequential || args.t===Sequence || args.t===Range) {
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
