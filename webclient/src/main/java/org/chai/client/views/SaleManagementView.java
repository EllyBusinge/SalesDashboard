package org.chai.client.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.chai.client.webclient;
import org.chai.client.controllers.SalesManagementController;
import org.chai.client.events.AppEvents;
import org.chai.client.model.ProductSummary;
import org.chai.client.util.ProgressIndicator;
import org.chai.client.widgets.AdjustablePagingToolBar;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class SaleManagementView extends View {

	private LayoutContainer productsView;
	public static final int PAGE_SIZE = 50;
	Button refresh, searchProducts, uploadProducts;

	private EditorGrid<ProductSummary> grid;
	private GroupSummaryView view;
	private GroupingStore<ProductSummary> salesStore;
	private ColumnModel cm;
	AdjustablePagingToolBar toolBar;
	private ToolBar buttonToolBar;
	private PagingLoader<PagingLoadResult<ProductSummary>> loader;

	private ProductSummary currentProduct;
	private FileUploadField fileToUpload;
	private FieldLabel labelPf;
	private Window window;

	private String productDescription;
	private String productType;
	private String salePeriod;
	private String lowProfit;
	private String highProfit;
	private boolean isMinValue;

	public SaleManagementView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.SalesView) {
			LayoutContainer wrapper = (LayoutContainer) Registry.get(webclient.CENTER_PANEL);
			wrapper.removeAll();
			wrapper.add(productsView);
			wrapper.layout();
			return;
		} 
	}

	@Override
	protected void initialize() {
		productsView = new LayoutContainer();

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		SummaryColumnConfig<Double> id = new SummaryColumnConfig<Double>("id", "id", 20);
		configs.add(id);

		SummaryColumnConfig<Integer> courseUnits = new SummaryColumnConfig<Integer>("salePeriod", "Sale Period", 150);
		courseUnits.setSummaryType(SummaryType.COUNT);
		courseUnits.setSummaryRenderer(new SummaryRenderer() {
			public String render(Number value, Map<String, Number> data) {
				return value.intValue() > 1 ? "(" + value.intValue() + " Products)" : "(1 Product)";
			}
		});

		configs.add(courseUnits);

		SummaryColumnConfig<Double> itemCode = new SummaryColumnConfig<Double>("productCode", "Product Code", 100);
		configs.add(itemCode);

		SummaryColumnConfig<Double> itemDescription = new SummaryColumnConfig<Double>("productDescription", "Product Description", 120);
		configs.add(itemDescription);

		SummaryColumnConfig<Double> itemCategory = new SummaryColumnConfig<Double>("productTypeName", "Product Type", 100);
		configs.add(itemCategory);

		SummaryColumnConfig<Double> buyingPrice = new SummaryColumnConfig<Double>("buyingPrice", "Buying Price", 100);
		configs.add(buyingPrice);

		SummaryColumnConfig<Double> sellingPrice = new SummaryColumnConfig<Double>("sellingPrice", "Selling Price", 100);
		configs.add(sellingPrice);

		SummaryColumnConfig<Double> unitsSold = new SummaryColumnConfig<Double>("unitsSold", "Units Sold", 100);
		configs.add(unitsSold);

		SummaryColumnConfig<Double> profitMade = new SummaryColumnConfig<Double>("profitMade", "Profit", 100);
		profitMade.setSummaryType(SummaryType.SUM);
		profitMade.setSummaryRenderer(new SummaryRenderer() {
			@Override
			public String render(Number value, Map<String, Number> data) {
				return "Total Profit = " + value.intValue() + " /=";
			}
		});
		configs.add(profitMade);
		SummaryColumnConfig<Double> dateCreated = new SummaryColumnConfig<Double>("dateCreated", "Date Created", 150);
		configs.add(dateCreated);

		cm = new ColumnModel(configs);
		cm.setHidden(0, true); 

		toolBar = new AdjustablePagingToolBar(PAGE_SIZE, 50);

		initialiseSalesLoader(false);

		salesStore = new GroupingStore<ProductSummary>(loader);
		salesStore.groupBy("salePeriod");

		view = new GroupSummaryView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				String l = data.models.size() == 1 ? "Product" : "Products";
				return data.group + " (" + data.models.size() + " " + l + ")";
			}
		});

		grid = new EditorGrid<ProductSummary>(salesStore, cm);
		grid.setAutoExpandColumn("salePeriod");
		grid.setAutoExpandMax(10000);
		grid.setView(view);
		grid.setStripeRows(true);
		grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<ProductSummary>>() {
			public void handleEvent(SelectionChangedEvent<ProductSummary> be) {
				currentProduct = (ProductSummary) be.getSelection().get(0);
				MessageBox.alert("Alert", "Selected " + currentProduct.getId() + " -- " + currentProduct.getProductCode() , null);
			}
		});
		grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
			}
		});

		refresh = new Button("refresh");
		refresh.setWidth(90);
		refresh.setIcon(IconHelper.createStyle("refresh"));
		refresh.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				refresh();
			}
		});

		searchProducts = new Button("Filter Products");
		searchProducts.setWidth(130);
		searchProducts.setIcon(IconHelper.createStyle("search"));
		searchProducts.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				filterProducts();
			}
		});

		uploadProducts = new Button("Upload Products");
		uploadProducts.setWidth(140);
		uploadProducts.setIcon(IconHelper.createStyle("upload"));
		uploadProducts.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				uploadProducts();
			}
		});

		buttonToolBar = new ToolBar();
		buttonToolBar.setSpacing(50);
		buttonToolBar.add(searchProducts);
		buttonToolBar.add(uploadProducts);
		buttonToolBar.add(refresh);

		loadProducts();

		getView(grid, buttonToolBar);	}

	private void filterProducts() {
		initWindow("Filter Products/Items.", 450, 415);
		ProgressIndicator.showProgressBar();
		window.add(getFilterView());
		ProgressIndicator.hideProgressBar();
		window.show();
	}

	private ContentPanel getFilterView() {
		ContentPanel panel = new ContentPanel();
		panel.setBorders(false);
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setHeight(380);
		panel.setLayout(new FillLayout());

		TabPanel tabs = new TabPanel();
		tabs.setBorders(false);
		tabs.setBodyBorder(false);
		tabs.setTabPosition(TabPosition.TOP);
		
		final TabItem productTab = new TabItem();
		productTab.setText("Profit Made");
		productTab.setLayout(new FormLayout());
		productTab.addListener(Events.Select, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				salePeriod = productDescription = productType = lowProfit = highProfit = "";
			}
		});

		final TabItem productTypeTab = new TabItem();
		productTypeTab.setText("Product Performance");
		productTypeTab.setLayout(new FormLayout());
		productTypeTab.addListener(Events.Select, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				salePeriod = productDescription = productType = lowProfit = highProfit = "";
			}
		});

		productTab.add(getWidgetPanel(true));
		productTypeTab.add(getWidgetPanel(false));

		tabs.add(productTab);
		tabs.add(productTypeTab);

		Button buttonSave = new Button("Ok");
		buttonSave.setIcon(IconHelper.createStyle("ok"));
		buttonSave.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				closeWindow();
				ProgressIndicator.showProgressBar();
				Timer t = new Timer() {
					@Override
					public void run() {
						ProgressIndicator.hideProgressBar();
						loadProducts();
					}
				};
				t.schedule(1000);
				initialiseSalesLoader(true);
			}
		});

		Button buttonCancel = new Button("Cancel");
		buttonCancel.setIcon(IconHelper.createStyle("cancel"));
		buttonCancel.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				closeWindow();
			}
		});

		ToolBar buttonToolBar = new ToolBar();
		buttonToolBar.setSpacing(10);
		buttonToolBar.add(buttonSave);
		buttonToolBar.add(buttonCancel);

		panel.setBottomComponent(buttonToolBar);
		panel.add(tabs);
		return panel;
	}

	private ContentPanel getWidgetPanel(boolean isProfitsTab) {
		final ContentPanel panel = new ContentPanel();
		panel.setBorders(false);
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setHeight(395);

		FormData formData = new FormData();
		formData.setMargins(new Margins(5, 5, 5, 5));

		final SimpleComboBox<String> chooseProductType = new SimpleComboBox<String>();
		final SimpleComboBox<String> chooseProduct = new SimpleComboBox<String>();
		final SimpleComboBox<String> chooseSalePeriod = new SimpleComboBox<String>();

		chooseSalePeriod.disable();
		chooseProductType.disable();

		Radio radioBtnPdt = new Radio();
		radioBtnPdt.setBoxLabel("Product");
		radioBtnPdt.setValue(true);
		radioBtnPdt.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				Radio caller = (Radio)be.getSource();
				if (caller.getValue()) {
					chooseProduct.enable();
					chooseSalePeriod.disable();
					chooseProductType.disable();
				}
			}
		});

		Radio radioBtnTyp = new Radio();
		radioBtnTyp.setBoxLabel("Product Type");
		radioBtnTyp.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				Radio caller = (Radio)be.getSource();
				if (caller.getValue()) {
					chooseSalePeriod.disable();
					chooseProduct.disable();
					chooseProductType.enable();
				}
			}
		});

		Radio radioBtnTym = new Radio();
		radioBtnTym.setBoxLabel("Month");
		radioBtnTym.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				Radio caller = (Radio)be.getSource();
				if (caller.getValue()) {
					chooseSalePeriod.enable();
					chooseProduct.disable();
					chooseProductType.disable();
				}
			}
		});

		RadioGroup radioGroup2 = new RadioGroup();
		radioGroup2.add(radioBtnPdt);
		radioGroup2.add(radioBtnTyp);
		radioGroup2.add(radioBtnTym);

		FieldLabel label = new FieldLabel(radioGroup2, "Filter By");
		label.setLabelWidth(150);
		label.setHeight(50);
		panel.add(label, formData);		

		chooseProduct.setName("product");
		chooseProduct.setWidth(236);
		chooseProduct.setFieldLabel("Choose Product");
		chooseProduct.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
				productDescription = se.getSelectedItem().getValue();
			}
		});
		loadProducts(chooseProduct);

		label = new FieldLabel(chooseProduct, "Choose Product");
		label.setLabelWidth(150);
		label.setHeight(50);
		panel.add(label, formData);

		chooseProductType.setName("productType");
		chooseProductType.setWidth(236);
		chooseProductType.setFieldLabel("Choose Product Type");
		chooseProductType.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
				productType = se.getSelectedItem().getValue();
			}
		});
		loadProductTypes(chooseProductType);

		label = new FieldLabel(chooseProductType, "Choose Product Type");
		label.setLabelWidth(150);
		label.setHeight(50);
		panel.add(label, formData);
		
		chooseSalePeriod.setName("product");
		chooseSalePeriod.setWidth(236);
		chooseSalePeriod.setFieldLabel("Choose Month");
		chooseSalePeriod.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
				salePeriod = se.getSelectedItem().getValue();
			}
		});
		loadSalePeriods(chooseSalePeriod);

		label = new FieldLabel(chooseSalePeriod, "Choose Month");
		label.setLabelWidth(150);
		label.setHeight(50);
		panel.add(label, formData);

		if (isProfitsTab) {
			final NumberField lowValue = new NumberField();
			lowValue.addKeyListener(new KeyListener() {
				public void componentKeyUp(ComponentEvent event) {
					if (lowValue.getValue() != null) {
						lowProfit = lowValue.getValue().toString();
					}
				}
			});
			lowValue.setWidth(236);
			label = new FieldLabel(lowValue, "Profit Value(MIN)");
			label.setLabelWidth(150);
			label.setHeight(50);
			panel.add(label, formData);

			final NumberField highValue = new NumberField();
			highValue.addKeyListener(new KeyListener() {
				public void componentKeyUp(ComponentEvent event) {
					if (highValue.getValue() != null) {
						highProfit = highValue.getValue().toString();
					}
				}
			});
			highValue.setWidth(236);
			label = new FieldLabel(highValue, "Profit Value(MAX)");
			label.setLabelWidth(150);
			label.setHeight(50);
			panel.add(label, formData);
		} else {
			Radio radioBtnHigh = new Radio();
			radioBtnHigh.setBoxLabel("High Performance");
			radioBtnHigh.setValue(true);
			radioBtnHigh.addListener(Events.Change, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					Radio caller = (Radio)be.getSource();
					if (caller.getValue()) {
						labelPf.setText("Min Profit Value");
						isMinValue = true;
					}
				}
			});

			Radio radioBtnLow = new Radio();
			radioBtnLow.setBoxLabel("Low Performance");
			radioBtnLow.addListener(Events.Change, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					Radio caller = (Radio)be.getSource();
					if (caller.getValue()) {
						labelPf.setText("Max Profit Value");
						isMinValue = false;
					}
				}
			});

			RadioGroup radioGroup = new RadioGroup();
			radioGroup.add(radioBtnHigh);
			radioGroup.add(radioBtnLow);

			label = new FieldLabel(radioGroup, "Choose Performance");
			label.setLabelWidth(150);
			label.setHeight(50);
			panel.add(label, formData);	

			final NumberField profitValueField = new NumberField();
			profitValueField.addKeyListener(new KeyListener() {
				public void componentKeyUp(ComponentEvent event) {
					if (isMinValue) {
						if (profitValueField.getValue() != null) {
							lowProfit = profitValueField.getValue().toString();
						}
					} else {
						if (profitValueField.getValue() != null) {
							highProfit = profitValueField.getValue().toString();
						}
					}
				}
			});
			profitValueField.setWidth(236);
			labelPf = new FieldLabel(profitValueField, "Min Profit Value");
			labelPf.setLabelWidth(150);
			labelPf.setHeight(50);
			panel.add(labelPf, formData);
		}
		return panel;
	}

	private void initialiseSalesLoader(final boolean isSearchRequest) {
		loader = new BasePagingLoader<PagingLoadResult<ProductSummary>>(
				new RpcProxy<PagingLoadResult<ProductSummary>>() {
					@Override
					public void load(Object loadConfig, final AsyncCallback<PagingLoadResult<ProductSummary>> callback) {
						ProgressIndicator.showProgressBar();
						final PagingLoadConfig pagingLoadConfig = (PagingLoadConfig) loadConfig;

						if (pagingLoadConfig.getSortField() == null || pagingLoadConfig.getSortField().trim().equals("")) {
							pagingLoadConfig.setSortField("id");
						} else if (pagingLoadConfig.getSortField().trim().equals("salePeriod")) {
							pagingLoadConfig.setSortField("sale.salePeriod");
						} else if (pagingLoadConfig.getSortField().trim().equals("productTypeName")) {
							pagingLoadConfig.setSortField("productType.productTypeName");
						} else {
							pagingLoadConfig.setSortField("id");
						} 

						Scheduler.get().scheduleDeferred(
								new ScheduledCommand() {
									@Override
									public void execute() {
										final SalesManagementController controller = (SalesManagementController) SaleManagementView.this.getController();
										try {
											if (isSearchRequest) {
												controller.getSearchProductList(pagingLoadConfig, callback, salePeriod, productDescription, productType, 
														lowProfit, highProfit);
											} else {
												controller.getProductsList(pagingLoadConfig, callback);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
					}
				});
		loader.setRemoteSort(true);
		toolBar.bind(loader);
	}

	public void loadProducts() {
		GroupingStore<ProductSummary> store = new GroupingStore<ProductSummary>(loader);
		store.groupBy("salePeriod");
		grid.reconfigure(store, cm);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				PagingLoadConfig config = new BasePagingLoadConfig(0, PAGE_SIZE);
				loader.load(config);
			}
		});
	}

	private void loadProducts(final SimpleComboBox<String> combo) {
		Scheduler.get().scheduleDeferred(
				new ScheduledCommand() {
					@Override
					public void execute() {
						SalesManagementController controller = (SalesManagementController) SaleManagementView.this.getController();	
						controller.getAllProducts(combo);
					}
				});		
	}
	
	private void loadSalePeriods(final SimpleComboBox<String> combo) {
		Scheduler.get().scheduleDeferred(
				new ScheduledCommand() {
					@Override
					public void execute() {
						SalesManagementController controller = (SalesManagementController) SaleManagementView.this.getController();	
						controller.getAllSales(combo);
					}
				});		
	}

	private void loadProductTypes(final SimpleComboBox<String> combo) {
		Scheduler.get().scheduleDeferred(
				new ScheduledCommand() {
					@Override
					public void execute() {
						SalesManagementController controller = (SalesManagementController) SaleManagementView.this.getController();	
						controller.getAllProductTypes(combo);
					}
				});		
	}

	private void getView(Component component, Component topComponent) {
		BorderLayout layout = new BorderLayout();
		layout.setEnableState(false);
		productsView.setLayout(layout);

		ContentPanel cp = new ContentPanel();
		cp.setLayout(new FitLayout());
		cp.setHeaderVisible(false);
		cp.setBorders(false);
		cp.add(component);
		cp.setBottomComponent(toolBar);
		cp.setTopComponent(topComponent);

		productsView.add(cp, new BorderLayoutData(LayoutRegion.CENTER));
	}

	private ContentPanel getUploadProductsView() {
		FormData formData = new FormData("100%");
		formData.setMargins(new Margins(15, 5, 5, 5));

		final String url = GWT.getModuleBaseURL()+ "salesupload";

		final FormPanel panel = new FormPanel();
		panel.setAction(url);
		panel.setEncoding(Encoding.MULTIPART);
		panel.setMethod(Method.POST);
		panel.setHeaderVisible(false);
		panel.setFrame(false);
		panel.setBodyBorder(false);
		panel.setBorders(false);
		panel.addListener(Events.Submit, new Listener<FormEvent>() {
			@Override
			public void handleEvent(FormEvent be) {
				refresh();
				closeWindow();
				String response = be.getResultHtml();
				String[] ar = response.split("_");
				if (ar[0].toLowerCase().matches("(.*)success(.*)")) {
					MessageBox.info("Status", "Products have been uploaded successfully.", null);
				} else {
					MessageBox.info("Status", "File was not uploaded.", null);
				}
			}
		});

		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.LEFT);
		layout.setLabelWidth(130);
		panel.setLayout(layout);

		fileToUpload = new FileUploadField();
		fileToUpload.setAllowBlank(false);
		fileToUpload.setName("fileName");
		fileToUpload.setFieldLabel("File To Upload");

		panel.add(fileToUpload, formData);

		Button buttonSave = new Button("Ok");
		buttonSave.setIcon(IconHelper.createStyle("ok"));
		buttonSave.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!panel.isValid()) {
					MessageBox.info("Error", "Some details are missing or are incorrect", null);
				} else {
					panel.submit();
				}
			}
		});

		Button buttonCancel = new Button("Cancel");
		buttonCancel.setIcon(IconHelper.createStyle("cancel"));
		buttonCancel.addListener(Events.Select, new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				//closeWindow();
				MessageBox.info("", url, null);
			}
		});

		panel.addButton(buttonSave);
		panel.addButton(buttonCancel);	
		return panel;
	}

	private void refresh() {
		initialiseSalesLoader(false);
		loadProducts();
	}

	public void closeWindow() {
		ProgressIndicator.showProgressBar();
		window.hide();
		ProgressIndicator.hideProgressBar();
	}

	private void uploadProducts() {
		initWindow("Upload Product Details", 450, 150);
		ProgressIndicator.showProgressBar();
		window.add(getUploadProductsView());
		ProgressIndicator.hideProgressBar();
		window.show();
	}

	private void initWindow(String title, int width, int height) {
		window = new Window();
		window.setHeading(title);
		window.setModal(true);
		window.setMaximizable(false);
		window.setDraggable(true);
		window.setResizable(false);
		window.setPixelSize(width, height);
	}
}