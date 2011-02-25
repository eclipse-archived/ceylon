shared interface TypeDeclaration
        satisfies Declaration {

    doc "Return all matching members of the given type."
    shared formal Set<M> members<M>(Type<M> type = MemberDeclaration)
            given M satisfies MemberDeclaration;

}