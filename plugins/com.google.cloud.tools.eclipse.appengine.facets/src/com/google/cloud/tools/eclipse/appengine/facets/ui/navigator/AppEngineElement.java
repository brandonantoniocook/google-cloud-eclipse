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

package com.google.cloud.tools.eclipse.appengine.facets.ui.navigator;

import java.util.Objects;
import org.eclipse.jface.resource.ImageDescriptor;

/** An element of a {@code appengine-web.xml}. */
public class AppEngineElement {
  private final String label;
  private final String value;
  private final ImageDescriptor image;

  public AppEngineElement(String label, String value, ImageDescriptor image) {
    this.label = label;
    this.value = value;
    this.image = image;
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AppEngineElement other = (AppEngineElement) obj;
    return Objects.equals(this.label, other.label) && Objects.equals(this.value, other.value);
  }

  public ImageDescriptor getImageDescriptor() {
    return image;
  }

  @Override
  public String toString() {
    return label + ": " + value;
  }
}
