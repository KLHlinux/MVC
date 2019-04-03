<%@page import="com.atguigu.mvcapp.domin.Customer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
		<%
			Object msg = request.getAttribute("message");
			if(msg != null){
		%>
			<br>
			<font color="red"> <%= msg %> </font>
			<br>
			<br>
		<%
			}
			
			String id = null;
			String oldName = null;
			String name = null;
			String address = null;
			String phone = null;
			
			//第一次edit()返回不是空的，第二次提交update()返回是一个空值
			//(因为提交表单会创建一个新的request，清空了Attribute里的内容)
			Customer customer = (Customer)request.getAttribute("customer");
			if(customer != null){
				id = customer.getId() +"";
				oldName = customer.getName();
				name = customer.getName();
				address = customer.getAddress();
				phone = customer.getPhone();
			}else{
				id = request.getParameter("id");
				oldName = request.getParameter("oldName");
				name = request.getParameter("oldName");
				address = request.getParameter("address");
				phone = request.getParameter("phone");
			}
			
		%>

		<form action="update.do" method="post">
		
			<!-- 使用隐藏域来保存要修改的Customer对象的id和oldName -->
			<input type="hidden" name="id" value="<%= id %>" />
			<input type="hidden" name="oldName" value="<%= oldName %>" />
		
			<table>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"
					 value="<%= name %>" /></td>
				</tr>
				<tr>
					<td>Address:</td>
					<td><input type="text" name="address"
					 value="<%= address %>" /></td>
				</tr>
				<tr>
					<td>Phone:</td>
					<td><input type="text" name="phone"
					 value="<%= phone %>" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Update" /></td>
				</tr>	
			</table>
		</form>
</body>
</html>

