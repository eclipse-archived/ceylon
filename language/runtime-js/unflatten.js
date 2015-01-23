function unflatten(ff, $$$mptypes) {
  if (ff.$flattened$)return ff.$flattened$;
  if (ff.jsc$)ff=ff.jsc$;
  var mm=getrtmm$$(ff);
  if (mm && mm.ps) {
    var ru=function ru(seq,$mptypes) {
      if (seq===undefined || seq.size === 0) { return ff(); }
      var pmeta = mm.ps;
      var _lim=Math.max(pmeta.length,seq.size);
      var a = [];
      for (var i = 0; i < _lim; i++) {
        if (pmeta[i]&&pmeta[i]['seq']) {
          a.push(seq.skip(i).sequence());
          break;//we're done
        } else if (seq.size > i) {
          a.push(seq.$_get(i));
        }
      }
      if ($mptypes && mm.tp)a.push($mptypes);
      return ff.apply(ru, a);
    }
    ru.$crtmm$={$t:mm.$t,ps:[{$t:{t:'T',l:[]}}]};
    for (var i=0;i<mm.ps.length;i++){
      ru.$crtmm$.ps[0].$t.l.push(mm.ps[i].$t);
    }
  } else {
    var ru=function ru(seq) {
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
