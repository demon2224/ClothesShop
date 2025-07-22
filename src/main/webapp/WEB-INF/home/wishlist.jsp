<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Clothes - Wishlist</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="assets\home\images\faviconn.png">

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
                                        <li>wishlist</li>
                                    </ul>

                                </div>
                            </div>
                        </div>
                    </div>
                    <!--breadcrumbs area end-->

                    <!--shopping cart area start -->
                    <div class="shopping_cart_area">
                        <form action="#">
                            <div class="row">
                                <div class="col-12">
                                    <div class="table_desc wishlist" style="max-height: 600px; overflow-y: auto;">
                                        <div class="cart_page table-responsive">
                                            <c:if test="${sessionScope.WISHLIST == null || sessionScope.WISHLIST.size() == 0}">
                                                <div style="text-align: center;">
                                                    <img  src="assets/home/img/cart/emptycart2.png" alt="Empty cart">
                                                </div>
                                            </c:if>
                                            <c:if test="${sessionScope.WISHLIST != null && sessionScope.WISHLIST.size() != 0}">
                                                <table>
                                                    <thead>
                                                        <tr><th class="product_thumb" style="min-width: 165px;">Image</th>
                                                            <th class="product_name" style="min-width: 280px;">Product</th>
                                                            <th class="product-price">Price</th>
                                                            <th class="product_quantity"style="min-width: 150px;">Stock Status</th>
                                                            <th class="product_total">Add To Cart</th>
                                                            <th class="product_remove">Delete</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${sessionScope.WISHLIST}" var="p">
                                                            <tr>
                                                                <td class="product_thumb"><a href="singleproduct?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a></td>
                                                                <td class="product_name"><a href="singleproductS?product_id=${p.id}">${p.name}</a></td>
                                                                <td class="product-price">${p.getFormattedSalePrice()}&#273;</td>
                                                                <c:if test="${p.status == true}">
                                                                    <td class="product_quantity">In Stock</td>
                                                                    <td class="product_total">
                                                                        <a href="cart?action=Add&product_id=${p.id}&quantity=1"
                                                                           style="display: block; width: 100%; background: #018576; color: #fff; padding: 10px 0; text-align: center; text-transform: capitalize; font-size: 13px; text-decoration: none;">
                                                                            <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                                        </a>
                                                                    </td>
                                                                </c:if>
                                                                <c:if test="${p.status == false}">
                                                                    <td class="product_quantity">Out of Stock</td>
                                                                    <td class="product_total">
                                                                        <a href="wishlist?action=Delete&product_id=${p.id}">Remove</a>
                                                                    </td>
                                                                </c:if>
                                                                <td class="product_remove"><a href="wishlist?action=Delete&product_id=${p.id}">X</a></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
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
        <!-- all js here -->

    </body>
</html>
