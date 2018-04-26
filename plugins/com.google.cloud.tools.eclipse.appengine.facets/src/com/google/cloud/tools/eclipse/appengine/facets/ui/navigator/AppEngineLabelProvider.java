
package com.google.cloud.tools.eclipse.appengine.facets.ui.navigator;

import com.google.cloud.tools.appengine.AppEngineDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class AppEngineLabelProvider extends LabelProvider {
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
    return null;
  }
}
