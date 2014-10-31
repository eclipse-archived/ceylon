# *** BEGIN LICENSE BLOCK ***
#
# *** END LICENSE BLOCK ***

%define section free

%define major_version 1
%define minor_version 1
%define micro_version 1
%define ceylon_home /usr/lib/ceylon/%{major_version}.%{minor_version}.%{micro_version}

# The name of the source zip file (without .zip)
%define name_source %{name}
# The name of the root folder within the source zip
%define folder_source %{name}

# Make sure rpmbuild leaves JAR files alone!
%define __jar_repack 0
%global __provides_exclude ^.*$
%global __requires_exclude ^.*$

Name: ceylon-%{version}
Epoch: 0
Version: %{major_version}.%{minor_version}.%{micro_version}
Release: 1%{?dist}
Summary: Ceylon language

Group: Development/Languages
License: ASL 2.0 and GPL v 2 + Classpath exception
URL: http://www.ceylon-lang.org//
Source0: http://downloads.ceylon-lang.org/cli/%{name_source}.zip
BuildRoot: %{_tmppath}/%{name}-%{version}-%{release}-root
BuildArch:     noarch
BuildRequires: zip
Requires(post): %{_sbindir}/update-alternatives
Requires(postun): %{_sbindir}/update-alternatives

%description
Ceylon is a programming language for writing large programs in a team
environment. The language is elegant, highly readable, extremely typesafe,
and makes it easy to get things done. And it's easy to learn for programmers
who are familiar with mainstream languages used in business computing.
Ceylon has a full-featured Eclipse-based development environment, allowing
developers to take best advantage of the powerful static type system.
Programs written in Ceylon execute on any JVM.

%prep
%setup -q -n %{folder_source}


%build
export LANG=en_US.UTF-8


%install
rm -rf $RPM_BUILD_ROOT%{ceylon_home}
# CEYLON_HOME and subdirs
mkdir -p $RPM_BUILD_ROOT%{ceylon_home}/{bin,lib,repo,doc,doc/en,samples,templates}
install -d -m 755 %{buildroot}%{_mandir}/man1

