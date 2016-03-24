# Running the samples

If you are trying these examples right from the unzipped distribution .zip file
you only need to enter each directory and type `ant`, this will automatically
compile, generate documentation and run the example.

If you copied the examples to another place or you are running an installed
version of Ceylon you will need to tell Ant where Ceylon is installed. You can
do this either in two ways, using an environment variable (use `set` instead
of `export` on Windows):

```
export CEYLON_HOME=/path/to/ceylon/install
ant
```

or by passing it as an argument to ant:

```
ant -Dceylon.home=/path/to/ceylon/install
```

