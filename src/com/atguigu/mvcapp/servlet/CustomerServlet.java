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

	//�����������ӿڱ�̣������е��ýӿڵķ����������ع��ľ����ʵ�֡�
	//�⽫�����ڴ���Ľ��ʹ�����и��õĿ���ֲ�ԺͿ���չ��
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
		//��ȡServletPath: /edit.do ��  /addCustomer.do
		String servletPath = request.getServletPath();
		
		//ȥ��/��.do�����������edit��addCustomer�������ַ���
		String methodName = servletPath.substring(1);
		methodName = methodName.substring(0, methodName.length()-3);
		
		//Ӧ���˷���
		try {
			//���÷����ȡmethodName��Ӧ�ķ���:edit��addCustomer��Ӧ��Method
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);
			//���÷�����ö�Ӧ�ķ���
			method.invoke(this, request,response);
		} catch (Exception e) {
			//e.printStackTrace();
			//��Ҫ���õķ��������ڣ���Ӧһ��error.jspҳ��
			response.sendRedirect("error.jsp");
		}
		
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String forwardPath = "/error.jsp";
				
		//1. ��ȡ�������id
		String idStr = request.getParameter("id");
		
		//2. ����CustomerDAO��customerDAO.get(id) ��ȡ��id��Ӧ��Customer����customer
		try {
			Customer customer = customerDAO.get(Integer.parseInt(idStr));
			if(customer != null) {
				forwardPath = "/updatecustomer.jsp";
				//3. ��customer����request��	
				request.setAttribute("customer", customer);
			}
		} catch (Exception e) {	}
			
		//4.��Ӧupdatecustomer.jspҳ�� (ת���ķ�ʽ)�� 
		request.getRequestDispatcher(forwardPath).forward(request, response);
	}
	
	private void update(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//1.��ȡ��������id��name��address��phone��oldName
		String id = request.getParameter("id");
		String oldName = request.getParameter("oldName");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		
				
		//2.�Ƚ�name��oldName �Ƿ���ͬ������ͬ˵��(name�����Ҳ��ý����ж�����)
		//2.1������ͬ������CustomerDAO �� getCountWithName(String name) ��ȡ�µ�name�����ݿ����Ƿ����
		if(!oldName.equalsIgnoreCase(name)) {
			long count = customerDAO.getCountWithName(name);//nameΪ�µ��Һ�oldName����ͬ
			//2.2������ֵ����0������Ӧupdatecustomer.jspҳ��(ͨ��ת���ķ�ʽ)��
			if(count > 0 ) {
				//2.2.1��updatecustomer.jspҳ����ʾһ��������Ϣ��(�û���name�Ѿ���ռ�ã�������ѡ��)
				//��request�з���һ������message�ַ�����(�û���name�Ѿ���ռ�ã�������ѡ��)��
				//��ҳ����ͨ��request.getAttribute("message") �ķ�ʽ����ʾ
				request.setAttribute("message", "�û���"+name+"�Ѿ���ռ�ã�������ѡ��");
				
				//2.2.2 updatecustomer.jsp�ı�ֵ���Ի��ԡ�
				//address ��phone ��ʾ�ύ�����µ�ֵ����name��ʾoldName�����������ύ��name
				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				
				//2.2.3 ����������return
				return;
			}
		}
		//3.����֤ͨ������ѱ�������װΪһ��Customer ����customer
		Customer customer = new Customer(name,address,phone);
		customer.setId(Integer.parseInt(id));
		
		//4.����CustomerDAO �� update(Customer customer) ִ�б������ 
		customerDAO.update(customer);
		
		//5.�ض���query.do
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
			//����ֱ�ӷ��ز�ѯҳ��
		}
		
		//ʹ�����ض���
		response.sendRedirect("query.do");
	}

	private void query(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//���ģ����ѯ���������(��jspҳ���ϻ�ȡ��)
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		
		//�����������װΪһ��CriteriaCustomer����
		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);
		
		//1.����CustomerDAO��getforListWithCriteriaCustomer()�õ�Customer�ļ���
		List<Customer> customers = customerDAO.getforListWithCriteriaCustomer(cc);
		
		//2.��Customer �ļ��Ϸ���request��
		request.setAttribute("customers", customers);
		
		//3.ת��ҳ�浽index.jsp(����ʹ���ض���,��Ϊ��Ҫ��request��ļ��ϴ���ȥjspҳ����)
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
	}

	private void addCustomer(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//1.��ȡ��������name��address��phone
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		
		//2.����name�Ƿ��Ѿ���ռ�ã�
		//2.1����CustomerDAO �� getCountWithName(String name) ��ȡname�����ݿ����Ƿ����
		long count = customerDAO.getCountWithName(name);
		
		//2.2������ֵ����0������Ӧnewcustomer.jspҳ��(ͨ��ת���ķ�ʽ)��
		if( count > 0 ) {
			//2.2.1Ҫ����newcustomer.jspҳ����ʾһ��������Ϣ��(�û���name�Ѿ���ռ�ã�������ѡ��)
			//��request�з���һ������message�ַ�����(�û���name�Ѿ���ռ�ã�������ѡ��)��
			//��ҳ����ͨ��request.getAttribute("message") �ķ�ʽ����ʾ
			request.setAttribute("message", "�û���"+name+"�Ѿ���ռ�ã�������ѡ��");
			
			//2.2.2 newcustomer.jsp�ı�ֵ���Ի��ԡ�
			//��ʽ��value="<%= request.getParameter("name") == null ? "" : request.getParameter("name") %>"
			request.getRequestDispatcher("/newcustomer.jsp").forward(request, response);
			//2.2.3 ��������:return
			return;
		}
		
		//3.�ѱ�������װΪһ��Customer ����customer
		Customer customer = new Customer(name,address,phone);
		
		//4.����CustomerDAO �� save(Customer customer) ִ�б������ 
		customerDAO.save(customer);
		
		//5.�ض���success.jspҳ�� ��ʹ���ض�����Ա�����ֱ����ظ��ύ����
		response.sendRedirect("success.jsp");
	}

}














