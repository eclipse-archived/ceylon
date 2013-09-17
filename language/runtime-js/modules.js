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
          lm = require(mpath);
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
    if (typeof(meta.$$METAMODEL$$)==='function') {
      var mm = meta.$$METAMODEL$$();
      meta.$$METAMODEL$$=mm;
    }
    var name=String$(meta.$$METAMODEL$$['$mod-name']);
    defineAttr($$modulo,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','name']});
    defineAttr($$modulo,'qualifiedName',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','qualifiedName']});
    var version=String$(meta.$$METAMODEL$$['$mod-version']);
    defineAttr($$modulo,'version',function(){return version;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','version']});
    defineAttr($$modulo,'members',function(){
      if (this.meta.$$METAMODEL$$['$pks$'] === undefined) {
        this.meta.$$METAMODEL$$['$pks$'] = {};
        for (mem in this.meta.$$METAMODEL$$) {
          if (typeof(mem) === 'string' && mem[0]!=='$') {
            this.meta.$$METAMODEL$$['$pks$'][mem] = Paquete(mem, this, this.meta.$$METAMODEL$$[mem]);
          }
        }
      }
      var m = [];
      for (mem in this.meta.$$METAMODEL$$['$pks$']) {
        if (typeof(mem) === 'string') {
          m.push(this.meta.$$METAMODEL$$['$pks$'][mem]);
        }
      }
      return m.reifyCeylonType({Element:{t:Package$meta$declaration}});
    },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Package$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','members']});
    defineAttr($$modulo,'dependencies',function(){
      var deps = this.meta.$$METAMODEL$$['$mod-deps'];
      if (deps.length === 0) return getEmpty();
      if (typeof(deps[0]) === 'string') {
        var _d = [];
        for (var i=0; i<deps.length;i++) {
          var spos = deps[i].lastIndexOf('/');
          _d.push(Importa(String$(deps[i].substring(0,spos)), String$(deps[i].substring(spos+1))));
        }
        deps = _d.reifyCeylonType({Element:{t:Import$meta$declaration}});
        this.meta.$$METAMODEL$$['$mod-deps'] = deps;
      }
      return deps;
    },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Import$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','dependencies']});
    function findPackage(pknm){
      if (this.meta.$$METAMODEL$$['$pks$'] === undefined) this.members;
      var pk = this.meta.$$METAMODEL$$['$pks$'][pknm];
      return pk===undefined ? null : pk;
    }
    $$modulo.findPackage=findPackage;
    findPackage.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Package$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','findPackage']};
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
  defineAttr($$modulo,'string',function(){return String$("Module[" + this.name+"/" + this.version + "]");},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

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
      var filter;
      if (extendsType($$$mptypes.Kind,{t:FunctionDeclaration$meta$declaration})) {
        filter = function(m) { return m['$mt']==='mthd'; };
      } else if (extendsType($$$mptypes.Kind,{t:ValueDeclaration$meta$declaration})) {
        filter = function(m) { return m['$mt']==='attr' || m['$mt']==='gttr' || m['$mt']==='obj'; };
      } else if (extendsType($$$mptypes.Kind,{t:ClassDeclaration$meta$declaration})) {
        filter = function(m) { return m['$mt']==='cls'; };
      } else if (extendsType($$$mptypes.Kind,{t:InterfaceDeclaration$meta$declaration})) {
        filter = function(m) { return m['$mt']==='ifc'; };
      } else if (extendsType($$$mptypes.Kind,{t:FunctionOrValueDeclaration$meta$declaration})) {
        filter = function(m) { return m['$mt']==='mthd' || m['$mt']==='attr' || m['$mt']==='gttr' || m['$mt']==='obj'; };
      } else if (extendsType($$$mptypes.Kind,{t:ClassOrInterfaceDeclaration$meta$declaration})) {
        filter = function(m) { return m['$mt']==='cls' || m['$mt']==='ifc'; };
      } else {
        //Dejamos pasar todo
        filter = function(m) { return true; }
      }
      var r=[];
      for (var mn in this.pkg) {
        var m = this.pkg[mn];
        if (filter(m)) {
          var mt = m['$mt'];
          if (mt === 'mthd') {
            r.push(OpenFunction(this, m));
          } else if (mt==='cls') {
            r.push(OpenClass(this, m));
          } else if (mt==='ifc') {
            r.push(OpenInterface(this, m));
          } else if (mt==='attr'||mt==='gttr'||mt==='obj') {
            r.push(OpenValue(this, m));
          }
        }
      }
      return r.reifyCeylonType({Element:$$$mptypes.Kind});
    }
    $$paquete.members=members;
    members.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','members']};
    function annotatedMembers($$$mptypes){
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
          return null;
        } else if (extendsType({t:FunctionDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='mthd') {
            return OpenFunction(this, m);
          }
          return null;
        } else if (extendsType({t:FunctionOrValueDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='attr'||m==='gttr'||m==='obj') {
            return OpenValue(this, m);
          } else if (mt==='mthd') {
            return OpenFunction(this, m);
          }
          return null;
        } else if (extendsType({t:ClassDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='cls') {
            return OpenClass(this, m);
          }
          return null;
        } else if (extendsType({t:InterfaceDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='ifc') {
            return OpenInterface(this, m);
          }
          return null;
        } else if (extendsType({t:ClassOrInterfaceDeclaration$meta$declaration}, $$$mptypes.Kind)) {
          if (mt==='ifc') {
            return OpenInterface(this, m);
          } else if (mt==='cls') {
            return OpenClass(this, m);
          }
          return null;
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
        return (m['var']==='1' ? OpenVariable:OpenValue)(this, m);
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
  defineAttr($$paquete,'string',function(){return String$("Package[" + this.name+"]");},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});
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

