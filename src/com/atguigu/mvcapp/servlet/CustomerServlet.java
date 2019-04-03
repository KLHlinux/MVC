package com.atguigu.mvcapp.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.mvcapp.dao.CriteriaCustomer;
import com.atguigu.mvcapp.dao.CustomerDAO;
import com.atguigu.mvcapp.dao.factory.CustomerDAOFactory;
import com.atguigu.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.atguigu.mvcapp.dao.impl.CustomerDAOXMLImpl;
import com.atguigu.mvcapp.domin.Customer;

@WebServlet("*.do")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//深入理解面向接口编程：在类中调用接口的方法，而不必关心具体的实现。
	//这将有利于代码的解耦，使程序有更好的可移植性和可扩展性
	//private CustomerDAO customerDAO = new CustomerDAOXMLImpl();
	//private CustomerDAO customerDAO = new CustomerDAOJdbcImpl();
	private CustomerDAO customerDAO =
			CustomerDAOFactory.getInstance().getCustomerDAO();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String method = request.getParameter("method");
//		
//		switch(method){
//			case "add": 	add(request,response);		break;
//			case "query": 	query(request,response);	break;
//			case "delete": 	delete(request,response);	break;
//			case "update": 	update(request,response);	break;
//		}
//	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		//获取ServletPath: /edit.do 或  /addCustomer.do
		String servletPath = request.getServletPath();
		
		//去除/和.do，获得类似于edit或addCustomer这样的字符串
		String methodName = servletPath.substring(1);
		methodName = methodName.substring(0, methodName.length()-3);
		
		//应用了反射
		try {
			//利用反射获取methodName对应的方法:edit或addCustomer对应的Method
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);
			//利用反射调用对应的方法
			method.invoke(this, request,response);
		} catch (Exception e) {
			//e.printStackTrace();
			//若要调用的方法不存在，响应一个error.jsp页面
			response.sendRedirect("error.jsp");
		}
		
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String forwardPath = "/error.jsp";
				
		//1. 获取请求参数id
		String idStr = request.getParameter("id");
		
		//2. 调用CustomerDAO的customerDAO.get(id) 获取和id对应的Customer对象customer
		try {
			Customer customer = customerDAO.get(Integer.parseInt(idStr));
			if(customer != null) {
				forwardPath = "/updatecustomer.jsp";
				//3. 将customer放入request中	
				request.setAttribute("customer", customer);
			}
		} catch (Exception e) {	}
			
		//4.响应updatecustomer.jsp页面 (转发的方式)： 
		request.getRequestDispatcher(forwardPath).forward(request, response);
	}
	
	private void update(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//1.获取表单参数：id，name，address，phone，oldName
		String id = request.getParameter("id");
		String oldName = request.getParameter("oldName");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		
				
		//2.比较name和oldName 是否相同，若相同说明(name可用且不用进行判断重名)
		//2.1若不相同，调用CustomerDAO 的 getCountWithName(String name) 获取新的name在数据库中是否存在
		if(!oldName.equalsIgnoreCase(name)) {
			long count = customerDAO.getCountWithName(name);//name为新的且和oldName不相同
			//2.2若返回值大于0，则响应updatecustomer.jsp页面(通过转发的方式)：
			if(count > 0 ) {
				//2.2.1在updatecustomer.jsp页面显示一个错误消息：(用户名name已经被占用，请重新选择！)
				//在request中放入一个属性message字符串：(用户名name已经被占用，请重新选择！)，
				//在页面上通过request.getAttribute("message") 的方式来显示
				request.setAttribute("message", "用户名"+name+"已经被占用，请重新选择！");
				
				//2.2.2 updatecustomer.jsp的表单值可以回显。
				//address ，phone 显示提交表单的新的值，而name显示oldName，而不是新提交的name
				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				
				//2.2.3 结束方法：return
				return;
			}
		}
		//3.若验证通过，则把表单参数封装为一个Customer 对象customer
		Customer customer = new Customer(name,address,phone);
		customer.setId(Integer.parseInt(id));
		
		//4.调用CustomerDAO 的 update(Customer customer) 执行保存操作 
		customerDAO.update(customer);
		
		//5.重定向到query.do
		response.sendRedirect("query.do");
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String idStr = request.getParameter("id");
		int id = 0;
		
		try {
			id = Integer.parseInt(idStr);
			//System.out.println(id);
			customerDAO.delete(id);
		} catch (Exception e) { 
			//出错直接返回查询页面
		}
		
		//使用了重定向
		response.sendRedirect("query.do");
	}

	private void query(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//获得模糊查询的请求参数(在jsp页面上获取的)
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		
		//把请求参数封装为一个CriteriaCustomer对象
		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);
		
		//1.调用CustomerDAO的getforListWithCriteriaCustomer()得到Customer的集合
		List<Customer> customers = customerDAO.getforListWithCriteriaCustomer(cc);
		
		//2.把Customer 的集合放入request中
		request.setAttribute("customers", customers);
		
		//3.转发页面到index.jsp(不能使用重定向,因为需要将request里的集合传回去jsp页面中)
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
	}

	private void addCustomer(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//1.获取表单参数：name，address，phone
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		
		//2.检验name是否已经被占用：
		//2.1调用CustomerDAO 的 getCountWithName(String name) 获取name在数据库中是否存在
		long count = customerDAO.getCountWithName(name);
		
		//2.2若返回值大于0，则响应newcustomer.jsp页面(通过转发的方式)：
		if( count > 0 ) {
			//2.2.1要求在newcustomer.jsp页面显示一个错误消息：(用户名name已经被占用，请重新选择！)
			//在request中放入一个属性message字符串：(用户名name已经被占用，请重新选择！)，
			//在页面上通过request.getAttribute("message") 的方式来显示
			request.setAttribute("message", "用户名"+name+"已经被占用，请重新选择！");
			
			//2.2.2 newcustomer.jsp的表单值可以回显。
			//方式：value="<%= request.getParameter("name") == null ? "" : request.getParameter("name") %>"
			request.getRequestDispatcher("/newcustomer.jsp").forward(request, response);
			//2.2.3 结束方法:return
			return;
		}
		
		//3.把表单参数封装为一个Customer 对象customer
		Customer customer = new Customer(name,address,phone);
		
		//4.调用CustomerDAO 的 save(Customer customer) 执行保存操作 
		customerDAO.save(customer);
		
		//5.重定向到success.jsp页面 ：使用重定向可以避免出现表单的重复提交问题
		response.sendRedirect("success.jsp");
	}

}














