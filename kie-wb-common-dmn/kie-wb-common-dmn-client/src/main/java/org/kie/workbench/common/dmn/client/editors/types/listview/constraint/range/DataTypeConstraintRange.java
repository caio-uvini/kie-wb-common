/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.dmn.client.editors.types.listview.constraint.range;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import elemental2.dom.Element;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.kie.workbench.common.dmn.client.editors.types.listview.constraint.common.DataTypeConstraintComponent;
import org.uberfire.client.mvp.UberElemental;

@Dependent
public class DataTypeConstraintRange implements DataTypeConstraintComponent {

    private final View view;

    private String constraintValue;

    @Inject
    public DataTypeConstraintRange(final View view) {
        this.view = view;
    }

    @PostConstruct
    void setup() {
        view.init(this);
    }

    @Override
    public String getValue() {
        return constraintValue;
    }

    @Override
    public void setValue(final String value) {
        constraintValue = value;
    }

    @Override
    public Element getElement() {
        return view.getElement();
    }

    public interface View extends UberElemental<DataTypeConstraintRange>,
                                  IsElement {

    }
}