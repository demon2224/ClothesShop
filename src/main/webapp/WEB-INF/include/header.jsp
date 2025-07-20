<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .shopping_cart {
        position: relative;
    }
    .shopping_cart:hover .mini_cart {
        display: block !important;
    }
    /* Add transition for smooth effect */
    .mini_cart {
        transition: all 0.3s ease;
    }
    /* Fix cart icon alignment */
    .header_right_info {
        display: flex;
        align-items: center;
        justify-content: flex-end;
    }
    .header_right_info .search_bar {
        margin-right: 20px;
    }
    .header_right_info .shopping_cart {
        display: flex;
        align-items: center;
    }
    .header_right_info .shopping_cart a {
        display: flex;
        align-items: center;
        text-decoration: none;
        color: inherit;
    }
</style>

<div class="header_area">
    <!--header top-->
    <div class="header_top">
        <div class="row align-items-center">
            <div class="col-lg-6 col-md-6">
            </div>
            <div class="col-lg-6 col-md-6">
                <div class="header_links">
                    <ul>
                        <!-- Menu chỉ cho User và Admin -->
                        <c:if test="${sessionScope.account != null}">
                            <li><a href="wishlist" title="wishlist">My wishlist</a></li>
                            <li><a href="cart" title="My cart">My cart</a></li>
                            </c:if>

                        <!-- Menu cho Guest -->
                        <c:if test="${sessionScope.account == null}">
                            <li><a href="home?btnAction=Login" title="Login">Login</a></li>
                            </c:if>

                        <!-- Menu cho User và Admin đã đăng nhập -->
                        <c:if test="${sessionScope.account != null}">
                            <!-- Menu đặc biệt cho Admin -->
                            <c:if test="${sessionScope.account.roleID == 1}">
                                <li><a href="admin" title="Admin Dashboard">Admin Dashboard</a></li>
                                </c:if>
                            <li><a href="profile">Hello, ${sessionScope.account.firstName} ${sessionScope.account.lastName}!</a></li>
                            <li><a href="home?btnAction=Logout">Logout</a></li>
                            </c:if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!--header top end-->

    <!--header middel-->
    <div class="header_middel">
        <div class="row align-items-center">
            <!--logo start-->
            <div class="col-lg-3 col-md-3">
                <div class="logo">
                    <a href="home"><img src="assets\home\img\logo\logo.jpg.png" alt=""></a>
                </div>
            </div>
            <!--logo end-->
            <div class="col-lg-9 col-md-9">
                <div class="header_right_info">
                    <div class="search_bar">
                        <form action="home" method="get" >
                            <input name="txtSearch" value="" placeholder="Search..." type="text">
                            <button name="btnAction" value="Search" type="submit"><i class="fa fa-search"></i></button>
                        </form>
                    </div>
                    <div class="shopping_cart" id="cart-icon">
                        <!-- Chỉ hiện mini cart cho User và Admin -->
                        <c:if test="${sessionScope.account != null}">
                            <c:if test="${sessionScope.CART != null && sessionScope.CART.size() != 0}">
                                <a href="#"><i class="fa fa-shopping-cart"></i> ${sessionScope.CART.size()} Items <i class="fa fa-angle-down"></i></a>
                                </c:if>
                                <c:if test="${sessionScope.CART == null || sessionScope.CART.size() == 0}">
                                <a href="#"><i class="fa fa-shopping-cart"></i><i class="fa fa-angle-down"></i></a>
                                    </c:if>

                            <!--mini cart-->
                            <div class="mini_cart">
                                <c:forEach items="${sessionScope.CART}" var="c">
                                    <div class="cart_item">
                                        <div class="cart_img">
                                            <a href="singleproduct?product_id=${c.product.id}"><img src="${c.product.images[0]}" alt=""></a>
                                        </div>
                                        <div class="cart_info">
                                            <a href="singleproduct?product_id=${c.product.id}">${c.product.name}</a>
                                            <span class="cart_price">${c.product.getSalePrice()}&#273;</span>
                                            <span class="quantity">Quantity: ${c.quantity}</span>
                                        </div>
                                        <div class="cart_remove">
                                            <a title="Remove this item" href="cart?action=Delete&product_id=${c.product.id}&curPage=header.jsp"><i class="fa fa-times-circle"></i></a>
                                        </div>
                                    </div>
                                </c:forEach>
                                <div class="total_price">
                                    <span>Total</span>
                                    <span class="prices">
                                        <c:set var="totalPrice" value="0" />
                                        <c:forEach items="${sessionScope.CART}" var="c">
                                            <c:set var="productTotal" value="${c.product.getSalePrice() * c.quantity}" />
                                            <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                        </c:forEach>
                                        ${totalPrice}&#273;
                                    </span>
                                </div>
                                <div class="cart_button">
                                    <a href="checkout">Check out</a>
                                </div>
                            </div>
                            <!--mini cart end-->
                        </c:if>

                        <!-- Hiện icon cart đơn giản cho Guest -->
                        <c:if test="${sessionScope.account == null}">
                            <a href="home?btnAction=Login" title="Login to use cart"><i class="fa fa-shopping-cart"></i></a>
                            </c:if>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!--header middel end-->
    <div class="header_bottom">
        <div class="row">
            <div class="col-12">
                <div class="main_menu_inner">
                    <div class="main_menu d-none d-lg-block">
                        <nav>
                            <ul>
                                <li class="${requestScope.CURRENTSERVLET == "Home" ? "active" : ""}"><a href="home">Home</a></li>
                                <li class="${requestScope.CURRENTSERVLET == "Shop" ? "active" : ""}"><a href="shop">SHOP</a></li>
                                <li class="${requestScope.CURRENTSERVLET == "Contact" ? "active" : ""}"><a href="contact">contact us</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>