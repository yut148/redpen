/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
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
package cc.redpen.validator.sentence;

import cc.redpen.RedPenException;
import cc.redpen.model.Sentence;
import cc.redpen.util.ResourceExtractor;
import cc.redpen.validator.ValidationError;
import cc.redpen.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Validate input sentences contain invalid expression.
 */
final public class InvalidExpressionValidator extends Validator {
    private static final String DEFAULT_RESOURCE_PATH = "default-resources/invalid-expression";
    private static final Logger LOG =
            LoggerFactory.getLogger(InvalidExpressionValidator.class);
    private Set<String> invalidExpressions;
    private Set<String> customInvalidExpressions;

    @Override
    public void validate(List<ValidationError> errors, Sentence sentence) {
        Consumer<String> tConsumer = value -> {
            int startPosition = sentence.getContent().indexOf(value);
            if (startPosition != -1) {
                errors.add(createValidationErrorWithPosition(sentence,
                        sentence.getOffset(startPosition),
                        sentence.getOffset(startPosition + value.length()), value));
            }
        };

        invalidExpressions.stream().forEach(tConsumer);
        customInvalidExpressions.stream().forEach(tConsumer);
    }

    @Override
    protected void init() throws RedPenException {

        String lang = getSymbolTable().getLang();
        String defaultDictionaryFile = DEFAULT_RESOURCE_PATH
                + "/invalid-expression-" + lang + ".dat";
        invalidExpressions = ResourceExtractor.WORD_LIST.loadCachedFromResource(defaultDictionaryFile, "invalid expression");

        customInvalidExpressions = new HashSet<>();
        Optional<String> listStr = getConfigAttribute("list");
        listStr.ifPresent(f -> {
            LOG.info("User defined invalid expression list found.");
            customInvalidExpressions.addAll(Arrays.asList(f.split(",")));
            LOG.info("Succeeded to add elements of user defined list.");
        });

        Optional<String> confFile = getConfigAttribute("dict");
        confFile.ifPresent(f -> {
            LOG.info("user dictionary file is " + f);
            try {
                customInvalidExpressions.addAll(ResourceExtractor.WORD_LIST.load(new FileInputStream(f)));
            } catch (IOException e) {
                LOG.error("Failed to load user dictionary.");
                return;
            }
            LOG.info("Succeeded to load specified user dictionary.");
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidExpressionValidator that = (InvalidExpressionValidator) o;

        if (invalidExpressions != null ? !invalidExpressions.equals(that.invalidExpressions) : that.invalidExpressions != null)
            return false;
        return !(customInvalidExpressions != null ? !customInvalidExpressions.equals(that.customInvalidExpressions) : that.customInvalidExpressions != null);

    }

    @Override
    public int hashCode() {
        int result = invalidExpressions != null ? invalidExpressions.hashCode() : 0;
        result = 31 * result + (customInvalidExpressions != null ? customInvalidExpressions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InvalidExpressionValidator{" +
                "invalidExpressions=" + invalidExpressions +
                ", customInvalidExpressions=" + customInvalidExpressions +
                '}';
    }
}
