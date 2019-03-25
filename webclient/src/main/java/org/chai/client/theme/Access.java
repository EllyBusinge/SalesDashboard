package org.chai.client.theme;

import com.extjs.gxt.themes.client.access.image.AccessImages;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.GWT;

public class Access extends Theme {

	private static final long serialVersionUID = 1L;
	public static Theme ACCESS = new Access();

	public Access() {
		super("access", "Access", "gxt/themes/access/css/xtheme-access.css");
	}

	public Access(String name) {
		super("access", name, "gxt/themes/access/css/xtheme-access.css");
	}

	@Override
	public void init() {
		super.init();
		GXT.IMAGES = GWT.create(AccessImages.class);
	}
}