package org.chai.server.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.chai.shared.model.Branch;
import org.chai.shared.model.Product;
import org.chai.shared.model.ProductType;
import org.chai.shared.model.Sale;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SalesUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1138351574987917351L;	
	private org.chai.server.service.SalesService salesService;
	private org.chai.server.service.BranchService branchService;
	private static final String DEFAULT_TEMP_DIR = ".";
	private static final String UPLOAD_DIRECTORY = getUploadDirectory();
	private File uploadedFile;
	
	@Override
	public void init() {
		ServletContext sctx = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);
		salesService = (org.chai.server.service.SalesService) ctx.getBean("salesService");
		branchService = (org.chai.server.service.BranchService) ctx.getBean("branchService");
	}

	public static boolean isUnix() {
		String seperator = System.getProperty("path.separator", "");
		return seperator.equals(":");
	}

	public static boolean isWindows() {
		String seperator = System.getProperty("path.separator", "");
		return seperator.equals(";");
	}
	
	public static String getUploadDirectory() {
		String path = "";
		if (isUnix()) {
			path = fileSeparator() + "tmp" + fileSeparator() + "uploaded";
		} else if(isWindows()){
			path = "d:\\uploaded\\";
		}
		return path;
	}
	
	private static String fileSeparator() {
		return File.separator;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			File tempDir = getTempDir();
			if (!tempDir.exists()) {
				tempDir.mkdirs();
			}

			// Parse the request
			try {
				List<FileItem> fileItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem fileItem : fileItems) {
					// process only file upload
					if (!fileItem.isFormField()) {
						String fileName = fileItem.getName();
						// get only the file name not whole path
						if (fileName != null) {
							fileName = FilenameUtils.getName(fileName);
							fileName = System.currentTimeMillis() + fileName;
						}

						uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
						if (uploadedFile.createNewFile()) {
							fileItem.write(uploadedFile);
						} else {
							response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The file already exists in repository.");
						}
					}
				}
				if (uploadedFile != null) {
					if (uploadedFile.exists())
						processFile(uploadedFile);

					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().print("Success");
					response.flushBuffer();
				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while creating the file : " + e.getMessage());
			}
		} else {
			response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Request contents type is not supported.");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	private File getTempDir() {
		return new File(DEFAULT_TEMP_DIR);
	}
	
	private void processFile(File uploadedFile) {
		try {
			FileInputStream file = new FileInputStream(uploadedFile);
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			// Rows 1, 2, 3 --> Branch information.
			// Row 4 --> Sale Date
			Row branchCodeRow = sheet.getRow(1);
			String branchCode = branchCodeRow.getCell(2).getStringCellValue();
			Row branchNameRow = sheet.getRow(2);
			String branchName = branchNameRow.getCell(2).getStringCellValue();
			Row locationRow = sheet.getRow(3);
			String location = locationRow.getCell(2).getStringCellValue();
			Row saleDateRow = sheet.getRow(4);
			Cell cell1 = saleDateRow.getCell(0);
			Object salePeriod;

			if (HSSFDateUtil.isCellDateFormatted(cell1)) {
				salePeriod = cell1.getDateCellValue();
			} else {
				cell1.setCellType(Cell.CELL_TYPE_STRING);
				salePeriod = cell1.getStringCellValue();
			}
					
			if (branchCode != null && branchName != null && location != null && salePeriod != null) {
				Branch branch = new Branch();
				branch.setBranchCode(branchCode);
				branch.setBranchName(branchName);
				branch.setLocation(location);
				
				//save branch.. and get it's reference value..
				branch = branchService.saveBranch(branch);
				
				Sale sale = new Sale();
				sale.setBranch(branch);
				if (salePeriod instanceof Date) {
					Date period = (Date)salePeriod;
					SimpleDateFormat format = new SimpleDateFormat("MMM/yyyy");
					sale.setSalePeriod(format.format(period));
				}
				
				ProductType currentProductType = null;
				
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					int rowNo = row.getRowNum();
					// Rows 0 can be ignored..
					if (rowNo > 5 ) {
							List<Object> cellValues = new ArrayList<Object>();
							// pick Product categories..logic all Product categories have one cell with a value.. rest of the cells are empty
							Iterator<Cell> cellIterator = row.cellIterator();
							while (cellIterator.hasNext()) {
								Cell cell = cellIterator.next();
								Object cellValue = null;
								if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
									switch (cell.getCellType()) {
									case Cell.CELL_TYPE_NUMERIC:
										if (HSSFDateUtil.isCellDateFormatted(cell)) {
											cellValue = cell.getDateCellValue();
										} else {
											cell.setCellType(Cell.CELL_TYPE_STRING);
											cellValue = cell.getStringCellValue();
										}
										break;
									case Cell.CELL_TYPE_STRING:
										cellValue = cell.getStringCellValue();
										break;
									case Cell.CELL_TYPE_FORMULA:
										cellValue = getCellValue(cell);
										break;
									}
									cellValues.add(cellValue);
								} 
							}
							
							if (cellValues != null) {
								if (cellValues.size() == 1) {
									String productTyp = (String)cellValues.get(0);
									if (productTyp != null) {
										currentProductType = new ProductType();
										currentProductType.setProductTypeName(productTyp);
										currentProductType = salesService.saveProductType(currentProductType);
									}									
								} else if (cellValues.size() == 5) {
									Product product = new Product();
									product.setProductCode((String)cellValues.get(0));
									product.setProductType(currentProductType);
									product.setProductDescription((String)cellValues.get(1));
									
									int unitsSold = new Integer((String)cellValues.get(2)).intValue();
									double buyingPrice = new Double((String)cellValues.get(3)).doubleValue();
									double sellingPrice = (Double)cellValues.get(4);
									product.setUnitsSold(unitsSold);
									product.setBuyingPrice(buyingPrice);
									product.setSellingPrice(sellingPrice);
									
									double profit = (sellingPrice - buyingPrice) * unitsSold;
									product.setProfitMade(profit);
									product.setSale(sale);
									sale.addSaleProduct(product);
								}
							}
					}
				}
				salesService.saveSale(sale);
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCachedFormulaResultType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().replaceAll("'", "");
		}
		return null;
	}
}