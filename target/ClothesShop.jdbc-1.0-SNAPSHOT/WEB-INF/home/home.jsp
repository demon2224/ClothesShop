<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Clothes - Shop</title>
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

                    <!--pos home section-->
                    <div class=" pos_home_section">
                        <div class="row pos_home">
                            <div class="col-lg-3 col-md-8 col-12">
                                <!--sidebar banner-->
                                <div class="sidebar_widget banner mb-35">
                                    <div class="banner_img mb-35">
                                        <a href="#"><img src="assets\home\img\banner\banner5.jpg" alt=""></a>
                                    </div>
                                    <div class="banner_img">
                                        <a href="#"><img src="assets\home\img\banner\banner6.jpg" alt=""></a>
                                    </div>
                                </div>
                                <!--sidebar banner end-->

                                <!--wishlist block start-->
                                <!-- Chỉ hiển thị wishlist cho User và Admin đã đăng nhập -->
                                <c:if test="${sessionScope.account != null && sessionScope.WISHLIST != null}">
                                    <!--wishlist block start-->
                                    <div class="sidebar_widget wishlist mb-35" id="wishlist-small">
                                        <div class="block_title">
                                            <h3><a href="wishlist">Wishlist</a></h3>
                                        </div>
                                        <c:forEach items="${sessionScope.WISHLIST}" var="p" varStatus="loop">
                                            <c:if test="${loop.index <= 2}">
                                                <div class="cart_item">
                                                    <div class="cart_img">
                                                        <a href="singleproduct?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a>
                                                    </div>
                                                    <div class="cart_info">
                                                        <a href="singleproduct?product_id=${p.id}">${p.name}</a>
                                                        <span class="cart_price">$${p.salePrice}</span>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                        <div class="block_content">
                                            <p>${sessionScope.WISHLIST.size()}  products</p>
                                            <a href="wishlist">» My wishlists</a>
                                        </div>
                                    </div>
                                </c:if>
                                <!--wishlist block end-->
                                <!--sidebar banner-->
                                <div class="sidebar_widget bottom ">
                                    <div class="banner_img">
                                        <a href="#"><img src="assets\home\img\banner\banner9.jpg" alt=""></a>
                                    </div>
                                </div>
                                <!--sidebar banner end-->
                            </div>
                            <div class="col-lg-9 col-md-12">
                                <!--banner slider start-->
                                <div class="banner_slider slider_1">
                                    <!-- Static banner instead of carousel -->
                                    <div class="single_slider" style="background-image: url(assets/home/img/slider/slide_2.png)">
                                        <div class="slider_content">
                                            <div class="slider_content_inner">
                                                <h1>Men's Fashion</h1>
                                                <p>Thời trang, phong cách trẻ trung. </p>
                                                <a href="shop" class="button-shop fixed-shop-btn" style="z-index: 999; position: relative; pointer-events: auto;">Shop now</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--banner slider start-->

                                <!--new product area start-->
                                <div class="new_product_area">
                                    <div class="block_title">
                                        <h3>Mẫu mới 2025</h3>
                                    </div>
                                    <div class="row">
                                        <c:if test="${requestScope.LIST_PRODUCTS_NEW != null}">
                                            <c:forEach var="p" items="${requestScope.LIST_PRODUCTS_NEW}">
                                                <div class="col-lg-3 col-md-6">
                                                    <div class="single_product">
                                                        <div class="product_thumb">
                                                            <a href="singleproduct?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a>
                                                            <div class="img_icone">
                                                                <img src="assets/home/img/cart/span-new.png" alt="">
                                                            </div>
                                                            <c:if test="${p.discount != 0}">
                                                                <span class="discount">Up to ${p.discount * 100}%</span>
                                                            </c:if>
                                                            <div class="product_action">
                                                                <!-- Chỉ cho phép User và Admin thêm vào giỏ hàng -->
                                                                <c:if test="${sessionScope.account != null}">
                                                                    <form action="cart" method="post">
                                                                        <input type="hidden" name="action" value="Add">
                                                                        <input type="hidden" name="product_id" value="${p.id}">
                                                                        <input type="hidden" name="quantity" value="1">
                                                                        <button type="submit" style="display: block; border: none; width: 100%; background: #018576; color: #fff; padding: 7px 0; text-transform: capitalize; font-size: 13px;">
                                                                            <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                                <!-- Guest sẽ được yêu cầu đăng nhập -->
                                                                <c:if test="${sessionScope.account == null}">
                                                                    <a href="home?btnAction=Login" style="display: block; border: none; width: 100%; background: #018576; color: #fff; padding: 7px 0; text-transform: capitalize; font-size: 13px; text-decoration: none; text-align: center;">
                                                                        <i class="fa fa-shopping-cart"></i> Đăng nhập để mua
                                                                    </a>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                        <div class="product_content">
                                                            <div style="display: flex; justify-content: center">
                                                                <c:if test="${p.price != p.salePrice}">
                                                                    <span style="margin-right: 10px; font-weight: 400;" class="old_price" id="oldprice">${p.price}&#273;</span>
                                                                </c:if>
                                                                <span class="current_price">${p.salePrice}đ</span>
                                                            </div>
                                                            <h3 class="product_title"><a href="singleproduct?product_id=${p.id}">${p.name}</a></h3>
                                                        </div>
                                                        <div class="product_info">
                                                            <ul>
                                                                <li>
                                                                    <!-- Chỉ cho phép User và Admin thêm vào wishlist -->
                                                                    <c:if test="${sessionScope.account != null}">
                                                                        <form action="wishlist" method="post">
                                                                            <input type="hidden" name="action" value="Add">
                                                                            <input type="hidden" name="product_id" value="${p.id}">
                                                                            <button type="submit" style="color: red; border: none; border-radius: 4px; font-size: 13px; padding: 2px 11px; font-weight: 600;">Yêu thích</button>
                                                                        </form>
                                                                    </c:if>
                                                                </li>
                                                                <!-- Guest chỉ có thể xem sản phẩm -->
                                                                <li><a href="singleproduct?product_id=${p.id}" title="View Detail">Xem sản phẩm</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                    </div>
                                </div>
                                <!--new product area start-->

                                <!--featured product start-->
                                <div class="featured_product">
                                    <div class="block_title">
                                        <h3>Bán chạy nhất</h3>
                                    </div>
                                    <div class="row">
                                        <c:forEach items="${requestScope.LIST_PRODUCTS_SELLER}" var="p">
                                            <div class="col-lg-3 col-md-6">
                                                <div class="single_product">
                                                    <div class="product_thumb">
                                                        <a href="singleproduct?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a>
                                                        <div class="hot_img">
                                                            <img src="assets\home\img\cart\span-hot.png" alt="">
                                                        </div>
                                                        <div class="product_action">
                                                            <!-- Chỉ cho phép User và Admin thêm vào giỏ hàng -->
                                                            <c:if test="${sessionScope.account != null}">
                                                                <form action="cart" method="post">
                                                                    <input type="hidden" name="action" value="Add">
                                                                    <input type="hidden" name="product_id" value="${p.id}">
                                                                    <input type="hidden" name="quantity" value="1">
                                                                    <button type="submit" style="display: block; border: none; width: 100%; background: #018576; color: #fff; padding: 7px 0; text-transform: capitalize; font-size: 13px;">
                                                                        <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                                    </button>
                                                                </form>
                                                            </c:if>
                                                            <!-- Guest sẽ được yêu cầu đăng nhập -->
                                                            <c:if test="${sessionScope.account == null}">
                                                                <a href="home?btnAction=Login" style="display: block; border: none; width: 100%; background: #018576; color: #fff; padding: 7px 0; text-transform: capitalize; font-size: 13px; text-decoration: none; text-align: center;">
                                                                    <i class="fa fa-shopping-cart"></i> Đăng nhập để mua
                                                                </a>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                    <div class="product_content">
                                                        <div style="display: flex; justify-content: center">
                                                            <c:if test="${p.price != p.salePrice}">
                                                                <span style="margin-right: 10px; font-weight: 400;" class="old_price" id="oldprice">Rs. ${p.price}&#273;</span>
                                                            </c:if>
                                                            <span class="current_price">${p.salePrice}&#273;
                                                            </span>
                                                        </div>
                                                        <h3 class="product_title"><a href="singleproduct?product_id=${p.id}">${p.name}</a></h3>
                                                    </div>
                                                    <div class="product_info">
                                                        <ul>
                                                            <li>
                                                                <!-- Chỉ cho phép User và Admin thêm vào wishlist -->
                                                                <c:if test="${sessionScope.account != null}">
                                                                    <form action="wishlist" method="post">
                                                                        <input type="hidden" name="action" value="Add">
                                                                        <input type="hidden" name="product_id" value="${p.id}">
                                                                        <button type="submit" style="color: red; border: none; border-radius: 4px; font-size: 13px; padding: 2px 11px; font-weight: 600;">Yêu thích</button>
                                                                    </form>
                                                                </c:if>
                                                            </li>
                                                            <!-- Guest chỉ có thể xem sản phẩm -->
                                                            <li><a href="singleproduct?product_id=${p.id}" title="View Detail">Xem sản phẩm</a></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <!--featured product end-->

                                <!--banner area start-->
                                <div class="banner_area mb-60">
                                    <div class="row">
                                        <div class="col-lg-6 col-md-6">
                                            <div class="single_banner">
                                                <a href="filter?discount=dis40"><img src="assets\home\img\banner\banner7.jpg" alt=""></a>
                                                <div class="banner_title">
                                                    <p>Up to <span> 40%</span> off</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-lg-6 col-md-6">
                                            <div class="single_banner">
                                                <a href="filter?discount=dis25"><img src="assets\home\img\banner\banner8.jpg" alt=""></a>
                                                <div class="banner_title title_2">
                                                    <p>sale off <span> 25%</span></p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--banner area end-->

                                <!--brand logo strat-->
                                <div class="brand_logo mb-60">
                                    <div class="block_title">
                                        <h3>Thương hiệu</h3>
                                    </div>
                                    <div class="row">
                                        <!-- Supplier logos -->
                                        <c:forEach items="${requestScope.LIST_SUPPLIERS}" var="s">
                                            <div class="col-lg-2 col-md-3">
                                                <div class="single_brand">
                                                    <a><img src="${s.image}" alt=""></a>
                                                    </a>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <!--brand logo end-->
                            </div>
                        </div>
                    </div>
                    <!--pos home section end-->
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
