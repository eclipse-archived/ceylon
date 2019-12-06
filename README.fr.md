# Ceylon

Ceci est la version 1.3.4-SNAPSHOT _"You'll Thank Me Later"_ de l'outil 
de commande Ceylon. Il s'agit d'une version de production de la plateforme.

Ceylon est un langage de programmation moderne, modulaire et statiquement typé. 
pour les machines virtuelles Java et JavaScript. Le langage se caractérise par
une syntaxe flexible et très lisible, un système de typage statique unique et d'une 
élégance hors du commun, une architecture de modules puissante et d'excellents outils,
incluant entre autre un superbe IDE basé sur Eclipse.

Ceylon permet le développement de modules multi-plateformes qui s'éxecute de manière
portative dans les deux environements de machines virtuelles. Alternativement, un
module de Ceylon peut cibler l'une ou l'autre plate-forme, auquel cas il peut 
interopérer avec du code nativement écrit pour cette plate-forme.

Pour en savoir plus sur Ceylon, rendez vous sur <http://ceylon-lang.org>.

Pour lire ceci dans d'autres langues : [`English`](/README.md).

## Composition de la distribution


- `cmr`                 - *Ceylon Module Resolver* module
- `common`              - Common code module
- `compiler-java`       - JVM compiler module
- `compiler-js`         - JS compiler module
- `dist`                - Build files 
- `language`            - Ceylon language module
- `model`               - Type model module
- `runtime`             - Runtime module
- `typechecker`         - Typechecker module
- `langtools-classfile` - Java tools classfile module fork
- `tool-provider`       - Ceylon tool provider module
- `LICENSE-ASL` 	- La licence ASL de Ceylon
- `LICENSE-GPL-CP` 	- La licence GPL/CP de Ceylon
- `LICENSE-LGPL` 	- La licence LGPL de Ceylon
- `README.md` 		- Ce fichier

## Construire la distribution

Allez dans le dossier `dist` et suivez les instructions dans le fichier [`BUILD.md`](/dist/BUILD.md) .

## Code source

Le code source est disponible sur GitHub:

<http://github.com/ceylon>

## Problèmes

Des Bugs et suggestions peuvent être rapporté dans le suivi des problèmes de GitHub.

<http://github.com/ceylon/ceylon/issues>

## Systèmes sur lesquels Ceylon fonctionne

Puisque Ceylon fonctionne sur la JVM il devrait fonctionner sur toutes les plates-formes
qui supporte une JVM compatible avec Java 7 ou 8. Cependant, nous avons testé les plateformes
suivantes pour nous assurer qu'elles fonctionnent :

### Linux

- Ubuntu "wily" 15.10 (64 bit) JDK 1.7.0_95 (IcedTea) Node 0.10.25
- Fedora 23 (64 bit), JDK 1.8.0_77 (OpenJDK)
- Fedora 22 (64 bit), JDK 1.8.0_72 (OpenJDK)
- Fedora 22 (64 bit), JDK 1.7.0_71 (Oracle)

### Windows

- Windows 10 Home (64 bit) 1.8.0_77
- Windows 7 (64 bit) 1.7.0_05 (Oracle)
- Windows Server 2008 R2 SP1 JDK 1.7.0_04

### OSX

- OSX 10 Lion (10.8.5) JDK 1.7.0_40 (Oracle) Node 0.10.17
- OSX 11 El Capitan (10.11.6) JDK 1.7.0_80 (Oracle) Node 0.10.35

## License

La distribution de Ceylon et le code qu'elle contient est :

- en partie sous licence ASL v2.0 comme indiqué dans le fichier
 `LICENSE-ASL` qui accompagnait ce code, et
- en partie sous licence GPL v2 + Classpath Exception comme indiqué
dans le fichier `LICENSE-GPL-CP` qui accompagnait ce code.

### Termes de la licence pour les travaux d'un tiers

Ce logiciel utilise un certain nombre d'autres travaux qui sont documentés
dans le fichier `NOTICE` qui accompagne ce code.

### Repository

Le contenu de ce dépôt de code, [disponible ici sur GitHub][ceylon], 
est publié sous licence ASL v2.0 comme indiqué dans le fichier `LICENSE-ASL` 
qui accompagne ce code.

[ceylon]: https://github.com/ceylon/ceylon

En soumettant une "pull request" ou en contribuant d'une autre manière à ce
dépot, vous acceptez que votre publication soit sous la licence mentionné
ci-dessus.

## Remerciements

Nous sommes profondément redevables aux bénévoles de la communauté qui ont contribué à la réalisation de ce projet
et à une partie substantielle de la base de code actuelle de Ceylon, travaillant souvent sur leur propre temps libre.

Ceylon est un projet de la[Fondation Eclipse] (http://eclipse.org).
