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
  var name=String$(mm['$mod-name']);
  atr$($$modulo,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','name']});
  atr$($$modulo,'qualifiedName',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','qualifiedName']});
  var version=String$(mm['$mod-version']);
  atr$($$modulo,'version',function(){return version;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','version']});
  atr$($$modulo,'members',function(){
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
    return m.length===0?getEmpty():ArraySequence(m,{Element$ArraySequence:{t:Package$meta$declaration}});
  },undefined,{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Package$meta$declaration}}},$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$at','members']});
  atr$($$modulo,'dependencies',function(){
    if (typeof(meta.$mod$imps)==='function')meta.$mod$imps=meta.$mod$imps();
    var deps=mm['$mod-deps']||getEmpty();
    if (deps !== getEmpty()) {
      var _d=[];
      for (var d in meta.$mod$imps) {
        var spos = d.lastIndexOf('/');
        _d.push(Importa(String$(d.substring(0,spos)), String$(d.substring(spos+1)),this,meta.$mod$imps[d]));
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
  resourceByPath.$crtmm$=function(){return {mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Resource}]},$ps:[{$nm:'path',$mt:'prm',$t:{t:String$}}],$cont:Modulo,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Module','$m','resourceByPath']};};
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
function Importa(name,version,mod,anns,$$importa){
  $init$Importa();
  if ($$importa===undefined)$$importa=new Importa.$$;
  Import$meta$declaration($$importa);
  $$importa.name=name;
  $$importa.version=version;
  $$importa._cont=mod;
  $$importa.$anns=anns;
  atr$($$importa,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','name']});
  atr$($$importa,'version',function(){return version;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','version']});
  atr$($$importa,'shared',function(){
    if (typeof(this.$anns)==='function')this.$anns=this.anns();
    if (this.$anns)for (var i=0;i<this.$anns.length;i++) {
      if (this.$anns[i]===shared)return true;
    }
return false;},undefined,{mod:$CCMM$,$t:{t:Boolean$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','shared']});
    atr$($$importa,'optional',function(){
    if (typeof(this.$anns)==='function')this.$anns=this.$anns();
    if (this.$anns)for (var i=0;i<this.$anns.length;i++) {
      if (this.$anns[i]===optional)return true;
    }
return version;},undefined,{mod:$CCMM$,$t:{t:Boolean$},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','optional']});
    atr$($$importa,'container',function(){
      if (this._cont===undefined) {
          this._cont = getModules$meta().find(this.name,this.version);
      }
      return this._cont;
    },undefined,{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:Importa,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Import','$at','container']});
  atr$($$importa,'string',function(){
    return "import " + name + "/" + version;
  },undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:Importa,d:['ceylon.language','Object','$at','string']};});
    return $$importa;
}
Importa.$crtmm$={mod:$CCMM$,'super':{t:Basic},satisfies:[{t:Import$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Import']};
ex$.Importa=Importa;
function $init$Importa(){
    if (Importa.$$===undefined){
        initTypeProto(Importa,'Importa',Basic,$init$Import$meta$declaration());
    }
    return Importa;
}
ex$.$init$Importa=$init$Importa;
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
    atr$($$paquete,'name',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','name']});
    atr$($$paquete,'qualifiedName',function(){return name;},undefined,{mod:$CCMM$,$t:{t:String$},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','qualifiedName']});
    var container=container;
    atr$($$paquete,'container',function(){return container;},undefined,{mod:$CCMM$,$t:{t:Module$meta$declaration},$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$at','container']});
    function members($$$mptypes){
      var filter=[];
      if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('m');
      if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('a','g','o', 's');
      if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('c');
      if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('i');
      if (extendsType({t:AliasDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('als');
      var r=[];
      for (var mn in this.pkg) {
        var m = this.pkg[mn];
        var mt = m['$mt'];
        if (filter.indexOf(mt)>=0 && m['$an'] && m['$an']['shared']) {
          if (mt === 'm') {
            r.push(OpenFunction(this, m));
          } else if (mt==='c') {
            r.push(OpenClass(this, m));
          } else if (mt==='i') {
            r.push(OpenInterface(this, m));
          } else if (mt==='a'||mt==='g'||mt==='o') {
            r.push(OpenValue(this, m));
          } else if (mt==='s') {
            r.push(OpenSetter(OpenValue(this, m)));
          } else if (mt==='als') {
            r.push(OpenAlias(_findTypeFromModel(this,m)));
          }
        }
      }
      return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Kind$members});
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
            if (is$(ans[j],$$$mptypes.Annotation$annotatedMembers)) {
              rv.push(ms[i]);
              break;
            }
          }
        }
      }
      return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:$$$mptypes.Kind$annotatedMembers});
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
        if ((mt==='a'||mt==='g'||mt==='o'||mt==='s')&&extendsType({t:FunctionOrValueDeclaration$meta$declaration}, $$$mptypes.Kind$getMember)) {
          return mt==='s'?OpenSetter(OpenValue(this, m)):OpenValue(this, m);
        } else if (mt==='m'&&extendsType($$$mptypes.Kind$getMember,{t:FunctionOrValueDeclaration$meta$declaration})){
          return OpenFunction(this, m);
        } else if (mt==='c'&&extendsType($$$mptypes.Kind$getMember,{t:ClassOrInterfaceDeclaration$meta$declaration})){
          return OpenClass(this, m);
        } else if (mt==='i'&&extendsType($$$mptypes.Kind$getMember,{t:ClassOrInterfaceDeclaration$meta$declaration})){
          return OpenInterface(this, m);
        } else if (mt==='als'&&extendsType($$$mptypes.Kind$getMember,{t:AliasDeclaration$meta$declaration})){
          return OpenAlias(_findTypeFromModel(this,m));
        } else {
console.log("WTF do I do with this " + name$3 + " metatype " + mt + " Kind " + $$$mptypes.Kind$getMember);
        }
      }
      return null;
    }
    $$paquete.getMember=getMember;
    getMember.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},'Kind']},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$tp:{Kind:{'satisfies':[{t:NestableDeclaration$meta$declaration}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getMember']};
    function getValue(name$4) {
      var m = this.pkg[name$4];
      if (m && (m['$mt']==='a' || m['$mt']==='g' || m['$mt'] === 'o' ||m['$mt']==='s')) {
        return OpenValue(this, m);
      }
      return null;
    }
    $$paquete.getValue=getValue;
    getValue.$crtmm$={mod:$CCMM$,$t:{ t:'u', l:[{t:Null},{t:ValueDeclaration$meta$declaration}]},$ps:[{$nm:'name',$mt:'prm',$t:{t:String$}}],$cont:Paquete,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','getValue']};
    function getClassOrInterface(name$5){
      var ci = this.pkg[name$5];
      if (ci && ci['$mt']==='c') {
        return OpenClass(this, ci);
      } else if (ci && ci['$mt']==='i') {
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
      if (f && f['$mt']==='m') {
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
        if (is$(an, $$$mptypes.Annotation$annotations)) r.push(an);
      }
      return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Annotation$annotations});
    }
    $$paquete.annotations=annotations;
    annotations.$crtmm$={mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:'Annotation'}},$ps:[],$cont:Paquete,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','Package','$m','annotations']};
  atr$($$paquete,'string',function(){return String$("package " + this.name);},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['ceylon.language','Object','$at','string']});
    return $$paquete;
}
Paquete.$crtmm$={mod:$CCMM$,'super':{t:Basic},satisfies:[{t:Package$meta$declaration}],$an:function(){return[shared()];},d:['ceylon.language.meta.declaration','Package']};
ex$.Paquete=Paquete;
function $init$Paquete(){
    if (Paquete.$$===undefined){
        initTypeProto(Paquete,'Paquete',Basic,$init$Package$meta$declaration());
    }
    return Paquete;
}
ex$.$init$Paquete=$init$Paquete;
$init$Paquete();

