import ceylon.language.meta.declaration {
  AliasDeclaration, OpenType, NestableDeclaration,
  Package, Module, TypeParameter
}
import ceylon.language {
  MetamodelAnnotation=Annotation
}

shared native class OpenAlias(shared Object _alias) satisfies AliasDeclaration {
  shared actual native OpenType extendedType;
  shared actual OpenType openType => extendedType;
  shared actual native String qualifiedName;
  shared actual native Boolean toplevel;
  shared actual Module containingModule { throw Exception("OpenAlias.containingModule"); }
  shared actual native NestableDeclaration|Package container;
  shared actual Package containingPackage { throw Exception("OpenAlias.containingPackage"); }
  shared actual native Annotation[] annotations<out Annotation>()
    given Annotation satisfies MetamodelAnnotation;
  shared actual native Boolean annotated<Annotation>()
          given Annotation satisfies MetamodelAnnotation;
  shared actual native TypeParameter[] typeParameterDeclarations;
  shared actual native TypeParameter? getTypeParameterDeclaration(String name);
  shared actual native Boolean equals(Object other);
  shared actual native String name;
  shared actual native Boolean shared;
  shared actual native Boolean actual;
  shared actual native Boolean formal;
  shared actual native Boolean default;
  shared actual String string => "alias ``qualifiedName``";
  shared actual Integer hash => string.hash;
}