rm -f bin/*.bat
cp -pr bin/* $RPM_BUILD_ROOT%{ceylon_home}/bin
cp -pr repo/* $RPM_BUILD_ROOT%{ceylon_home}/repo
cp -pr lib/* $RPM_BUILD_ROOT%{ceylon_home}/lib
cp -pr doc/en/* $RPM_BUILD_ROOT%{ceylon_home}/doc/en
cp -pr samples/* $RPM_BUILD_ROOT%{ceylon_home}/samples
cp -pr templates/* $RPM_BUILD_ROOT%{ceylon_home}/templates
cp -pr contrib/* $RPM_BUILD_ROOT%{ceylon_home}/contrib
cp -pr doc/man/man1/ceylon.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-%{version}.1
cp -pr doc/man/man1/ceylon-all.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-all-%{version}.1
cp -pr doc/man/man1/ceylon-bash-completion.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-bash-completion-%{version}.1
cp -pr doc/man/man1/ceylon-build.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-build-%{version}.1
cp -pr doc/man/man1/ceylon-classpath.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-classpath-%{version}.1
cp -pr doc/man/man1/ceylon-compile-js.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-compile-js-%{version}.1
cp -pr doc/man/man1/ceylon-compile.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-compile-%{version}.1
cp -pr doc/man/man1/ceylon-config.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-config-%{version}.1
cp -pr doc/man/man1/ceylon-copy.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-copy-%{version}.1
cp -pr doc/man/man1/ceylon-doc-tool.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-doc-tool-%{version}.1
cp -pr doc/man/man1/ceylon-doc.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-doc-%{version}.1
cp -pr doc/man/man1/ceylon-help.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-help-%{version}.1
cp -pr doc/man/man1/ceylon-import-jar.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-import-jar-%{version}.1
cp -pr doc/man/man1/ceylon-info.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-info-%{version}.1
cp -pr doc/man/man1/ceylon-plugin.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-plugin-%{version}.1
cp -pr doc/man/man1/ceylon-run-js.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-run-js-%{version}.1
cp -pr doc/man/man1/ceylon-run.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-run-%{version}.1
cp -pr doc/man/man1/ceylon-src.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-src-%{version}.1
cp -pr doc/man/man1/ceylon-test-js.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-test-js-%{version}.1
cp -pr doc/man/man1/ceylon-test.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-test-%{version}.1
cp -pr doc/man/man1/ceylon-version.1 $RPM_BUILD_ROOT%{_mandir}/man1/ceylon-version-%{version}.1

%post
%{_sbindir}/update-alternatives --install %{_bindir}/ceylon ceylon %{ceylon_home}/bin/ceylon 11000 \
    --slave %{_prefix}/lib/ceylon/ceylon ceylon-dir %{_prefix}/lib/ceylon/%{version} \
    --slave %{_mandir}/man1/ceylon.1.gz ceylon-man %{_mandir}/man1/ceylon-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-all.1 ceylon-all-man %{_mandir}/man1/ceylon-all-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-bash-completion.1 ceylon-bash-completion-man %{_mandir}/man1/ceylon-bash-completion-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-build.1 ceylon-build-man %{_mandir}/man1/ceylon-build-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-classpath.1 ceylon-classpath-man %{_mandir}/man1/ceylon-classpath-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-compile-js.1 ceylon-compile-js-man %{_mandir}/man1/ceylon-compile-js-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-compile.1 ceylon-compile-man %{_mandir}/man1/ceylon-compile-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-config.1 ceylon-config-man %{_mandir}/man1/ceylon-config-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-copy.1 ceylon-copy-man %{_mandir}/man1/ceylon-copy-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-doc-tool.1 ceylon-doc-tool-man %{_mandir}/man1/ceylon-doc-tool-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-doc.1 ceylon-doc-man %{_mandir}/man1/ceylon-doc-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-help.1 ceylon-help-man %{_mandir}/man1/ceylon-help-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-import-jar.1 ceylon-import-jar-man %{_mandir}/man1/ceylon-import-jar-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-info.1 ceylon-info-man %{_mandir}/man1/ceylon-info-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-plugin.1 ceylon-plugin-man %{_mandir}/man1/ceylon-plugin-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-run-js.1 ceylon-run-js-man %{_mandir}/man1/ceylon-run-js-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-run.1 ceylon-run-man %{_mandir}/man1/ceylon-run-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-src.1 ceylon-src-man %{_mandir}/man1/ceylon-src-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-test-js.1 ceylon-test-js-man %{_mandir}/man1/ceylon-test-js-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-test.1 ceylon-test-man %{_mandir}/man1/ceylon-test-%{version}.1.gz \
    --slave %{_mandir}/man1/ceylon-version.1 ceylon-version-man %{_mandir}/man1/ceylon-version-%{version}.1.gz

%postun
if [ $1 -eq 0 ] ; then
    %{_sbindir}/update-alternatives --remove ceylon %{ceylon_home}/bin/ceylon
fi

%files
%defattr(-,root,root)
%attr(755,root,root) %{ceylon_home}/bin/ceylon
%{ceylon_home}/bin/ceylon-sh-setup
%{ceylon_home}/repo/*
%{ceylon_home}/lib/*
%doc %{ceylon_home}/doc/*
%doc %{_mandir}/man1/*
%{ceylon_home}/samples/*
%{ceylon_home}/templates/*
%{ceylon_home}/contrib/*


%changelog
* Fri Oct 10 2014 Tako Schotanus <tschotan@redhat.com> 1.1.0-2
- Fixed installation of man pages and main folder link
* Thu Oct 09 2014 Tako Schotanus <tschotan@redhat.com> 1.1.0-1
- Not scanning files for (OSGi) Provides and Requires definitions anymore
- Not copying manual pages to the global directory anymore because that conflicts with the alternatives
* Wed Oct 08 2014 Stephane Epardaud <separdau@redhat.com> 1.1.0-0
- Update for 1.1.0
* Sun Nov 10 2013 Tako Schotanus <tschotan@redhat.com> 1.0.0-0
- Update for 1.0.0
- Added contrib folder
- Using alternatives system now
* Tue Sep 24 2013 Tako Schotanus <tschotan@redhat.com> 0.6.1-0
- Update for 0.6.1
* Fri Sep 20 2013 Stephane Epardaud <separdau@redhat.com> 0.6.0-0
- Update for 0.6
* Wed Mar 13 2013 Tako Schotanus <tschotan@redhat.com> 0.5.0-1
- Removed references to ceylon-cp.sh that doesn't exist anymore
* Wed Oct 31 2012 Tako Schotanus <tschotan@redhat.com> 0.5.0-0
- Update for 0.5
* Thu Oct 25 2012 Stephane Epardaud <separdau@redhat.com> 0.4.0-0
- Update for 0.4
* Fri Jul 06 2012 Tako Schotanus <tschotan@redhat.com> 0.3.1-0
- Update for 0.3.1 and some small changes to simplify updating the version
* Thu Jun 21 2012 Tako Schotanus <tschotan@redhat.com> 0.3.0-1
- Some changes to simplify the build process a bit
* Mon May 14 2012 Stephane Epardaud <separdau@redhat.com> 0.3.0-0
- Update for 0.3
* Thu Mar 15 2012 Stephane Epardaud <separdau@redhat.com> 0.2.0-0
- Update for 0.2
* Tue Dec 20 2011 Mladen Turk <mturk@redhat.com> 0.1.0-0
- Initial build

