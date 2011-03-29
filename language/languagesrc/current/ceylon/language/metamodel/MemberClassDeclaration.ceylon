shared interface MemberClassDeclaration<out X>
        satisfies MemberDeclaration & ClassDeclaration<X> {}