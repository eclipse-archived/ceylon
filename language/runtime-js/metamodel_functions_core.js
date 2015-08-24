function getrtmm$$(x) {
  if (x===undefined||x===null)return undefined;
  if (typeof(x.$crtmm$)==='function')x.$crtmm$=x.$crtmm$();
  return x.$crtmm$;
}
ex$.getrtmm$$=getrtmm$$;
//From a runtime metamodel, get the model definition by following the path into the module's model.
function get_model(mm) {
  var map=typeof(mm.mod)==='function'?mm.mod():mm.mod;
  var path=mm.d;
  for (var i=0; i < path.length; i++) {
    var _p=path[i];
    if (i===0 && _p==='$')_p='ceylon.language';
    else if (i==path.length-1&&_p==='$set' && map.nm && map.$set)return map;
    map = map[_p];
  }
  return map;
}

function pushTypes(list, types,targ$2) {
  for (var i=0; i<types.length; i++) {
    var t = types[i];
    var nt;
    if (t.t === 'u') {
      nt=applyUnionType(t, targ$2);
    } else if (t.t === 'i') {
      nt=applyIntersectionType(t, targ$2);
    } else if (typeof(t)==='string') {
      nt=typeLiteral$meta({Type$typeLiteral:targ$2[t]},targ$2);
    } else {
      nt=typeLiteral$meta({Type$typeLiteral:t},targ$2);
    }
    if (nt && !list.contains(nt))list.push(nt);
  }
  return list;
}

function applyUnionType(ut,targ$2) { //return AppliedUnionType
  var cases = pushTypes([], flattentype$(ut.l,'u'),targ$2);
  return AppliedUnionType$jsint(ut, cases.rt$(ut), {Union$AppliedUnionType:ut});
}
function applyIntersectionType(it,targ$2) { //return AppliedIntersectionType
  var sats = pushTypes([], flattentype$(it.l,'i'),targ$2);
  return AppliedIntersectionType$jsint(it, sats.rt$(it), {Union$AppliedIntersectionType:it});
}

//Shortcut for modules$meta().find(m/v).findPackage(p)
function fmp$(m,v,p) {
  return modules$meta().find(m,v).findPackage(p);
}
ex$.fmp$=fmp$;
function lmp$(x,p) {
  return Modulo$jsint(x).findPackage(p);
}
ex$.lmp$=lmp$;
//Some type arithmetic for metamodel queries
//rt=real type, ta=Container type argument
function mmfca$(rt, ta) {
  if (!ta.t.$$)return rt;
  ta=ta.t;
  if (extendsType({t:rt},{t:ta}))return ta;
  if (extendsType({t:ta},{t:rt}))return rt;
  var mm=getrtmm$$(ta);
  if (mm) {
    //Supertypes
    var st;while((st=mm['super'])) {
      if (extendsType({t:rt},st)){
        return st.t;
      }
      mm=getrtmm$$(st.t);
    }
    //Satisfied types
    mm=getrtmm$$(ta);
    var sts=mm['sts'];
    if (sts) for(var i=0;i<sts.length;i++) {
      st=sts[i];
      if (st.t.$$ && extendsType({t:rt},st))return st.t;
      //TODO recurse st?
    }
  }
  return rt;
}
ex$.mmfca$=mmfca$;
//Retrieve a prototype's member, UNLESS it's a property
function getnpmem$(proto,n) {
  if (n.substring(0,6)==='$prop$')return undefined;
  if (proto['$prop$get'+n[0].toUpperCase()+n.substring(1)])return undefined;
  return proto[n];
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
//Generate the qualified name of a type
function qname$(mm) {
  if (mm.t==='u' || mm.t==='i') {
    var qn='';
    for (var i=0; i < mm.l.length; i++) {
      if (i>0) {
        qn+= mm.t==='u' ? '|' : '&';
      }
      qn+=qname$(mm.l[i]);
    }
    return qn;
  }
  if (mm.t) {
    mm=mm.t;
  }
  if (mm.$crtmm$)mm=getrtmm$$(mm);
  if (!mm.d && mm._alias)mm=getrtmm$$(mm._alias);
  if (!mm.d)return "[unnamed type]";
  var qn=mm.d[0];
  if (qn==='$')qn='ceylon.language';
  for (var i=1; i<mm.d.length; i++){
    var n=mm.d[i];
    var p=n.indexOf('$');
    if(p!==0)qn+=(i==1?"::":".")+(p>0?n.substring(0,p):n);
  }
  return qn;
}
