object hearts extends Suit("Hearts") {}
object diamonds extends Suit("Diamonds") {}
object clubs extends Suit("Clubs") {}
object spades extends Suit("Spades") {}

@error object extra extends Suit("Extra") {}

class BrokenCase() extends Broken() {}

void test() {
    Suit s = hearts;

    switch (s)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    
    @error switch (s)
    case (hearts) {}
    case (hearts,diamonds) {}
    case (clubs,spades) {}
    
    switch (s)
    case (hearts,diamonds) {}
    case (clubs,spades) {}
    
    switch (s)
    case (hearts) {}
    case (diamonds) {}
    else {}
    
    @error switch (s)
    case (hearts) {}
    case (hearts) {}
    case (diamonds) {}
    else {}
    
    //TODO: should this be an error?
    switch (s)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    else {}
    
    @error switch (s)
    case (hearts) {}
    case (diamonds) {}
    
    Suit? maybe = null;
    
    switch (maybe)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    case (is Null) {}
    
    switch (maybe)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    case (null) {}
    
    switch (maybe)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    else {}
    
    @error switch (maybe)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    
    variable Suit ss = clubs;
    
    switch (ss)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    
    variable Suit? mss = null;
    
    @error switch (mss)
    case (is Suit) {}
    case (is Null) {}
    
    Suit sss { return spades; }
    
    switch (sss)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}    
    
    Suit? msss { return null; }
    
    @error switch (msss)
    case (is Suit) {}
    case (is Null) {}
    
    switch (s!=hearts then s)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    case (null) {}
    
    @error switch (s!=hearts then s)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    case (is Null) {}
    
    
}