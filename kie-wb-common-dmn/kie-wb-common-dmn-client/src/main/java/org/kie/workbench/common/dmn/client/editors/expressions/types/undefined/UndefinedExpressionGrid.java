/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.dmn.client.editors.expressions.types.undefined;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.enterprise.event.Event;

import com.ait.lienzo.shared.core.types.EventPropagationMode;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.dmn.api.definition.HasExpression;
import org.kie.workbench.common.dmn.api.definition.HasName;
import org.kie.workbench.common.dmn.api.definition.v1_1.DMNModelInstrumentedBase;
import org.kie.workbench.common.dmn.api.definition.v1_1.Expression;
import org.kie.workbench.common.dmn.client.commands.expressions.types.undefined.SetCellValueCommand;
import org.kie.workbench.common.dmn.client.editors.expressions.types.ExpressionEditorDefinition;
import org.kie.workbench.common.dmn.client.editors.expressions.types.ExpressionEditorDefinitions;
import org.kie.workbench.common.dmn.client.editors.expressions.types.ExpressionType;
import org.kie.workbench.common.dmn.client.editors.expressions.types.context.ExpressionCellValue;
import org.kie.workbench.common.dmn.client.editors.expressions.types.undefined.selector.UndefinedExpressionSelectorPopoverView;
import org.kie.workbench.common.dmn.client.session.DMNEditorSession;
import org.kie.workbench.common.dmn.client.widgets.grid.BaseDelegatingExpressionGrid;
import org.kie.workbench.common.dmn.client.widgets.grid.BaseExpressionGrid;
import org.kie.workbench.common.dmn.client.widgets.grid.BaseExpressionGridRenderer;
import org.kie.workbench.common.dmn.client.widgets.grid.ExpressionGridCache;
import org.kie.workbench.common.dmn.client.widgets.grid.controls.container.CellEditorControlsView;
import org.kie.workbench.common.dmn.client.widgets.grid.controls.list.HasListSelectorControl;
import org.kie.workbench.common.dmn.client.widgets.grid.controls.list.ListSelectorView;
import org.kie.workbench.common.dmn.client.widgets.grid.handlers.DelegatingGridWidgetCellSelectorMouseEventHandler;
import org.kie.workbench.common.dmn.client.widgets.grid.handlers.DelegatingGridWidgetEditCellMouseEventHandler;
import org.kie.workbench.common.dmn.client.widgets.grid.model.DMNGridColumn;
import org.kie.workbench.common.dmn.client.widgets.grid.model.DMNGridData;
import org.kie.workbench.common.dmn.client.widgets.grid.model.ExpressionEditorChanged;
import org.kie.workbench.common.dmn.client.widgets.grid.model.ExpressionEditorGridRow;
import org.kie.workbench.common.dmn.client.widgets.grid.model.GridCellTuple;
import org.kie.workbench.common.dmn.client.widgets.grid.model.GridCellValueTuple;
import org.kie.workbench.common.dmn.client.widgets.layer.DMNGridLayer;
import org.kie.workbench.common.dmn.client.widgets.panel.DMNGridPanel;
import org.kie.workbench.common.stunner.core.client.api.SessionManager;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.event.selection.DomainObjectSelectionEvent;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandFactory;
import org.kie.workbench.common.stunner.core.client.command.SessionCommandManager;
import org.kie.workbench.common.stunner.core.domainobject.DomainObject;
import org.kie.workbench.common.stunner.core.util.DefinitionUtils;
import org.kie.workbench.common.stunner.forms.client.event.RefreshFormPropertiesEvent;
import org.uberfire.ext.wires.core.grids.client.widget.grid.NodeMouseEventHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridSelectionManager;
import org.uberfire.ext.wires.core.grids.client.widget.layer.pinning.GridPinnedModeManager;

public class UndefinedExpressionGrid extends BaseDelegatingExpressionGrid<Expression, DMNGridData, UndefinedExpressionUIModelMapper> implements HasListSelectorControl {

    private final UndefinedExpressionSelectorPopoverView.Presenter undefinedExpressionSelector;
    private final Supplier<ExpressionEditorDefinitions> expressionEditorDefinitionsSupplier;
    private final ExpressionGridCache expressionGridCache;

    public UndefinedExpressionGrid(final GridCellTuple parent,
                                   final Optional<String> nodeUUID,
                                   final HasExpression hasExpression,
                                   final Optional<Expression> expression,
                                   final Optional<HasName> hasName,
                                   final DMNGridPanel gridPanel,
                                   final DMNGridLayer gridLayer,
                                   final DMNGridData gridData,
                                   final DefinitionUtils definitionUtils,
                                   final SessionManager sessionManager,
                                   final SessionCommandManager<AbstractCanvasHandler> sessionCommandManager,
                                   final CanvasCommandFactory<AbstractCanvasHandler> canvasCommandFactory,
                                   final Event<ExpressionEditorChanged> editorSelectedEvent,
                                   final Event<RefreshFormPropertiesEvent> refreshFormPropertiesEvent,
                                   final Event<DomainObjectSelectionEvent> domainObjectSelectionEvent,
                                   final CellEditorControlsView.Presenter cellEditorControls,
                                   final ListSelectorView.Presenter listSelector,
                                   final TranslationService translationService,
                                   final int nesting,
                                   final UndefinedExpressionSelectorPopoverView.Presenter undefinedExpressionSelector,
                                   final Supplier<ExpressionEditorDefinitions> expressionEditorDefinitionsSupplier,
                                   final ExpressionGridCache expressionGridCache) {
        super(parent,
              nodeUUID,
              hasExpression,
              expression,
              hasName,
              gridPanel,
              gridLayer,
              gridData,
              new BaseExpressionGridRenderer(gridData),
              definitionUtils,
              sessionManager,
              sessionCommandManager,
              canvasCommandFactory,
              editorSelectedEvent,
              refreshFormPropertiesEvent,
              domainObjectSelectionEvent,
              cellEditorControls,
              listSelector,
              translationService,
              nesting);
        this.undefinedExpressionSelector = undefinedExpressionSelector;
        this.expressionEditorDefinitionsSupplier = expressionEditorDefinitionsSupplier;
        this.expressionGridCache = expressionGridCache;

        //Render the cell content to Lienzo's SelectionLayer so we can handle Events on child elements
        getRenderer().setColumnRenderConstraint((isSelectionLayer, gridColumn) -> true);

        setEventPropagationMode(EventPropagationMode.NO_ANCESTORS);

        super.doInitialisation();
    }

