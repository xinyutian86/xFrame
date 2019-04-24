package com.xframe.controller;

import java.io.BufferedReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.xframe.utils.StringHelper;

/**
 * 
 * @ClassName: BaseController
 * @Description: 通用的Controller类 、带有基本的Model增删改查 与request处理
 * @author 创建者
 * @date 2018年1月5日 下午1:45:20
 * 
 * @param <M>
 */
public class BaseController<M extends Model<M>> extends Controller {

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @Description: TODO(获取请求参数为String类型的)
	 * @param key 请求参数的key
	 * @return String
	 */
	public String getParaToStr(String key) {
		String para = this.getAttrForStr(key);
		if (StringUtils.isBlank(para))
			para = this.getPara(key);
		if (StringUtils.isBlank(para))
			return null;
		return para;
	}

	/**
	 * 
	 * @Title: getParaToInt
	 * @Description: 获取单个请求参数，不存在返回默认值、如果返回值大于最大值则返回最大值
	 * @param name：参数名
	 * @param maxValue：最大值
	 * @param defaultValue 默认值
	 * @return Integer 返回类型
	 */
	public Integer getParaToInt(String name, int maxValue, int defaultValue) {
		Integer value = this.getParaToInt(name, defaultValue);
		if (value != null && value.intValue() > maxValue) {
			return maxValue;
		}
		return value;
	}

	/**
	 * 
	 * @Title: getParaToInt
	 * @Description: 获取单个请求参数，不存在返回默认值、如果返回值大于最大值则返回最大值
	 * @param name：参数名
	 * @param maxValue：最大值
	 * @param defaultValue 默认值
	 * @return Integer 返回类型
	 */
	public Integer getParaToInt(String name, int defaultValue) {
		Integer value = this.getParaToInt(name);
		if (value == null)
			value = defaultValue;
		return value;
	}

	/**
	 * 获取请求参数为int类型的数据
	 */
	public Integer getParaToInt(String key) {

		Integer tempInt = this.getParaToInt(key, null);
		if (tempInt == null) {
			tempInt = this.getAttrForInt(key);
		}
		return tempInt;
	}

	/**
	 * 
	 * @Description: TODO(获取双精度浮点类型的 请求参数)
	 * @param key 请求参数key
	 * @return Double
	 */
	public Double getParaToDouble(String key) {
		String para = this.getPara(key, null);
		if (StringUtils.isBlank(para))
			para = this.getAttr(key);
		if (StringUtils.isBlank(para))
			return null;
		return Double.valueOf(para);
	}

	/**
	 * 获取布尔类型的请求参数
	 */
	public Boolean getParaToBoolean(String key) {
		String value = this.getAttrForStr(key);
		if (StringUtils.isBlank(value)) {
			value = this.getPara(key);
		}

		if (StringUtils.isBlank(value))
			return null;
		value = value.trim().toLowerCase();
		if ("1".equals(value) || "true".equals(value)) {
			return Boolean.TRUE;
		} else if ("0".equals(value) || "false".equals(value)) {
			return Boolean.FALSE;
		}
		return null;

	}

	/**
	 * 获取Date类型的请求参数，参数必须符合 yyyy-MM-dd格式
	 */
	public Date getParaToDate(String key) {
		Date date = this.getParaToDate(key, null);
		if (date == null) {
			String dateStr = getAttrForStr(key);
			if (StringUtils.isBlank(dateStr))
				return null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr.trim());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;

	}

