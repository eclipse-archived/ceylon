function getT$name() {return this.constructor.T$name;}
function getT$all() {return this.constructor.T$all;}
function initType(type, typeName) {
    var cons = function() {}
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=2; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for (var $ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
    cons.prototype.getT$name = getT$name;
    cons.prototype.getT$all = getT$all;
}
function initTypeProto(type, typeName) {
    initType.apply(this, arguments);
    var args = [].slice.call(arguments, 2);
    args.unshift(type);
    inheritProto.apply(this, args);
}
function initTypeProtoI(type, typeName) {
  initType.apply(this, arguments);
  var args = [].slice.call(arguments, 2);
  if (type.$$.T$all['ceylon.language::Object']===undefined) {
    type.$$.T$all['ceylon.language::Object']=$_Object;
    //args.unshift($_Object);
  }
  args.unshift(type);
  inheritProto.apply(this, args);
}
function initExistingType(type, cons, typeName) {
    type.$$ = cons;
    cons.T$name = typeName;
    cons.T$all = {}
    cons.T$all[typeName] = type;
    for (var i=3; i<arguments.length; ++i) {
        var superTypes = arguments[i].$$.T$all;
        for (var $ in superTypes) {cons.T$all[$] = superTypes[$]}
    }
    var proto = cons.prototype;
    if (proto !== undefined) {
        try {
            proto.getT$name = getT$name;
            proto.getT$all = getT$all;
        } catch (exc) {
            // browser probably prevented access to the prototype
        }
    }
}
function initExistingTypeProto(type, cons, typeName) {
    var args = [].slice.call(arguments, 0);
    args.push($init$Basic());
    initExistingType.apply(this, args);
    var proto = cons.prototype;
    if ((proto !== undefined) && (proto.getHash === undefined)) {
    	var origToString = proto.toString;
        try {
            inheritProto(type, Basic);
            proto.toString = origToString;
        } catch (exc) {
            // browser probably prevented access to the prototype
        }
    }
}
function inheritProto(type) {
    var proto = type.$$.prototype;
    for (var i=1; i<arguments.length; ++i) {
        var superProto = arguments[i].$$.prototype;
        var names = Object.getOwnPropertyNames(superProto);
        for (var j=0; j<names.length; ++j) {
            var name = names[j];
            var desc = Object.getOwnPropertyDescriptor(superProto, name);
            // only copy own, enumerable properties
            if (desc && desc.enumerable) {
                if (desc.get) {
                    // defined through getter/setter, so copy the definition
                    Object.defineProperty(proto, name, desc);
                } else {
                  var ow=proto[name]===undefined || desc.value.$fml===undefined;
                  if (ow && proto[name]) {
                    //Check if we really need to overwrite an existing method
                    //Get the container of the existing method, and its metamodel
                    var em = getrtmm$$(proto[name]);
                    em = em && em.$cont && em.$cont.$$ && em.$cont.$$.prototype;
                    if (em) {
                      //Now the container of the new method, and its metamodel
                      var nm = getrtmm$$(desc.value);
                      nm = nm && nm.$cont && nm.$cont.$$ && nm.$cont.$$.prototype;
                      if (nm) {
                        //Overwrite of container of new method extends container of current method
                        //but not the other way around
                        ow=em.getT$all()[nm.getT$name()]===undefined;
                      }
                    }
                  }
                  if (ow) proto[name] = desc.value;
                }
            }
        }
    }
}
// Define a property on the given object (which may be a prototype).
// "get" and "set" are getter/setter functions, and the latter is optional.
function atr$(obj, name, get, set, metamodel,settermm) {
  Object.defineProperty(obj, name, {get: get, set: set, configurable: true, enumerable: true});
  //if (name[0]==='$')name=name.substring(1);//names matching reserved words are prefixed with $
  obj['$prop$get'+name[0].toUpperCase()+name.substring(1)] = {get:get, set:set, $crtmm$:metamodel};
  if (settermm)set.$crtmm$=settermm;
}
// Create a copy of the given property. The name of the copied property is name+suffix.
// This is used in closure mode to provide access to inherited attribute implementations.
function copySuperAttr(obj, name, suffix) {
    var desc;
    var o = obj;
    // It may be an inherited property, so check the prototype chain.
    do {
        if ((desc = Object.getOwnPropertyDescriptor(o, name))) {break;}
        o = o.__proto__;
    } while (o);
    if (desc) {
        Object.defineProperty(obj, name+suffix, desc);
    }
}
// read/writeAttrib return the getter/setter for the given property as defined in the
// given type. This is used in prototype mode to access inherited attribute implementations.
function attrGetter(type, name) {
    return Object.getOwnPropertyDescriptor(type.$$.prototype, name).get;
}
function attrSetter(type, name, value) {
    return Object.getOwnPropertyDescriptor(type.$$.prototype, name).set;
}
ex$.initType=initType;
ex$.initTypeProto=initTypeProto;
ex$.initTypeProtoI=initTypeProtoI;
ex$.initExistingType=initExistingType;
ex$.initExistingTypeProto=initExistingTypeProto;
ex$.inheritProto=inheritProto;
ex$.atr$=atr$;
ex$.copySuperAttr=copySuperAttr;
ex$.attrGetter=attrGetter;
ex$.attrSetter=attrSetter;

function removeSupertypes(list) {
    for (var i=0; i < list.length; i++) {
        for (var j=i; i < list.length; i++) {
            if (i!==j) {
                if (extendsType(list[i],list[j])) {
                    list[j]=list[i];
                } else if (extendsType(list[j],list[i])) {
                    list[i]=list[j];
                }
            }
        }
    }
}

function find_type_in_list$(t,l) {
  for (var i=0; i < l.length; i++) {
    if (l.t==='u' || l.t==='i') {
      var _t=find_type_in_list(t,l.l);
      if (_t)return _t;
    } else if (l.t===t.t) {
      return t;
    }
  }
  return undefined;
}
function ut$(a,b){
  if (a.t==='u'){
    //add to current union if not present
    //TODO b can already be a union
    var et=find_type_in_list$(b,a.l);
    if (et) {
      if (a.a&&b.a) {
        //union the targs or something
      } else {
        return a;
      }
    } else {
      var na=a.l.slice(0);
      na.push(b);
      return {t:'u',l:na};
    }
  } else if (b.t==='u') {
    //new union with a+list of b's
    var et=find_type_in_list$(a,b.l);
    if (et) {
      if (a.a&&b.a) {
      } else {
        return b;
      }
    } else {
      var na=b.l.slice(0);
      na.push(a);
      return {t:'u',l:na};
    }
  } else if (a.t==='i'||b.t==='i') {
    //new union
  } else if (a.t===b.t) {
    if (a.a&&b.a) {
      return {t:a.t,a:it$(a.a,b.a)};
    }
    return a;
  }
  return {t:'u',l:[a,b]};
}
function it$(a,b){
  if (a.t==='i') {
    //add to the current intersection if not present
    //TODO b can already be an intersection
    var et=find_type_in_list$(b,a.l);
    if (et) {
      if (a.a&&b.a) {
        //intersect the targs or something
      } else {
        return a;
      }
    } else {
      var na=a.l.slice(0);
      na.push(b);
      return {t:'i',l:na};
    }
  } else if (b.t==='i') {
    //create a new intersection with list of a+list of b's
    var et=find_type_in_list$(a,b.l);
    if (et) {
      if (a.a&&b.a) {
      } else {
        return b;
      }
    } else {
      var na=b.l.slice(0);
      na.push(a);
      return {t:'i',l:na};
    }
  } else if (a.t==='u'||b.t==='u') {
    //Create new intersection
  } else if (a.t===b.t) {
    if (a.a&&b.a) {
      return {t:a.t,a:ut$(a.a,b.a)};
    }
    return a;
  }
  return {t:'i',l:[a,b]};
}
//Sets the type arguments for an object
//obj: The object to set
//targs: A JS object with the type arguments
//t: the type function
function set_type_args(obj,targs,t) {
  if (obj===undefined)return;
  if (obj.$$targs$$ === undefined) {
    obj.$$targs$$={};
  }
  for (x in targs) {
    var n=targs[x];
    if (!n)continue;
    var e=obj.$$targs$$[x];
    if (e) {
      var mm=getrtmm$$(t);
      var iance=mm && mm.tp && mm.tp[x] && mm.tp[x].dv;
      if (iance === 'out') {
        //Intersection
        n=it$(e,n);
      } else if (iance === 'in') {
        //Union
        n=ut$(e,n);
      }
    }
    obj.$$targs$$[x] = n;
  }
}
function add_type_arg(obj, name, type) {
    if (obj===undefined)return;
    if (obj.$$targs$$ === undefined) {
        obj.$$targs$$={};
    }
    obj.$$targs$$[name]=type;
}
function wrapexc(e,loc,file) {
  if (loc !== undefined) e.$loc=loc;
  if (file !== undefined) e.$file=file;
  return e;
}
function throwexc(e,loc,file) {
  if (loc !== undefined) e.$loc=loc;
  if (file !== undefined) e.$file=file;
  throw e;
}

//for optimized ranges
//"exit optimized range loop"
function eorl$(comp) {
  if (comp===equal())return function(a,b){return a.compare(b)===equal();}
  return function(a,b){return a.compare(b)!==comp;}
}
//for shorter assert
function asrt$(cond,msg,loc,file) {
  if (!cond)throw wrapexc(AssertionError(msg),loc,file);
}
ex$.set_type_args=set_type_args;
ex$.add_type_arg=add_type_arg;
ex$.throwexc=throwexc;
ex$.wrapexc=wrapexc;
ex$.eorl$=eorl$;
ex$.asrt$=asrt$;
var INIT$={a:1};
ex$.INIT$=INIT$;

//For foo[bar]=baz
function setObjectProperty(object, key, value) {
  object[key]=value;
}
ex$.setObjectProperty=setObjectProperty;
