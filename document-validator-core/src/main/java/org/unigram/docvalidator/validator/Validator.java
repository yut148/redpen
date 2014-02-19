/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unigram.docvalidator.validator;


import java.util.List;

import org.unigram.docvalidator.store.FileContent;
import org.unigram.docvalidator.util.CharacterTable;
import org.unigram.docvalidator.util.ResultDistributor;
import org.unigram.docvalidator.util.ValidatorConfiguration;
import org.unigram.docvalidator.util.DocumentValidatorException;
import org.unigram.docvalidator.util.ValidationError;

/**
 * Validate input document.
 */
public interface Validator {
  /**
   * check the input document and returns the invalid points.
   * @param document input
   * @param distributor flush output
   * @return List of ValidationError
   */
  List<ValidationError> check(FileContent document,
      ResultDistributor distributor);

  /**
   * load configuration files.
   * @param conf validator configuration
   * @param charTable character configuration
   * @return true succeeded, otherwise false
   * @throws DocumentValidatorException
   */
  boolean loadConfiguration(ValidatorConfiguration conf,
      CharacterTable charTable) throws DocumentValidatorException;
}