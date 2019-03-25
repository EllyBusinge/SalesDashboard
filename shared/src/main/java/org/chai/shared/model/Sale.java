package org.chai.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model Representation of a sale
 * @author Elly
 *
 */
public class Sale extends BaseModel {
	
	private static final long serialVersionUID = 502945641295548161L;
	
	private Branch branch;
	
	private String salePeriod;
	
	private List<Product> products;
	
	public Sale() {
		this.setDateCreated(new Date());
		this.products = new ArrayList<Product>();
	}
	
	public Sale(Sale sale) {
		this.setId(sale.getId());
		this.setBranch(new Branch(sale.getBranch()));
		this.setSalePeriod(sale.getSalePeriod());
		if (sale.getDateCreated() != null)
			this.setDateCreated(new Date(sale.getDateCreated().getTime()));
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setSalePeriod(String salePeriod) {
		this.salePeriod = salePeriod;
	}

	public String getSalePeriod() {
		return salePeriod;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}
	
	public void addSaleProduct(Product product) {
		this.products.add(product);
	}
}