	/**
	 * 
	 * @Description: json格式字符串 转换成 map @param key : 请求参数的key ：字符串的 key @return
	 * 设定文件 @return List<Object> ：List<valueType>集合 @throws
	 */
	@SuppressWarnings("rawtypes")
	public Map getParaToList(String key) {
		String jsonStr = this.getParaToStr(key);
		if (StringUtils.isBlank(jsonStr))
			return null;
		try {
			return mapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解决响应json兼容ie11
	 */
	@Override
	public void renderJson(Object object) {
		String userAgent = getRequest().getHeader("User-Agent");
		System.out.println("User-Agent:"+userAgent);
		if (userAgent==null) {
			super.renderJson(object);
		}else {
			if (userAgent.toLowerCase().indexOf("msie") != -1 || userAgent.toLowerCase().indexOf("rv:11") != -1) {
				render(new JsonRender(object).forIE());
			} else {
				super.renderJson(object);
			}
		}
		
	}

	/**
	 * 取Request中的数据对象
	 * 
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	protected <T> T getRequestObject(Class<T> valueType) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		StringBuilder json = new StringBuilder();
		BufferedReader reader = this.getRequest().getReader();
		String line = null;
		while ((line = reader.readLine()) != null) {
			json.append(line);
		}
		reader.close();
		if (StringUtils.isEmpty(json.toString())) {
			return null;
		}
		return mapper.readValue(json.toString(), valueType);
	}

	/**
	 * 
	 * @Title: getParaTrim
	 * @Description: 获取单个请求参数，如果不存在就返回默认值
	 * @param name：参数名
	 * @param defaultValue 默认值
	 * @return String 返回类型
	 */
	public String getParaTrim(String name, String defaultValue) {
		String value = this.getPara(name);
		if (StrKit.notBlank(value)) {
			return value.trim();
		} else {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @Title: getAttrTrim @Description: 从request域中获取指定的数据，如果不存在就返回默认值 @param
	 * name：指定Key @param defaultValue：默认返回值 @return 设定文件 @return String 返回类型 @throws
	 */
	public String getAttrTrim(String name, String defaultValue) {
		String value = getAttr(name);
		if (StrKit.notBlank(value)) {
			return value.trim();
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取M的class
	 * 
	 * @return M
	 */
	@SuppressWarnings("unchecked")
	public Class<M> getClazz() {
		// getClass：获取当前被实例化的class对象
		// getGenericSuperclass：获取该类的泛型父类的Type
		Type t = getClass().getGenericSuperclass();

		// 返回表示此类型实际类型参数的 Type 对象的数组
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	/**
	 * 通用根据id查找
	 */
	public Record getById(String id, String idKeyName) {

		List<Record> list = Db.find("SELECT * FROM " + dbTible(null) + " WHERE " + idKeyName + " = '" + id + "'");
		// 对象 域（属性）名 驼峰处理
		return list.size() > 0 ? attributeConversionByList(list).get(0) : null;
	}

	/**
	 * 通用新增
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		renderText(getModel(((Model<?>) Class.forName(getClazz().getName()).newInstance()).getClass()).save() + "");
	}

	/**
	 * 通用修改
	 * 
	 * @throws Exception
	 */
	public void update() throws Exception {
		renderText(getModel(((Model<?>) Class.forName(getClazz().getName()).newInstance()).getClass()).update() + "");
	}

	/**
	 * 通用删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {

		renderText(getModel(((Model<?>) Class.forName(getClazz().getName()).newInstance()).getClass()).delete() + "");
	}

	/**
	 * 分页查询出来的属性 _ 下划线格式的属性名换成 驼峰格式
	 * 
	 * @param list
	 * @return
	 */
	protected Page<Record> attributeConversion(Page<Record> paramList) {

		if (paramList == null)
			return paramList;

		// 得到查询出来的所有的行
		List<Record> list = paramList.getList();
		for (Record record : list) {
			// 循环得到 没一行 的所有列名（字段名）
			String[] columnNames = record.getColumnNames();
			for (String string : columnNames) {
				// 循环字段名 得到每一个字段对于的值
				Object db_value = record.get(string);
				// 通过驼峰处理后 重新创建 一个域 添加这个字段的值
				record.set(StringHelper.underlineToCamel2(string), db_value);
				if (!StringHelper.underlineToCamel2(string).equals(string)) {
					record.remove(string);
				}
			}

		}

		return paramList;
	}

	/**
	 * 集合的 通用model 属性名 驼峰处理
	 * 
	 * @param list
	 * @return
	 */
	protected List<Record> attributeConversionByList(List<Record> list) {
		if (list == null)
			return list;

		for (Record record : list) {
			// 循环得到 没一行 的所有列名（字段名）
			String[] columnNames = record.getColumnNames();
			for (String string : columnNames) {
				// 循环字段名 得到每一个字段对于的值
				Object db_value = record.get(string);
				record.set(StringHelper.underlineToCamel2(string), db_value);
				if (!StringHelper.underlineToCamel2(string).equals(string)) {
					record.remove(string);
				}
			}
		}

		return list;
	}

	/**
	 * 集合的 通用model 属性名 驼峰处理
	 * 
	 * @param record
	 * @return
	 */
	protected Record attributeConversion(Record record) {
		if (record == null)
			return record;

		// 循环得到 没一行 的所有列名（字段名）
		String[] columnNames = record.getColumnNames();
		for (String string : columnNames) {
			// 循环字段名 得到每一个字段对于的值
			Object db_value = record.get(string);
			// 通过驼峰处理后 重新创建 一个域 添加这个字段的值
			record.set(StringHelper.underlineToCamel2(string), db_value);
			if (!StringHelper.underlineToCamel2(string).equals(string)) {
				record.remove(string);
			}
		}

		return record;
	}

	/**
	 * 集合的 通用model 属性名 驼峰处理 把属性名变成 下划线 _格式
	 * 
	 * @param list
	 * @return
	 */
	protected List<Record> camelToUnderlineByList_(List<Record> list) {
		if (list == null)
			return list;
		for (Record record : list) {
			// 循环得到 没一行 的所有列名（字段名）
			String[] columnNames = record.getColumnNames();
			for (String string : columnNames) {
				// 循环字段名 得到每一个字段对于的值
				Object db_value = record.get(string);
				// 通过驼峰处理后 重新创建 一个域 添加这个字段的值
				record.set(StringHelper.camelToUnderline(string), db_value);
				if (!StringHelper.camelToUnderline(string).equals(string)) {
					record.remove(string);
				}
			}
		}
		return list;
	}

	/**
	 * 集合的 通用model 属性名 驼峰处理 把属性名变成 下划线 _格式
	 * 
	 * @param list
	 * @return
	 */
	protected Record camelToUnderline(Record record) {
		if (record == null)
			return record;
		// 循环得到 没一行 的所有列名（字段名）
		String[] columnNames = record.getColumnNames();
		for (String string : columnNames) {
			// 循环字段名 得到每一个字段对于的值
			Object db_value = record.get(string);
			// 通过驼峰处理后 重新创建 一个域 添加这个字段的值
			record.set(StringHelper.camelToUnderline(string), db_value);
			if (!StringHelper.camelToUnderline(string).equals(string)) {
				record.remove(string);
			}
		}

		return record;
	}

	public String dbTible(String prefix) {
		if (StringUtils.isNotBlank(prefix)) {
			return prefix + dbTible(null);
		}
		return StringHelper.camelToUnderline(getClazz().getSimpleName()).substring(0);
	}

	//////////////////////////////// 报表导出模板样式/////////////////////////////////////////
	// 大标题的样式
	public CellStyle bigTitle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 字体加粗

		style.setFont(font);

		style.setAlignment(CellStyle.ALIGN_CENTER); // 横向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 纵向居中

		return style;
	}

	// 小标题的样式
	public CellStyle title(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 14);

		style.setFont(font);

		style.setAlignment(CellStyle.ALIGN_CENTER); // 横向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 纵向居中
		font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 字体加粗
		style.setBorderTop(CellStyle.BORDER_THIN); // 上细线
		style.setBorderBottom(CellStyle.BORDER_THIN); // 下细线
		style.setBorderLeft(CellStyle.BORDER_THIN); // 左细线
		style.setBorderRight(CellStyle.BORDER_THIN); // 右细线
		return style;
	}

	// 文字样式
	public CellStyle text(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		style.setAlignment(CellStyle.ALIGN_CENTER); // 横向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 纵向居中

		style.setBorderTop(CellStyle.BORDER_THIN); // 上细线
		style.setBorderBottom(CellStyle.BORDER_THIN); // 下细线
		style.setBorderLeft(CellStyle.BORDER_THIN); // 左细线
		style.setBorderRight(CellStyle.BORDER_THIN); // 右细线
		return style;
	}
}
