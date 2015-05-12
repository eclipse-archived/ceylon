"An interface model that you can inspect."
shared sealed interface Interface<out Type>
    satisfies InterfaceModel<Type> {}
