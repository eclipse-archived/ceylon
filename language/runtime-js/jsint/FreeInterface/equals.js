function(other) {
  return is$(other,{t:FreeInterface$jsint}) &&
    other.declaration.equals(this.declaration) &&
    this.typeArgumentWithVariances.equals(other.typeArgumentWithVariances);
}
