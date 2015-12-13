function unflatten(ff, $$$mptypes) {
  if (ff.$flattened$)return ff.$flattened$;
  if (ff.jsc$)ff=ff.jsc$;
  var mm=getrtmm$$(ff);
  if (mm && mm.ps) {
    if (mm.ps.length===0)return ff;
    var last=mm.ps[mm.ps.length-1];
    var iadic=variadicness(last.$t);
    if (is$(last,{t:Tuple}))iadic=0;
    var ru;
    if (iadic && mm.ps.length===1) {
      //Single variadic parm, passed as sequence
      ru=function rus(seq,$mptypes) {
        if (seq===undefined || seq.size === 0)seq=empty();
        return ff(seq,$mptypes);
      }
      ru.$crtmm$={$t:mm.$t,ps:[{$t:{t:last.$t.t,a:last.$t.a},nm:'unf0',mt:'prm'}]};
    } else if (iadic) {
      //Args and variadic, gets unfinished Tuple
      var cut=mm.ps.length-1;
      ru=function ruv(seq,$mptypes) {
        var a=[];
        for (var i=0;i<Math.min(seq.size,cut);i++) {
          a.push(seq.$_get(i));
        }
        if (seq.size<=cut) {
          //Fill positions of defaulted args
          for (var i=seq.size;i<cut;i++)a.push(undefined);
          a.push(empty());
        } else {
          var s=[];
          for (var i=cut;i<seq.size;i++)s.push(seq.$_get(i));
          a.push(s.$sa$(last.$t));
        }
        if ($mptypes)a.push($mptypes);
        return ff.apply(0,a);
      }
    } else {
      ru=function rut(tup,$mptypes) {
        var a=[];
        if (tup.size>mm.ps.length) {
          var _t=tup;
          for (var i=0;i<mm.ps.length-1;i++) {
            a.push(_t.head);
            _t=_t.rest;
          }
          a.push(_t);
        } else {
          for (var i=0;i<tup.size;i++) {
            a.push(tup.$_get(i));
          }
          //Fill positions of defaulted args
          for (var i=tup.size;i<mm.ps.length;i++)a.push(undefined);
          if ($mptypes)a.push($mptypes);
        }
        return ff.apply(0,a);
      }
    }
    if (!ru.$crtmm$) {
      ru.$crtmm$={$t:mm.$t,ps:[{$t:{t:'T',l:[]},nm:'unf'}]};
      for (var i=0;i<mm.ps.length;i++){
        ru.$crtmm$.ps[0].$t.l.push(mm.ps[i].$t);
      }
      if (iadic)ru.$crtmm$.ps[0].$t.l[mm.ps.length-1].seq=iadic;
    }
  } else {
    var ru=function runomm(seq) {
      if (seq===undefined || seq.size === 0) { return ff(); }
      var a = [];
      for (var i = 0; i < seq.size; i++) {
        a[i] = seq.$_get(i);
      }
      a[i]=ru.$$targs$$;
      return ff.apply(ru, a);
    }
  }
  ru.$unflattened$=ff;
  ru.$$targs$$={Return$Callable:$$$mptypes.Return$unflatten,Arguments$Callable:{t:'T',l:[$$$mptypes.Args$unflatten]}};
  return ru;
}
