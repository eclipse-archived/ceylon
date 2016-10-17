import org.openide.awt {
    actionReference,
    actionID
}
import org.netbeans.api.editor.mimelookup {
    mimeRegistration
}
import org.netbeans.modules.csl.spi {
    languageRegistration,
    DefaultLanguageConfig
}
import org.netbeans.api.lexer {
    Language
}
import org.openide.util.lookup {
    serviceProvider
}

mimeRegistration {
    mimeType = "text/x-java";
    service = `class Bug6566`;
}
actionReference {
    path = "Yop";
    name = "Reset model";
}
actionID {
    category = "Tools";
    id = "blabla";
}
serviceProvider {
    service = `class Bug6566`;
}
shared class Bug6566() {  }


languageRegistration {
	mimeType = {"text/x-ceylon"};
}
class MyLanguage() extends DefaultLanguageConfig() {
	shared actual String displayName => nothing;
	shared actual Language<out Anything> lexerLanguage => nothing;
}

