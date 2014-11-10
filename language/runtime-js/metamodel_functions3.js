function find$ann(cont,ant) {
  var _m=getrtmm$$(cont);
  if (!(_m && _m.an))return null;
  if (typeof(_m.an)==='function')_m.an=_m.an();
  for (var i=0; i < _m.an.length; i++) {
    if (is$(_m.an[i],{t:ant}))return _m.an[i];
  }
  return null;
}
//Find the real declaration of something from its model definition
function _findTypeFromModel(pkg,mdl,cont) {
  var mod = pkg.container;
  //TODO this is very primitive needs a lot of rules replicated from the JsIdentifierNames
  var nm = mdl.nm;
  var mt = mdl['mt'];
  if (mt === 'a' || mt === 'g' || mt === 'o' || mt === 's') {
    nm = '$prop$get' + nm[0].toUpperCase() + nm.substring(1);
  }
  if (cont) {
    var imm=getrtmm$$(cont);
    if (mt==='c'||mt==='i')nm=nm+'$'+imm.d[imm.d.length-1];
  }else if (pkg.suffix) {
    nm+=pkg.suffix;
  }
  var out=cont?cont.$$.prototype:mod.meta;
  var rv=out[nm];
  if (rv===undefined)rv=out['$_'+nm];
  if (rv===undefined){
    rv=out['$init$'+nm];
    if (typeof(rv)==='function')rv=rv();
  }
  return rv;
}
//Pass a {t:Bla} and get a FreeClass,FreeInterface,etc (OpenType).
//second arg is the container for the second argument, if any
function _openTypeFromTarg(targ,o) {
  if (targ.t==='u' || targ.t==='i') {
    var tl=[];
    for (var i=0; i < targ.l.length; i++) {
      var _ct=targ.l[i];
      if (typeof(_ct)==='string') {
        tl.push(OpenTvar$jsint(OpenTypeParam$jsint(o,_ct)));
      } else {
        tl.push(_ct.t?_openTypeFromTarg(_ct,o):_ct);
      }
    }
    return (targ.t==='u'?FreeUnion$jsint:FreeIntersection$jsint)(tl.rt$({t:OpenType$meta$declaration}));
  } else if (targ.t==='T') {
    var mm=getrtmm$$(Tuple);
    var lit = typeLiteral$meta({Type$typeLiteral:targ});
  } else {
    var mm=getrtmm$$(targ.t);
    var lit = typeLiteral$meta({Type$typeLiteral:targ.t});
  }
  if (targ.a && lit)lit._targs=targ.a;
  var mdl = get_model(mm);
  if (mdl.mt==='i') {
    return FreeInterface$jsint(lit);
  } else if (mdl.mt==='c' || mdl.mt==='o') {
    return FreeClass$jsint(lit);
  }
  console.log("Don't know WTF to return for " + lit + " metatype " + mdl.mt);
}
//used to be InterfaceModel.declaration
function coimoddcl$(ifc) {
  if (ifc._decl)return ifc._decl;
  var mm = getrtmm$$(ifc.tipo);
  var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
  var cls = is$(ifc,{t:ClassModel$meta$model});
  var _mod = getModules$meta().find(_m['$mod-name'],_m['$mod-version']);
  ifc._decl = (cls?OpenClass$jsint:OpenInterface$jsint)(_mod.findPackage(mm.d[0]), ifc.tipo);
  return ifc._decl;
}
function clsparamtypes(cls) {
  var ps=cls.tipo.$crtmm$.ps;
  if (!ps || ps.length==0)return getEmpty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!cls.$targs)throw TypeApplicationException$meta$model("This class model needs type parameters " + cls.string);
      pt=cls.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model("Class model is missing type argument for "
        + cls.string + "<" + ps[i].$t + ">");
    }
    r.push(typeLiteral$meta({Type$typeLiteral:pt}));
  }
  return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:{t:Type$meta$model,a:{t:Anything}}});
}
function funparamtypes(fun) {
  var ps=fun.tipo.$crtmm$.ps;
  if (!ps || ps.length==0)return getEmpty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!fun.$targs)throw TypeApplicationException$meta$model("This function model needs type parameters: " +fun.string);
      pt=fun.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model("Function model is missing type argument for "
        + fun.string + "<" + ps[i].$t + ">");
    }
    r.push(typeLiteral$meta({Type$typeLiteral:pt}));
  }
  return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:{t:Type$meta$model,a:{t:Anything}}});
}
function funmodstr$(fun) {
  var mm=fun.tipo.$crtmm$;
  var qn;
  if (mm.$cont) {
    qn=qname$(mm.$cont);
    if (mm.$cont.$crtmm$.tp) {
      var cnt=fun.$$targs$$&&fun.$$targs$$.Container$Function&&fun.$$targs$$.Container$Function.a;
      if (!cnt)cnt=fun.$$targs$$&&fun.$$targs$$.Container$Method&&fun.$$targs$$.Container$Method.a;
      qn+="<";var first=true;
      for (var tp in mm.$cont.$crtmm$.tp) {
        if (first)first=false;else qn+=",";
        var _ta=cnt&&cnt[tp];
        qn+=qname$(_ta||Anything);
      }
      qn+=">";
    }
    qn+="."+mm.d[mm.d.length-1];
  } else {
    qn=qname$(mm);
  }
  if (mm.tp) {
    qn+="<";
    var first=true;
    for (var tp in mm.tp) {
      if (first)first=false; else qn+=",";
      var targ=fun.$targs[tp];
      if (targ.t) {
        var _m=getrtmm$$(targ.t);
        qn+=qname$(_m);
      } else {
        qn+=tp;
      }
    }
    qn+=">";
  }
  return qn;
}
function funtypearg$(fun) {
  var mm = fun.tipo.$crtmm$;
  if (mm) {
    if (mm.tp) {
      if (fun.$targs===undefined)throw TypeApplicationException$meta$model("Missing type arguments for "+fun.string);
      var targs={};
      var ord=[];
      for (var tp in mm.tp) {
        var param = OpenTypeParam$jsint(fun.tipo,tp);
        var targ = fun.$targs[tp];
        if (targ) {
          targ=typeLiteral$meta({Type$typeLiteral:targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        targs[param.qualifiedName]=[param,targ];
        ord.push(param.qualifiedName);
      }
      return TpMap$jsint(targs,ord,{V$TpMap:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}});
    }
    return getEmpty();
  }
  throw Exception("FunctionModel.typeArguments-we don't have a metamodel!");
}
function coicont$(coi) {
  if (coi.$parent)return coi.$parent;
  var cont = getrtmm$$(coi.tipo).$cont;
  if (cont===undefined)return null;
  if (cont===0)return coi.containingPackage;
  var cmm=getrtmm$$(cont);
  var _t={t:cont};
  var _out=undefined;
  if (coi.src$ && coi.src$.outer$) {
    _out=coi.src$.outer$;
    if (_out.$$targs$$) {
      _t.a=_out.$$targs$$;
    }
  }
  var rv;
  if (get_model(cmm).mt === 'i')
    rv=AppliedInterface$jsint(cont,{Type$Interface:_t});
  //TODO tipos de parametros
  rv=AppliedClass(cont,{Type$Class:_t,Arguments$Class:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
  if (_out)rv.src$=_out;
  return rv;
}
function coistr$(coi) {
  var mm = getrtmm$$(coi.tipo);
  var qn=coi.tipo.$$ && coi.tipo.$$.prototype && coi.tipo.$$.prototype.getT$name ? coi.tipo.$$.prototype.getT$name() : qname$(mm);
  if (mm.tp) {
    qn+="<";
    var first=true;
    for (var tp in mm.tp) {
      var targ;
      if (coi.$$targs$$ && coi.$$targs$$.Type$ClassOrInterface && coi.$$targs$$.Type$ClassOrInterface.a && coi.$$targs$$.Type$ClassOrInterface.a[tp]) {
        var _targ=coi.$$targs$$.Type$ClassOrInterface.a[tp];
        if (typeof(_targ)==='string') {
          console.log("TODO buscar " + tp + "->" + _targ + " para " + coi.declaration.qualifiedName);
          _targ={t:Anything};
        }
        targ=typeLiteral$meta({Type$typeLiteral:_targ});
      } else {
        targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
      }
      if (first)first=false; else qn+=",";
      if (targ.declaration) {
        qn+=targ.declaration.qualifiedName;
      } else {
        qn+=targ.string;
      }
    }
    qn+=">";
  }
  return qn;
}
function coihash$(coi) {
  var mm = getrtmm$$(coi.tipo);
  var h=qname$(mm).hash;
  if (mm.tp) {
    for (var tp in mm.tp) {
      var targ;
      if (coi.$$targs$$ && coi.$$targs$$.Type$ClassOrInterface && coi.$$targs$$.Type$ClassOrInterface.a && coi.$$targs$$.Type$ClassOrInterface.a[tp]) {
        var _targ=coi.$$targs$$.Type$ClassOrInterface.a[tp];
        if (typeof(_targ)==='string') {
          console.log("TODO buscar " + tp + "->" + _targ + " para " + coi.declaration.qualifiedName);
          _targ={t:Anything};
        }
        targ=typeLiteral$meta({Type$typeLiteral:_targ});
      } else {
        targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
      }
      h+=targ.hash;
    }
  }
  if (coi.$bound)h+=coi.$bound.hash;
  return h;
}
function coitarg$(coi){
  var mm = getrtmm$$(coi.tipo);
  if (mm) {
    if (mm.tp) {
      var targs={};
      var ord=[];
      for (var tp in mm.tp) {
        var param = OpenTypeParam$jsint(coi.tipo,tp);
        var targ;
        if (coi.$$targs$$ && coi.$$targs$$.Type$ClassOrInterface && coi.$$targs$$.Type$ClassOrInterface.a
            && coi.$$targs$$.Type$ClassOrInterface.a[tp]) {
          var _targ=coi.$$targs$$.Type$ClassOrInterface.a[tp];
          if (typeof(_targ)==='string') {
            console.log("TODO buscar " + tp + "->" + _targ + " para " + coi.declaration.qualifiedName);
            _targ={t:Anything};
          }
          targ=typeLiteral$meta({Type$typeLiteral:_targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        targs[param.qualifiedName]=[param,targ];
        ord.push(param.qualifiedName);
      }
      return TpMap$jsint(targs,ord,{V$TpMap:{t:Type$meta$model,a:{Type$Type:{t:Anything}}}});
    }
    return getEmpty();
  }
  throw new Error("ClassOrInterface.typeArguments: missing metamodel!");
}
function coiexttype$(coi){
  var sc = coi.tipo.$crtmm$['super'];
  if (sc === undefined)return null;
  var mm = getrtmm$$(sc.t);
  var ac;
  if (mm.$cont) {
    ac=AppliedMemberClass(sc.t, {Type$MemberClass:sc,Arguments$MemberClass:{t:Sequential,a:{Element$Iterable:{t:Anything}}},Container$MemberClass:mm.$cont});
  } else {
    ac=AppliedClass(sc.t, {Type$Class:sc,Arguments$Class:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
  }
  if (sc.a)ac.$targs=sc.a;
  return ac;
}
function coisattype$(coi){
  var ints = coi.tipo.$crtmm$.sts;
  if (ints && ints.length) {
    function resolveTypeArguments(root,type) {
      if (type.a) {
        var t2 = {t:type.t, a:{}};
        for (var targ in type.a) {
          t2.a[targ]=typeof(type.a[targ])==='string' ?
            t2.a[targ]=root.$$targs$$.Type$ClassOrInterface.a[type.a[targ]]
            : t2.a[targ]=type.a[targ];
          if (t2.a[targ] && t2.a[targ].a) {
            t2.a[targ]=resolveTypeArguments(root,t2.a[targ]);
          }
        }
        type=t2;
      }
      return type;
    }
    var rv = [];
    for (var i=0; i < ints.length; i++) {
      var ifc = resolveTypeArguments(coi,ints[i]);
      var mm=getrtmm$$(ifc.t);
      if (mm.$cont) {
        rv.push(AppliedMemberInterface(ifc.t, {Type$MemberInterface:ifc}));
      } else {
        rv.push(AppliedInterface$jsint(ifc.t, {Type$Interface:ifc}));
      }
    }
    return rv.rt$({t:InterfaceModel$meta$model,a:{Type$InterfaceModel:{t:Anything}}});
  }
  return getEmpty();
}
function coigetcoi$(coi,name$2,types$3,$$$mptypes,noInherit){
  if (!extendsType($$$mptypes.Kind$getClassOrInterface, {t:ClassOrInterface$meta$model}))throw IncompatibleTypeException$meta$model("Kind must be ClassOrInterface");
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getClassOrInterface);
  if(types$3===undefined){types$3=getEmpty();}
  var mm = getrtmm$$(_tipo);
  var nom = name$2 + '$' + mm.d[mm.d.length-1];
  var ic = _tipo.$$.prototype[nom];
  if (!ic) {
    if (noInherit)return null;
    var pere=mm['super'];
    while (!ic && pere) {
      mm=getrtmm$$(pere.t);
      nom=mm&&mm.d?name$2+'$'+mm.d[mm.d.length-1]:undefined;
      if (nom)ic=_tipo.$$.prototype[nom];
      if (!ic)pere=mm['super'];
    }
  }
  if (ic) {
    mm = getrtmm$$(ic);
    var md = get_model(mm);
    var rv;
    var ict={t:ic};
    var _cont={t:_tipo};
    if (coi.$targs)_cont.a=coi.$targs;
    if (md.mt==='i') {
      if (!extendsType({t:Interface$meta$model},{t:$$$mptypes.Kind$getClassOrInterface.t}))throw IncompatibleTypeException$meta$model("Member " + name$2 + " is an interface");
      validate$typeparams(ict,ic.$crtmm$.tp,types$3);
      rv=AppliedMemberInterface(ic, {Container$MemberInterface:_cont,Type$MemberInterface:ict});
    } else if (md.mt==='c'){
      if (!extendsType({t:Class$meta$model},{t:$$$mptypes.Kind$getClassOrInterface.t}))throw IncompatibleTypeException$meta$model("Member " + name$2 + " is a class");
      validate$typeparams(ict,ic.$crtmm$.tp,types$3);
      rv=AppliedMemberClass(ic, {Container$MemberClass:_cont,Type$MemberClass:ict, Arguments$MemberClass:$$$mptypes.Arguments$getClassOrInterface});
    } else {
      throw IncompatibleTypeException$meta$model("Member " + name$2 + " is not a class or interface");
    }
    if (ict.a)rv.$targs=ict.a;
    rv.$parent=coi;
    return rv;
  }
  return null;
}
function coiTypeOf(coi,instance$8){
  var _t={t:coi.tipo};
  if (coi.$targs)_t.a=coi.$targs;
  return is$(instance$8,_t);
}
function coiclasse$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=getEmpty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getClasses);
  for (m in _tipo.$$.prototype) {
    var mem=getnpmem$(_tipo.$$.prototype,m);
    if (mem && mem.$$) {
      var mm=getrtmm$$(mem);
      if (mm && mm.d && mm.d[mm.d.length-2]==='$c') {
        if (noInherit && mm.$cont!==coi.tipo)continue;
        if (!extendsType({t:mem},$$$mptypes.Type$getClasses))continue;
        var anns=allann$(mm);
        if (anns && coi$is$anns(anns,ats) && validate$params(mm.ps,$$$mptypes.Arguments$getClasses,'',1)) {
          mems.push(AppliedMemberClass(mem, {Container$MemberClass:_tipo,Type$MemberClass:{t:mem}, Arguments$MemberClass:$$$mptypes.Arguments$getClasses}));
        }
      }
    }
  }
  return mems.length===0?getEmpty():ArraySequence(mems,{Element$ArraySequence:{t:MemberClass$meta$model,a:{Arguments$MemberClass:$$$mptypes.Arguments$getClasses,Container$MemberClass:$$$mptypes.Container$getClasses,Type$MemberClass:$$$mptypes.Type$getClasses}}});
}
function coicla$(coi,name,types,cont,noInherit) {
  var rv=coigetcoi$(coi,name,types,{Container$getClassOrInterface:cont,
    Kind$getClassOrInterface:Class$meta$model},noInherit);
  if (rv && !is$(rv, {t:AppliedMemberClass})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not a class");
  }
  return rv;
}
function coigetifc$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=getEmpty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getInterfaces);
  for (m in _tipo.$$.prototype) {
    var mem=getnpmem$(_tipo.$$.prototype,m);
    if (mem && mem.$$) {
      var mm=getrtmm$$(mem);
      if (mm && mm.d && mm.d[mm.d.length-2]==='$i') {
        if (noInherit && mm.$cont!==coi.tipo)continue;
        if (!extendsType({t:mem},$$$mptypes.Type$getInterfaces))continue;
        var anns=allann$(mm);
        if (anns && coi$is$anns(anns,ats)) {
          mems.push(AppliedMemberInterface(mem, {Container$MemberInterface:_tipo,Type$MemberInterface:{t:mem}}));
        }
      }
    }
  }
  return mems.length===0?getEmpty():ArraySequence(mems,{Element$ArraySequence:{t:MemberInterface$meta$model,a:{Container$MemberInterface:$$$mptypes.Container$getInterfaces,Type$MemberInterface:$$$mptypes.Type$getInterfaces}}});
}
function coiifc$(coi,name,types,cont,noInherit){
  var rv=coigetcoi$(coi,name,types,{Container$getClassOrInterface:cont,
    Kind$getClassOrInterface:Interface$meta$model},noInherit);
  if (rv && !is$(rv, {t:AppliedMemberInterface})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not an interface");
  }
  return rv;
}
function coiatr$(coi,name,m,noInherit){
  if (!extendsType({t:coi.tipo},m.Container$getAttribute && m.Container$getAttribute.t!==Nothing)) {
    throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  }
  var _tipo=mmfca$(coi.tipo,m.Container$getAttribute);
  var nom = '$prop$get' + name[0].toUpperCase() + name.substring(1);
  var at = _tipo.$$.prototype[nom];
  if (!at) {
    nom = '$prop$get$_' + name;
    at = _tipo.$$.prototype[nom];
    if (!at)return null;
  }
  var mm=getrtmm$$(at);
  var _t=m.Get$getAttribute;
  if (mm && mm.$t) {
    if (!extendsType(mm.$t,_t))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
    if (!extendsType(m.Set$getAttribute,at.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
    _t=mm.$t;
  }
  if (noInherit && at) {
    var mm=getrtmm$$(at);
    if (mm&&mm.$cont!==coi.tipo)return null;
  }
  var rv=AppliedAttribute(name, at, {Get$Attribute:_t,Set$Attribute:at.set?_t:{t:Nothing}, Container$Attribute:{t:_tipo}});
  if (coi.$targs)rv.$$targs$$.Container$Attribute.a=coi.$targs;
  rv.$parent=coi;
  return rv;
}
function coigetatr$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=getEmpty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getAttributes);
  for (m in _tipo.$$.prototype) {
    if (m.substring(0,6)==='$prop$') {
      var mm=getrtmm$$(_tipo.$$.prototype[m]);
      if (mm) {
        if (noInherit && mm.$cont!==coi.tipo)continue;
        if (!extendsType(mm.$t,$$$mptypes.Get$getAttributes))continue;
        var setter=_tipo.$$.prototype[m].set && extendsType($$$mptypes.Set$getAttributes,mm.$t);
        if ($$$mptypes.Set$getAttributes.t!==Nothing && !setter)continue;
        var anns=allann$(mm);
        if (anns && coi$is$anns(anns,ats)) {
          var atname=mm.d[mm.d.length-1];
          if (atname.indexOf('$')>0)atname=atname.substring(0,atname.indexOf('$'));
          mems.push(AppliedAttribute(atname,_tipo.$$.prototype[m],{Container$Attribute:{t:coi.tipo},Get$Attribute:mm.$t,Set$Attribute:setter?mm.$t:{t:Nothing}}));
        }
      }
    }
  }
  return mems.length==0?getEmpty():ArraySequence(mems,{Element$ArraySequence:{t:Attribute$meta$model,a:{Set$Attribute:$$$mptypes.Set$getAttributes,Container$Attribute:$$$mptypes.Container$getAttributes,Get$Attribute:$$$mptypes.Get$getAttributes}}});
}
function coimtd$(coi,name,types,$$$mptypes,noInherit){
  if (!extendsType({t:coi.tipo},$$$mptypes.Container$getMethod) && $$$mptypes.Container$getMethod.t!==Nothing) {
    throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  }
  if (types===undefined)types=getEmpty();
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getMethod);
  var fun = _tipo.$$.prototype[name];
  if (!fun) return null;
  var mm=getrtmm$$(fun);
  var _t=$$$mptypes.Type$getMethod;
  var _a=$$$mptypes.Arguments$getMethod;
  if (mm) {
    if (mm.$t){
      _t=mm.$t;
      if (typeof(_t)==='string') {
        var _ta;
        if (coi.$targs && coi.$targs[_t])_t=coi.$targs[_t];
        else if ($$$mptypes.Container$getMethod && $$$mptypes.Container$getMethod.a && $$$mptypes.Container$getMethod.a[_t])_t=$$$mptypes.Container$getMethod.a[_t];
        else if (mm.tp && mm.tp[_t])_ta=mm.tp[_t];
        else if (_tipo.$crtmm$.tp && _tipo.$crtmm$.tp[_t])_ta=_tipo.$crtmm$.tp[_t];
        if (_ta && _ta.sts)_t=_ta.sts.length===1?_ta.sts[0]:{t:'i',l:_ta.sts};
        if (typeof(_t)==='string')_t={t:Anything};
      }
      if (!extendsType(_t,$$$mptypes.Type$getMethod))throw IncompatibleTypeException$meta$model("Incompatible Type argument");
    }
    validate$params(mm.ps,_a,"Wrong number of Arguments for getMethod");
    _a=tupleize$params(mm.ps);
  }
  if (fun && noInherit) {
    var mm=getrtmm$$(fun);
    if (mm && mm.$cont!==coi.tipo)return null;
  }
  return AppliedMethod(fun, types, {Container$Method:{t:_tipo},Type$Method:_t,Arguments$Method:_a});
}
function coigetmtd$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=getEmpty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getMethods);
  for (m in _tipo.$$.prototype) {
    var mem=getnpmem$(_tipo.$$.prototype,m);
    if (mem && mem.$$===undefined) {
      var mm=getrtmm$$(mem);
      if (mm && mm.d && mm.d[mm.d.length-2]=='$m') {
        if (noInherit && mm.$cont!==coi.tipo)continue;
        if (!extendsType(mm.$t,$$$mptypes.Type$getMethods))continue;
        var anns=allann$(mm);
        if (!mm.tp && anns && coi$is$anns(anns,ats) && validate$params(mm.ps,$$$mptypes.Arguments$getMethods,'',1)) {
          var types=[].rt$({t:Type$meta$model,a:{Type:{t:Anything}}});
          if (mm.ps) for (var i=0; i<mm.ps.length;i++) {
            types.push(typeLiteral$meta({Type$typeLiteral:mm.ps[i].$t}));
          }
          mems.push(AppliedMethod(mem,undefined,{Container$Method:{t:_tipo},Type$Method:mm.$t,Arguments$Method:types}));
        }
      }
    }
  }
  return mems.length===0?getEmpty():ArraySequence(mems,{Element$ArraySequence:{t:Method$meta$model,a:{Container$Method:$$$mptypes.Container$getMethods,Arguments$Method:$$$mptypes.Arguments$getMethods,Type$Method:$$$mptypes.Type$getMethods}}});
}
function coicase$(coi){
  var cts = coi.tipo.$crtmm$.of;
  if (cts && cts.length > 0) {
    var rv=[];
    for (var i=0; i < cts.length; i++) {
      if (typeof(cts[i])==='function') {
        rv.push(cts[i]());
      }
    }
    return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:{t:coi.tipo}});
  }
  return getEmpty();
}
function allann$(mm) {
  if (typeof(mm.an)==='function')mm.an=mm.an();
  var a=getAnnotationsForBitmask(mm.pa);
  if (a) {
    if (mm.an) {
      for (var i=0;i<mm.an.length;i++)a.push(mm.an[i]);
    }
  } else {
    a=mm.an;
  }
  return a||[];
}
//Union Type of the receiving type with a new one
function coiut$(coi,t,ot){
  if (is$(t,{t:AppliedUnionType$jsint})) {
    return t.union(coi,{Other$union:ot});
  }
  var _ut={t:'u',l:[coi.$$targs$$.Type$ClassOrInterface,ot]};
  return AppliedUnionType$jsint(_ut,[coi,t].rt$(_ut,1),{Union$AppliedUnionType:_ut});
}
//Intersection type of the receiving type with a new one
function coiit$(coi,t,ot){
  if (is$(t,{t:AppliedIntersectionType$jsint})) {
    return t.intersection(coi,{Other$intersection:ot});
  }
  var _ut={t:'i',l:[coi.$$targs$$.Type$ClassOrInterface,ot]};
  return AppliedIntersectionType$jsint(_ut,[coi,t].rt$(_ut,1),{Union$AppliedIntersectionType:_ut});
}
