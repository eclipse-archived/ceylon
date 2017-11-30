import ceylon.language.meta.declaration {
    Declaration
}

void food(DList<> dlist) => dlist.getFromFirst(0);

interface DList<Element=Declaration> satisfies List<Element> {}
