import java.lang{JInteger=Integer}
import java.util.\ifunction{BiFunction}

@noanno
shared alias Bug6650 => BiFunction<JInteger,JInteger,Boolean>;
@noanno
void bug6650() {
    Bug6650 filterPredicate = nothing;
    JInteger element = JInteger(0);
    if (!filterPredicate.apply(element, element)) {}
}