//internal
function tpl$(elems,spread){
  if (spread!==undefined) {
    if (is$(spread,{t:Sequential})) {
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
    if (elems[i]===null) {
      types.push({t:Null});
    } else if (elems[i]===undefined) {
      types.push({t:Anything});
    } else if (elems[i].getT$all && elems[i].getT$name) {
      var _et={t:elems[i].getT$all()[elems[i].getT$name()]};
      if (elems[i].$$targs$$)_et.a=elems[i].$$targs$$;
      types.push(_et);
    } else {
      console.log("Tuple: WTF do I use for the type of " + elems[i]);
      types.push({t:Anything});
    }
  }
  //Check if I need to do this
  //if (spread && spread.$$targs$$) {
  //  types.push(spread.$$targs$$.Element$Sequence);
  //}
  types={t:'T',l:types};
  $init$Tuple();
  var that=new Tuple.$$;
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
  that.getFromFirst=function(i){
    if (spread && i>=elems.length) {
      return spread.getFromFirst(i-elems.length);
    }
    return elems[i];
  };
  that.getFromFirst.$crtmm$=Tuple.$$.prototype.getFromFirst.$crtmm$;
  that.iterator=function(){
    if (spread) {
      return ChainedIterator(elems,spread,{Element$ChainedIterator:types,Other$ChainedIterator:spread.$$targs$$.Element$Sequential});
    }
    return elems.iterator();
  }
  that.iterator.$crtmm$=Tuple.$$.prototype.iterator.$crtmm$;
  that.contains=function(i) { return elems.contains(i) || (spread&&spread.contains(i)); }
  that.contains.$crtmm$=Tuple.$$.prototype.contains.$crtmm$;
  that.withLeading=function(a,b){
    var e2 = elems.slice(0); e2.unshift(a);
    var t2 = types.l.slice(0); t2.unshift(b.Other$withLeading);
    return tpl$(e2,spread);
  }
  that.withLeading.$crtmm$=Tuple.$$.prototype.withLeading.$crtmm$;
  that.span=function(a,b){//from,to
    if (spread) {
      if (a>=elems.length&&b>=elems.length){
        return spread.span(a-elems.length,b-elems.length);
      }
      if (b>=elems.length) {
        var s1=elems.spanFrom(a);
        var s2=spread.spanTo(b-elems.length);
        return s1.chain(s2,{Other$chain:spread.$$targs$$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
      }
      if (a>=elems.length) {
        var s1=spread.span(a-elems.length,0);
        var s2=elems.span(elems.length-1,b)
        return s1.chain(s2,{Other$chain:types,OtherAbsent$chain:{t:Nothing}}).sequence();
      }
    }
    var r=elems.span(a,b);
    return r.size===0?empty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.span.$crtmm$=Tuple.$$.prototype.span.$crtmm$;
  that.spanTo=function(x){
    if (spread) {
      if (x<0)return empty();
      return this.span(0,x);
    }
    var r=elems.spanTo(x);
    return r.size===0?empty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.spanTo.$crtmm$=Tuple.$$.prototype.spanTo.$crtmm$;
  that.spanFrom=function(x){
    if (x===0)return that;
    if (spread) {
      if (x>=elems.length) {
        return spread.spanFrom(x-elems.length);
      } else if (x<0) {
        return this;
      }
      return elems.spanFrom(x).chain(spread,{Other$chain:spread.$$targs$$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
    }
    var r=elems.spanFrom(x);
    return r.size===0?empty():tpl$(r);
  }
  that.spanFrom.$crtmm$=Tuple.$$.prototype.spanFrom.$crtmm$;
  that.measure=function(a,b){//from,length
    if(b===0)return empty();
    if (spread) {
      if (a>=elems.length) {
          var m1=spread.measure(a-elems.length,b);
        if (b>0) {
          return m1;
        } else {
          console.log("missing tpl.measure with negative length");
        }
      }
      if (b>0 && a+b-1>=elems.length) {
        var m1=elems.measure(a,elems.length-a);
        var m2=spread.measure(0,b-m1.size);
        return m1.chain(m2,{Other$chain:spread.$$targs$$.Element$Sequence,OtherAbsent$chain:{t:Nothing}}).sequence();
      }
    }
    var r=elems.measure(a,b);
    return r.size===0?empty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.measure.$crtmm$=Tuple.$$.prototype.measure.$crtmm$;
  that.equals=function(o){
    if (spread) {
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
    return elems.equals(o);
  }
  that.equals.$crtmm$=List.$$.prototype.equals.$crtmm$;
  that.withTrailing=function(a,b){
    if (spread) {
      return tpl$(elems,spread.withTrailing(a,b));
    }
    var e2=elems.slice(0);e2.push(a);
    var t2=types.l.slice(0);t2.push(b.Other$withTrailing);
    return tpl$(e2);
  }
  that.withTrailing.$crtmm$=Sequential.$$.prototype.withTrailing.$crtmm$;
  that.chain=function(a,b){return elems.chain(a,b);}
  that.chain.$crtmm$=Iterable.$$.prototype.chain.$crtmm$;
  that.longerThan=function(i){return elems.longerThan(i);}
  that.longerThan.$crtmm$=Iterable.$$.prototype.longerThan.$crtmm$;
  that.shorterThan=function(i){return elems.shorterThan(i);}
  that.shorterThan.$crtmm$=Iterable.$$.prototype.shorterThan.$crtmm$;
  atr$(that,'hash',function(){
    return elems.hash+(spread?spread.hash:0);
  },undefined,List.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'rest',function(){
    return elems.size===1?spread||empty():tpl$(elems.slice(1),spread);
  },undefined,Tuple.$$.prototype.$prop$getRest.$crtmm$);
  atr$(that,'rest_',function(){
    return that.rest;
  },undefined,Tuple.$$.prototype.$prop$getRest.$crtmm$);
  atr$(that,'size',function(){
    return elems.size+(spread?spread.size:0);
  },undefined,Tuple.$$.prototype.$prop$getSize.$crtmm$);
  atr$(that,'lastIndex',function(){
    return elems.size-1+(spread?spread.size:0);
  },undefined,Tuple.$$.prototype.$prop$getLastIndex.$crtmm$);
  atr$(that,'last',function(){
    return spread?spread.last:elems[elems.size-1];
  },undefined,Tuple.$$.prototype.$prop$getLast.$crtmm$);
  atr$(that,'string',function(){
    return '['+commaList(elems)+(spread?', '+commaList(spread):'')+']';
  },undefined,Tuple.$$.prototype.$prop$getString.$crtmm$);
  that.nativeArray=function(){
    if (spread) {
      var e=new Array(elems.length+spread.size);
      for (var i=0;i<elems.length;i++) {
        e[i]=elems[i];
      }
      var elem;for(var iter=spread.iterator();(elem=iter.next())!==finished();) {
        e[i++]=elem;
      }
      return e;
    }
    return elems.slice(0);
  };
  that.append=function(elems){
    var na=that.nativeArray();
    for (var i=0;i<elems.size;i++) {
      na.push(elems.$_get(i));
    }
    return tpl$(na);
  };
  return that;
}
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
