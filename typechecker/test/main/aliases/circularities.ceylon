
@error interface Xa satisfies Ya {}
interface Ya => Za;
@error interface Za satisfies Xa {}

@error class Xb() extends Yb() {}
class Yb() => Zb();
@error class Zb() extends Xb() {}

@error alias Xc => Yc;
@error alias Yc => Zc;
@error alias Zc => Xc;