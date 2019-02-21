package test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel文件操作工具类，包括读、写、合并等功能
 * 
 * @author add by ts 2017.07.27
 *
 */
public class ExcelUtil {

	// %%%%%%%%-------常量部分 开始----------%%%%%%%%%
	/**
	 * 默认的开始读取的行位置为第一行（索引值为0）
	 */
	private final static int READ_START_POS = 0;

	/**
	 * 默认结束读取的行位置为最后一行（索引值=0，用负数来表示倒数第n行）
	 */
	private final static int READ_END_POS = 0;

	/**
	 * 默认Excel内容的开始比较列位置为第一列（索引值为0）
	 */
	private final static int COMPARE_POS = 0;

	/**
	 * 默认多文件合并的时需要做内容比较（相同的内容不重复出现）
	 */
	private final static boolean NEED_COMPARE = true;

	/**
	 * 默认多文件合并的新文件遇到名称重复时，进行覆盖
	 */
	private final static boolean NEED_OVERWRITE = false;

	/**
	 * 默认读取第一个sheet中（只有当ONLY_ONE_SHEET = true时有效）
	 */
	private final static int SELECTED_SHEET = 0;

	/**
	 * 默认从第一个sheet开始读取（索引值为0）
	 */
	private final static int READ_START_SHEET = 0;

	/**
	 * 默认在最后一个sheet结束读取（索引值=0，用负数来表示倒数第n行）
	 */
	private final static int READ_END_SHEET = 0;

	/**
	 * 默认打印各种信息
	 */
	private final static boolean PRINT_MSG = false;

	// %%%%%%%%-------常量部分 结束----------%%%%%%%%%

	// %%%%%%%%-------字段部分 开始----------%%%%%%%%%

	/**
	 * 设定开始读取的位置，默认为0
	 */
	private static int startReadPos = READ_START_POS;

	/**
	 * 设定结束读取的位置，默认为0，用负数来表示倒数第n行
	 */
	private static int endReadPos = READ_END_POS;

	/**
	 * 设定开始比较的列位置，默认为0
	 */
	private static int comparePos = COMPARE_POS;

	/**
	 * 设定汇总的文件是否需要替换，默认为true
	 */
	private static boolean isOverWrite = NEED_OVERWRITE;

	/**
	 * 设定是否需要比较，默认为true(仅当不覆写目标内容是有效，即isOverWrite=false时有效)
	 */
	private static boolean isNeedCompare = NEED_COMPARE;

	/**
	 * 设定操作的sheet在索引值
	 */
	private static int selectedSheetIdx = SELECTED_SHEET;

	/**
	 * 设定操作的sheet的名称
	 */
	private static String selectedSheetName = "";

	/**
	 * 设定开始读取的sheet，默认为0
	 */
	private static int startSheetIdx = READ_START_SHEET;

	/**
	 * 设定结束读取的sheet，默认为0，用负数来表示倒数第n行
	 */
	private static int endSheetIdx = READ_END_SHEET;

	/**
	 * 设定是否打印消息
	 */
	private static boolean printMsg = PRINT_MSG;

	// %%%%%%%%-------字段部分 结束----------%%%%%%%%%

	/**
	 * 自动根据文件扩展名，调用对应的写入方法
	 * 
	 * @Title: writeExcel
	 * @param src_rowList
	 * @param dist_xlsPath
	 * @throws IOException
	 */
	public static boolean writeExcel(List<Row> src_rowList, String src_xlsPath, String dist_xlsPath, String sheetName) throws IOException {
		// 扩展名为空时，
		if (dist_xlsPath.equals("")) {
			throw new IOException("目标文件路径不能为空！");
		}
		// 获取扩展名
		String ext = dist_xlsPath.substring(dist_xlsPath.lastIndexOf(".") + 1);
		try {
			if ("xls".equals(ext)) { // 使用xls方式写入
				return writeExcel_xls(src_rowList, src_xlsPath, dist_xlsPath, sheetName);
			} else if ("xlsx".equals(ext)) { // 使用xlsx方式写入
				writeExcel_xlsx(src_rowList, src_xlsPath, dist_xlsPath, sheetName);
			} else { // 依次尝试xls、xlsx方式写入
				out("您要操作的文件没有扩展名，正在尝试以xls方式写入...");
				try {
					return writeExcel_xls(src_rowList, src_xlsPath, dist_xlsPath, sheetName);
				} catch (IOException e1) {
					out("尝试以xls方式写入，结果失败！，正在尝试以xlsx方式读取...");
					try {
						writeExcel_xlsx(src_rowList, src_xlsPath, dist_xlsPath, sheetName);
					} catch (IOException e2) {
						out("尝试以xls方式写入，结果失败！\n请您确保您的文件是Excel文件，并且无损，然后再试。");
						throw e2;
					}
				}
			}
		} catch (IOException e) {
			throw e;
		}
		return false;
	}

