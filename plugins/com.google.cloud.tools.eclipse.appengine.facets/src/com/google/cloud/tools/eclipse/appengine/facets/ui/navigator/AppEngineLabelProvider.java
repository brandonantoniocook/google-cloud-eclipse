package com.google.cloud.tools.eclipse.appengine.facets.ui.navigator;

import com.google.cloud.tools.appengine.AppEngineDescriptor;
import com.google.cloud.tools.eclipse.appengine.ui.AppEngineImages;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class AppEngineLabelProvider extends LabelProvider {
  private LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

  @Override
  public String getText(Object element) {
    if (element instanceof AppEngineDescriptor) {
      return "App Engine";
    } else if (element instanceof AppEngineElement) {
      return element.toString();
    }
    return null;
  }

  @Override
  public Image getImage(Object element) {
    if (element instanceof AppEngineDescriptor) {
      return resources.createImage(AppEngineImages.appEngine(16));
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
