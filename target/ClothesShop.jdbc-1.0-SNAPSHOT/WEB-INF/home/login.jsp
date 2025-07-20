<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Clothes_Login</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="assets\home\img\faviconn.png">

        <!-- all css here -->
        <%@include file="/WEB-INF/include/add_css.jsp"%>
    </head>
    <body>
        <!-- Add your site or application content here -->

        <!--pos page start-->
        <div class="pos_page">
            <div class="container">
                <!--pos page inner-->
                <div class="pos_page_inner">
                    <!--header area -->
                    <%@include file="/WEB-INF/include/header.jsp" %>
                    <!--header end -->
                    <!--breadcrumbs area start-->
                    <div class="breadcrumbs_area">
                        <div class="row">
                            <div class="col-12">
                                <div class="breadcrumb_content">
                                    <ul>
                                        <li><a href="home">home</a></li>
                                        <li><i class="fa fa-angle-right"></i></li>
                                        <li>login</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--breadcrumbs area end-->

                    <!-- customer login start -->
                    <div class="customer_login">
                        <div class="row">
                            <!--login area start-->
                            <div class="col-lg-6 col-md-6">
                                <div class="account_form">
                                    <h2>login</h2>
                                    <form action="login" method="post">
                                        <p>
                                            <label>Username or email</label><br/>
                                            <input type="text" name="txtUsername" value="${requestScope.uName}"/>
                                        <h7 style="color: red">${requestScope.msg}</h7>
                                        </p>
                                        <p style="position: relative">
                                            <label>Passwords </label>
                                            <input type="password" name="txtPassword" value=""/>
                                        </p>
                                        <p>
                                        </p>
                                        <div  style="margin-top: 10px" class="login_submit">
                                            <button type="submit" name="btnAction" value="Login">login</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <!--login area start-->

                            <!--register area start-->
                            <div class="col-lg-6 col-md-6">
                                <div class="account_form register">
                                    <h2>Register</h2>
                                    <form action="register" method="post">
                                        <h6 style="color: red">${requestScope.ERROR}</h6>
                                        <h6 style="color: green">${requestScope.SUCCESS}</h6>
                                        <div style="display: flex; justify-content: space-between">
                                            <p style="width: 45%">
                                                <label>First Name</label>
                                                <input name="firstname" type="text" required>
                                            </p>
                                            <p style="width: 45%">
                                                <label>Last Name</label>
                                                <input name="lastname" type="text" required>
                                            </p>
                                        </div>
                                        <p>
                                            <label>Username</label>
                                            <input name="username" type="text" required/>
                                        </p>
                                        <p style="position: relative">
                                            <label>Passwords</label>
                                            <input name="password" type="password" required>
                                        </p>
                                        <p>
                                            <label>Email</label>
                                            <input name="email" type="email" required>
                                        </p>
                                        <div class="login_submit">
                                            <button name="btnAction" value="Register" type="submit">Register</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <!--register area end-->
                        </div>
                    </div>
                    <!-- customer login end -->

                </div>
                <!--pos page inner end-->
            </div>
            <!--pos page end-->
        </div>
        <!--pos page end-->
        <%@ include file="/WEB-INF/include/footer.jsp" %>
    </body>
</html>

