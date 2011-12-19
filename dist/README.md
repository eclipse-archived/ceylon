# Ceylon distribution

Ceylon is a programming language for writing large programs in a team environment. 
The language is elegant, highly readable, extremely typesafe, and makes it easy to get things done. 
And it's easy to learn for programmers who are familiar with mainstream languages used in business computing. 
Ceylon has a full-featured Eclipse-based development environment, allowing developers to take best advantage of 
the powerful static type system. Programs written in Ceylon execute on any JVM.

Read more about ceylon at <http://ceylon-lang.org>.

## Distribution layout

- bin:          Unix/Windows commands:
-- ceylon:      Run a Ceylon program
-- ceylonc:     Compile a Ceylon program
-- ceylond:     Document a Ceylon program
- doc:          The Ceylon spec in HTML and PDF format
- lib:          Required libraries for the Ceylon commands
- repo:         Required bootstrap Ceylon modules (language, tools)
- runtime-repo: Required runtime Ceylon modules (module system)
- samples:      Sample Ceylon modules
- LICENSE:      The Ceylon license
- README.md:    This file

Note that the `ceylon.language` API documentation is in <repo/ceylon/language/0.1/module-doc>.

## Running programs

### Here's everything you need to compile and run the samples:

```
cd samples/helloworld
../../bin/ceylonc com.acme.helloworld
../../bin/ceylond com.acme.helloworld
../../bin/ceylon com.acme.helloworld/1.0.0 John Doe
```

```
cd samples/no-module
../../bin/ceylonc default
../../bin/ceylond default
../../bin/ceylon default
```

## License

The Ceylon distribution is and contains work released

- partly under the ASL v2.0 as provided in the LICENSE file that accompanied this code.
- partly under the GPL v2 + Classpath Exception as available 
  [in the ceylon-compiler repository](https://github.com/ceylon/ceylon-compiler/blob/master/LICENSE)

### License terms for 3rd Party Works

This software uses a number of other works, the license terms of which are 
documented in the NOTICE file that accompanied this code.

### Repository

The content of the ceylon-dit git repository 
The content of this code repository (available [here on github](https://github.com/ceylon/ceylon-dist)) 
is released under the ASL v2.0 as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to this repository, you
agree to license your contribution under the license mentioned above.