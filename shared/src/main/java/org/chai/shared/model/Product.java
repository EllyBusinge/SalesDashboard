package org.chai.shared.model;

import java.util.Date;

/**
 * Model Representation of a product.
 * @author Elly
 *
 */
public class Product extends BaseModel {
	
	private static final long serialVersionUID = 5206825167682751618L;
	
	/** Uniquely assigned code to a product **/
	private String productCode;
	
	private String productDescription;
	
	private ProductType productType;
	
	private double buyingPrice;
	
	private double sellingPrice;
	
	private double profitMade;
	
	private int unitsSold;
	
	private Sale sale;
	
	public Product() {
		this.setDateCreated(new Date());
	}
	
	public Product(Product product) {
		this.setId(product.getId());
		this.setProductCode(product.getProductCode());
		this.setProductType(new ProductType(product.getProductType()));
		this.setProductDescription(product.getProductDescription());
		this.setBuyingPrice(product.getBuyingPrice());
		this.setSellingPrice(product.getSellingPrice());
		this.setProfitMade(product.getProfitMade());
		this.setUnitsSold(product.getUnitsSold());
		this.setSale(new Sale(product.getSale()));
		if (product.getDateCreated() != null)
			this.setDateCreated(new Date(product.getDateCreated().getTime()));
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public double getBuyingPrice() {
		return buyingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setProfitMade(double profitMade) {
		this.profitMade = profitMade;
	}

	public double getProfitMade() {
		return profitMade;
	}

	public void setUnitsSold(int unitsSold) {
		this.unitsSold = unitsSold;
	}

	public int getUnitsSold() {
		return unitsSold;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Sale getSale() {
		return sale;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public ProductType getProductType() {
		return productType;
	}
}