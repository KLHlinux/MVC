package com.atguigu.mvcapp.servlet;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.atguigu.mvcapp.dao.factory.CustomerDAOFactory;

/**
 * Servlet implementation class InitServlet
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	public void init() throws ServletException {
		CustomerDAOFactory.getInstance().setType("jdbc");
		
		//读取类路径下的switch.properties文件
		InputStream in = 
				getServletContext().getResourceAsStream("/WEB-INF/classes/switch.properties");
		
		Properties properties = new Properties();
		
		try {
			//properties和/WEB-INF/classes/switch.properties路径下的文件关联起来
			properties.load(in);
			//获取switch.properties的type属性值
			String type = properties.getProperty("type");
			//赋值CustomerDAOFactory的type属性值
			CustomerDAOFactory.getInstance().setType(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
