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
    return ArraySequence(arr, $$$mptypes);
}
internalSort.$$metamodel$$=function(){return{
  $an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','internalSort'],
  $ps:[{$nm:'comparing',$t:{t:Callable,a:{Return:{t:Comparison},Arguments:{t:'T',l:['Element','Element']}}},$mt:'prm'},{$nm:'elements',$t:{t:Iterable,a:{Absent:{t:Null},Element:'Element'}},$mt:'prm'}],
  $tp:{Element:{}}, $t:{t:Sequential,a:{Absent:{t:Null},Element:'Element'}}
}};

function flatten(tf, $$$mptypes) {
  function rf() {
    var t = getEmpty();
    var e = null;
    var argc = arguments.length;
    var last = argc>0 ? arguments[argc-1] : undefined;
    if (typeof(last) === 'object' && typeof(last.Args) === 'object' && (last.Args.t==='T'||typeof(last.Args.t) === 'function')) {
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
      t = Tuple(arguments[i], t, {First:c, Element:e, Rest:rest});
    }
    return tf(t, t.$$targs$$);
  };
  rf.$$targs$$=$$$mptypes;
  return rf;
}
flatten.$$metamodel$$=function(){return{
  $an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','flatten'],
  $tp:{Return:{},Args:{'satisfies':[{t:Sequential,a:{Absent:{t:Null},Element:{t:Anything}}}]}},
  $t:{t:Callable,a:{Return:'Return',Arguments:{t:Sequential,a:{Absent:{t:Null},Element:{t:Anything}}}}},
  $p:[{$nm:'tupleFunction',$t:{t:Callable,a:{Return:'Return',Arguments:{t:'T',l:[{t:Sequential,a:{Absent:{t:Null},Element:{t:Anything}}}]}}},$mt:'prm'}]
}};

function unflatten(ff, $$$mptypes) {
  if (typeof(ff.$$metamodel$$)==='function')ff.$$metamodel$$=ff.$$metamodel$$();
  if (ff.$$metamodel$$ && ff.$$metamodel$$.$ps) {
    var ru=function ru(seq) {
      if (seq===undefined || seq.size === 0) { return ff(); }
      var pmeta = ff.$$metamodel$$.$ps;
      var _lim=Math.max(pmeta.length,seq.size);
      var a = [];
      for (var i = 0; i < _lim; i++) {
        if (pmeta[i]&&pmeta[i]['seq']) {
          a.push(seq.skipping(i).sequence);
        } else if (seq.size > i) {
          a.push(seq.$get(i));
        }
      }
      a.push(ru.$$targs$$);
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
  ru.$$targs$$=$$$mptypes;
  return ru;
}
unflatten.$$metamodel$$=function(){return{
  $an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','unflatten'],
  $tp:{Return:{},Args:{'satisfies':[{t:Sequential,a:{Absent:{t:Null},Element:{t:Anything}}}]}},
  $ps:[{$nm:'flatFunction',$t:{t:Callable,a:{Return:'Return',Arguments:{t:Sequential,a:{Absent:{t:Null},Element:{t:Anything}}}}},$mt:'prm'}],
  $t:{t:Callable,a:{Return:'Return',Arguments:{t:'T',l:[{t:Sequential,a:{Absent:{t:Null},Element:{t:Anything}}}]}}}
}};
exports.flatten=flatten;
exports.unflatten=unflatten;

//internal
function toTuple(iterable) {
  var seq = iterable.sequence;
  return Tuple(seq.first, seq.rest.sequence,
    {First:seq.$$targs$$.Element, Element:seq.$$targs$$.Element, Rest:{t:Sequential, a:seq.$$targs$$}});
}
exports.toTuple=toTuple;

function integerRangeByIterable(range, step, $$$mptypes) {
    return Comprehension(function(){
        var a = range.first;
        var b = range.last;
        if (a>b) {
            a += step;
            return function() {
                a -= step;
                return a<b ? getFinished() : a;
            }
        }
        a-=step;
        return function() {
            a += step;
            return a>b ? getFinished() : a;
        }
    }, {Element:range.$$targs$$.Element, Absent:range.$$targs$$.Absent});
}
integerRangeByIterable.$$metamodel$$=function(){return{
  $an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','integerRangeByIterable'],
  $tp:{Element:{'satisfies':[{t:Ordinal,a:{Other:'Element'}},{t:Comparable,a:{Other:'Element'}}]}},
  $t:{t:Iterable,a:{Absent:{t:Nothing},Element:'Element'}},
  $ps:[{$nm:'range',$t:{t:Range,a:{Element:'Element'}},$mt:'prm'},{$nm:'step',$t:{t:Integer},$mt:'prm'}]
}};
exports.integerRangeByIterable=integerRangeByIterable;
