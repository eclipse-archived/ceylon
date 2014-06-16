if (typeof(this.meta.$mod$imps)==='function')this.meta.$mod$imps=this.meta.$mod$imps();
var deps=this.m$['$mod-deps']||getEmpty();
if (deps !== getEmpty()) {
  var _d=[];
  for (var d in this.meta.$mod$imps) {
    var spos = d.lastIndexOf('/');
    _d.push(Importa$jsint(d.substring(0,spos), d.substring(spos+1),this,this.meta.$mod$imps[d]));
  }
  deps = _d.length===0?getEmpty():ArraySequence(_d,{Element$ArraySequence:{t:Import$meta$declaration}});
  this.m$['$mod-deps'] = deps;
}
return deps;
