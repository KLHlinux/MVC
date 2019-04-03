<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

		<c:if test="${requestScope.message != null }">
			<br>
			<font color="red"> ${requestScope.message } </font>
			<br>
			<br>
		</c:if>
		
		
		<!--第一次edit()返回不是空的，第二次提交update()返回是一个空值
			(因为提交表单会创建一个新的request，清空了Attribute里的内容)-->
		<c:set var="id" value="${customer !=null ? customer.id : param.id }"></c:set>
		<c:set var="oldname" value="${customer !=null ? customer.name : param.oldName }"></c:set>
		<c:set var="name" value="${customer !=null ? customer.name : param.oldName }"></c:set>
		<c:set var="address" value="${customer !=null ? customer.address : param.address }"></c:set>
		<c:set var="phone" value="${customer !=null ? customer.phone : param.phone }"></c:set>
		
		

		<form action="update.do" method="post">
		
			<!-- 使用隐藏域来保存要修改的Customer对象的id和oldName -->
			<input type="hidden" name="id" value="${id }" />
			<input type="hidden" name="oldName" value="${oldname }" />
		
			<table>
				<tr>
					<td>--Name:</td>
					<td><input type="text" name="name"
					 value="${name }" /></td>
				</tr>
				<tr>
					<td>Address:</td>
					<td><input type="text" name="address"
					 value="${address }" /></td>
				</tr>
				<tr>
					<td>Phone:</td>
					<td><input type="text" name="phone"
					 value="${phone }" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Update" /></td>
				</tr>	
			</table>
		</form>
</body>
</html>

