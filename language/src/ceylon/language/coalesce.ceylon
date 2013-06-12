"Return a sequence containing the given values which are
 not null. If there are no values which are not null,
 return an empty sequence."
shared {Element&Object*} coalesce<Element>(
        "The values, some of which may be null."
        {Element*} values) 
                => values.coalesced;
