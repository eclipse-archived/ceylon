/* Metamodel module and package objects */
var $loadedModules$={};
exports.$loadedModules$=$loadedModules$;
function $addmod$(mod, modname) {
  $loadedModules$[modname] = mod;
}
exports.$addmod$=$addmod$;
function modules$2(){
    var $$modules=new modules$2.$$;
    $defat($$modules,'list',function(){
        var mods=[];
        for (var m in $loadedModules$) {
          var slashPos = m.lastIndexOf('/');
          mods.push(this.find(m.substring(0,slashPos), m.substring(slashPos+1)));
        }
        return ArraySequence(mods,{Element$Iterable:{t:Module$meta$declaration}});
    },undefined,{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Module$meta$declaration}}},$cont:modules$2,$an:function(){return[shared()];},d:['ceylon.language.meta','modules','$at','list']});
    function find(name,version){
        var modname = name + "/" + (version?version:"unversioned");
        var lm = $loadedModules$[modname];
        if (!lm) {
          var mpath;
          if (name === 'default' && version=='unversioned') {
            mpath = name + "/" + name;
          } else {
            mpath = name.replace(/\./g,'/') + '/' + version + "/" + name + "-" + version;
          }
          try {lm = require(mpath);}catch(e){return null;}
        }
        if (lm && lm.$CCMM$) {
          lm = Modulo(lm);
          $loadedModules$[modname] = lm;
        }
        return lm === undefined ? null : lm;
    }
    $$modules.find=find;
    find.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Module$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'version',$mt:'prm',$t:{t:String$}}],$cont:modules$2,$an:function(){return[shared()];},d:['ceylon.language.meta','modules','$m','find']};
    $defat($$modules,'$default',function(){
        return find('default',"unversioned");
    },undefined,{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Module$meta$declaration}]},$cont:modules$2,$an:function(){return[shared()];},d:['ceylon.language.meta','modules','$at','default']});
    return $$modules;
}
function $init$modules$meta(){
    if (modules$2.$$===undefined){
        initTypeProto(modules$2,'ceylon.language.meta::modules',Basic);
    }
    return modules$2;
}
exports.$init$modules$meta=$init$modules$meta;
$init$modules$meta();
var modules$meta=modules$2();
var getModules$meta=function(){
    return modules$meta;
}
exports.getModules$meta=getModules$meta;

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
  var name=String$(mm['$mod-name']);
  $defat($$modulo,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','name']});
  $defat($$modulo,'qualifiedName',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','qualifiedName']});
  var version=String$(mm['$mod-version']);
  $defat($$modulo,'version',function(){return version;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','version']});
  $defat($$modulo,'members',function(){
    if (mm['$pks$'] === undefined) {
      mm['$pks$'] = {};
      for (mem in this.meta.$CCMM$) {
        if (typeof(mem) === 'string' && mem[0]!=='$') {
          mm['$pks$'][mem] = Paquete(mem, this, mm[mem]);
        }
      }
    }
    var m = [];
    for (mem in mm['$pks$']) {
      if (typeof(mem) === 'string') {
        m.push(mm['$pks$'][mem]);
      }
    }
    return ArraySequence(m,{Element$Iterable:{t:Package$meta$declaration}});
  },undefined,{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Package$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','members']});
  $defat($$modulo,'dependencies',function(){
    if (typeof(meta.$mod$imps)==='function')meta.$mod$imps=meta.$mod$imps();
    var deps=mm['$mod-deps']||[];
    if (typeof(deps[0]) === 'string') {
      var _d=[];
      for (var d in meta.$mod$imps) {
        var spos = d.lastIndexOf('/');
        _d.push(Importa(String$(d.substring(0,spos)), String$(d.substring(spos+1)),this,meta.$mod$imps[d]));
      }
      deps = ArraySequence(_d,{Element$Iterable:{t:Import$meta$declaration}});
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
  findPackage.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findPackage']};
  function findImportedPackage(pknm){
    var pk = this.findPackage(pknm);
    if (pk) return pk;
    if (pknm.match('^ceylon\\.language')) {
      var clmod=getModules$meta().find('ceylon.language', $CCMM$['$mod-version']);
      var deps=clmod.dependencies;
      if (deps===getEmpty())deps=[];
      deps.push(Importa('ceylon.language', $CCMM$['$mod-version']),clmod);
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
  findImportedPackage.$crtmm$=function(){return{mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findImportedPackage']};};
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
      if ($is(an, $$$mptypes.Annotation$annotations)) r.push(an);
    }
    return r.reifyCeylonType({Element$Iterable:$$$mptypes.Annotation$annotations});
  }
  $$modulo.annotations=annotations;
  $defat($$modulo,'string',function(){return String$("module " + this.name+"/" + this.version);},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['ceylon.language','Object','$at','string']});

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
  resourceByPath.$crtmm$=function(){return {mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Resource}]},$ps:[{$nm:'path',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','resourceByPath']};};
  return $$modulo;
}
Modulo.$crtmm$={mod:$CCMM$,'super':{t:Basic},satisfies:[{t:Module$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Module']};
exports.Modulo=Modulo;
function $init$Modulo(){
    if (Modulo.$$===undefined){
        initTypeProto(Modulo,'Modulo',Basic,Module$meta$declaration);
    }
    return Modulo;
}
exports.$init$Modulo=$init$Modulo;
$init$Modulo();
function Importa(name,version,mod,anns,$$importa){
  $init$Importa();
  if ($$importa===undefined)$$importa=new Importa.$$;
  Import$meta$declaration($$importa);
  $$importa.name=name;
  $$importa.version=version;
  $$importa._cont=mod;
  $$importa.$anns=anns;
  $defat($$importa,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','name']});
  $defat($$importa,'version',function(){return version;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','version']});
  $defat($$importa,'shared',function(){
    if (typeof(this.$anns)==='function')this.$anns=this.anns();
    if (this.$anns)for (var i=0;i<this.$anns.length;i++) {
      if (this.$anns[i]===shared)return true;
    }
return false;},undefined,{mod:$CCMM$,$t:{t:Boolean$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','shared']});
    $defat($$importa,'optional',function(){
    if (typeof(this.$anns)==='function')this.$anns=this.$anns();
    if (this.$anns)for (var i=0;i<this.$anns.length;i++) {
      if (this.$anns[i]===optional)return true;
    }
return version;},undefined,{mod:$CCMM$,$t:{t:Boolean$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','optional']});
    $defat($$importa,'container',function(){
      if (this._cont===undefined) {
          this._cont = getModules$meta().find(this.name,this.version);
      }
      return this._cont;
    },undefined,{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','container']});
  $defat($$importa,'string',function(){
    return String$("import " + name + "/" + version);
  },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:Importa,d:['ceylon.language','Object','$at','string']};});
    return $$importa;
}
Importa.$crtmm$={mod:$CCMM$,'super':{t:Basic},satisfies:[{t:Import$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Import']};
exports.Importa=Importa;
function $init$Importa(){
    if (Importa.$$===undefined){
        initTypeProto(Importa,'Importa',Basic,Import$meta$declaration);
    }
    return Importa;
}
exports.$init$Importa=$init$Importa;
$init$Importa();
function Paquete(name, container, pkg, $$paquete){
    $init$Paquete();
    if ($$paquete===undefined)$$paquete=new Paquete.$$;
    Package$meta$declaration($$paquete);
    $$paquete.pkg=pkg;
    var name=name;
    //determine suffix for declarations
    var suffix = '';
    if (name!==container.name) {
      var _s = name.substring(container.name.length);
      suffix = _s.replace(/\./g, '$');
    }
    $$paquete.suffix=suffix;
    $defat($$paquete,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','name']});
    $defat($$paquete,'qualifiedName',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','qualifiedName']});
    var container=container;
    $defat($$paquete,'container',function(){return container;},undefined,{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','container']});
    function members($$$mptypes){
      var filter=[];
      if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('mthd');
      if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('attr','gttr','obj');
      if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('cls');
      if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('ifc');
      if (extendsType({t:AliasDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('als');
      var r=[];
      for (var mn in this.pkg) {
        var m = this.pkg[mn];
        var mt = m['$mt'];
        if (filter.indexOf(mt)>=0 && m['$an'] && m['$an']['shared']) {
          if (mt === 'mthd') {
            r.push(OpenFunction(this, m));
          } else if (mt==='cls') {
            r.push(OpenClass(this, m));
          } else if (mt==='ifc') {
            r.push(OpenInterface(this, m));
          } else if (mt==='attr'||mt==='gttr'||mt==='obj') {
            r.push(OpenValue(this, m));
          } else if (mt==='als') {
            r.push(OpenAlias(_findTypeFromModel(this,m)));
          }
        }
      }
      return r.reifyCeylonType({Element$Iterable:$$$mptypes.Kind$members});
    }
    $$paquete.members=members;
    members.$crtmm$={mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','members']};
  function annotatedMembers($$$mptypes){
    var ms=this.members({Kind$members:$$$mptypes.Kind$annotatedMembers});
    if (ms.length>0) {
      var rv=[];
      for (var i=0; i < ms.length; i++) {
        if (ms[i].tipo && ms[i].tipo.$crtmm$) {
          var mm=getrtmm$$(ms[i].tipo);
          var ans=mm.$an;
          if (typeof(ans)==='function'){ans=ans();mm.$an=ans;}
          if (ans) for (var j=0; j<ans.length;j++) {
            if ($is(ans[j],$$$mptypes.Annotation$annotatedMembers)) {
              rv.push(ms[i]);
              break;
            }
          }
        }
      }
      return rv.reifyCeylonType({Element$Iterable:$$$mptypes.Kind$annotatedMembers});
    }
    return getEmpty();
  }
  $$paquete.annotatedMembers=annotatedMembers;
  annotatedMembers.$crtmm$={mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','annotatedMembers']};
    function getMember(name$3,$$$mptypes){
      var m = this.pkg[name$3];
      if (m) {
        var mt = m['$mt'];
        //There's a member alright, but check its type
        if (extendsType({t:ValueDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='attr'||m==='gttr'||m==='obj') {
            return OpenValue(this, m);
          }
        } else if (extendsType({t:FunctionDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='mthd') {
            return OpenFunction(this, m);
          }
        } else if (extendsType({t:FunctionOrValueDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='attr'||m==='gttr'||m==='obj') {
            return OpenValue(this, m);
          } else if (mt==='mthd') {
            return OpenFunction(this, m);
          }
        } else if (extendsType({t:ClassDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='cls') {
            return OpenClass(this, m);
          }
        } else if (extendsType({t:InterfaceDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='ifc') {
            return OpenInterface(this, m);
          }
        } else if (extendsType({t:ClassOrInterfaceDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='ifc') {
            return OpenInterface(this, m);
          } else if (mt==='cls') {
            return OpenClass(this, m);
          }
        } else if (extendsType({t:AliasDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          if (mt==='als')
          return OpenAlias(_findTypeFromModel(this,m));
        } else {
console.log("WTF do I do with this " + name$3 + " Kind " + className($$$mptypes.Kind$getMember));
        }
      }
      return null;
    }
    $$paquete.getMember=getMember;
    getMember.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getMember']};
    function getValue(name$4) {
      var m = this.pkg[name$4];
      if (m && (m['$mt']==='attr' || m['$mt']==='gttr' || m['$mt'] === 'obj')) {
        return OpenValue(this, m);
      }
      return null;
    }
    $$paquete.getValue=getValue;
    getValue.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:ValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getValue']};
    function getClassOrInterface(name$5){
      var ci = this.pkg[name$5];
      if (ci && ci['$mt']==='cls') {
        return OpenClass(this, ci);
      } else if (ci && ci['$mt']==='ifc') {
        return OpenInterface(this, ci);
      }
      return null;
    }
    $$paquete.getClassOrInterface=getClassOrInterface;
    getClassOrInterface.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:ClassOrInterfaceDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getClassOrInterface']};
  function getAlias(an) {
    var al=this.pkg[an];
    if (al && al.$mt==='als') {
      var rta = _findTypeFromModel(this, al);
      return OpenAlias(rta);
    }
    return null;
  }
  $$paquete.getAlias=getAlias;
  getAlias.$crtmm$=function(){return{mod:$CCMM$,d:['ceylon.language.meta.declaration','Package','$m','getAlias']};};
    function getFunction(name$6){
      var f = this.pkg[name$6];
      if (f && f['$mt']==='mthd') {
        return OpenFunction(this, f);
      }
      return null;
    }
    $$paquete.getFunction=getFunction;
    getFunction.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:FunctionDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getFunction']};
    function annotations($$$mptypes){
      var _k = '$pkg$ans$' + this.name.replace(/\./g,'$');
      var anns = this.container.meta[_k];
      if (typeof(anns) === 'function') {
        anns = anns();
        this.container.meta[_k]=anns;
      } else if (anns === undefined) {
        anns = [];
      }
      var r = [];
      for (var i=0; i < anns.length; i++) {
        var an = anns[i];
        if ($is(an, $$$mptypes.Annotation$annotations)) r.push(an);
      }
      return r.reifyCeylonType({Element$Iterable:$$$mptypes.Annotation$annotations});
    }
    $$paquete.annotations=annotations;
    annotations.$crtmm$={mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Annotation'}},$ps:[],$cont:Paquete,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','annotations']};
  $defat($$paquete,'string',function(){return String$("package " + this.name);},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['ceylon.language','Object','$at','string']});
    return $$paquete;
}
Paquete.$crtmm$={mod:$CCMM$,'super':{t:Basic},satisfies:[{t:Package$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Package']};
exports.Paquete=Paquete;
function $init$Paquete(){
    if (Paquete.$$===undefined){
        initTypeProto(Paquete,'Paquete',Basic,Package$meta$declaration);
    }
    return Paquete;
}
exports.$init$Paquete=$init$Paquete;
$init$Paquete();

