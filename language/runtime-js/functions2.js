function internalSort(comp, elems, $$$mptypes) {
    if (elems===undefined) {return getEmpty();}
    var arr = [];
    var it = elems.iterator();
    var e;
    while ((e=it.next()) !== getFinished()) {arr.push(e);}
    if (arr.length === 0) {return getEmpty();}
    arr.sort(function(a, b) {
        var cmp = comp(a,b);
        return (cmp===larger) ? 1 : ((cmp===smaller) ? -1 : 0);
    });
    return ArraySequence(arr, {Element$Iterable:$$$mptypes.Element$internalSort});
}
internalSort.$crtmm$=function(){return{
  $an:function(){return[shared()];},mod:$CCMM$,d:['ceylon.language','internalSort'],
  $ps:[{$nm:'comparing',$t:{t:Callable,a:{Return$Callable:{t:Comparison},Arguments$Callable:{t:'T',l:['Element$internalSort','Element$internalSort']}}},$mt:'prm'},{$nm:'elements',$t:{t:Iterable,a:{Absent$Iterable:{t:Null},Element$Iterable:'Element$internalSort'}},$mt:'prm'}],
  $tp:{Element$internalSort:{}}, $t:{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:'Element$internalSort'}}
}};

function flatten(tf, $$$mptypes) {
  function rf() {
    var t = getEmpty();
    var e = null;
    var argc = arguments.length;
    var last = argc>0 ? arguments[argc-1] : undefined;
    if (typeof(last) === 'object' && typeof(last.Args$flatten) === 'object' && (last.Args$flatten.t==='T'||typeof(last.Args$flatten.t) === 'function')) {
      argc--;
    }
    for (var i=argc-1; i>=0; i--) {
      var c = arguments[i]===null ? Null :
        arguments[i] === undefined ? Empty :
        arguments[i].getT$all ? arguments[i].getT$all() :
        Anything;
      if (e === null) {
        e = c;
      } else if (e.t === 'u' && e.l.length > 0) {
        var l = [c];
        for (var j=0; j < e.l.length; j++) {
          l[j+1] = e.l[j];
        }
      } else {
        e = {t:'u', l:[e, c]};
      }
      var rest;
      if (t === getEmpty()) {
        rest={t:Empty};
      } else {
        rest={t:Tuple, a:t.$$targs$$};
      }
      t = Tuple(arguments[i], t, {First$Tuple:c, Element$Tuple:e, Rest$Tuple:rest});
    }
    return tf(t, t.$$targs$$);
  };
  rf.$$targs$$={Return$Callable:$$$mptypes.Return$flatten,Arguments$Callable:$$$mptypes.Args$flatten};
  return rf;
}
flatten.$crtmm$=function(){return{
  $an:function(){return[shared()];},mod:$CCMM$,d:['ceylon.language','flatten'],
  $tp:{Return$flatten:{},Args$flatten:{'satisfies':[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}},
  $t:{t:Callable,a:{Return$Callable:'Return',Arguments$Callable:{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}}},
  $p:[{$nm:'tupleFunction',$t:{t:Callable,a:{Return$Callable:'Return$flatten',Arguments$Callable:{t:'T',l:[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}}},$mt:'prm'}]
}};

function unflatten(ff, $$$mptypes) {
  if (getrtmm$$(ff) && ff.$crtmm$.$ps) {
    var ru=function ru(seq,$mptypes) {
      if (seq===undefined || seq.size === 0) { return ff(); }
      var pmeta = ff.$crtmm$.$ps;
      var _lim=Math.max(pmeta.length,seq.size);
      var a = [];
      for (var i = 0; i < _lim; i++) {
        if (pmeta[i]&&pmeta[i]['seq']) {
          a.push(seq.skip(i).sequence);
          break;//we're done
        } else if (seq.size > i) {
          a.push(seq.$get(i));
        }
      }
      if ($mptypes && ff.$crtmm$.$tp)a.push($mptypes);
      return ff.apply(ru, a);
    }
  } else {
    var ru=function ru(seq) {
      if (seq===undefined || seq.size === 0) { return ff(); }
      var a = [];
      for (var i = 0; i < seq.size; i++) {
        a[i] = seq.$get(i);
      }
      a[i]=ru.$$targs$$;
      return ff.apply(ru, a);
    }
  }
  ru.$$targs$$={Return$Callable:$$$mptypes.Return$unflatten,Arguments$Callable:{t:'T',l:[$$$mptypes.Args$unflatten]}};
  return ru;
}
unflatten.$crtmm$=function(){return{
  $an:function(){return[shared()];},mod:$CCMM$,d:['ceylon.language','unflatten'],
  $tp:{Return$unflatten:{},Args$unflatten:{'satisfies':[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}},
  $ps:[{$nm:'flatFunction',$t:{t:Callable,a:{Return$Callable:'Return$unflatten',Arguments$Callable:{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}}},$mt:'prm'}],
  $t:{t:Callable,a:{Return$Callable:'Return$unflatten',Arguments$Callable:{t:'T',l:[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}}}
}};
ex$.flatten=flatten;
ex$.unflatten=unflatten;

//internal
function tpl$(elems,types,spread){
  $init$Tuple();
  var that=new Tuple.$$;
  that.$$targs$$=types;
  that.$opt=1;
  if (spread!==undefined) {
    var iter=spread.iterator();
    for (var e=iter.next();e!==getFinished();e=iter.next()) {
      elems.push(e);
    }
  }
  var _t=types.Element$Tuple;
  if (_t===undefined)_t=types.Element$Sequence;
  if (_t===undefined)_t=types.Element$List;
  if (_t===undefined)_t=types.Element$Collection;
  if (_t===undefined)_t=types.Element$Iterable;
  _t={t:_t};
  elems.$$targs$$={Element$Iterable:_t,Element$Sequential:_t,Element$List:_t,Element$Array:_t,Element$Sequence:_t,Absent$Iterable:{t:Nothing},
    Element$Collection:_t,Key$Correspondence:{t:Integer},Item$Correspondence:_t};
  that.$elems=elems;
  that.first_=elems[0];
  that.$get=function(i) { return elems[i]||null; }
  that.$get.$crtmm$=Tuple.$$.prototype.$get.$crtmm$;
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
  $defat(that,'hash',function(){
    return elems.hash;
  },undefined,List.$$.prototype.$prop$getHash.$crtmm$);
  $defat(that,'rest',function(){
    return elems.length==1?getEmpty():elems.slice(1).reifyCeylonType({Element$Sequence:_t.t==='u'?{t:'u',l:_t.l.slice(1)}:_t});
  },undefined,Tuple.$$.prototype.$prop$getRest.$crtmm$);
  $defat(that,'size',function(){
    return elems.length;
  },undefined,Tuple.$$.prototype.$prop$getSize.$crtmm$);
  $defat(that,'lastIndex',function(){
    return elems.length-1;
  },undefined,Tuple.$$.prototype.$prop$getLastIndex.$crtmm$);
  $defat(that,'last',function(){
    return elems[elems.length-1];
  },undefined,Tuple.$$.prototype.$prop$getLast.$crtmm$);
  $defat(that,'reversed',function(){
    return elems.reversed;
  },undefined,Tuple.$$.prototype.$prop$getReversed.$crtmm$);
  return that;
}
ex$.tpl$=tpl$;
