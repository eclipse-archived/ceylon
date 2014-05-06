import org.antlr.runtime { Token }

class UsesAntrl() {
    shared Token? token = null;
}

class NotDirectly() {
    shared UsesAntrl? uses = null;
}

void run() {
    print(`NotDirectly`.getAttribute<NotDirectly,UsesAntrl?>("uses"));
}