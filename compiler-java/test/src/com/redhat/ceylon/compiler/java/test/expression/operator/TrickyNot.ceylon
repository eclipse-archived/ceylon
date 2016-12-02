import java.lang{JInteger=Integer}
import java.util.\ifunction{BiFunction}
shared alias FilterFunction => BiFunction<JInteger,JInteger,Boolean>;
void trickyNot() {
    FilterFunction filterPredicate = nothing;
    Integer element = JInteger(0);
    //Boolean predicateResult = ;
    if (!filterPredicate.apply(element, element)) {}
}