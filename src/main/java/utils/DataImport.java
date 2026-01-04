package utils;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataImport {
	public static String filePath = "C:\\Users\\Sandesh Velhal\\eclipse-workspace\\Servicenow-AllModule\\src\\test\\resources\\testdata.xlsx";
	
	
	public static Object[][] getData(String sheetName) {
        Object[][] data = null;

        try {
            FileInputStream fis = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            int rows = sheet.getPhysicalNumberOfRows();
            int cols = sheet.getRow(0).getLastCellNum();

            //System.out.println("rows : "+rows);
            //System.out.println("cols : "+cols);

            data = new Object[rows - 1][cols];

            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
                    //System.out.println("data "+ data[i - 1][j]);
                }
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;        
    }
}