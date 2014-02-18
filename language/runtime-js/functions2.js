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
flatten.$$metamodel$$=function(){return{
  $an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','flatten'],
  $tp:{Return$flatten:{},Args$flatten:{'satisfies':[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}},
  $t:{t:Callable,a:{Return$Callable:'Return',Arguments$Callable:{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}}},
  $p:[{$nm:'tupleFunction',$t:{t:Callable,a:{Return$Callable:'Return$flatten',Arguments$Callable:{t:'T',l:[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}}},$mt:'prm'}]
}};

function unflatten(ff, $$$mptypes) {
  if (typeof(ff.$$metamodel$$)==='function')ff.$$metamodel$$=ff.$$metamodel$$();
  if (ff.$$metamodel$$ && ff.$$metamodel$$.$ps) {
    var ru=function ru(seq,$mptypes) {
      if (seq===undefined || seq.size === 0) { return ff(); }
      var pmeta = ff.$$metamodel$$.$ps;
      var _lim=Math.max(pmeta.length,seq.size);
      var a = [];
      for (var i = 0; i < _lim; i++) {
        if (pmeta[i]&&pmeta[i]['seq']) {
          a.push(seq.skipping(i).sequence);
          break;//we're done
        } else if (seq.size > i) {
          a.push(seq.$get(i));
        }
      }
      if ($mptypes && ff.$$metamodel$$.$tp)a.push($mptypes);
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
unflatten.$$metamodel$$=function(){return{
  $an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','unflatten'],
  $tp:{Return$unflatten:{},Args$unflatten:{'satisfies':[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}},
  $ps:[{$nm:'flatFunction',$t:{t:Callable,a:{Return$Callable:'Return$unflatten',Arguments$Callable:{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}}},$mt:'prm'}],
  $t:{t:Callable,a:{Return$Callable:'Return$unflatten',Arguments$Callable:{t:'T',l:[{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Anything}}}]}}}
}};
exports.flatten=flatten;
exports.unflatten=unflatten;

//internal
function toTuple(iterable) {
  var seq = iterable.sequence;
  return Tuple(seq.first, seq.rest.sequence,
    {First$Tuple:seq.$$targs$$.Element$Iterable, Element$Tuple:seq.$$targs$$.Element$Iterable, Rest$Tuple:{t:Sequential, a:seq.$$targs$$}});
}
exports.toTuple=toTuple;
