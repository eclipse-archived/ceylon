function(other) {
  return is$(other,{t:FreeClass$jsint}) &&
    other.declaration.equals(this.declaration) &&
    this.typeArguments.equals(other.typeArguments);
}
