<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Thanh toán</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="assets\home\img\faviconn.png">

        <!-- all css here -->
        <%@include file="/WEB-INF/include/add_css.jsp"%>
    </head>
    <body>
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
                                        <li>Thanh toán</li>
                                    </ul>

                                </div>
                            </div>
                        </div>
                    </div>
                    <!--breadcrumbs area end-->

                    <!--Checkout page section-->
                    <div class="Checkout_section">
                        <div class="row">
                            <div class="col-12">
                                <div class="user-actions mb-20">
                                    <c:if test="${sessionScope.account == null}">
                                        <h3> 
                                            <i class="fa fa-file-o" aria-hidden="true"></i>
                                            Để thanh toán đơn hàng, bạn cần đăng nhập >>
                                            <a class="Returning" href="home?btnAction=Login">Nhấn vào đây để đăng nhập</a>     
                                        </h3>
                                    </c:if>
                                    <c:if test="${sessionScope.account != null}">
                                        <h3> 
                                            <i class="fa fa-file-o" aria-hidden="true"></i>
                                            Chỉnh sửa thông tin >>
                                            <a class="Returning" href="profile">Tài khoản của tôi</a>     
                                        </h3>
                                    </c:if>
                                </div>
                            </div>
                            <div class="checkout_form">
                                <div class="row">
                                    <div class="col-lg-6 col-md-6" style="padding: 0 36px;">
                                        <c:if test="${requestScope.MESSAGE != null}">
                                            <h3 style="color: ${requestScope.CHECK == 'true' ? 'green': 'red'}">
                                                ${requestScope.MESSAGE}
                                            </h3>
                                        </c:if>
                                        <form style="padding: 20px; border: 1px black solid;" action="checkout" method="POST">
                                            <h3>Thông tin thanh toán</h3>
                                            <div class="row">
                                                <div class="col-lg-6 mb-30">
                                                    <label>Họ <span>*</span></label>
                                                    <input type="text" name="firstName" value="${sessionScope.account != null ? sessionScope.account.firstName: ''}" readonly>    
                                                </div>
                                                <div class="col-lg-6 mb-30">
                                                    <label>Tên <span>*</span></label>
                                                    <input type="text" name="lastName" value="${sessionScope.account != null ? sessionScope.account.lastName: ''}" readonly> 
                                                </div>
                                                <div class="col-12 mb-30">
                                                    <label>Email</label>
                                                    <input type="email" name="email" value="${sessionScope.account != null ? sessionScope.account.email: ''}" readonly>     
                                                </div>
                                                <div class="col-12 mb-30">
                                                    <label>Địa chỉ <span>*</span></label>
                                                    <input placeholder="Số nhà và tên đường" 
                                                           type="text" 
                                                           name="address" 
                                                           value="${sessionScope.account != null ? sessionScope.account.address: ''}" 
                                                           required>     
                                                </div>
                                                <div class="col-lg-6 mb-30">
                                                    <label>Số điện thoại<span>*</span></label>
                                                    <input type="tel" 
                                                           name="phone" 
                                                           value="${sessionScope.account != null ? sessionScope.account.phone: ''}" 
                                                           pattern="[0-9]{10}" 
                                                           required 
                                                           title="Vui lòng nhập số điện thoại 10 chữ số"> 
                                                </div> 
                                                <div class="col-lg-6 mb-30">
                                                    <label>Email<span>*</span></label>
                                                    <input type="email" 
                                                           name="emailConfirm" 
                                                           value="${sessionScope.account != null ? sessionScope.account.email: ''}" 
                                                           readonly> 
                                                </div>
                                            </div>
                                        </form>    
                                    </div>
                                    <div class="col-lg-6 col-md-6" style="padding: 0 36px;">
                                        <c:if test="${sessionScope.CART != null && sessionScope.CART.size() > 0 }">
                                            <form action="checkout" method="GET">    
                                                <h3>Đơn hàng của bạn</h3> 
                                                <div class="order_table table-responsive mb-30">
                                                    <table>
                                                        <thead>
                                                            <tr>
                                                                <th>Sản phẩm</th>
                                                                <th>Tổng tiền</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach items="${sessionScope.CART}" var="c">
                                                                <tr>
                                                                    <td>${c.product.name}<strong> × ${c.quantity}</strong></td>
                                                                    <td> ${c.product.getSalePrice() * c.quantity}đ</td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                        <tfoot>
                                                            <tr>
                                                                <th>Tổng giỏ hàng</th>
                                                                <td>
                                                                    <c:set var="totalPrice" value="0" />
                                                                    <c:forEach items="${sessionScope.CART}" var="c">
                                                                        <c:set var="productTotal" value="${c.product.getSalePrice() * c.quantity}" />
                                                                        <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                                                    </c:forEach>
                                                                    ${totalPrice}đ
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>Phí vận chuyển</th>
                                                                <td><strong>Miễn phí</strong></td>
                                                            </tr>
                                                            <tr class="order_total">
                                                                <th>Tổng thanh toán</th>
                                                                <td><strong>
                                                                        <c:set var="totalPrice" value="0" />
                                                                        <c:forEach items="${sessionScope.CART}" var="c">
                                                                            <c:set var="productTotal" value="${c.product.getSalePrice() * c.quantity}" />
                                                                            <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                                                        </c:forEach>
                                                                        ${totalPrice}đ  
                                                                    </strong>
                                                                </td>
                                                            </tr>
                                                        </tfoot>
                                                    </table>     
                                                </div>
                                                <div class="payment_method">
                                                    <h3>PHƯƠNG THỨC THANH TOÁN</h3>
                                                    <c:forEach items="${requestScope.PAYMENTS}" var="p">
                                                        <div class="panel-default">
                                                            <input id="payment_${p.paymentID}" 
                                                                   name="check_method" 
                                                                   type="radio" 
                                                                   value="${p.paymentID}"
                                                                   onclick="handlePaymentSelection('${p.paymentMethod}')" 
                                                                   checked>
                                                            <label for="payment_${p.paymentID}">${p.paymentMethod}</label>
                                                        </div> 
                                                    </c:forEach>
                                                </div> 
                                                <c:if test="${sessionScope.CART != null && sessionScope.CART.size() > 0}">
                                                    <c:if test="${sessionScope.account != null && sessionScope.account.roleID == 2}">
                                                        <div class="order_button">
                                                            <button type="submit">Thanh toán</button> 
                                                        </div>    
                                                    </c:if>
                                                </c:if>
                                            </form>         
                                        </c:if>
                                        <c:if test="${sessionScope.CART == null || sessionScope.CART.size() == 0}">
                                            <div style="text-align: center;">
                                                <img  src="assets/home/img/cart/emptycart1.png" alt="Empty cart">
                                            </div>
                                            <div class="order_button">
                                                <form action="home" method="GET">
                                                    <button type="submit">Mua sắm ngay</button> 
                                                </form>
                                            </div>   
                                        </c:if>
                                    </div>
                                </div> 
                            </div>        
                        </div>
                        <!--Checkout page section end-->
                    </div>
                    <!--pos page inner end-->
                </div>
            </div>
            <!--pos page end-->
            <!--footer area start-->
            <%@ include file="/WEB-INF/include/footer.jsp" %>
            <!--footer area end-->
    </body>
</html>

