function(other) {
  return is$(other,{t:FreeInterface$jsint}) &&
    other.declaration.equals(this.declaration) &&
    this.typeArguments.equals(other.typeArguments);
}
