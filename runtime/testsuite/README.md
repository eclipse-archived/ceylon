
## Recreating necessary test modules

The runtime tests use a couple of pre-compiled modules that you might need to recreate
at some point, for example when binary compatibility has been broken and the current
modules cause errors.

The sources for these modules can be found in the standard Ceylon `source` folder
and you should be able to compile them by just running `ceylon compile` in this folder.

And then to update the test repo run:

 - `rm -rf src/test/resources/repo/{hello,foo}`
 - `rm -rf src/test/resources/alternative/moduletest`
 - `cp -a modules/{hello,foo} src/test/resources/repo/`
 - `cp -a modules/moduletest src/test/resources/alternative/`


