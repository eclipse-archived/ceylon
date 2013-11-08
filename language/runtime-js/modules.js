/* Metamodel module and package objects */
var $loadedModules$={};
exports.$loadedModules$=$loadedModules$;
function $addmod$(mod, modname) {
  $loadedModules$[modname] = mod;
}
exports.$addmod$=$addmod$;
function modules$2(){
    var $$modules=new modules$2.$$;
    defineAttr($$modules,'list',function(){
        var mods=[];
        for (var m in $loadedModules$) {
          var slashPos = m.lastIndexOf('/');
          mods.push(this.find(m.substring(0,slashPos), m.substring(slashPos+1)));
        }
        return mods.reifyCeylonType({Element:{t:Module$meta$declaration}});
    },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Module$meta$declaration}}},$cont:modules$2,$an:function(){return[shared()];},d:['ceylon.language.meta','modules','$at','list']});
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
        if (lm && lm.$$METAMODEL$$) {
          lm = Modulo(lm);
          $loadedModules$[modname] = lm;
        }
        return lm === undefined ? null : lm;
    }
    $$modules.find=find;
    find.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Module$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'version',$mt:'prm',$t:{t:String$}}],$cont:modules$2,$an:function(){return[shared()];},d:['ceylon.language.meta','modules','$m','find']};
    defineAttr($$modules,'$default',function(){
        return find('default',"unversioned");
    },undefined,{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Module$meta$declaration}]},$cont:modules$2,$an:function(){return[shared()];},d:['ceylon.language.meta','modules','$at','default']});
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
  var mm = meta.$$METAMODEL$$;
  if (typeof(mm)==='function') {
    mm=mm();meta.$$METAMODEL$$=mm;
  }
  var name=String$(mm['$mod-name']);
  defineAttr($$modulo,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','name']});
  defineAttr($$modulo,'qualifiedName',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','qualifiedName']});
  var version=String$(mm['$mod-version']);
  defineAttr($$modulo,'version',function(){return version;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','version']});
  defineAttr($$modulo,'members',function(){
    if (mm['$pks$'] === undefined) {
      mm['$pks$'] = {};
      for (mem in this.meta.$$METAMODEL$$) {
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
    return m.reifyCeylonType({Element:{t:Package$meta$declaration}});
  },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Package$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','members']});
  defineAttr($$modulo,'dependencies',function(){
    var deps = mm['$mod-deps'];
    if (deps.length === 0) return getEmpty();
    if (typeof(deps[0]) === 'string') {
      var _d = [];
      for (var i=0; i<deps.length;i++) {
        var spos = deps[i].lastIndexOf('/');
        _d.push(Importa(String$(deps[i].substring(0,spos)), String$(deps[i].substring(spos+1))));
      }
      deps = _d.reifyCeylonType({Element:{t:Import$meta$declaration}});
      mm['$mod-deps'] = deps;
    }
    return deps;
  },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Import$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','dependencies']});
  function findPackage(pknm){
    if (mm['$pks$'] === undefined) this.members;
    var pk = mm['$pks$'][pknm];
    return pk===undefined ? null : pk;
  }
  $$modulo.findPackage=findPackage;
  findPackage.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findPackage']};
  function findImportedPackage(pknm){
    var pk = this.findPackage(pknm);
    if (pk) return pk;
    var deps=this.dependencies;
    for (var i=0; i < deps.length; i++) {
      pk = deps[i].container.findImportedPackage(pknm);
      if (pk)return pk;
    }
    return null;
  }
  $$modulo.findImportedPackage=findImportedPackage;
  findImportedPackage.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findImportedPackage']};
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
      if (isOfType(an, $$$mptypes.Annotation)) r.push(an);
    }
    return r.reifyCeylonType({Element:$$$mptypes.Annotation});
  }
  $$modulo.annotations=annotations;
  defineAttr($$modulo,'string',function(){return String$("module " + this.name+"/" + this.version);},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

    annotations.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:Modulo,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','annotations']};
    return $$modulo;
}
Modulo.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:Module$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Module']};
exports.Modulo=Modulo;
function $init$Modulo(){
    if (Modulo.$$===undefined){
        initTypeProto(Modulo,'Modulo',Basic,Module$meta$declaration);
    }
    return Modulo;
}
exports.$init$Modulo=$init$Modulo;
$init$Modulo();
function Importa(name, version, $$importa){
    $init$Importa();
    if ($$importa===undefined)$$importa=new Importa.$$;
    Import$meta$declaration($$importa);
    var name=name;
    defineAttr($$importa,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','name']});
    var version=version;
    defineAttr($$importa,'version',function(){return version;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','version']});
    defineAttr($$importa,'shared',function(){
console.log("IMPLEMENT! Import.shared");
return false;},undefined,{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','shared']});
    defineAttr($$importa,'optional',function(){
console.log("IMPLEMENT! Import.optional");
return version;},undefined,{mod:$$METAMODEL$$,$t:{t:Boolean$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','optional']});
    defineAttr($$importa,'container',function(){
      if (this._cont===undefined) {
          this._cont = getModules$meta().find(this.name,this.version);
      }
      return this._cont;
    },undefined,{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','container']});
    return $$importa;
}
Importa.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:Import$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Import']};
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
    defineAttr($$paquete,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','name']});
    defineAttr($$paquete,'qualifiedName',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','qualifiedName']});
    var container=container;
    defineAttr($$paquete,'container',function(){return container;},undefined,{mod:$$METAMODEL$$,$t:{t:Module$meta$declaration},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','container']});
    function members($$$mptypes){
      var filter=[];
      if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind))filter.push('mthd');
      if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind))filter.push('attr','gttr','obj');
      if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind))filter.push('cls');
      if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind))filter.push('ifc');
      if (extendsType({t:AliasDeclaration$meta$declaration},$$$mptypes.Kind))filter.push('als');
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
      return r.reifyCeylonType({Element:$$$mptypes.Kind});
    }
    $$paquete.members=members;
    members.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','members']};
  function annotatedMembers($$$mptypes){
    var ms=this.members({Kind:$$$mptypes.Kind});
    if (ms.length>0) {
      var rv=[];
      for (var i=0; i < ms.length; i++) {
        if (ms[i].tipo && ms[i].tipo.$$metamodel$$) {
          var mm=ms[i].tipo.$$metamodel$$;
          if (typeof(mm)==='function'){mm=mm();ms[i].tipo.$$metamodel$$=mm;}
          var ans=mm.$an;
          if (typeof(ans)==='function'){ans=ans();mm.$an=ans;}
          if (ans) for (var j=0; j<ans.length;j++) {
            if (isOfType(ans[j],$$$mptypes.Annotation)) {
              rv.push(ms[i]);
              break;
            }
          }
        }
      }
      return rv.reifyCeylonType({Element:$$$mptypes.Kind});
    }
    return getEmpty();
  }
  $$paquete.annotatedMembers=annotatedMembers;
  annotatedMembers.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','annotatedMembers']};
    function getMember(name$3,$$$mptypes){
      var m = this.pkg[name$3];
      if (m) {
        var mt = m['$mt'];
        //There's a member alright, but check its type
        if (extendsType({t:ValueDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='attr'||m==='gttr'||m==='obj') {
            return OpenValue(this, m);
          }
        } else if (extendsType({t:FunctionDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='mthd') {
            return OpenFunction(this, m);
          }
        } else if (extendsType({t:FunctionOrValueDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='attr'||m==='gttr'||m==='obj') {
            return OpenValue(this, m);
          } else if (mt==='mthd') {
            return OpenFunction(this, m);
          }
        } else if (extendsType({t:ClassDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='cls') {
            return OpenClass(this, m);
          }
        } else if (extendsType({t:InterfaceDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='ifc') {
            return OpenInterface(this, m);
          }
        } else if (extendsType({t:ClassOrInterfaceDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='ifc') {
            return OpenInterface(this, m);
          } else if (mt==='cls') {
            return OpenClass(this, m);
          }
        } else if (extendsType({t:AliasDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='als')
          return OpenAlias(_findTypeFromModel(this,m));
        } else {
console.log("WTF do I do with this " + name$3 + " Kind " + className($$$mptypes.Kind));
        }
      }
      return null;
    }
    $$paquete.getMember=getMember;
    getMember.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getMember']};
    function getValue(name$4) {
      var m = this.pkg[name$4];
      if (m && (m['$mt']==='attr' || m['$mt']==='gttr' || m['$mt'] === 'obj')) {
        return OpenValue(this, m);
      }
      return null;
    }
    $$paquete.getValue=getValue;
    getValue.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getValue']};
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
    getClassOrInterface.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassOrInterfaceDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getClassOrInterface']};
  function getAlias(an) {
    var al=this.pkg[an];
    if (al && al.$mt==='als') {
      var rta = _findTypeFromModel(this, al);
      return OpenAlias(rta);
    }
    return null;
  }
  $$paquete.getAlias=getAlias;
  getAlias.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['ceylon.language.meta.declaration','Package','$m','getAlias']};};
    function getFunction(name$6){
      var f = this.pkg[name$6];
      if (f && f['$mt']==='mthd') {
        return OpenFunction(this, f);
      }
      return null;
    }
    $$paquete.getFunction=getFunction;
    getFunction.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getFunction']};
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
        if (isOfType(an, $$$mptypes.Annotation)) r.push(an);
      }
      return r.reifyCeylonType({Element:$$$mptypes.Annotation});
        return getEmpty();
    }
    $$paquete.annotations=annotations;
    annotations.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:Paquete,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','annotations']};
  defineAttr($$paquete,'string',function(){return String$("package " + this.name);},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});
    return $$paquete;
}
Paquete.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:Package$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Package']};
exports.Paquete=Paquete;
function $init$Paquete(){
    if (Paquete.$$===undefined){
        initTypeProto(Paquete,'Paquete',Basic,Package$meta$declaration);
    }
    return Paquete;
}
exports.$init$Paquete=$init$Paquete;
$init$Paquete();

