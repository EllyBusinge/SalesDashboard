package org.chai.client.widgets;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.core.client.GWT;

public class AdjustablePagingToolBar extends PagingToolBar {

	public AdjustablePagingToolBar(int pageSize, int minLimit) {
		super(pageSize);

		SimpleComboBox<String> pageSizeCombo = new SimpleComboBox<String>();
		pageSizeCombo.setAutoWidth(true);
		pageSizeCombo.setTriggerAction(TriggerAction.ALL);
		pageSizeCombo.setEmptyText("");
		if (minLimit == 10) {
			pageSizeCombo.add("10");
			pageSizeCombo.add("20");
			pageSizeCombo.add("30");
			pageSizeCombo.add("40");
			pageSizeCombo.add("50");
		} else if (minLimit == 20) {
			pageSizeCombo.add("20");
			pageSizeCombo.add("30");
			pageSizeCombo.add("40");
			pageSizeCombo.add("50");
		} else {
			pageSizeCombo.add("30");
			pageSizeCombo.add("40");
			pageSizeCombo.add("50");
		}

		pageSizeCombo.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
				String[] pageSizeText = se.getSelectedItem().getValue().split("[^\\d]");
				if (pageSizeText.length > 0) {
					try {
						Integer newPageSize = Integer.parseInt(pageSizeText[0]);
						AdjustablePagingToolBar.this.setPageSize(newPageSize);
						AdjustablePagingToolBar.this.refresh();
					} catch (NumberFormatException e) {
						GWT.log("Page size "+pageSizeText[0]+" is not a number", e);
					}
				} else {
					GWT.log("Page size could not be found in the selected text " + se.getSelectedItem().getValue());
				}
			}
		});

		add(new SeparatorToolItem());
		add(pageSizeCombo);
	}

	@Override
	protected void doLoadRequest(int offset, int limit) {
		GWT.log("limit=" + limit + ", pageSize=" + pageSize);
		if (isReuseConfig() && config != null) {
			config.setOffset(offset);
			config.setLimit(limit);
			loader.load(config);
		} else {
			loader.setLimit(limit);
			loader.load(offset, limit);
		}
	}
}