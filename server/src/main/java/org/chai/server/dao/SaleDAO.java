package org.chai.server.dao;

import java.util.List;

import org.chai.shared.model.Sale;

public interface SaleDAO extends BaseDAO<Sale> {

	List<Sale> getAllSales();
	
	boolean saveSale(Sale sale);
	
	boolean deleteSale(Sale sale);
	
	Sale getSale(Integer saleId);
}
