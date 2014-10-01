@noanno
abstract class XX1643() satisfies Number<XX1643>&Comparable<XX1643|YY1643> {
    compare(XX1643|YY1643 other) => nothing;
    
}
@noanno
abstract class YY1643() satisfies Number<YY1643>&Comparable<XX1643|YY1643> {
    compare(XX1643|YY1643 other) => nothing;
}
void use1643(XX1643 xx, YY1643 yy, XX1643|YY1643 xy) {
    variable Comparison c;
    c = xx <=> xx;
    c = xx <=> yy;
    c = xx <=> xy;//breaks
    
    c = yy <=> xx;
    c = yy <=> yy;
    c = yy <=> xy;//breaks
    
    c = xy <=> xx;
    c = xy <=> yy;
    c = xy <=> xy;//breaks
}