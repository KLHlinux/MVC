package com.atguigu.mvcapp.test;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.atguigu.mvcapp.dao.CriteriaCustomer;
import com.atguigu.mvcapp.dao.CustomerDAO;
import com.atguigu.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.atguigu.mvcapp.domin.Customer;

class CustomerDAOJdbcImplTest {

	private CustomerDAO customerDAO = 
			new CustomerDAOJdbcImpl();
	@Test
	void testgetforListWithCriteriaCustomer() {
		CriteriaCustomer cc = new CriteriaCustomer("j",null,null);
		List<Customer> customers = customerDAO.getforListWithCriteriaCustomer(cc);
		System.out.println(customers);
	}
	
	@Test
	void testGetAll() {
		List<Customer> customers = customerDAO.getAll();
		System.out.println(customers);
	}

	@Test
	void testSave() {
		Customer customer = new Customer();
		customer.setName("Mart2");
		customer.setAddress("BeiJing");
		customer.setPhone("13130012811");
		
		customerDAO.save(customer);
	}

	@Test
	void testGetInteger() {
		Customer customer = customerDAO.get(1);
		System.out.println(customer);
	}

	@Test
	void testDelete() {
		customerDAO.delete(5);
	}

	@Test
	void testGetCountWithName() {
		long count = customerDAO.getCountWithName("Jerry");
		System.out.println(count);
	}

}
