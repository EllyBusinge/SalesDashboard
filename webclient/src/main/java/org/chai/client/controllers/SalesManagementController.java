package org.chai.client.controllers;

import java.util.ArrayList;
import java.util.List;

import org.chai.client.events.AppEvents;
import org.chai.client.model.ProductSummary;
import org.chai.client.service.SalesServiceAsync;
import org.chai.client.util.PagingUtil;
import org.chai.client.util.ProgressIndicator;
import org.chai.client.views.SaleManagementView;
import org.chai.shared.model.Product;
import org.chai.shared.model.ProductType;
import org.chai.shared.model.Sale;
import org.chai.shared.model.paging.FilterComparison;
import org.chai.shared.model.paging.FilterConfig;
import org.chai.shared.model.paging.PagingLoadResult;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SalesManagementController extends Controller {

	private SaleManagementView saleManagementView;

	private SalesServiceAsync salesService;

	public SalesManagementController(SalesServiceAsync salesService){
		super();
		this.salesService = salesService;
		registerEventTypes(AppEvents.SalesView);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = (EventType) event.getType();
		if (type == AppEvents.SalesView) {
			forwardToView(saleManagementView, event);
		} 
	}

	@Override
	public void initialize() {
		super.initialize();
		saleManagementView = new SaleManagementView(this);
	}

	public void deleteProduct(final ProductSummary Product) {
		salesService.deleteProduct(Product.getProduct(), new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				ProgressIndicator.hideProgressBar();
				if (result) {
					MessageBox.info("Status-Success", "Product " + Product.getProductCode() + "[" + Product.getId() + "]" + " Deleted Successfully", null);
				} else {
					MessageBox.alert("Status-Failed", "Deletion Of Product " + Product.getProductCode() + "[" + Product.getId() + "]" + " Failed", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Status-Failed", "Deletion Of Product " + Product.getProductCode() + "[" + Product.getId() + "]" + " Failed", null);
			}
		});
	}

	public void getProductsList(final PagingLoadConfig pagingLoadConfig, 
			final AsyncCallback<com.extjs.gxt.ui.client.data.PagingLoadResult<ProductSummary>> callback) {
		salesService.getProductsList(PagingUtil.createPagingLoadConfig(pagingLoadConfig), new AsyncCallback<PagingLoadResult<Product>>() {
			@Override
			public void onSuccess(PagingLoadResult<Product> result) {
				ProgressIndicator.hideProgressBar();
				List<ProductSummary> Products = new ArrayList<ProductSummary>();
				List<Product> data = result.getData();

				for (Product Product : data) {
					Products.add(new ProductSummary(Product));
				}
				callback.onSuccess(new BasePagingLoadResult<ProductSummary>(Products, result.getOffset(), result.getTotalLength()));
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Status-Failed", "Request to server failed with error " + caught.getLocalizedMessage(), null);
			}
		});
	}
	
	public void getSearchProductList(final PagingLoadConfig pagingLoadConfig, 
			final AsyncCallback<com.extjs.gxt.ui.client.data.PagingLoadResult<ProductSummary>> callback, String salePeriod, String productDescription, 
			String productType, String lowProfit, String highProfit) {
		org.chai.shared.model.paging.PagingLoadConfig searchPagingLoadConfig = PagingUtil.createPagingLoadConfig(pagingLoadConfig); 

		if (productDescription != null && productDescription != "") { 
			searchPagingLoadConfig.addFilter(new FilterConfig ("productDescription", productDescription, FilterComparison.EQUAL_TO)); 
		}

		if (productType != null && productType != "") { 
			searchPagingLoadConfig.addFilter(new FilterConfig ("productType.productTypeName", productType, FilterComparison.EQUAL_TO)); 
		}
		
		if (salePeriod != null && salePeriod != "") { 
			searchPagingLoadConfig.addFilter(new FilterConfig ("sale.salePeriod", salePeriod, FilterComparison.EQUAL_TO)); 
		}  
		
		if (lowProfit != null && lowProfit != "") { 
			searchPagingLoadConfig.addFilter(new FilterConfig ("profitMade", new Double(lowProfit), FilterComparison.GREATER_THAN_OR_EQUAL_TO)); 
		}
		
		if (highProfit != null && highProfit != "") { 
			searchPagingLoadConfig.addFilter(new FilterConfig ("profitMade", new Double(highProfit), FilterComparison.LESS_THAN_OR_EQUAL_TO)); 
		}
		
		salesService.getProductsList(searchPagingLoadConfig, new AsyncCallback<PagingLoadResult<Product>>() {
			@Override
			public void onSuccess(PagingLoadResult<Product> result) {
				ProgressIndicator.hideProgressBar();
				List<ProductSummary> Products = new ArrayList<ProductSummary>();
				List<Product> data = result.getData();

				for (Product Product : data) {
					Products.add(new ProductSummary(Product));
				}
				callback.onSuccess(new BasePagingLoadResult<ProductSummary>(Products, result.getOffset(), result.getTotalLength()));
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Status-Failed", "Request to server failed with error " + caught.getLocalizedMessage(), null);
			}
		});
	}
	
	public void getAllProducts(final SimpleComboBox<String> combo) {
		salesService.getAllProducts(new AsyncCallback<List<Product>>() {
			@Override
			public void onSuccess(List<Product> products) {
				for (Product product : products) {
					combo.add(product.getProductDescription());
					combo.setData(product.getProductDescription(), product.getProductDescription());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Status-Failed", "Request to server failed with error " + caught.getLocalizedMessage(), null);
			}
		});
	}
	
	public void getAllSales(final SimpleComboBox<String> combo) {
		salesService.getAllSales(new AsyncCallback<List<Sale>>() {
			@Override
			public void onSuccess(List<Sale> sales) {
				for (Sale sale : sales) {
					combo.add(sale.getSalePeriod());
					combo.setData(sale.getSalePeriod(), sale.getSalePeriod());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Status-Failed", "Request to server failed with error " + caught.getLocalizedMessage(), null);
			}
		});
	}
	
	public void getAllProductTypes(final SimpleComboBox<String> combo) {
		salesService.getAllProductTypes(new AsyncCallback<List<ProductType>>() {
			@Override
			public void onSuccess(List<ProductType> productTypes) {
				for (ProductType productType : productTypes) {
					combo.add(productType.getProductTypeName());
					combo.setData(productType.getProductTypeName(), productType.getProductTypeName());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Status-Failed", "Request to server failed with error " + caught.getLocalizedMessage(), null);
			}
		});
	}
}
