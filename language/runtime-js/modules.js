/* Metamodel module and package objects */
var $loadedModules$={};
exports.$loadedModules$=$loadedModules$;
function $addmod$(mod) {
  var modname = mod.$$METAMODEL$$['$mod-name'] + '/' + mod.$$METAMODEL$$['$mod-version'];
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
        return mods.reifyCeylonType({t:Sequential,a:{Element:{t:Module$model$declaration}}});
    },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Module$model$declaration}}},$cont:modules$2,$an:function(){return[shared()];},pkg:'ceylon.language.model',d:$$METAMODEL$$['ceylon.language.model']['modules']['$at']['list']});
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
    find.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Module$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}},{$nm:'version',$mt:'prm',$t:{t:String$}}],$cont:modules$2,$an:function(){return[shared()];},pkg:'ceylon.language.model',d:$$METAMODEL$$['ceylon.language.model']['modules']['$m']['find']};
    defineAttr($$modules,'$default',function(){
        return find('default',"unversioned");
    },undefined,{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Module$model$declaration}]},$cont:modules$2,$an:function(){return[shared()];},pkg:'ceylon.language.model',d:$$METAMODEL$$['ceylon.language.model']['modules']['$at']['default']});
    return $$modules;
}
function $init$modules$model(){
    if (modules$2.$$===undefined){
        initTypeProto(modules$2,'ceylon.language.model::modules',Basic);
    }
    return modules$2;
}
exports.$init$modules$model=$init$modules$model;
$init$modules$model();
var modules$model=modules$2();
var getModules$model=function(){
    return modules$model;
}
exports.getModules$model=getModules$model;

function Modulo(meta, $$modulo){
    $init$Modulo();
    if ($$modulo===undefined)$$modulo=new Modulo.$$;
    Module$model$declaration($$modulo);
    $$modulo.meta=meta;
    var name=String$(meta.$$METAMODEL$$['$mod-name']);
    defineAttr($$modulo,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']['$at']['name']});
    var version=String$(meta.$$METAMODEL$$['$mod-version']);
    defineAttr($$modulo,'version',function(){return version;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']['$at']['version']});
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
      return m.reifyCeylonType({t:Sequential,a:{Element:{t:Package$model$declaration}}});
    },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Package$model$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']['$at']['members']});
    defineAttr($$modulo,'dependencies',function(){
      var deps = this.meta.$$METAMODEL$$['$mod-deps'];
      if (deps.length === 0) return getEmpty();
      if (typeof(deps[0]) === 'string') {
        var _d = [];
        for (var i=0; i<deps.length;i++) {
          var spos = deps[i].lastIndexOf('/');
          _d.push(Importa(String$(deps[i].substring(0,spos)), String$(deps[i].substring(spos+1))));
        }
        deps = _d.reifyCeylonType({t:Sequential,a:{Element:{t:Import$model$declaration}}});
        this.meta.$$METAMODEL$$['$mod-deps'] = deps;
      }
      return deps;
    },undefined,{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Import$model$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']['$at']['dependencies']});
    function findPackage(pknm){
      if (this.meta.$$METAMODEL$$['$pks$'] === undefined) this.members;
      var pk = this.meta.$$METAMODEL$$['$pks$'][pknm];
      return pk===undefined ? null : pk;
    }
    $$modulo.findPackage=findPackage;
    findPackage.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:Package$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']['$m']['findPackage']};
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
      return r.reifyCeylonType({t:Sequential,a:{Element:$$$mptypes.Annotation}});
    }
    $$modulo.annotations=annotations;
    annotations.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:Modulo,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation$model,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']['$m']['annotations']};
    return $$modulo;
}
Modulo.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:Module$model$declaration}],$an:function(){return[shared()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Module']};
exports.Modulo=Modulo;
function $init$Modulo(){
    if (Modulo.$$===undefined){
        initTypeProto(Modulo,'Modulo',Basic,Module$model$declaration);
    }
    return Modulo;
}
exports.$init$Modulo=$init$Modulo;
$init$Modulo();
function Importa(name, version, $$importa){
    $init$Importa();
    if ($$importa===undefined)$$importa=new Importa.$$;
    Import$model$declaration($$importa);
    var name=name;
    defineAttr($$importa,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Import']['$at']['name']});
    var version=version;
    defineAttr($$importa,'version',function(){return version;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Import']['$at']['version']});
    return $$importa;
}
Importa.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:Import$model$declaration}],$an:function(){return[shared()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Import']};
exports.Importa=Importa;
function $init$Importa(){
    if (Importa.$$===undefined){
        initTypeProto(Importa,'Importa',Basic,Import$model$declaration);
    }
    return Importa;
}
exports.$init$Importa=$init$Importa;
$init$Importa();
function Paquete(name, container, pkg, $$paquete){
    $init$Paquete();
    if ($$paquete===undefined)$$paquete=new Paquete.$$;
    Package$model$declaration($$paquete);
    $$paquete.pkg=pkg;
    var name=name;
    defineAttr($$paquete,'name',function(){return name;},undefined,{mod:$$METAMODEL$$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$at']['name']});
    var container=container;
    defineAttr($$paquete,'container',function(){return container;},undefined,{mod:$$METAMODEL$$,$t:{t:Module$model$declaration},$cont:Paquete,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$at']['container']});
    function members($$$mptypes){
        return getEmpty();
    }
    $$paquete.members=members;
    members.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['members']};
    function annotatedMembers($$$mptypes){
        return getEmpty();
    }
    $$paquete.annotatedMembers=annotatedMembers;
    annotatedMembers.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Kind'}},$ps:[],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]},Annotation:{}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['annotatedMembers']};
    function getMember(name$3,$$$mptypes){
        return null;
    }
    $$paquete.getMember=getMember;
    getMember.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:TopLevelOrMemberDeclaration$model$declaration}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['getMember']};
    function getValue(name$4){
        return null;
    }
    $$paquete.getValue=getValue;
    getValue.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ValueDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['getValue']};
    function getClassOrInterface(name$5){
        return null;
    }
    $$paquete.getClassOrInterface=getClassOrInterface;
    getClassOrInterface.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:ClassOrInterfaceDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['getClassOrInterface']};
    function getFunction(name$6){
        return null;
    }
    $$paquete.getFunction=getFunction;
    getFunction.$$metamodel$$={mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:FunctionDeclaration$model$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['getFunction']};
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
      return r.reifyCeylonType({t:Sequential,a:{Element:$$$mptypes.Annotation}});
        return getEmpty();
    }
    $$paquete.annotations=annotations;
    annotations.$$metamodel$$={mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:Paquete,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation$model,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']['$m']['annotations']};
    return $$paquete;
}
Paquete.$$metamodel$$={mod:$$METAMODEL$$,'super':{t:Basic},satisfies:[{t:Package$model$declaration}],$an:function(){return[shared()];},pkg:'ceylon.language.model.declaration',d:$$METAMODEL$$['ceylon.language.model.declaration']['Package']};
exports.Paquete=Paquete;
function $init$Paquete(){
    if (Paquete.$$===undefined){
        initTypeProto(Paquete,'Paquete',Basic,Package$model$declaration);
    }
    return Paquete;
}
exports.$init$Paquete=$init$Paquete;
$init$Paquete();

