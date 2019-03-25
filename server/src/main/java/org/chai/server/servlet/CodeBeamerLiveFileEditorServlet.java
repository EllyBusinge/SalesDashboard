package org.chai.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeBeamerLiveFileEditorServlet extends HttpServlet {

	private static final long serialVersionUID = 1138351574987917351L;	

	@Override
	public void init() {}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Content-Disposition", "filename=\"ProcNetLiveFileEditor.jnlp\";");
		response.setContentType("application/x-java-jnlp-file");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String fileId = request.getParameter("fileId");
		String cb_ipaddress = request.getParameter("cbipaddress");
		String cb_port = request.getParameter("cbport");
		String tc_ipaddress = request.getParameter("tcipaddress");
		String tc_port = request.getParameter("tcport");
		
		PrintWriter out = response.getWriter();
		
		String jnlpMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + 
			"<jnlp spec=\"1.0+\" codebase=\"http://" + tc_ipaddress + ":" + tc_port + "/\" >" +
				"<information>" +
					"<title>Procnet Live File Editor</title>" +
					"<vendor>Planet Systems (U) Ltd</vendor>" + 
					"<homepage href=\"http://" + tc_ipaddress + ":" + tc_port + "/\" />" +
					"<description>This application perform live editing of files stored via WEBDAV in Code Beamer Application.</description>" +
				"</information>" + 
				"<security>" +
					"<all-permissions/>" +
				"</security>" +
				"<resources>" +
					"<j2se version=\"1.6+\" />" +
					"<jar href=\"ProcNetLiveFileEditor.jar\" />" +
					"<jar href=\"ProcNetLiveFileEditor_lib/commons-logging-1.1.3.jar\" />" + 
					"<jar href=\"ProcNetLiveFileEditor_lib/httpclient-cache-4.1.2.jar\" />" + 
					"<jar href=\"ProcNetLiveFileEditor_lib/httpmime-4.1.2.jar\" />" +
					"<jar href=\"ProcNetLiveFileEditor_lib/httpclient-4.3.5.jar\" />" +
					"<jar href=\"ProcNetLiveFileEditor_lib/commons-io-2.4.jar\" />" +
					"<jar href=\"ProcNetLiveFileEditor_lib/httpcore-4.3.2.jar\" />" +
					"<jar href=\"ProcNetLiveFileEditor_lib/commons-codec-1.6.jar\" />" +
				"</resources>" +
				"<application-desc main-class=\"org.planetSystem.liveFileEditor.LiveFileEditor\">" + 
					"<argument>" + username + "</argument>" +  
					"<argument>" + password + "</argument>" +  
					"<argument>" + fileId + "</argument>" + 
					"<argument>" + cb_ipaddress + "</argument>" +  
					"<argument>" + cb_port + "</argument>" +  
				"</application-desc>" +  
			"</jnlp>";
		out.println(jnlpMessage);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}