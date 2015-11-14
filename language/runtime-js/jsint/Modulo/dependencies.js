if (typeof(this.meta.$mod$imps)==='function')this.meta.$mod$imps=this.meta.$mod$imps();
var deps=this.m$.$mdep$;
if (deps===undefined){
  var _d=[];
  var hasLangMod=false;
  for (var d in this.meta.$mod$imps) {
    var spos = d.lastIndexOf('/');
    if (!hasLangMod && d.startsWith('ceylon.language/'))hasLangMod=true;
    _d.push(Importa$jsint(d.substring(0,spos), d.substring(spos+1),this,this.meta.$mod$imps[d]));
  }
  if (!hasLangMod) {
    _d.push(Importa$jsint('ceylon.language',$CCMM$()['$mod-version'],this,[]));
  }
  deps = _d.length===0?empty():ArraySequence(_d,{Element$ArraySequence:{t:Import$meta$declaration}});
  this.m$.$mdep$=deps;
}
return deps;
