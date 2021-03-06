<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="customer.*"%>
<jsp:useBean class="user.UserBean" id="user" scope="session" />
<!doctype html>
<html>
<head>
<title>顧客管理（CSV）</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>顧客管理（CSV）</h1>
    <div class="main">
        <h2>CSV操作</h2>
        <form name="form1" action="CustomerCsvServlet" method="post"
            onsubmit="return funcConfirm1()">
            <p>
                <button name="state" value="read">一括処理</button>
                <input type="file" name="filepath1">
            </p>
        </form>
        <form name="form2" action="CustomerCsvServlet" method="post"
            onsubmit="return funcConfirm2()">
            <p>
                <button name="state" value="write">エクスポート</button>
                <input type="file" name="filepath2">
            </p>
        </form>
        <form action="LoginServlet" method="post">
            <p>
                <button name="state" value="top">メニュー画面</button>
            </p>
        </form>
    </div>
</body>

<script type="text/javascript">
    function funcConfirm1() {
        if (document.form1.filepath1.value == "") {
            alert("一括処理のファイルが選択されていません。");
            return false;
        }
    }
    function funcConfirm2() {
        if (document.form2.filepath2.value == "") {
            alert("エクスポートのファイルが選択されていません。");
            return false;
        }
    }
</script>
</html>
