package com.wyfx.business.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 从Excle表中导入数据
 */
public class ImportExcel {
    private static final Logger logger = LoggerFactory.getLogger(ImportExcel.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    /**
     * 读入excel文件，解析后返回
     *
     * @param file List<StudentScore>
     * @throws IOException
     */
    public static List readExcel(MultipartFile file, Integer scoreReportId) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        /*List<StudentScore> list = new ArrayList<StudentScore>();*/
        List list = new ArrayList();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第二行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    /*StudentScore studentScore = excelToObject(cells,scoreReportId);
                    list.add(studentScore);*/
                }
            }
            workbook.close();
        }
        return list;
    }

    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            //数字
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            //字符串
            case Cell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            //Boolean
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            //公式
            case Cell.CELL_TYPE_FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            //空值
            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
            //故障
            case Cell.CELL_TYPE_ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    /*public static StudentScore excelToObject(String[] cells,Integer scoreReportId){
        StudentScore studentScore = new StudentScore();
        studentScore.setScoreReportId(scoreReportId);
        for(int i = 0 ; i<cells.length; i++){
            switch (i){
                case 0:
                    studentScore.setTestPermitNumber(cells[i]);
                    break;
                case 1:
                    studentScore.setMonitorNumber(cells[i]);
                    break;
                case 2:
                    studentScore.setName(cells[i]);
                    break;
                case 3:
                    studentScore.setSchool(cells[i]);
                    break;
                case 4:
                    if(isNumber(cells[i])){
                        studentScore.setChinese(Double.parseDouble(cells[i]));
                    }else {
                        studentScore.setEnglish(Double.parseDouble("0"));
                    }
                    break;
                case 5:
                    if(isNumber(cells[i])){
                        studentScore.setMath(Double.parseDouble(cells[i]));
                    }else {
                        studentScore.setEnglish(Double.parseDouble("0"));
                    }
                    break;
                case 6:
                    if(isNumber(cells[i])){
                        studentScore.setMoralitySociety(Double.parseDouble(cells[i]));
                    }else {
                        studentScore.setEnglish(Double.parseDouble("0"));
                    }
                    break;
                case 7:
                    if(isNumber(cells[i])){
                        studentScore.setScience(Double.parseDouble(cells[i]));
                    }else {
                        studentScore.setEnglish(Double.parseDouble("0"));
                    }
                    break;
                case 8:
                    if(isNumber(cells[i])){
                        studentScore.setEnglish(Double.parseDouble(cells[i]));
                    }else {
                        studentScore.setEnglish(Double.parseDouble("0"));
                    }
                    break;
                default:
                    System.out.println("参数存在问题");
                    break;
            }
        }
        return studentScore;
    }*/

    public static boolean isNumber(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }


}
