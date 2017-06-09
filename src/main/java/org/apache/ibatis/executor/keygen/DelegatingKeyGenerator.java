/**
 *    Copyright 2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.keygen;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author Christian Hempe
 */
public class DelegatingKeyGenerator implements KeyGenerator {
  public static final String CUSTOM_KEY_SUFFIX = "!customKey";
  private boolean executeBefore;
  private String[] keyProperties;
  private CustomKeyGenerator delegate;

  public DelegatingKeyGenerator(CustomKeyGenerator delegate, String keyProperty, boolean executeBefore) {
    this.delegate = delegate;
    this.keyProperties = delimitedStringToArray(keyProperty);
    this.executeBefore = executeBefore;
  }

  @Override
  public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    if (executeBefore) {
      delegate.processBefore(executor, ms, stmt, parameter, keyProperties);
    }

  }

  @Override
  public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    if (!executeBefore) {
      delegate.processAfter(executor, ms, stmt, parameter, keyProperties);
    }

  }

  private static String[] delimitedStringToArray(String in) {
    if (in == null || in.trim().length() == 0) {
      return null;
    } else {
      return in.split(",");
    }
  }
}
