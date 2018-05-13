/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.eclipse.appengine.facets.ui.navigator.model;

import com.google.cloud.tools.appengine.AppEngineDescriptor;
import com.google.common.base.Preconditions;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;

/**
 * A model representation of the {@code appengine-web.xml}.
 */
public class AppEngineWebDescriptor extends AppEngineResourceElement {
  private final AppEngineDescriptor descriptor;

  public AppEngineWebDescriptor(IFacetedProject project, IFile file,
      AppEngineDescriptor descriptor) {
    super(project, file);
    this.descriptor = Preconditions.checkNotNull(descriptor);
  }

  public AppEngineDescriptor getDescriptor() {
    return descriptor;
  }
}
