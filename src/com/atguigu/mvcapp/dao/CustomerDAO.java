package com.atguigu.mvcapp.dao;

import java.util.List;

import com.atguigu.mvcapp.domin.Customer;

public interface CustomerDAO {
	
	/**
	 * ��������������List
	 * @param cc����װ�˲�ѯ����
	 * @return
	 */
	public List<Customer> getforListWithCriteriaCustomer(CriteriaCustomer cc);
	
	public List<Customer> getAll();
	
	public void save(Customer customer);
	
	public Customer get(Integer id);
	
	public void delete(Integer id);
	
	public void update(Customer customer);
	
	/**
	 * ���غ�name��ȵļ�¼��
	 * @param name
	 * @return
	 */
	public long getCountWithName(String name);
	
}
