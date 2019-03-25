package org.chai.client.theme;

import com.extjs.gxt.ui.client.util.Theme;

public class Slate extends Theme {

	private static final long serialVersionUID = 1L;
	public static Theme SLATE = new Slate();

	public Slate() {
		super("slate", "Slate", "gxt/themes/slate/css/xtheme-slate.css");
	}

	public Slate(String name) {
		super("slate", name, "gxt/themes/slate/css/xtheme-slate.css");
	}
}