"An interface model that you can inspect."
shared interface Interface<out Type=Anything>
    satisfies InterfaceModel<Type> {}
