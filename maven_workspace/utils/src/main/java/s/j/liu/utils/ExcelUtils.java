package s.j.liu.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @version v0.0.1
 * @since 2017-06-29 09:29:00
 * @author Shengjun Liu
 *
 */
@SuppressWarnings("unused")
public class ExcelUtils {
  private String filePath = null;
  private String fileType = null;
  private InputStream is = null;
  private int numOfSheets = 0;
  private int currSheet = 0;
  private int currPosition = 0;

  HSSFWorkbook hssfWorkbook = null;

  public ExcelUtils() {
  }

  @SuppressWarnings("null")
  public ExcelUtils(String filePath) throws IOException {
    this.filePath = filePath;
    if (filePath == null && filePath.trim().equals("")) {
      throw new IOException("The filePath is not specific.");
    }
    fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
    if (fileType.equals("xlsx") || fileType.equals("xls")) {
      is = new FileInputStream(filePath);
      hssfWorkbook = new HSSFWorkbook(is);
      numOfSheets = hssfWorkbook.getNumberOfSheets();
    } else {
      throw new IOException("The fileType is not specific.");
    }
  }

  public int getNumberOfSheets() {
    return numOfSheets;
  }

  public int getNumberOfSheetLines(int sheet) {
    if (sheet > numOfSheets) {
      return -1;
    } else {
      return hssfWorkbook.getSheetAt(sheet).getLastRowNum();
    }
  }

  public HSSFSheet getHssfSheet(int sheet) {
    if (sheet > numOfSheets) {
      return null;
    } else {
      return hssfWorkbook.getSheetAt(sheet);
    }
  }

  public List<String> getHssfSheetContent(int sheet) {
    HSSFSheet hssfSheet = getHssfSheet(sheet);
    List<String> rowList = new ArrayList<String>();
    if (sheet > numOfSheets) {
      return null;
    } else {
      for (int i = 0; i < hssfSheet.getLastRowNum(); i++) {
        String value = getRowContent(hssfSheet.getRow(i));
        if (value != null)
          rowList.add(value.substring(0, value.length() - 1));
      }
    }
    return rowList;
  }

  public HSSFRow getHssfRow(HSSFSheet hssfSheet, int rowNumber) {
    return hssfSheet.getRow(rowNumber);
  }

  public HSSFRow getHssfRow(int hssfSheet, int rowNumber) {
    return getHssfSheet(hssfSheet).getRow(rowNumber);
  }

  @SuppressWarnings("deprecation")
  public String getRowContent(HSSFRow hssfRow) {
    StringBuffer buffer = new StringBuffer();
    int filedColumns = hssfRow.getLastCellNum();
    HSSFCell hssfCell = null;
    for (int i = 0; i < filedColumns; i++) {
      hssfCell = hssfRow.getCell(i);
      String cellvalue = null;
      if (hssfCell != null) {
        switch (hssfCell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:
          if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
            cellvalue = hssfCell.getDateCellValue().toLocaleString();
          } else {
            cellvalue = String.valueOf(hssfCell.getNumericCellValue());
          }
          break;
        case HSSFCell.CELL_TYPE_STRING:
          cellvalue = hssfCell.getStringCellValue();
          break;
        default:
          cellvalue = "";
          break;
        }
      }
      buffer.append(cellvalue).append(",");
    }
    return buffer.toString();
  }

  public static void main(String[] args) throws IOException {
    ExcelUtils excelUtils = new ExcelUtils("C:/Users/LSJ/Desktop/1.xls");
    List<String> list = excelUtils.getHssfSheetContent(0);
    for (String string : list) {
      System.out.println(string);
    }
  }

}
