"An interface model that you can inspect."
shared sealed interface Interface<out Type=Anything>
    satisfies InterfaceModel<Type> {}
