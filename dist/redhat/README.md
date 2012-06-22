## How to build the RPM

This is going to be from scratch asuming a clean Fedora install.

First we're going to make sure we've got all the core development tools installed:

 - `sudo yum groupinstall "Development Tools"`

Plus some usefull tools for making RPMs:

 - `sudo yum install rpmdevtools`

Now we'll set up the build environemnt for making an RPM:

 - `rpmdev-setuptree`

**For the following commands make sure your current directory is the `ceylon-dist` project.**

To build an RPM we need a `.spec` file so we copy it to the build environment:

 - `cp redhat/ceylon.spec ~/rpmbuild/SPECS/`

The next thing we need to create an RPM are the *sources*, which in our case is the official distribution ZIP file, so let's create it:

 - `ant release`

The result we'll again copy to the build environment (you'll have to adjust the release number to coincide with the package you actually built of course):

 - `cp ceylon-0.3.zip ~/rpmbuild/SPECS/`

**At this point make sure that the version as defined in the `ceylon.spec` you copied to the build environment is the same 
as the package that was just built!**

You can open it up in an editor and check that the following three lines contain the correct information:

```
%define major_version 0
%define minor_version 3
%define micro_version 0
```

And finally we get to the point where we actually build the RPM package:

 - `rpmbuild -bb --clean ceylon.spec`

The result you'll be able to find in `~/rpmbuild/RPMS/noarch`!
