/*
* JBoss, Home of Professional Open Source
* Copyright 2008, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package ceylon.modules.mc.runtime;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloader.spi.ClassNotFoundHandler;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloading.plugins.vfs.PackageVisitor;
import org.jboss.classloading.plugins.vfs.VFSResourceVisitor;
import org.jboss.classloading.spi.dependency.policy.ClassLoaderPolicyModule;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory;
import org.jboss.classloading.spi.vfs.policy.VFSClassLoaderPolicy;
import org.jboss.classloading.spi.visitor.ResourceFilter;
import org.jboss.classloading.spi.visitor.ResourceVisitor;
import org.jboss.vfs.VirtualFile;

/**
 * VFSClassLoaderPolicyModule.
 *
 * @author <a href="ales.justin@jboss.org">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VFSClassLoaderPolicyModule extends ClassLoaderPolicyModule
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   /** Our cached vfs roots */
   private VirtualFile[] vfsRoots;

   /**
    * Class not found handler.
    */
   private ClassNotFoundHandler classNotFoundHandler;

   /**
    * Create a new VFSClassLoaderPolicyModule.
    *
    * @param classLoadingMetaData the classloading metadata
    * @param contextName the context name
    * @param vfsRoots the vfs roots
    */
   public VFSClassLoaderPolicyModule(ClassLoadingMetaData classLoadingMetaData, String contextName, VirtualFile... vfsRoots)
   {
      super(classLoadingMetaData, contextName);
      if (vfsRoots == null || vfsRoots.length == 0)
         throw new IllegalArgumentException("Illegal VFS roots: " + Arrays.toString(vfsRoots));
      this.vfsRoots = vfsRoots;
   }

   @Override
   protected List<Capability> determineCapabilities()
   {
      // While we are here, check the roots
      VirtualFile[] roots = vfsRoots;

      List<Capability> capabilities = super.determineCapabilities();
      if (capabilities != null)
         return capabilities;

      // We need to work it out
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      capabilities = new CopyOnWriteArrayList<Capability>();

      // We have a module capability
      Object version = getVersion();
      Capability capability = factory.createModule(getName(), version);
      capabilities.add(capability);

      // Do we determine package capabilities
      ClassFilter included = getIncluded();
      ClassFilter excluded = getExcluded();
      ClassFilter excludedExport = getExcludedExport();
      ExportAll exportAll = getExportAll();
      if (exportAll != null)
      {
         Set<String> exportedPackages = PackageVisitor.determineAllPackages(roots, null, exportAll, included, excluded, excludedExport);
         for (String packageName : exportedPackages)
         {
            capability = factory.createPackage(packageName, version);
            capabilities.add(capability);
         }
      }

      return capabilities;
   }

   @Override
   protected VFSClassLoaderFactory getClassLoadingMetaData()
   {
      return (VFSClassLoaderFactory) super.getClassLoadingMetaData();
   }

   @Override
   public VFSClassLoaderPolicy getPolicy()
   {
      return (VFSClassLoaderPolicy) super.getPolicy();
   }

   @Override
   protected VFSClassLoaderPolicy determinePolicy()
   {
      VirtualFile[] roots = vfsRoots;
      VFSClassLoaderPolicy policy = VFSClassLoaderPolicy.createVFSClassLoaderPolicy(getContextName(), roots);

      String[] packageNames = getPackageNames();
      policy.setExportedPackages(packageNames);
      policy.setIncluded(getIncluded());
      policy.setExcluded(getExcluded());
      policy.setExcludedExport(getExcludedExport());
      policy.setExportAll(getExportAll());
      policy.setImportAll(isImportAll());
      policy.setShutdownPolicy(getShutdownPolicy());
      policy.setCacheable(isCacheable());
      policy.setBlackListable(isBlackListable());
      policy.setDelegates(getDelegates());

      if (classNotFoundHandler != null)
         policy.addClassNotFoundHandler(classNotFoundHandler);

      return policy;
   }

   @Override
   public void reset()
   {
      super.reset();
      vfsRoots = null;
   }

   @Override
   public void visit(ResourceVisitor visitor, ResourceFilter filter, ResourceFilter recurseFilter, URL... urls)
   {
      ClassLoader classLoader = getClassLoader();
      if (classLoader == null)
         throw new IllegalStateException("ClassLoader has not been constructed for " + getContextName());

      VirtualFile[] roots = vfsRoots;
      if (roots != null && roots.length > 0)
      {
         ClassFilter included = getIncluded();
         ClassFilter excluded = getExcluded();
         VFSResourceVisitor.visit(roots, null, included, excluded, classLoader, visitor, filter, recurseFilter, urls);
      }
   }

   /**
    * Set class not found handler.
    *
    * @param classNotFoundHandler the class not found handler
    */
   void setClassNotFoundHandler(ClassNotFoundHandler classNotFoundHandler)
   {
      this.classNotFoundHandler = classNotFoundHandler;
   }
}
