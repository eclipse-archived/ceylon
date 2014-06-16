/* Metamodel module and package objects */
var $loadedModules$={};
ex$.$loadedModules$=$loadedModules$;
function $addmod$(mod, modname) {
  $loadedModules$[modname] = mod;
}
ex$.$addmod$=$addmod$;

function Modulo(meta, $$modulo){
  $init$Modulo();
  if ($$modulo===undefined)$$modulo=new Modulo.$$;
  Module$meta$declaration($$modulo);
  $$modulo.meta=meta;
  $$modulo.$anns=meta.$mod$ans$;
  var mm = meta.$CCMM$;
  if (typeof(mm)==='function') {
    mm=mm();meta.$CCMM$=mm;
  }
  var name=mm['$mod-name'];
  atr$($$modulo,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:$_String},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','name']});
  atr$($$modulo,'qualifiedName',function(){return name;},undefined,{mod:$CCMM$,$t:{t:$_String},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','qualifiedName']});
  var version=$_String(mm['$mod-version']);
  atr$($$modulo,'version',function(){return version;},undefined,{mod:$CCMM$,$t:{t:$_String},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','version']});
  atr$($$modulo,'members',function(){
    if (mm['$pks$'] === undefined) {
      mm['$pks$'] = {};
      for (mem in this.meta.$CCMM$) {
        if (typeof(mem) === 'string' && mem[0]!=='$') {
          mm['$pks$'][mem] = Paquete$jsint(mem, this, mm[mem]);
        }
      }
    }
    var m = [];
    for (mem in mm['$pks$']) {
      if (typeof(mem) === 'string') {
        m.push(mm['$pks$'][mem]);
      }
    }
    return m.length===0?getEmpty():ArraySequence(m,{Element$ArraySequence:{t:Package$meta$declaration}});
  },undefined,{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Package$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','members']});
  atr$($$modulo,'dependencies',function(){
    if (typeof(meta.$mod$imps)==='function')meta.$mod$imps=meta.$mod$imps();
    var deps=mm['$mod-deps']||getEmpty();
    if (deps !== getEmpty()) {
      var _d=[];
      for (var d in meta.$mod$imps) {
        var spos = d.lastIndexOf('/');
        _d.push(Importa$jsint(d.substring(0,spos), d.substring(spos+1),this,meta.$mod$imps[d]));
      }
      deps = _d.length===0?getEmpty():ArraySequence(_d,{Element$ArraySequence:{t:Import$meta$declaration}});
      mm['$mod-deps'] = deps;
    }
    return deps;
  },undefined,{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Import$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','dependencies']});
  function findPackage(pknm){
    if (mm['$pks$'] === undefined) this.members;
    if (pknm==='$')pknm='ceylon.language';
    var pk = mm['$pks$'][pknm];
    return pk===undefined ? null : pk;
  }
  $$modulo.findPackage=findPackage;
  findPackage.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:$_String}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findPackage']};
  function findImportedPackage(pknm){
    var pk = this.findPackage(pknm);
    if (pk) return pk;
    if (pknm.match('^ceylon\\.language')) {
      var clmod=getModules$meta().find('ceylon.language', $CCMM$['$mod-version']);
      var deps=clmod.dependencies;
      if (deps===getEmpty())deps=[];
      deps.push(Importa$jsint('ceylon.language', $CCMM$['$mod-version']),clmod);
      return clmod.findPackage(pknm);
    }
    var deps=this.dependencies;
    for (var i=0; i < deps.length; i++) {
      pk = deps[i].container.findImportedPackage(pknm);
      if (pk)return pk;
    }
    return null;
  }
  $$modulo.findImportedPackage=findImportedPackage;
  findImportedPackage.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:$_String}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findImportedPackage']};};
function annotations($$$mptypes){
  var anns = this.meta.$mod$ans$;
  if (typeof(anns) === 'function') {
    anns = anns();
    this.meta.$mod$ans$=anns;
  } else if (anns === undefined) {
    anns = [];
  }
  var r = [];
  for (var i=0; i < anns.length; i++) {
    var an = anns[i];
    if (is$(an, $$$mptypes.Annotation$annotations)) r.push(an);
  }
  return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Annotation$annotations});
}
  $$modulo.annotations=annotations;
  atr$($$modulo,'string',function(){return "module " + this.name+"/" + this.version;},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['ceylon.language','Object','$at','string']});

  annotations.$crtmm$={mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Annotation'}},$ps:[],$cont:Modulo,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','annotations']};

  function resourceByPath(_path) {
    var mpath;
    var sep = getOperatingSystem().fileSeparator;
    if ($$modulo.name === 'default' && $$modulo.version=='unversioned') {
      mpath = $$modulo.name;
    } else {
      mpath = $$modulo.name.replace(/\./g,sep) + sep + $$modulo.version;
    }
    if (_path[0]===sep) {
      mpath += _path;
    } else {
      mpath += sep + _path;
    }
    if (getRuntime().name === 'node.js') {
      var _fr=require;//this is so that requirejs leaves us the fuck alone
      var pm=_fr('path');
      var mods=process.env.NODE_PATH.split(getOperatingSystem().pathSeparator);
      var fs=_fr('fs');
      for (var i=0; i<mods.length; i++) {
        var fp = pm.resolve(mods[i], mpath);
        if (fs.existsSync(fp)) {
          var f = fs.statSync(fp);
          if (f && f.isFile()) {
            return JsResource('file:'+fp);
          }
        }
      }
    } else if (getRuntime().name === 'Browser') {
      return JsResource(require.toUrl(mpath));
    } else {
      print("Resources unsupported in this environment.");
    }
    return null;
  }
  $$modulo.resourceByPath=resourceByPath;
  resourceByPath.$crtmm$=function(){return {mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Resource}]},$ps:[{$nm:'path',$mt:'prm',$t:{t:$_String}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','resourceByPath']};};
  return $$modulo;
}
Modulo.$crtmm$={mod:$CCMM$,'super':{t:Basic},satisfies:[{t:Module$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Module']};
ex$.Modulo=Modulo;
function $init$Modulo(){
    if (Modulo.$$===undefined){
        initTypeProto(Modulo,'Modulo',Basic,$init$Module$meta$declaration());
    }
    return Modulo;
}
ex$.$init$Modulo=$init$Modulo;
$init$Modulo();

