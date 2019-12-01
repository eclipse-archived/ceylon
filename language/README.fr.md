Module de Langue Ceylon
======================

Licence
-------

Le contenu de ce dépot est publié sous l'ASL v2.0
comme indiqué dans le fichier LICENSE accompagnant le code.

En soumettant une "pull request" ou tout autre contribution à ce dépot, vous consentez à l'utilisation de votre travail dans le cadre
de la licence citée précédemment

Structure du dépot :
--------------------

* `src/`          - L'implementation du module de langue Ceylon
* `runtime/`      - L'implémentation Java
* `test/`         - les tests

Build le compilateur et les outils
----------------------------

Pour mettre en place l'environnement de développementn pour compiler et pour build la distribution
jetez un oeil à [ceylon-dist](https://github.com/ceylon/ceylon-dist#ceylon-distribution).

Si une fois la distribution buildée, vous souhaitez build et tester le module de langue, 
retournez à `ceylon.language` puis executez

    ant clean publish
    
Pour executer les tests

    ant test

Autres commandes:

* `ant test`         - Executer les tests    
* `ant clean.repo`   - Nettoie le dépot local
* `ant publish`      - publie le module `ceylon.language` 
                       dans le dépot local.
