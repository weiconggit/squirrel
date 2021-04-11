package org.weicong.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;

/**
 * @author weicong
 * @time 2020年11月19日
 * @version 1.0
 */
public class TestGenerator {
	
	private static final String PROJECT_PATH = System.getProperty("user.dir");

	public static void main(String[] args) throws IOException {
		learn1();
//		learn2();
	}

	private static void learn1() throws IOException {
		// 初始化代码
		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		// 获取模板
		Template t = gt.getTemplate("hello,${name}");
		t.binding("name", "beetl");
		// 渲染结果
		String str = t.render();
		System.out.println(str);
	}
	
	private static void learn2() throws IOException {
		// 根目录
		String rootPath = TestGenerator.class.getClassLoader().getResource("").getPath();
		// 模板所在目录，/D:/1-workspace/eclipse/cherry-generator/target/test-classes/test
		String templatePath = rootPath + "test"; 
		// 输出目录，D:\1-workspace\eclipse\cherry-generator\out
		String outFileDir = PROJECT_PATH + File.separator + "out"; 
		
		// 模拟表结构数据
		Map<String, Object> map = new HashMap<>();
		map.put("className", "Test");
		map.put("tablePackage", "org.weicong");
		map.put("beanName", "beetltest");
		map.put("idType", "String");
		map.put("tableCnName", "beetl测试");
		map.put("author", "weicong");
		map.put("lastupdata", new Date());
		
		// 加载文件模板组
		FileResourceLoader resourceLoader = new FileResourceLoader(templatePath,"utf-8");
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		
		Template t = gt.getTemplate("TestDao.java"); // 加载具体模板
		t.binding(map); // 绑定数据
		//渲染结果，到新文件
		File f = new File(outFileDir + File.separator + "TestDao.java");
		f.getParentFile().mkdirs();
		f.createNewFile();
		FileWriter fw = new FileWriter(outFileDir + File.separator + "TestDao.java");
		t.renderTo(fw);
	}
}
