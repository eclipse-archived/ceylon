function OpenCallableConstructor$jsint(pkg, meta, that){
    if (meta===undefined)throw new Error("Constructor reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenCallableConstructor$jsint();
    if (that===undefined)that=new OpenCallableConstructor$jsint.$$;
    that.containingPackage_=pkg;
    var _mm=getrtmm$$(meta);
    if (_mm === undefined) {
      //it's a metamodel
      that.meta_=meta;
      that.tipo=_findTypeFromModel(pkg,meta);
      _mm = getrtmm$$(that.tipo);
    } else {
      //it's a type
      that.tipo = meta;
      that.meta_ = get_model(_mm);
    }
    that.name_=(that.meta&&that.meta.nm)||_mm.d[_mm.d.length-1];
    if (that.name_==='$def' || _mm.d[_mm.d.length-2]!=='$cn') {
      that.name_='';
    } else if (that.name_.indexOf('$')>0) {
      that.name_=that.name_.substring(0,that.name_.indexOf('$'));
    }
    CallableConstructorDeclaration$meta$declaration(that);
    return that;
}
