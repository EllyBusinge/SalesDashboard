package org.chai.server.dao.hibernate;

import java.util.List;

import org.chai.server.dao.SaleDAO;
import org.chai.shared.model.Sale;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.search.Search;

@Repository("saleDAO")
public class HibernateSaleDAO extends BaseDAOImpl<Sale> implements SaleDAO {

	@Override
	public boolean deleteSale(Sale sale) {
		return remove(sale);
	}

	@Override
	public List<Sale> getAllSales() {
		return findAll();
	}

	@Override
	public Sale getSale(Integer saleId) {
		Search search = new Search();
		search.addFilterEqual("id", saleId);
		List<Sale> sales =  search(search);
		if (sales != null && sales.size() > 0) {
			return sales.get(0);
		}
		return null;
	}

	@Override
	public boolean saveSale(Sale sale) {
		return save(sale);
	}
}
