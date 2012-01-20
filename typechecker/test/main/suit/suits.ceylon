object hearts extends Suit("Hearts") {}
object diamonds extends Suit("Diamonds") {}
object clubs extends Suit("Clubs") {}
object spades extends Suit("Spades") {}

@error object extra extends Suit("Extra") {}

void test() {
    Suit s = hearts;

    switch (s)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    
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
    case (is Nothing) {}
    
    switch (maybe)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    case (nothing) {}
    
    /*switch (maybe)
    case (hearts) {}
    case (diamonds) {}
    case (clubs) {}
    case (spades) {}
    case (null) {}*/
    
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
    
    
}