	/**
	 * 修改Excel（97-03版，xls格式）
	 * 
	 * @Title: writeExcel_xls
	 * @param src_rowList
	 * @param src_xlsPath
	 * @param dist_xlsPath
	 * @throws IOException
	 */
	private static boolean writeExcel_xls(List<Row> src_rowList, String src_xlsPath, String dist_xlsPath, String sheetName) throws IOException {
		// 判断文件路径是否为空
		if (dist_xlsPath == null || dist_xlsPath.equals("")) {
			out("目标文件路径不能为空");
			throw new IOException("目标文件路径不能为空");
		}
		// 判断文件路径是否为空
		if (src_xlsPath == null || src_xlsPath.equals("")) {
			out("源文件路径不能为空");
			throw new IOException("源文件路径不能为空");
		}
		// 判断列表是否有数据，如果没有数据，则返回
		if (src_rowList == null || src_rowList.size() == 0) {
			out("源文件文档为空");
			return false;
		}
		try {
			HSSFWorkbook wb = null;
			// 判断文件是否存在
			File file = new File(dist_xlsPath);
			if (file.exists()) {
				// 如果复写，则删除后
				if (isOverWrite) {
					file.delete();
					// 如果文件不存在，则创建一个新的Excel
					// wb = new HSSFWorkbook();
					// wb.createSheet("Sheet1");
					wb = new HSSFWorkbook(new FileInputStream(src_xlsPath));
				} else {
					// 如果文件存在，则读取Excel
					wb = new HSSFWorkbook(new FileInputStream(file));
				}
			} else {
				// 如果文件不存在，则创建一个新的Excel
				// wb = new HSSFWorkbook();
				// wb.createSheet("Sheet1");
				wb = new HSSFWorkbook(new FileInputStream(src_xlsPath));
			}
			// 将rowlist的内容写到Excel中
			return writeExcel_LL(wb, src_rowList, dist_xlsPath, sheetName);// TODO
																			// 暂时连接老练方法
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 修改Excel（2007版，xlsx格式）
	 * 
	 * @Title: writeExcel_xlsx
	 * @param src_rowList
	 * @param xlsPath
	 * @throws IOException
	 */
	private static void writeExcel_xlsx(List<Row> src_rowList, String src_xlsPath, String dist_xlsPath, String sheetName) throws IOException {

		// 判断文件路径是否为空
		if (dist_xlsPath == null || dist_xlsPath.equals("")) {
			out("文件路径不能为空");
			throw new IOException("文件路径不能为空");
		}
		// 判断文件路径是否为空
		if (src_xlsPath == null || src_xlsPath.equals("")) {
			out("文件路径不能为空");
			throw new IOException("文件路径不能为空");
		}

		// 判断列表是否有数据，如果没有数据，则返回
		if (src_rowList == null || src_rowList.size() == 0) {
			out("文档为空");
			return;
		}

		try {
			// 读取文档
			XSSFWorkbook wb = null;

			// 判断文件是否存在
			File file = new File(dist_xlsPath);
			if (file.exists()) {
				// 如果复写，则删除后
				if (isOverWrite) {
					file.delete();
					// 如果文件不存在，则创建一个新的Excel
					// wb = new XSSFWorkbook();
					// wb.createSheet("Sheet1");
					wb = new XSSFWorkbook(new FileInputStream(src_xlsPath));
				} else {
					// 如果文件存在，则读取Excel
					wb = new XSSFWorkbook(new FileInputStream(file));
				}
			} else {
				// 如果文件不存在，则创建一个新的Excel
				// wb = new XSSFWorkbook();
				// wb.createSheet("Sheet1");
				wb = new XSSFWorkbook(new FileInputStream(src_xlsPath));
			}
			// 将rowlist的内容添加到Excel中
			writeExcel(wb, src_rowList, dist_xlsPath, sheetName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改Excel，并另存为
	 * 
	 * @Title: WriteExcel
	 * @param wb
	 * @param rowList
	 * @param dist_xlsPath
	 */
	@SuppressWarnings("deprecation")
	private static void writeExcel(Workbook wb, List<Row> rowList, String dist_xlsPath, String sheetName) {
		if (wb == null) {
			out("操作文档不能为空！");
			return;
		}
		Sheet sheet = wb.getSheet(sheetName);// 修改第一个sheet中的值
		// 如果每次重写，那么则从开始读取的位置写，否则果获取源文件最新的行。
		int lastRowNum = isOverWrite ? startReadPos : sheet.getLastRowNum() + 1;
		int t = 0;// 记录最新添加的行数
		out("要添加的数据总条数为：" + rowList.size());
		CellStyle newstyle = wb.createCellStyle();
		for (Row row : rowList) {
			if (row == null) continue;
			// 判断是否已经存在该数据
			int pos = findInExcel(sheet, row);
			Row r = null;// 如果数据行已经存在，则获取后重写，否则自动创建新行。
			if (pos >= 0) {
				sheet.removeRow(sheet.getRow(pos));
				r = sheet.createRow(pos);
			} else {
				r = sheet.createRow(lastRowNum + t++);
			}
			// 用于设定单元格样式
			// 循环为新行创建单元格
			for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
				Cell distCell = r.createCell(i);// 获取数据类型
				Cell srcCell = row.getCell(i);
				if (srcCell == null) continue;
				if (srcCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					distCell.setCellFormula(srcCell.getCellFormula());// 复制单元格的值到新的单元格(公式类型)
				} else if (srcCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					distCell.setCellValue(srcCell.getNumericCellValue());// 复制单元格的值到新的单元格(数值类型)
				} else {
					distCell.setCellValue(getCellValue(srcCell));// 复制单元格的值到新的单元格(其他值类型)
				}
				// cell.setCellStyle(row.getCell(i).getCellStyle());//出错
				// if (srcCell == null) continue;
				copyCellStyle(srcCell.getCellStyle(), newstyle); // 获取原来的单元格样式
				distCell.setCellStyle(newstyle);// 设置样式
				// sheet.autoSizeColumn(i);//自动跳转列宽度
			}
		}
		out("其中检测到重复条数为:" + (rowList.size() - t) + " ，追加条数为：" + t);
		// 统一设定合并单元格
		setMergedRegion(sheet);
		try {
			wb.removeSheetAt(wb.getNumberOfSheets() - 1);// TODO
			// 重新将数据写入Excel中
			FileOutputStream outputStream = new FileOutputStream(dist_xlsPath);
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			out("写入Excel时发生错误！ ");
			e.printStackTrace();
		}
	}

	private static final int CHColumn = 1;// 源文件数据列
	private static final int LLColumn = 0;// 目标文件数据列

	/**
	 * 转化channel数据到 老练表
	 * 
	 * @param wb
	 * @param rowList
	 * @param dist_xlsPath
	 * @param sheetName
	 */
	@SuppressWarnings("deprecation")
	private static boolean writeExcel_LL(Workbook wb, List<Row> rowList, String dist_xlsPath, String sheetName) {
		if (wb == null) {
			out("操作文档不能为空！");
			return false;
		}
		Sheet sheet = wb.getSheet(sheetName);// 修改目标文件sheet中的值
		CellStyle newstyle = wb.createCellStyle();
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Cell srcCell = (rowList.get(i).getCell(CHColumn));
			Cell distCell = (sheet.getRow(i).getCell(LLColumn));
			if (srcCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());// 复制单元格的值到新的单元格(公式类型)
			} else if (srcCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				distCell.setCellValue(srcCell.getNumericCellValue());// 复制单元格的值到新的单元格(数值类型)
			} else {
				distCell.setCellValue(getCellValue(srcCell));// 复制单元格的值到新的单元格(其他值类型)
			}
			copyCellStyle(srcCell.getCellStyle(), newstyle); // 获取原来的单元格样式
			distCell.setCellStyle(newstyle);// 设置样式
			// System.out.println(distCell + " -- " + srcCell);
		}
		// 统一设定合并单元格
		// setMergedRegion(sheet);
		try {
			// 重新将数据写入Excel中
			FileOutputStream outputStream = new FileOutputStream(dist_xlsPath);
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
			return true;
		} catch (Exception e) {
			out("写入Excel时发生错误！ ");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 自动根据文件扩展名，调用对应的读取方法
	 * 
	 * @Title: writeExcel
	 * @param src_xlsPath
	 * @throws IOException
	 */
	public static List<Double> readExcel(String src_xlsPath) throws IOException {
		if (src_xlsPath.equals("")) {
			throw new IOException("源文件路径不能为空！");
		} else {
			File file = new File(src_xlsPath);
			if (!file.exists()) {
				throw new IOException("源文件不存在！");
			}
		}
		String ext = src_xlsPath.substring(src_xlsPath.lastIndexOf(".") + 1); // 获取扩展名
		try {
			if ("xls".equals(ext)) { // 使用xls方式读取
				return readExcel_xls(src_xlsPath);
			} else if ("xlsx".equals(ext)) { // 使用xlsx方式读取
				return readExcel_xlsx(src_xlsPath);
			} else {
				return null;
			}
		} catch (IOException e) {
			throw e;
		}
	}

	/***
	 * 读取Excel(97-03版，xls格式)
	 * 
	 * @throws Exception
	 * @Title: readExcel
	 */
	private static List<Double> readExcel_xls(String xlsPath) throws IOException {
		// 判断文件是否存在
		File file = new File(xlsPath);
		if (!file.exists()) {
			throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
		}
		HSSFWorkbook wb = null;// 用于Workbook级的操作，创建、删除Excel
		List<Double> rowList = new ArrayList<Double>();
		try {
			// 读取Excel
			wb = new HSSFWorkbook(new FileInputStream(file));
			// 读取Excel 97-03版，xls格式
			rowList = readExcel(wb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rowList;
	}

	/**
	 * //读取Excel 2007版，xlsx格式
	 * 
	 * @Title: readExcel_xlsx
	 * @return
	 * @throws Exception
	 */
	private static List<Double> readExcel_xlsx(String xlsPath) throws IOException {
		// 判断文件是否存在
		File file = new File(xlsPath);
		if (!file.exists()) {
			throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
		}

		XSSFWorkbook wb = null;
		List<Double> rowList = new ArrayList<Double>();
		try {
			FileInputStream fis = new FileInputStream(file);
			// 去读Excel
			wb = new XSSFWorkbook(fis);

			// 读取Excel 2007版，xlsx格式
			rowList = readExcel(wb);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return rowList;
	}

	/**
	 * 通用读取Excel
	 * 
	 * @Title: readExcel
	 * @param wb
	 * @return
	 */
	private static List<Double> readExcel(Workbook wb) {
		List<Double> rowList = new ArrayList<Double>();
		int sheetCount = 1;// 需要操作的sheet数量
		Sheet sheet = null;
		sheetCount = wb.getNumberOfSheets();// 获取可以操作的sheet总数量
		// 获取sheet数目
		for (int t = startSheetIdx; t < sheetCount + endSheetIdx; t++) {
			// 获取设定操作的sheet
			sheet = wb.getSheetAt(t);
			// 获取最后行号
			int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum > 0) { // 如果>0，表示有数据
				out("\n开始读取名为【" + sheet.getSheetName() + "】的内容：");
			}
			Row row = null;
			// 循环读取
			for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					out("第" + (i + 1) + "行：", false);
					// 获取每一单元格的值
					for (int index = 0; index < row.getLastCellNum(); index++) {
						String value = getCellValue(row.getCell(index));
						if (!value.isEmpty()) {
							out("[" + (value) + "]", false);
							rowList.add(Double.parseDouble(value));
						}
					}
					out("");
				}
			}
		}
		return rowList;
	}

	/***
	 * 读取单元格的值
	 * 
	 * @Title: getCellValue
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getCellValue(Cell cell) {
		Object result = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				result = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_ERROR:
				result = cell.getErrorCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				break;
			default:
				break;
			}
		}
		return result.toString();
	}

	/**
	 * 查找某行数据是否在Excel表中存在，返回行数。
	 * 
	 * @Title: findInExcel
	 * @param sheet
	 * @param row
	 * @return
	 */
	private static int findInExcel(Sheet sheet, Row row) {
		int pos = -1;

		try {
			// 如果覆写目标文件，或者不需要比较，则直接返回
			if (isOverWrite || !isNeedCompare) {
				return pos;
			}
			for (int i = startReadPos; i <= sheet.getLastRowNum() + endReadPos; i++) {
				Row r = sheet.getRow(i);
				if (r != null && row != null) {
					String v1 = getCellValue(r.getCell(comparePos));
					String v2 = getCellValue(row.getCell(comparePos));
					if (v1.equals(v2)) {
						pos = i;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pos;
	}

	/**
	 * 复制一个单元格样式到目的单元格样式
	 * 
	 * @param fromStyle
	 * @param toStyle
	 */
	private static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignmentEnum());
		// 边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottomEnum());
		toStyle.setBorderLeft(fromStyle.getBorderLeftEnum());
		toStyle.setBorderRight(fromStyle.getBorderRightEnum());
		toStyle.setBorderTop(fromStyle.getBorderTopEnum());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		// 背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		// 数据格式
		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPatternEnum());
		// toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());// 首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());// 旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignmentEnum());
		toStyle.setWrapText(fromStyle.getWrapText());

	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	private static void setMergedRegion(Sheet sheet) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			// 获取合并单元格位置
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstRow = ca.getFirstRow();
			if (startReadPos - 1 > firstRow) {// 如果第一个合并单元格格式在正式数据的上面，则跳过。
				// continue;
			}
			int lastRow = ca.getLastRow();
			int mergeRows = lastRow - firstRow;// 合并的行数
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			// 根据合并的单元格位置和大小，调整所有的数据行格式，
			for (int j = lastRow + 1; j <= sheet.getLastRowNum(); j++) {
				// 设定合并单元格
				sheet.addMergedRegion(new CellRangeAddress(j, j + mergeRows, firstColumn, lastColumn));
				j = j + mergeRows;// 跳过已合并的行
			}

		}
	}

	/**
	 * 打印消息，
	 * 
	 * @param msg
	 *            消息内容
	 * @param tr
	 *            换行
	 */
	private static void out(String msg) {
		if (printMsg) {
			out(msg, true);
		}
	}

	/**
	 * 打印消息，
	 * 
	 * @param msg
	 *            消息内容
	 * @param tr
	 *            换行
	 */
	private static void out(String msg, boolean tr) {
		if (printMsg) {
			System.out.print(msg + (tr ? "\n" : ""));
		}
	}

	public boolean isNeedCompare() {
		return isNeedCompare;
	}

	public void setNeedCompare(boolean isNeedCompare) {
		ExcelUtil.isNeedCompare = isNeedCompare;
	}

	public int getComparePos() {
		return comparePos;
	}

	public void setComparePos(int comparePos) {
		ExcelUtil.comparePos = comparePos;
	}

	public int getStartReadPos() {
		return startReadPos;
	}

	public void setStartReadPos(int startReadPos) {
		ExcelUtil.startReadPos = startReadPos;
	}

	public int getEndReadPos() {
		return endReadPos;
	}

	public void setEndReadPos(int endReadPos) {
		ExcelUtil.endReadPos = endReadPos;
	}

	public boolean isOverWrite() {
		return isOverWrite;
	}

	public void setOverWrite(boolean isOverWrite) {
		ExcelUtil.isOverWrite = isOverWrite;
	}

	public int getSelectedSheetIdx() {
		return selectedSheetIdx;
	}

	public void setSelectedSheetIdx(int selectedSheetIdx) {
		ExcelUtil.selectedSheetIdx = selectedSheetIdx;
	}

	public String getSelectedSheetName() {
		return selectedSheetName;
	}

	public void setSelectedSheetName(String selectedSheetName) {
		ExcelUtil.selectedSheetName = selectedSheetName;
	}

	public int getStartSheetIdx() {
		return startSheetIdx;
	}

	public void setStartSheetIdx(int startSheetIdx) {
		ExcelUtil.startSheetIdx = startSheetIdx;
	}

	public int getEndSheetIdx() {
		return endSheetIdx;
	}

	public void setEndSheetIdx(int endSheetIdx) {
		ExcelUtil.endSheetIdx = endSheetIdx;
	}

	public boolean isPrintMsg() {
		return printMsg;
	}

	public void setPrintMsg(boolean printMsg) {
		ExcelUtil.printMsg = printMsg;
	}

}
