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
    else if (i===path.length-1&&_p==='$set' && map.nm && map.$set)return map;
    if (map===undefined)console.trace("WRONG MODEL PATH " + path + " index " + i);
    if (map[_p]===undefined) {
      if (_p.indexOf('$') > 0) {
        //unshared stuff
        var _p2=_p.substring(0,_p.indexOf('$'));
        if (map[_p2]===undefined)console.trace("WRONG MODEL PATH with unshared " + path + " key " + _p + " (unshared " + _p2 + ")");
        _p=_p2;
      } else {
        console.trace("WRONG MODEL PATH " + path + " key " + _p);
      }
    }
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
//Read the type parameter list of a type and generate type arguments for it
function tparms2targs$(c,t){
  var mm=getrtmm$$(c).tp;
  if (mm) {
    if (Object.keys(mm).length == t.size) {
      var r={},i=0;
      for (var k in mm) {
        r[k]=t.$_get(i++).$$targs$$.Target$Type;
      }
      return r;
    }
  }
  return undefined;
}

// Taken from JsIdentifierNames.java
var reservedWords$ = [
  // Identifiers that have to be escaped because they are keywords in
  // JavaScript. We don't have to include identifiers that are also
  // keywords in Ceylon because no such identifiers can occur in Ceylon
  // source code anyway.
  //Language
  "undefined", "boolean", "byte", "char", "const",
  "debugger", "default", "delete", "do", "double", "enum", "export", "false",
  "final", "float", "goto", "implements", "instanceof", "int", "long",
  "native", "new", "null", "private", "protected", "public", "short", "static",
  "synchronized", "throws", "transient", "true", "typeof", "var", "volatile",
  "with", "abstract", "process", "require",
  //Types
  "Date", "Object", "Boolean", "Error", "Number", "RegExp",
  //JS Object
  "hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable",
  //JS Function
  "Function",
  "call", "arguments", "caller", "apply", "bind", "eval",
  //JS Number
  "toFixed", "valueOf", "toPrecision", "toExponential",
  //JS String
  "String",
  "charAt", "strike", "fixed", "sub", "charCodeAt",
  "trimLeft", "toLocaleUpperCase", "toUpperCase", "fontsize", "search",
  "toLocaleLowerCase", "small", "big", "fontcolor", "blink", "trim",
  "bold", "match", "substr", "trimRight", "replace", "split", "sup", "link",
  "localeCompare", "valueOf", "substring", "toLowerCase", "italics", "anchor",
  //JS Array
  "Array",
  "toLocaleString", "splice", "map", "forEach", "reverse",
  "join", "push", "shift", "pop", "sort", "unshift", "reduceRight", "reduce",
  "every", "filter",
  //String, Array, etc
  "length", "toString", "constructor", "prototype",
  "concat", "indexOf", "lastIndexOf", "slice", "get"
];

function escapePropertyName$(name){
  if (reservedWords$.indexOf(name)>=0)
    return '$_'+name;
  return name;
}

function getValuePropertyName$(propertyName){
  return '$prop$get'+propertyName[0].toUpperCase()+propertyName.substring(1);
}

// Find a method by name from the prototype
function findMethodByNameFromPrototype$(proto, name){
    var escapedName = escapePropertyName$(name);
    var decl = proto[escapedName];
    if (decl===undefined) {
      //Let's just look for this thing everywhere
      for (var key in proto) {
        var propname=getValuePropertyName$(key);
        if (!key.startsWith("$prop$get") && proto[propname]===undefined && typeof(proto[key])==='function') {
          var mm = getrtmm$$(proto[key]);
          var mod = mm && get_model(mm);
          if (mod && mod.nm === name) {
            return proto[key];
          }
        }
        var m$ = proto[propname] ? undefined : proto[key];
        decl = typeof(m$)==='function' && m$.$$===undefined ? getrtmm$$(m$) : undefined;
        if (decl && decl.d && decl.d[decl.d.length-1]===name){
          return proto[key];
        }else{ 
          decl = undefined;
        }
      }
    }
    return decl;
}

function rejectInheritedOrPrivate$(mm, tipo, noInherit){
  if (noInherit) {
    return (mm&&mm.$cont!==tipo);
  }else{
    //If we found a non-shared attribute and want inherited members, we ignore it
    return ((mm.pa & 1) == 0);
  }
}

function findModuleOrThrow$(name, version){
  var mod = modules$meta().find(name, version);
  if(!mod)
    throw AssertionError("Module "+name+"/"+version+" is not available");
  return mod;
}
ex$.findModuleOrThrow$=findModuleOrThrow$;
