package org.chai.shared.model;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * This serves as the base class for domain objects which can be edited.
 * @author Elly
 */
public class BaseModel extends LightEntity {

	private static final long serialVersionUID = 7939951597182605859L;
	
	/**unique identifier for the model object. Correlates to the database PK. */
	protected int id;
	
	/** The date when the object was last edited or changed. */
	protected Date dateChanged;
	
	/** The date when this data was first submitted. */
	protected Date dateCreated;
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Date getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
