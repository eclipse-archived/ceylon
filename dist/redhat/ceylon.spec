# *** BEGIN LICENSE BLOCK ***
#
# *** END LICENSE BLOCK ***

%define section free

%define major_version 0
%define minor_version 2
%define micro_version 0
%define ceylon_home /usr/lib/ceylon/%{major_version}.%{minor_version}

Name: ceylon
Epoch: 0
Version: %{major_version}.%{minor_version}.%{micro_version}
Release: 0%{?dist}
Summary: Ceylon language

Group: Development/Languages
License: ASL 2.0 and GPL v 2 + Classpath exception
URL: http://www.ceylon-lang.org//
Source0: https://github.com/ceylon/ceylon-%{major_version}.%{minor_version}.tar.gz
BuildRoot: %{_tmppath}/%{name}-%{version}-%{release}-root
BuildArch:     noarch
BuildRequires: zip
BuildRequires: ant

%description
Ceylon is a programming language for writing large programs in a team
environment. The language is elegant, highly readable, extremely typesafe,
and makes it easy to get things done. And it's easy to learn for programmers
who are familiar with mainstream languages used in business computing.
Ceylon has a full-featured Eclipse-based development environment, allowing
developers to take best advantage of the powerful static type system.
Programs written in Ceylon execute on any JVM.

%prep
%setup -q


%build
export LANG=en_US.UTF-8
pushd ceylon-spec
ant publish
popd
pushd ceylon.language
ant publish
popd
pushd ceylon-dist
ant
popd


%install
rm -rf $RPM_BUILD_ROOT%{ceylon_home}
# CEYLON_HOME and subdirs
mkdir -p $RPM_BUILD_ROOT/usr/bin
mkdir -p $RPM_BUILD_ROOT%{ceylon_home}/{bin,lib,repo,runtime-repo,doc}

pushd ceylon-dist
rm -f dist/bin/*.bat
install -m 755 dist/bin/ceylonc $RPM_BUILD_ROOT%{ceylon_home}/bin
install -m 755 dist/bin/ceylond $RPM_BUILD_ROOT%{ceylon_home}/bin
install -m 755 dist/bin/ceylon  $RPM_BUILD_ROOT%{ceylon_home}/bin
install -m 755 dist/bin/args.sh $RPM_BUILD_ROOT%{ceylon_home}/bin
cp -pr dist/repo/* $RPM_BUILD_ROOT%{ceylon_home}/repo
cp -pr dist/runtime-repo/* $RPM_BUILD_ROOT%{ceylon_home}/runtime-repo
cp -pr dist/lib/* $RPM_BUILD_ROOT%{ceylon_home}/lib
cp -pr dist/doc/* $RPM_BUILD_ROOT%{ceylon_home}/doc
popd
pushd $RPM_BUILD_ROOT/usr/bin
ln -s ../..%{ceylon_home}/bin/ceylon ceylon
ln -s ../..%{ceylon_home}/bin/ceylonc ceylonc
ln -s ../..%{ceylon_home}/bin/ceylond ceylond
popd

%files
%defattr(-,root,root)
%attr(755,root,root) %{ceylon_home}/bin/ceylon*
%attr(644,root,root) %{ceylon_home}/bin/args.sh
/usr/bin/ceylon*
%{ceylon_home}/bin/*
%{ceylon_home}/repo/*
%{ceylon_home}/runtime-repo/*
%{ceylon_home}/lib/*
%{ceylon_home}/doc/*


%changelog
* Tue Mar 15 2012 Stephane Epardaud <separdau@redhat.com> 0.2.0-0
- Update for 0.2
* Tue Dec 20 2011 Mladen Turk <mturk@redhat.com> 0.1.0-0
- Initial build


