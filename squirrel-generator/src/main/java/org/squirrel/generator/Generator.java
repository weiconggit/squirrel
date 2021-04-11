package org.squirrel.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;
import org.squirrel.framework.util.FileUtil;
import org.squirrel.generator.data.DataUtil;

/**
 * @author weicong
 * @time 2020年11月19日
 * @version 1.0
 */
public class Generator {

	public static final String DB_NAME = "test";
	private static final String TABLE_PACKAGE = "org.squirrel.sys"; // 包名称
	private static final String SYS_NAME = "基础服务"; // 所属系统名称
	
	private static final String FILE_SEPARATOR = File.separator;
	private static final String PROJECT_PATH = System.getProperty("user.dir");
	
	private static final String OUT_PATH = PROJECT_PATH + FILE_SEPARATOR + "out";
	private static final String TEMPLATE_PATH = new StringBuilder()
			.append(PROJECT_PATH).append(FILE_SEPARATOR)
			.append("src").append(FILE_SEPARATOR)
			.append("main").append(FILE_SEPARATOR)
			.append("resources").append(FILE_SEPARATOR)
			.append("template").toString();

	public static void main(String[] args) throws Exception {
		System.out.println(String.format("输出路径：%s", OUT_PATH));
		System.out.println(String.format("模板路径：%s", TEMPLATE_PATH));
		boolean clearOutPath = clearOutPath();
		if (!clearOutPath) {
			System.err.println("清空路径失败");
		}
		List<Map<String, Object>> tableMaps = DataUtil.getTableMaps(DataUtil.MYSQL);
		rendering(tableMaps);
		System.out.println("完成！");
	}

	/**
	 * 渲染并生成文件
	 * @param tableMaps
	 * @throws IOException
	 */
	private static void rendering(List<Map<String, Object>> tableMaps) throws IOException {
		if (tableMaps == null || tableMaps.isEmpty()) {
			System.err.println("无表结构数据");
			return;
		}
		// 读取文件名模板
		Map<String, String> readTemplate = readFileNameTemplate();
		
		// 加载文件模板组
		FileResourceLoader resourceLoader = new FileResourceLoader(TEMPLATE_PATH, "utf-8");
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template t = null;
		
		for (Map<String, Object> map : tableMaps) {
			map.put("tablePackage", TABLE_PACKAGE);
			map.put("moduleName", SYS_NAME);
			map.put("lastupdata", new Date());
			map.put("author", "weicong");
			map.put("version", "v1");
			Iterator<Entry<String, String>> iterator = readTemplate.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> next = iterator.next();
				 // 加载具体模板
				t = gt.getTemplate(next.getKey());
				// 绑定数据
				t.binding(map); 
				//渲染结果，到新文件
				String filePath = null;
				if (next.getKey().equals("mapper/Mapper.xml")) {
					filePath = new StringBuilder()
							.append(OUT_PATH).append(FILE_SEPARATOR)
							.append("mapper").append(FILE_SEPARATOR)
							.append(String.format(next.getValue(), map.get("tableNameHump"))).toString();
				} else {
					filePath = new StringBuilder()
							.append(OUT_PATH).append(FILE_SEPARATOR)
							.append(map.get("tableNameNoUnd")).append(FILE_SEPARATOR)
							.append(String.format(next.getValue(), map.get("tableNameHump"))).toString();
				}
				System.out.println(String.format("文件输出路径：%s", filePath));
				File f = new File(filePath);
				f.getParentFile().mkdirs();
				f.createNewFile();
				FileWriter fw = new FileWriter(filePath);
				t.renderTo(fw);
			}
		}
	}
	
	/**
	 * 清空输出目录
	 * 
	 * @return
	 */
	private static final boolean clearOutPath() {
		File file = new File(OUT_PATH);
//		File file = new File("C:\\Users\\user\\Desktop\\out");
		return FileUtil.deleteDirOrFile(file);
	}

	/**
	 * 文件名模板
	 * @return
	 */
	private static final Map<String, String> readFileNameTemplate() {
		// key: 模板位置，value: 生成文件的名称
		Map<String, String> map = new HashMap<>();
		map.put("java/Controller.java", "%sController.java");
		map.put("java/Service.java", "%sService.java");
		map.put("java/Mapper.java", "%sMapper.java");
		map.put("java/Entity.java", "%s.java");
		map.put("java/EntityVO.java", "%sVO.java");
		map.put("mapper/Mapper.xml", "%sMapper.xml");
		return map;
	}
}
