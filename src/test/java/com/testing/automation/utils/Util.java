package com.testing.automation.utils;

import com.jayway.jsonpath.JsonPath;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.apache.poi.ss.usermodel.CellType;

import java.io.*;
import java.util.*;


public class Util {

    @DataProvider(name = "TestDataJson")
    public static Object[][] getTestDataJson() {
        Object[][] obj = null;
        try {
            String jsonTestData = FileUtils.readFileToString(new File(FileNameConstants.TEST_DATA_JSON),"UTF-8");
            JSONArray jsonArray = JsonPath.read(jsonTestData, "$");
            int size = jsonArray.size();
            obj = new Object[size][1];
            for (int i=0; i<size; i++) {
                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>)jsonArray.get(i);
                obj[i][0] = map;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    @DataProvider(name = "TestDataCSV")
    public static Object[][] getTestDataCSV() {

        Object[][] objArray = null;
        List<Map<String, String>> testDataList = new ArrayList<>();
        Map<String, String> map = null;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(FileNameConstants.TEST_DATA_CSV));
            String[] line = null;
            csvReader.readNext();  // <- This skips the header

            while ( (line = csvReader.readNext()) != null) {
                map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                map.put("firstname", line[0]);
                map.put("lastname", line[1]);
                map.put("totalprice", line[2]);

                testDataList.add(map);
            }

            int size = testDataList.size();
            objArray = new Object[size][1];
            for (int i=0; i<size; i++) {
                objArray[i][0] = testDataList.get(i);
            }

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return objArray;
    }

    @DataProvider(name = "TestDataExcel")
    public static Object[][] getTestDataExcel() {

        Object[][] data = null;
        List<Map<String, String>> testDataList = new ArrayList<>();
        Map<String, String> map = null;
        try {
            FileInputStream fis = new FileInputStream(FileNameConstants.TEST_DATA_EXL);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells(); // Dynamic column count from header

            data = new Object[rowCount - 1][colCount]; // skip header row

            for (int i = 1; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    switch (sheet.getRow(i).getCell(j).getCellType()) {
                        case STRING:
                            data[i - 1][j] = sheet.getRow(i).getCell(j).getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i - 1][j] = sheet.getRow(i).getCell(j).getNumericCellValue();
                            break;
                        case BOOLEAN:
                            data[i - 1][j] = sheet.getRow(i).getCell(j).getBooleanCellValue();
                            break;
                        case BLANK:
                            data[i - 1][j] = "";
                            break;
                        default:
                            data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
                    }
                }
            }

            } catch (IOException  e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
