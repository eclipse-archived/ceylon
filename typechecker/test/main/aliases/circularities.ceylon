
@error interface Xa satisfies Ya {}
interface Ya => Za;
interface Za satisfies Xa {}

@error class Xb() extends Yb() {}
class Yb() => Zb();
class Zb() extends Xb() {}

@error alias Xc => Yc;
alias Yc => Zc;
alias Zc => Xc;