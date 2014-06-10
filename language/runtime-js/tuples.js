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
  that.$_get=function(i){
    var e=elems[i]
    return e===undefined?null:e;
  };
  that.$_get.$crtmm$=Tuple.$$.prototype.$_get.$crtmm$;
  that.iterator=function(){ return elems.iterator(); }
  that.iterator.$crtmm$=Tuple.$$.prototype.iterator.$crtmm$;
  that.contains=function(i) { return elems.contains(i); }
  that.contains.$crtmm$=Tuple.$$.prototype.contains.$crtmm$;
  that.withLeading=function(a,b) { return elems.withLeading(a,b); }
  that.withLeading.$crtmm$=Tuple.$$.prototype.withLeading.$crtmm$;
  that.span=function(a,b){ return elems.span(a,b); }
  that.span.$crtmm$=Tuple.$$.prototype.span.$crtmm$;
  that.spanTo=function(x){ return elems.spanTo(x); }
  that.spanTo.$crtmm$=Tuple.$$.prototype.spanTo.$crtmm$;
  that.spanFrom=function(x){ return elems.spanFrom(x); }
  that.spanFrom.$crtmm$=Tuple.$$.prototype.spanFrom.$crtmm$;
  that.segment=function(a,b){ return elems.segment(a,b); }
  that.segment.$crtmm$=Tuple.$$.prototype.segment.$crtmm$;
  that.equals=function(o){return elems.equals(o);}
  that.equals.$crtmm$=List.$$.prototype.equals.$crtmm$;
  that.withTrailing=function(a,b){return elems.withTrailing(a,b);}
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
    return elems.length==1?getEmpty():ArraySequence(elems.slice(1),{Element$Iterable:{t:'u',l:_t.l.slice(1)}});
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
    return elems.reversed;
  },undefined,Tuple.$$.prototype.$prop$getReversed.$crtmm$);
  return that;
}
ex$.tpl$=tpl$;
