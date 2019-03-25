package org.chai.client;

import java.util.Date;

import org.chai.client.controllers.SalesManagementController;
import org.chai.client.events.AppEvents;
import org.chai.client.service.SalesServiceAsync;
import org.chai.client.util.DateUtil;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.RootPanel;

public class webclient implements EntryPoint {
	
	public static final String VIEWPORT = "viewport";
	
	public static final String CENTER_PANEL = "center";
	
	private BorderLayoutData northData;

	private Viewport viewport;
	
	private LayoutContainer center;
	
	private static SalesManagementController salesManagementController;

	@Override
	public void onModuleLoad() {
		ThemeManager.register(org.chai.client.theme.Access.ACCESS);
		GXT.setDefaultTheme(org.chai.client.theme.Access.ACCESS, true);

		SalesServiceAsync salesService = SalesServiceAsync.Util.getInstance();
		initUI();
		salesManagementController = new SalesManagementController(salesService);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				Dispatcher.get().dispatch(AppEvents.SalesView);
			}
		});
		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(salesManagementController);
	}

	private void initUI() {
		viewport = new Viewport();
		viewport.setLayout(new BorderLayout());

		createNorth();
		createSouth();
		createCenter();

		Registry.register(VIEWPORT, viewport);
		Registry.register(CENTER_PANEL, center);

		RootPanel.get().add(viewport);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				northData.setSize((viewport.getHeight(true) - 604) + 300);
			}
		});
	}

	private void createNorth() {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='demo-header' class='x-small-editor'><div id=demo-title>Sales Management DashBoard</div></div>");

		HtmlContainer northWestItem = new HtmlContainer(sb.toString());
		northWestItem.setStateful(false);

		BorderLayoutData west_data = new BorderLayoutData(LayoutRegion.WEST, 300);

		BorderLayoutData west_data2 = new BorderLayoutData(LayoutRegion.WEST, 300);
		

		west_data2.setMargins(new Margins(5, 5, 5, 5));

		BorderLayoutData east_data = new BorderLayoutData(LayoutRegion.EAST, 70);
		east_data.setMargins(new Margins(3, 5, 5, 5));

		BorderLayoutData east_data2 = new BorderLayoutData(LayoutRegion.CENTER, 300);

		LayoutContainer northEastContainer = new LayoutContainer();
		northEastContainer.setBorders(false);
		northEastContainer.setLayout(new BorderLayout());
		northEastContainer.add(northWestItem, west_data);

		LayoutContainer northContainer = new LayoutContainer();
		northContainer.setBorders(true);
		northContainer.setLayout(new BorderLayout());
		northContainer.add(northWestItem, west_data);
		northContainer.add(northEastContainer, east_data2);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 33);
		data.setMargins(new Margins(5, 5, 5, 5));
		viewport.add(northContainer, data);
	}

	private void createSouth() {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='div-south' class='x-small-editor'><div id='div-theme'></div><div id=div-footer>&copy; Copy Right 2012-"+ DateUtil.getYearAsString(new Date())  + "</div></div>");
		HtmlContainer southPanel = new HtmlContainer(sb.toString());
		southPanel.setStateful(false);

		LayoutContainer container = new LayoutContainer();
		container.setBorders(true);
		container.add(southPanel);

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.SOUTH, 33);
		data.setMargins(new Margins(5, 5, 5, 5));
		viewport.add(container, data);
	}

	private void createCenter() {
		center = new LayoutContainer();
		center.setLayout(new FitLayout());
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(5, 5, 5, 5));
		viewport.add(center, data);
	}
}
