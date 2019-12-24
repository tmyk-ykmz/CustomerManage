
<%@ page import="customer.*"%>
<%@ page import="user.*"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean class="customer.CustomerBean" id="customer" scope="session" />
<jsp:useBean class="customer.CustomerListBean" id="customerList"
    scope="session" />
<!doctype html>
<html>
<head>
<title>顧客管理</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>顧客管理</h1>
    <div class="main">
        <h2>削除確認</h2>
        <table>
            <tr>
                <td>氏名</td>
                <td class="title"><%=customer.getName()%></td>
            </tr>
            <tr>
                <td>郵便番号</td>
                <td class="title"><%=customer.getZip()%></td>
            </tr>
            <tr>
                <td>住所１</td>
                <td class="title"><%=customer.getAddress1()%></td>
            </tr>
            <tr>
                <td>住所２</td>
                <td class="title"><%=customer.getAddress2()%></td>
            </tr>
            <tr>
                <td>TEL</td>
                <td class="title"><%=customer.getTel()%></td>
            </tr>
            <tr>
                <td>FAX</td>
                <td class="title"><%=customer.getFax()%></td>
            </tr>
            <tr>
                <td>E-mail</td>
                <td class="title"><%=customer.getEmail()%></td>
            </tr>
            <%--             <%
                while (customerList.hasNext()) {
                    CustomerBean ancustomer = customerList.getNext();
            %>
            <tr>
                <td class="center"><%=ancustomer.getName()%></td>
                <td><%=ancustomer.getZip() %></td>
                <td><%=ancustomer.getAddress1() %></td>
                <td><%=ancustomer.getAddress2() %></td>
                <td><%=ancustomer.getTel() %></td>
                <td><%=ancustomer.getFax() %></td>
                <td><%=ancustomer.getEmail() %></td>
             </tr>
            <%
                }
            %>
--%>
        </table>
        <form action="CustomerServlet" method="post">
            <p>
                <button name="state" value="ok" formaction="CustomerServlet">OK</button>
                <input type="button" value="戻る" onclick="history.back()">
            </p>
        </form>
    </div>
</body>