function(elem) {
    if (!nn$(elem))return false;
    for (var i=0; i<this.arr$.length; i++) {
        if (nn$(this.arr$[i]) && $eq$(elem,this.arr$[i])) {
            return true;
        }
    }
    return false;
}
