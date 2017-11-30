object a satisfies Bug6156Int<Bug6156Document> {
    shared actual Character getChar(Bug6156Document doc, Integer offset) => doc.getChar(offset);
}