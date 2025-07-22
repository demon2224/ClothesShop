<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Clothes-Cart</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="assets/home/img/faviconn.png">

        <!-- all css here -->
        <%@include file="/WEB-INF/include/add_css.jsp"%>
    </head>
    <body>
        <!--pos page start-->
        <div class="pos_page">
            <div class="container">  
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
                                        <li>Shopping Cart</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--breadcrumbs area end-->

                    <!--shopping cart area start -->
                    <div class="shopping_cart_area" id="shopping-cart-area">
                        <div class="row">
                            <div class="col-12">
                                <div class="table_desc" style="max-height: 600px; overflow-y: auto;">
                                    <div class="cart_page table-responsive">
                                        <c:if test="${sessionScope.CART == null || sessionScope.CART.size() == 0}">
                                            <div style="text-align: center;">
                                                <img  src="assets/home/img/cart/emptycart1.png" alt="Empty cart">
                                            </div>
                                        </c:if>
                                        <c:if test="${sessionScope.CART != null && sessionScope.CART.size() != 0}">
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th class="product_thumb" style="min-width: 165px;">Image</th>
                                                        <th class="product_name" style="min-width: 280px;">Product</th>
                                                        <th class="product-price">Price</th>
                                                        <th class="product_quantity" style="min-width: 150px;">Quantity</th>
                                                        <th class="product_total">Total</th>
                                                        <th class="product_remove">Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${sessionScope.CART}" var="c">
                                                        <tr>
                                                            <td class="product_thumb">
                                                                <a href="singleproduct?product_id=${c.product.id}">
                                                                    <img src="${c.product.images[0]}" alt="Photo">
                                                                </a>
                                                            </td>    
                                                            <td class="product_name">
                                                                <a href="singleproduct?product_id=${c.product.id}">${c.product.name}</a>
                                                            </td>                                                            
                                                            <td class="product-price">${c.product.getFormattedSalePrice()}đ</td>
                                                            <td class="product_quantity">
                                                                <!-- Di chuyển input vào trong form -->
                                                                <form action="cart" method="post">
                                                                    <input type="hidden" name="action" value="Update">
                                                                    <input type="hidden" name="product_id" value="${c.product.id}">
                                                                    <input id="input-quantity-${c.product.id}" name="quantity" min="0" max="${c.product.stock}" value="${c.quantity}" type="number" style="width: 70px;">
                                                                    <h6 id="error-stock-${c.product.id}" style="color: red"></h6>
                                                                    <button type="submit" style="color: #00bba6; display: inline-block; border: none; border-radius: 4px; padding: 2px 11px; margin-right: 10px; font-weight: 600;">
                                                                        Update
                                                                    </button>
                                                                </form>
                                                            </td>
                                                            <td class="product_total">
                                                                <fmt:formatNumber value="${c.product.getSalePrice() * c.quantity}" type="number" groupingUsed="true" />&#273;
                                                            </td>
                                                            <td class="product_remove">
                                                                <!-- Form for deleting product from cart -->
                                                                <form action="cart" method="post">
                                                                    <input type="hidden" name="action" value="Delete">
                                                                    <input type="hidden" name="product_id" value="${c.product.id}">
                                                                    <button type="submit" style="color: #00bba6; display: inline-block; border: none; border-radius: 4px; padding: 2px 11px; font-weight: 600;">
                                                                        Delete
                                                                    </button>
                                                                </form>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>   
                                        </c:if>
                                    </div>  
                                </div>
                            </div>
                        </div>

                        <!--coupon code area start-->
                        <div class="coupon_area">
                            <div class="row">
                                <div class="col-lg-12 col-md-12">
                                    <div class="coupon_code">
                                        <h3>Cart Totals</h3>
                                        <div class="coupon_inner">
                                            <div class="cart_subtotal">
                                                <p>Subtotal</p>
                                                <p class="cart_amount">
                                                    <c:set var="totalPrice" value="0" />
                                                    <c:forEach items="${sessionScope.CART}" var="c">
                                                        <c:set var="productTotal" value="${c.product.getSalePrice() * c.quantity}" />
                                                        <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                                    </c:forEach>
                                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" />&#273;
                                                </p>
                                            </div>
                                            <div class="cart_subtotal ">
                                                <p>Shipping</p>
                                                <p class="cart_amount"><span></span> Free Ship</p>
                                            </div>
                                            <div class="cart_subtotal">
                                                <p>Total</p>
                                                <p class="cart_amount">
                                                    <c:set var="totalPrice" value="0" />
                                                    <c:forEach items="${sessionScope.CART}" var="c">
                                                        <c:set var="productTotal" value="${c.product.getSalePrice() * c.quantity}" />
                                                        <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                                    </c:forEach>
                                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" />&#273;
                                                </p>
                                            </div>
                                            <div class="checkout_btn">
                                                <a href="checkout">Checkout</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--coupon code area end-->
                    </div>
                    <!--shopping cart area end -->
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
