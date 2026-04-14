package utils;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataImport {
    static String projectRoot = System.getProperty("user.dir");
    static Path excelPath = Paths.get(projectRoot, "src", "test", "resources", "testdata.xlsx");
    static String excelPathStr = excelPath.toString();

    public static Object[][] getData(String sheetName) {
        Object[][] data = null;
        try (FileInputStream fis = new FileInputStream(excelPathStr);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("DataImport: sheet not found: " + sheetName);
                return new Object[0][0];
            }

            DataFormatter formatter = new DataFormatter();
            int rows = sheet.getPhysicalNumberOfRows();
            Row header = sheet.getRow(0);
            int cols = (header != null) ? header.getLastCellNum() : 0;

            if (cols <= 0) {
                System.out.println("DataImport: no header cells for sheet: " + sheetName);
                return new Object[0][0];
            }

            if (rows <= 1) {
                return new Object[0][cols];
            }

            data = new Object[rows - 1][cols];
            for (int r = 1; r < rows; r++) {
                Row row = sheet.getRow(r);
                for (int c = 0; c < cols; c++) {
                    if (row == null) {
                        data[r - 1][c] = "";
                        continue;
                    }
                    Cell cell = row.getCell(c);
                    data[r - 1][c] = (cell == null) ? "" : formatter.formatCellValue(cell);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}