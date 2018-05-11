
package com.google.cloud.tools.eclipse.appengine.facets.ui.navigator;

import com.google.cloud.tools.appengine.AppEngineDescriptor;
import com.google.cloud.tools.appengine.api.AppEngineException;
import com.google.cloud.tools.eclipse.appengine.facets.AppEngineStandardFacet;
import com.google.cloud.tools.eclipse.appengine.facets.WebProjectUtil;
import com.google.cloud.tools.eclipse.appengine.ui.AppEngineImages;
import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.xml.sax.SAXException;

public class AppEngineLabelProvider extends LabelProvider implements IStyledLabelProvider {
  private LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

  @Override
  public String getText(Object element) {
    StyledString result = getStyledText(element);
    return result == null ? null : result.toString();
  }

  @Override
  public StyledString getStyledText(Object element) {
    if (element instanceof IProject) {
      IFacetedProject project = AppEngineContentProvider.getProject(element);
      if (project != null && AppEngineStandardFacet.hasFacet(project)) {
        IFile appEngineWebDescriptorFile =
            WebProjectUtil.findInWebInf(project.getProject(), new Path("appengine-web.xml"));
        if (appEngineWebDescriptorFile != null && appEngineWebDescriptorFile.exists()) {
          try (InputStream input = appEngineWebDescriptorFile.getContents()) {
            AppEngineDescriptor descriptor = AppEngineDescriptor.parse(input);
            StringBuilder qualifier = new StringBuilder();
            if (!Strings.isNullOrEmpty(descriptor.getProjectId())) {
              qualifier.append(descriptor.getProjectId());
            }
            if(!Strings.isNullOrEmpty(descriptor.getServiceId())) {
              if (qualifier.length() > 0) {
                qualifier.append(':');
              }
              qualifier.append(descriptor.getServiceId());
            }
            if (!Strings.isNullOrEmpty(descriptor.getProjectVersion())) {
              if (qualifier.length() > 0) {
                qualifier.append(':');
              }
              qualifier.append(descriptor.getProjectVersion());
            }
            StyledString str = new StyledString(((IProject) element).getName());
            if (qualifier.length() > 0) {
              str.append(" (", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
              str.append(qualifier.toString(), StyledString.QUALIFIER_STYLER);
              str.append(")", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
            }
            return str;
          } catch (IOException | CoreException | SAXException | AppEngineException ex) {
            return null; // continue on to the next label provider
          }
        }
      }
    } else if (element instanceof AppEngineDescriptor) {
      StyledString str = new StyledString("App Engine");
      str.append(" [standard", StyledString.QUALIFIER_STYLER);
      AppEngineDescriptor descriptor = (AppEngineDescriptor) element;
      try {
        str.append(": ", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
        str.append(
            Strings.isNullOrEmpty(descriptor.getRuntime()) ? "java7" : descriptor.getRuntime(),
            StyledString.QUALIFIER_STYLER);
      } catch (AppEngineException ex) {
      }
      str.append("]", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
      return str;
    } else if (element instanceof AppEngineElement) {
      return new StyledString(element.toString());
    }
    return null;
  }

  @Override
  public Image getImage(Object element) {
    if (element instanceof IProject) {
      IFacetedProject project = AppEngineContentProvider.getProject(element);
      if (project != null && AppEngineStandardFacet.hasFacet(project)) {
        return resources.createImage(AppEngineImages.appEngine(16));
      }
    } else if (element instanceof AppEngineDescriptor) {
      return resources.createImage(AppEngineImages.appEngine(16));
    } else if (element instanceof AppEngineElement
        && ((AppEngineElement) element).getImageDescriptor() != null) {
      return resources.createImage(((AppEngineElement) element).getImageDescriptor());
    }
    return null;
  }

  @Override
  public void dispose() {
    super.dispose();
    if (resources != null) {
      resources.dispose();
      resources = null;
    }
  }
}
