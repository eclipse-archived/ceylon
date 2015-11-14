function(elem) {
    if (!nn$(elem))return false;
    for (var i=0; i<this.length; i++) {
        if (nn$(this[i]) && elem.equals(this[i])) {
            return true;
        }
    }
    return false;
}
