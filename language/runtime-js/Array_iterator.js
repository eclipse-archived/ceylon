function() {
    var $idx$=0;
    var $arr$=this;
    return new ComprehensionIterator(function() {
        return ($idx$===$arr$.length) ? getFinished() : $arr$[$idx$++];
    }, this.$$targs$$);
}
