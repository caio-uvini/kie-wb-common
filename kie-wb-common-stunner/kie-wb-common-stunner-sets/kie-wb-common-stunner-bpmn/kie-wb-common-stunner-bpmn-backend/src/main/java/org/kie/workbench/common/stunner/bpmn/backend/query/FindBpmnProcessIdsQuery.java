/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.backend.query;

import javax.enterprise.context.ApplicationScoped;

import org.kie.workbench.common.services.refactoring.service.ResourceType;

@ApplicationScoped
public class FindBpmnProcessIdsQuery extends AbstractFindIdsQuery {

    public static final String NAME = FindBpmnProcessIdsQuery.class.getSimpleName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected ResourceType getProcessIdResourceType() {
        return ResourceType.BPMN2;
    }
}

