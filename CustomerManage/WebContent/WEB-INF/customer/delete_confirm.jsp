
<%@ page import="customer.*"%>
<%@ page import="user.*"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean class="customer.CustomerBean" id="customer" scope="session" />
<jsp:useBean class="customer.CustomerListBean" id="customerList" scope="request" />
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
                <th>氏名</th>
                <th>郵便番号</th>
                <th>住所１</th>
                <th>住所２</th>
                <th>TEL</th>
                <th>FAX</th>
                <th>E-mail</th>
            </tr>
            <%
                while (customerList.hasNext()) {
                    CustomerBean anCustomer = customerList.getNext();
            %>
            <tr>
                <td class="center"><%=anCustomer.getName()%></td>
                <td><%=anCustomer.getZip() %></td>
                <td><%=anCustomer.getAddress1() %></td>
                <td><%=anCustomer.getAddress2() %></td>
                <td><%=anCustomer.getTel() %></td>
                <td><%=anCustomer.getFax() %></td>
                <td><%=anCustomer.getEmail() %></td>
             </tr>
            <%
                }
            %>
        </table>
        <form action="LoginServlet" method="post">
            <p>
                <button name="state" value="ok" formaction="CustomerServlet">OK</button>
                <input type="button" value="戻る" onclick="history.back()">
            </p>
        </form>


        <!-- ｢削除確認｣画面 -->

    </div>
</body>