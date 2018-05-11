
package com.google.cloud.tools.eclipse.appengine.facets.ui.navigator;

import com.google.cloud.tools.appengine.AppEngineDescriptor;
import com.google.cloud.tools.eclipse.appengine.facets.AppEngineStandardFacet;
import com.google.cloud.tools.eclipse.appengine.facets.WebProjectUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.xml.sax.SAXException;

public class AppEngineContentProvider implements ITreeContentProvider {
  private static final Object[] EMPTY_ARRAY = new Object[0];
  private static final Logger logger = Logger.getLogger(AppEngineContentProvider.class.getName());

  @Override
  public Object[] getElements(Object inputElement) {
    return getChildren(inputElement);
  }

  @Override
  public boolean hasChildren(Object element) {
    return element instanceof IProject;
  }

  @Override
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof IProject) {
      IFacetedProject project = getProject(parentElement);
      if (project != null && AppEngineStandardFacet.hasFacet(project)) {
        IFile appEngineWebDescriptorFile =
            WebProjectUtil.findInWebInf(project.getProject(), new Path("appengine-web.xml"));
        if (appEngineWebDescriptorFile != null && appEngineWebDescriptorFile.exists()) {
          try (InputStream input = appEngineWebDescriptorFile.getContents()) {
            AppEngineDescriptor descriptor = AppEngineDescriptor.parse(input);

            List<Object> contents = new ArrayList<>();
            contents.add(descriptor);
            // if ("default".equals(descriptor.getServiceId())) {
            // addDatastoreIndexes(project, contents);
            // addCron(project, contents);
            // addTaskQueues(project, contents);
            // addDenialOfService(project, contents);
            // addDispatch(project, contents);
            // }
            return contents.toArray();
          } catch (CoreException | SAXException | IOException ex) {
            IPath path = appEngineWebDescriptorFile.getFullPath();
            logger.log(Level.WARNING, "Unable to parse " + path, ex);
          }
        }
      }
    }
    return EMPTY_ARRAY;
  }

  @Override
  public Object getParent(Object element) {
    return null;
  }

  /** Try to get a project from the given element, return {@code null} otherwise. */
  static IFacetedProject getProject(Object inputElement) {
    try {
      if (inputElement instanceof IFacetedProject) {
        return (IFacetedProject) inputElement;
      } else if (inputElement instanceof IProject) {
        return ProjectFacetsManager.create((IProject) inputElement);
      } else if (inputElement instanceof IResource) {
        return ProjectFacetsManager.create(((IResource) inputElement).getProject());
      } else if (inputElement instanceof IAdaptable) {
        IFacetedProject facetedProject =
            ((IAdaptable) inputElement).getAdapter(IFacetedProject.class);
        if (facetedProject != null) {
          return facetedProject;
        }
        IProject project = ((IAdaptable) inputElement).getAdapter(IProject.class);
        if (project != null) {
          return ProjectFacetsManager.create(project);
        }
        IResource resource = ((IAdaptable) inputElement).getAdapter(IResource.class);
        if (resource != null) {
          return ProjectFacetsManager.create(((IResource) inputElement).getProject());
        }
      }
    } catch (CoreException ex) {
      logger.log(Level.INFO, "Unable to obtain project", ex);
    }
    return null;
  }
}
