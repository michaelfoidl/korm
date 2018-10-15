/*
 * korm
 *
 * Copyright (c) 2018, Michael Foidl
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

package at.michaelfoidl.korm.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseProcessor extends AbstractProcessor {
    protected static final String KAPT_KORM_BUILD_DIRECTORY_OPTION_NAME = "kapt.korm.buildDir";
    protected static final String KAPT_KORM_SOURCE_DIRECTORY_OPTION_NAME = "kapt.korm.srcDir";
    protected static final String KAPT_KORM_ROOT_PACKAGE_OPTION_NAME = "kapt.korm.rootPackage";
    protected static final String KAPT_KORM_MIGRATION_PACKAGE_OPTION_NAME = "kapt.korm.migrationPackage";

    private Class<? extends Annotation> targetAnnotationClass;

    BaseProcessor(Class<? extends Annotation> targetAnnotationClass) {
        this.targetAnnotationClass = targetAnnotationClass;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(targetAnnotationClass.getName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> supportedOptions = provideSupportedOptions();
        supportedOptions.add(KAPT_KORM_BUILD_DIRECTORY_OPTION_NAME);
        supportedOptions.add(KAPT_KORM_SOURCE_DIRECTORY_OPTION_NAME);
        supportedOptions.add(KAPT_KORM_ROOT_PACKAGE_OPTION_NAME);
        supportedOptions.add(KAPT_KORM_MIGRATION_PACKAGE_OPTION_NAME);
        return supportedOptions;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            roundEnv
                    .getElementsAnnotatedWith(this.targetAnnotationClass)
                    .forEach(this::doProcess);
        }
        return true;
    }

    protected abstract void doProcess(Element element);

    protected abstract Set<String> provideSupportedOptions();

}
