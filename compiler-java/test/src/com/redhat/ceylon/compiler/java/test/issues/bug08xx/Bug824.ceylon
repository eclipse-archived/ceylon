@nomodel
interface Bug824_X {
    shared String name { return "Gavin"; }
}
@nomodel
interface Bug824_Y => Bug824_X;
@nomodel
interface Bug824_Z => Bug824_Y;
@nomodel
class Bug824_W() satisfies Bug824_Y {}
@nomodel
class Bug824_W2() satisfies Bug824_Z {}