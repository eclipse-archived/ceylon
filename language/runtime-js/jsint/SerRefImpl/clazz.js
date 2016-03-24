var _inst=this.$$targs$$.Instance$SerRefImpl;
if (typeof(_inst.t)!=='function') {
  throw new Error("WTF?");
}
var mm=getrtmm$$(_inst.t);
var args;
if (mm && mm.ps) {
  args={t:'T',l:[]};
  for (var i=0; i < mm.ps.length; i++) {
    var pt=mm.ps[i].$t;
    if (typeof pt === 'string') {
      pt=this.instance().$$targs$$[pt];
      if (!pt)console.log("AGUAS! no encuentro tipo de parametro " + mm.ps[i].$t + " en " + /*require('util').inspect(*/this.instance().$$targs$$);
    }
    args.l.push(pt);
  }
} else {
  args={t:Empty};
}
return AppliedClass$jsint(_inst.t,{Type$AppliedClass:_inst,Arguments$AppliedClass:args},_inst.a);
