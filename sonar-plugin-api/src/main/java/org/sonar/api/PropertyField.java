/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property field.
 *
 * @since 3.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PropertyField {
  /**
   * Unique key within a property. It shouldn't be prefixed.
   */
  String key();

  /**
   * The empty string "" is considered as null, so it's not possible to have empty strings for default values.
   */
  String defaultValue() default "";

  /**
   * This name will be displayed on the Settings page. This can be overridden/translated
   * by adding a a value for: <code>field.{key of parent property}.{key of this field}.name</code> in the language bundle.
   */
  String name();

  /**
   * If not empty, this description will be displayed on the Settings page. This can be overridden/translated
   * by adding a a value for: <code>field.{key of parent property}.{key of this field}.description</code> in the language bundle.
   */
  String description() default "";

  PropertyType type() default PropertyType.STRING;

  /**
   * Options for *_LIST types
   */
  String[] options() default {};
}