    @Override
    protected List<NodeMouseEventHandler> getNodeMouseClickEventHandlers(final GridSelectionManager selectionManager) {
        final List<NodeMouseEventHandler> handlers = new ArrayList<>();
        handlers.add(new DelegatingGridWidgetCellSelectorMouseEventHandler(selectionManager,
                                                                           this::getParentInformation,
                                                                           () -> nesting));
        handlers.add(new DelegatingGridWidgetEditCellMouseEventHandler(this::getParentInformation,
                                                                       () -> nesting));
        return handlers;
    }

    @Override
    protected List<NodeMouseEventHandler> getNodeMouseDoubleClickEventHandlers(final GridSelectionManager selectionManager,
                                                                               final GridPinnedModeManager pinnedModeManager) {
        return Collections.emptyList();
    }

    @Override
    protected void doInitialisation() {
        // Defer initialisation until after the constructor completes as
        // UndefinedExpressionColumn needs expressionEditorDefinitionsSupplier to have been set
    }

    @Override
    public UndefinedExpressionUIModelMapper makeUiModelMapper() {
        return new UndefinedExpressionUIModelMapper(this::getModel,
                                                    () -> expression,
                                                    listSelector,
                                                    translationService,
                                                    hasExpression);
    }

    @Override
    protected void initialiseUiColumns() {
        final DMNGridColumn undefinedExpressionColumn = new UndefinedExpressionColumn(this,
                                                                                      cellEditorControls,
                                                                                      undefinedExpressionSelector,
                                                                                      translationService);
        undefinedExpressionColumn.setMovable(false);
        undefinedExpressionColumn.setResizable(false);

        model.appendColumn(undefinedExpressionColumn);
    }

    @Override
    protected void initialiseUiModel() {
        model.appendRow(new ExpressionEditorGridRow());
        uiModelMapper.fromDMNModel(0,
                                   0);
    }

    @Override
    public double getPadding() {
        return PADDING;
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    public void onExpressionTypeChanged(final ExpressionType type) {
        final Optional<Expression> expression = expressionEditorDefinitionsSupplier
                .get()
                .stream()
                .filter(e -> e.getType().equals(type))
                .map(ExpressionEditorDefinition::getModelClass)
                .findFirst()
                .get();

        final Optional<ExpressionEditorDefinition<Expression>> expressionEditorDefinition = expressionEditorDefinitionsSupplier.get().getExpressionEditorDefinition(expression);
        expressionEditorDefinition.ifPresent(ed -> {
            Optional<BaseExpressionGrid> editor = Optional.empty();
            if (nodeUUID.isPresent()) {
                final String uuid = nodeUUID.get();
                editor = expressionGridCache.getExpressionGrid(uuid);
            }
            if (!editor.isPresent()) {
                ed.enrich(nodeUUID, hasExpression, expression);
                editor = ed.getEditor(parent,
                                      nodeUUID,
                                      hasExpression,
                                      expression,
                                      hasName,
                                      nesting);
            }

            final BaseExpressionGrid _editor = editor.get();
            final GridCellValueTuple<ExpressionCellValue> gcv = new GridCellValueTuple<>(parent.getRowIndex(),
                                                                                         parent.getColumnIndex(),
                                                                                         parent.getGridWidget(),
                                                                                         new ExpressionCellValue(editor));

            sessionCommandManager.execute((AbstractCanvasHandler) sessionManager.getCurrentSession().getCanvasHandler(),
                                          new SetCellValueCommand(gcv,
                                                                  nodeUUID,
                                                                  () -> uiModelMapper,
                                                                  expressionGridCache,
                                                                  () -> {
                                                                      resize(BaseExpressionGrid.RESIZE_EXISTING);
                                                                      _editor.selectFirstCell();
                                                                      if (sessionManager.getCurrentSession() instanceof DMNEditorSession) {
                                                                          final DMNEditorSession dmnEditorSession = (DMNEditorSession) sessionManager.getCurrentSession();
                                                                          dmnEditorSession.getGridPanel().setFocus(true);
                                                                      }
                                                                  },
                                                                  () -> {
                                                                      resize(BaseExpressionGrid.RESIZE_EXISTING_MINIMUM);
                                                                      selectCell(0, 0, false, false);
                                                                  }));
        });
    }

    @SuppressWarnings("unused")
    public void doAfterSelectionChange(final int uiRowIndex,
                                       final int uiColumnIndex) {
        if (nodeUUID.isPresent()) {
            final DMNModelInstrumentedBase base = hasExpression.asDMNModelInstrumentedBase();
            if (base instanceof DomainObject) {
                fireDomainObjectSelectionEvent((DomainObject) base);
                return;
            }
        }
        super.doAfterSelectionChange(uiRowIndex, uiColumnIndex);
    }
}
