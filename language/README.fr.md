Module de Langue Ceylon
======================

Licence
-------

Le contenu de ce dépôt est publié sous l'ASL v2.0
comme indiqué dans le fichier LICENSE accompagnant le code.

En soumettant une "pull request" ou tout autre contribution à ce dépôt, vous consentez à l'utilisation de votre travail dans le cadre
de la licence citée précédemment

Structure du dépôt :
--------------------

* `src/`          - L'implémentation du module de langue Ceylon
* `runtime/`      - L'implémentation Java
* `test/`         - Les tests

Build le compilateur et les outils
----------------------------

Pour mettre en place l'environnement de développement, pour compiler et pour build la distribution
jetez un oeil à [ceylon-dist](https://github.com/ceylon/ceylon-dist#ceylon-distribution).

Si une fois la distribution buildée, vous souhaitez build et tester le module de langue, 
retournez à `ceylon.language` puis executez

    ant clean publish
    
Pour exécuter les tests

    ant test

Autres commandes:

* `ant test`         - Exécuter les tests    
* `ant clean.repo`   - Nettoie le dépôt local
* `ant publish`      - Publie le module `ceylon.language` 
                       dans le dépôt local.
