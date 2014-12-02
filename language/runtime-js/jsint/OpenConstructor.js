function OpenConstructor$jsint(pkg, meta, that){
    if (meta===undefined)throw new Error("Constructor reference not found. Metamodel doesn't work with modules compiled in lexical scope style");
    $init$OpenConstructor$jsint();
    if (that===undefined)that=new OpenConstructor$jsint.$$;
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
    if (that.name_.indexOf('$')>0) {
      that.name_=that.name_.substring(0,that.name_.indexOf('$'));
    }
    ConstructorDeclaration$meta$declaration(that);
    return that;
}
