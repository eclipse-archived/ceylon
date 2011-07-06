@nomodel
class KlassTypeParamsSatisfies<U,V,W,X>()
    given U satisfies Equality
    given V satisfies Sized
    given W satisfies Equality & Sized
    given X satisfies Container & Sized
{
}