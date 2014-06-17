function(other) {
    if (other.constructor===String) {
        return other.valueOf()===this.valueOf();
    } else if (is$(other, {t:$_String})) {
        if (other.size===this.size) {
            var oi=other.iterator();
            var ti=this.iterator();
            var oc=oi.next(); var tc;
            while((tc=ti.next())!=getFinished()){
                if (!tc.equals(oc))return false;
                oc=oi.next();
            }
            return true;
        }
    }
    return false;
}
