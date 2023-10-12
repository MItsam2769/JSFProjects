package com.jsf.task;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.jsf.task.db.operations.UserDao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.hssf.usermodel.HSSFCell; // Import the appropriate classes for HSSF
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@ManagedBean
@SessionScoped
public class DashboardBean {   
    public String navigateToProfile() {
        System.out.println("Navigating to Profile");
        return "Profile.xhtml?faces-redirect=true"; // Redirect to a success page
    }
    public String navigateToDashboard() {
        System.out.println("Navigating to Dashboard");
        return "dashboard.xhtml?faces-redirect=true"; // Redirect to a success page
    }
    public String navigateToUpload() {
        System.out.println("Navigating -----");
         FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        User user = (User) session.getAttribute("user");
        // Now you have access to the user object, including the role property
        String role = user.getRole();
       
        if("INPUTTER".equals(role)) {
            System.out.println("Navigating to Upload");
            return "Upload.xhtml?faces-redirect=true";
        } else if("READER".equals(role)) {
            System.out.println("Navigating to Reader");
            return "Read.xhtml?faces-redirect=true";
        } else {
            return "dashboard.xhtml?faces-redirect=true";
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
    	  FacesContext facesContext = FacesContext.getCurrentInstance();
          HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
          User user = (User) session.getAttribute("user");
          String uploadedBy = user.getUsername();
        System.out.println("Getting File from "+ uploadedBy);
      int i=0;
        UploadedFile file = event.getFile();
        try (InputStream stream = file.getInputstream()) {
            HSSFWorkbook workbook = new HSSFWorkbook(stream); // Use HSSFWorkbook for XLS (Excel 97-2003) format

            HSSFSheet sheet = workbook.getSheetAt(0); // Assuming you have a single sheet
            for (Row row : sheet) {
                HSSFRow hssfRow = (HSSFRow) row;
                HSSFCell productIDCell = hssfRow.getCell(0);
                HSSFCell nameCell = hssfRow.getCell(1);
                HSSFCell descriptionCell = hssfRow.getCell(2);
                HSSFCell priceCell = hssfRow.getCell(3);

                double productID = productIDCell.getNumericCellValue();
                String name = nameCell.getStringCellValue();
                String description = descriptionCell.getStringCellValue();
                double price = priceCell.getNumericCellValue();
               
                // Call the DAO method to insert data into the Product table
                UserDao.insertProductData(productID, name, description, price, uploadedBy);
                System.out.println(i++);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file processing errors here
        }
    }


public List<Product> getProducts() {
	UserDao userDao = new UserDao();
    List<Product> products = new ArrayList<>();
    
    // Call  DAO method to fetch products
    try {
        products = userDao.loadProducts(); 
    } catch (Exception e) {
    
        e.printStackTrace();
    }
    
    return products;
}
}
