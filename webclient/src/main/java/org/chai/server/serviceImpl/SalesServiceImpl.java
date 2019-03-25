package org.chai.server.serviceImpl;

import java.util.List;

import javax.servlet.ServletException;

import org.chai.server.rpc.WebPersistentRemoteService;
import org.chai.shared.model.Product;
import org.chai.shared.model.ProductType;
import org.chai.shared.model.Sale;
import org.chai.shared.model.exceptions.DataAccessException;
import org.chai.shared.model.paging.PagingLoadConfig;
import org.chai.shared.model.paging.PagingLoadResult;
import org.springframework.web.context.WebApplicationContext;

public class SalesServiceImpl extends WebPersistentRemoteService implements org.chai.client.service.SalesService {
	
	private static final long serialVersionUID = 1L;
	
	private org.chai.server.service.SalesService salesService;

	@Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext ctx = getApplicationContext();
		salesService = (org.chai.server.service.SalesService)ctx.getBean("salesService");
	}

	@Override
	public boolean deleteProduct(Product product) throws DataAccessException {
		return salesService.deleteProduct(product);
	}

	@Override
	public boolean deleteSale(Sale sale) throws DataAccessException {
		return salesService.deleteSale(sale);
	}

	@Override
	public List<Product> getAllProducts() throws DataAccessException {
		return salesService.getAllProducts();
	}

	@Override
	public List<Sale> getAllSales() throws DataAccessException {
		return salesService.getAllSales();
	}

	@Override
	public Product getProduct(String productDescription) throws DataAccessException {
		return salesService.getProduct(productDescription);
	}

	@Override
	public Product getProduct(String productDescription, String productCode) throws DataAccessException {
		return salesService.getProduct(productDescription, productCode);
	}

	@Override
	public Product getProduct(Integer productId) throws DataAccessException {
		return salesService.getProduct(productId);
	}

	@Override
	public Product getProductUsingCode(String productCode) throws DataAccessException {
		return salesService.getProductUsingCode(productCode);
	}

	@Override
	public PagingLoadResult<Product> getProductsList(PagingLoadConfig pagingLoadConfig) throws DataAccessException {
		return salesService.getProductsList(pagingLoadConfig);
	}

	@Override
	public Sale getSale(Integer saleId) throws DataAccessException {
		return salesService.getSale(saleId);
	}

	@Override
	public Product saveProduct(Product product) throws DataAccessException {
		return salesService.saveProduct(product);
	}

	@Override
	public boolean saveSale(Sale sale) throws DataAccessException {
		return salesService.saveSale(sale);
	}

	@Override
	public boolean deleteProductType(ProductType productType) throws DataAccessException {
		return salesService.deleteProductType(productType);
	}

	@Override
	public List<ProductType> getAllProductTypes() throws DataAccessException {
		return salesService.getAllProductTypes();
	}

	@Override
	public ProductType getProductType(String productType) throws DataAccessException {
		return salesService.getProductType(productType);
	}

	@Override
	public ProductType saveProductType(ProductType productType) throws DataAccessException {
		return salesService.saveProductType(productType);
	}
}
