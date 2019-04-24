package com.xframe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	/**
	 * 读取并导入excel文件
	 * 
	 * @param file
	 * @return
	 */
	public static Map<String, Object> readExcel(File file, int tableRows) {
		Map<String, Object> info = new HashMap<>();
		try {
			Workbook workBook = null;
			if (file.getName().endsWith(".xls")) {
				workBook = new HSSFWorkbook(new FileInputStream(file));
			} else if (file.getName().endsWith(".xlsx")) {
				workBook = new XSSFWorkbook(new FileInputStream(file));
			}
			/**
			 * 可能有多行的数据，但是一次读入进来3000行
			 */
			// 一次读取3000条数据
			int pageSize = 3000;

			// 依次导入每个sheet里面的数据
			for (int i = 0; i < 1; i++) {
				Sheet sheet = workBook.getSheetAt(i);
				// 算出总记录数
				int totalCount = sheet.getLastRowNum();
				// 算出总页数
				int totalPage = getTotalPage(totalCount, pageSize);
				Row header = sheet.getRow(1);
				if (header != null) {
					int celNum = tableRows;// Excel列数:注如果导入列数有变化必须修改这里
					// int celNum = header.getPhysicalNumberOfCells();
					List<List<Object>> datas = null;
					List<Object> data = null;
					for (int j = 1; j <= totalPage; j++) {// j为页
						datas = new ArrayList<List<Object>>();
						int firstResult = j == 1 ? 1 : getFirstResult(j, pageSize) + 1;// 第一行
						int lastResult = pageSize * j > totalCount ? totalCount : pageSize * j;// 第二行
						for (int k = firstResult; k <= lastResult; k++) {
							Row row = sheet.getRow(k);
							if (row != null) {
								data = new ArrayList<Object>();
								for (int t = 0; t < celNum; t++) {
									Cell cell = row.getCell(t);
									if (cell == null) {
										data.add(null);
									} else {
										cell.setCellType(Cell.CELL_TYPE_STRING);
										String value = cell.getStringCellValue();
										if (value != null)
											value = value.trim();
										data.add(cell.getStringCellValue());
										System.out.println(value);
									}
								}
								// data.add(file.getPath());
								datas.add(data);
							}
						}
						info.put("sheet", i + 1);
						info.put("filePath", file.getPath());
						info.put("data", datas);
						// 导入数据
						System.out.println("sheet:" + i);
						System.out.println("filePath=" + file.getPath());
						System.out.println("datas" + datas);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public static int getTotalPage(int totalCount, int pageSize) {
		return (totalCount + pageSize - 1) / pageSize;
	}

	public static int getFirstResult(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
	
	
}
