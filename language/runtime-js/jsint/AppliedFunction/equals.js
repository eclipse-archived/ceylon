function(oo){
  return is$(oo,{t:AppliedFunction$jsint}) &&
         oo.tipo===this.tipo && oo.typeArguments.equals(this.typeArguments)
         && (this.$bound?this.$bound.equals(oo.$bound):oo.$bound===this.$bound);
}

