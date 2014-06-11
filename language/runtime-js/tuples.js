//internal
function tpl$(elems,types,spread){
  if (spread!==undefined) {
    var iter=spread.iterator();
    for (var e=iter.next();e!==getFinished();e=iter.next()) {
      elems.push(e);
    }
  }
  if (elems.length===0)return getEmpty();
  if (types===undefined || types.t!=='T') {
    types=[];
    for (var i=0; i < elems.length; i++){
      if (elems[i]===null) {
        types.push({t:Null});
      } else if (elems[i].getT$all && elems[i].getT$name) {
        types.push({t:elems[i].getT$all()[elems[i].getT$name()]});
      } else {
        console.log("WTF do I use for the type of " + elems[i]);
        types.push({t:Anything});
      }
    }
    types={t:'T',l:types};
  }
  $init$Tuple();
  var that=new Tuple.$$;
  that.$$targs$$=types;
  that.$opt=1;
  $_Object(that);
  var _t={t:'u',l:types.l};
  Sequence({Element$Sequence:_t},that);
  elems.$$targs$$={Element$Iterable:_t,Element$Sequential:_t,Element$List:_t,Element$Array:_t,Element$Sequence:_t,Absent$Iterable:{t:Nothing},
    Element$Collection:_t,Key$Correspondence:{t:Integer},Item$Correspondence:_t};
  set_type_args(that,elems.$$targs$$);
  that.$elems=elems;
  that.first_=elems[0];
  that.elementAt=function(i){
    var e=elems[i]
    return e===undefined?null:e;
  };
  that.elementAt.$crtmm$=Tuple.$$.prototype.elementAt.$crtmm$;
  that.iterator=function(){ return elems.iterator(); }
  that.iterator.$crtmm$=Tuple.$$.prototype.iterator.$crtmm$;
  that.contains=function(i) { return elems.contains(i); }
  that.contains.$crtmm$=Tuple.$$.prototype.contains.$crtmm$;
  that.withLeading=function(a,b){
    var e2 = elems.slice(0); e2.unshift(a);
    var t2 = _t.l.slice(0); t2.unshift(b.Other$withLeading);
    return tpl$(e2,{t:'T',l:t2});
  }
  that.withLeading.$crtmm$=Tuple.$$.prototype.withLeading.$crtmm$;
  that.span=function(a,b){
    var r=elems.span(a,b);
    return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.span.$crtmm$=Tuple.$$.prototype.span.$crtmm$;
  that.spanTo=function(x){
    var r=elems.spanTo(x);
    return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.spanTo.$crtmm$=Tuple.$$.prototype.spanTo.$crtmm$;
  that.spanFrom=function(x){
    var r=elems.spanFrom(x);
    return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.spanFrom.$crtmm$=Tuple.$$.prototype.spanFrom.$crtmm$;
  that.segment=function(a,b){
    var r=elems.segment(a,b);
    return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:_t});
  }
  that.segment.$crtmm$=Tuple.$$.prototype.segment.$crtmm$;
  that.equals=function(o){return elems.equals(o);}
  that.equals.$crtmm$=List.$$.prototype.equals.$crtmm$;
  that.withTrailing=function(a,b){
    var e2=elems.slice(0);e2.push(a);
    var t2=_t.l.slice(0);t2.push(b.Other$withTrailing);
    return tpl$(e2,{t:'T',l:t2});
  }
  that.withTrailing.$crtmm$=List.$$.prototype.withTrailing.$crtmm$;
  that.chain=function(a,b){return elems.chain(a,b);}
  that.chain.$crtmm$=Iterable.$$.prototype.chain.$crtmm$;
  that.longerThan=function(i){return elems.longerThan(i);}
  that.longerThan.$crtmm$=Iterable.$$.prototype.longerThan.$crtmm$;
  that.shorterThan=function(i){return elems.shorterThan(i);}
  that.shorterThan.$crtmm$=Iterable.$$.prototype.shorterThan.$crtmm$;
  atr$(that,'hash',function(){
    return elems.hash;
  },undefined,List.$$.prototype.$prop$getHash.$crtmm$);
  atr$(that,'rest',function(){
    return elems.length===1?getEmpty():tpl$(elems.slice(1),{t:'T',l:_t.l.slice(1)});
  },undefined,Tuple.$$.prototype.$prop$getRest.$crtmm$);
  atr$(that,'size',function(){
    return elems.length;
  },undefined,Tuple.$$.prototype.$prop$getSize.$crtmm$);
  atr$(that,'lastIndex',function(){
    return elems.length-1;
  },undefined,Tuple.$$.prototype.$prop$getLastIndex.$crtmm$);
  atr$(that,'last',function(){
    return elems[elems.length-1];
  },undefined,Tuple.$$.prototype.$prop$getLast.$crtmm$);
  atr$(that,'reversed',function(){
    return ArraySequence(elems.reversed,{Element$ArraySequence:_t});
  },undefined,Tuple.$$.prototype.$prop$getReversed.$crtmm$);
  return that;
}
ex$.tpl$=tpl$;
