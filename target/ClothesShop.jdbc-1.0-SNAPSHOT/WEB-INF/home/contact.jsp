<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Contact</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="assets\home\images\favicon.png">

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
                    <%@include file="/WEB-INF/include/header.jsp"%>
                    <!--header end -->
                    <!--breadcrumbs area start-->
                    <div class="breadcrumbs_area">
                        <div class="row">
                            <div class="col-12">
                                <div class="breadcrumb_content">
                                    <ul>
                                        <li><a href="home">home</a></li>
                                        <li><i class="fa fa-angle-right"></i></li>
                                        <li>contact</li>
                                    </ul>

                                </div>
                            </div>
                        </div>
                    </div>
                    <!--breadcrumbs area end-->

                    <!--contact area start-->
                    <div class="contact_area">
                        <div class="row">
                            <div class="col-lg-12 col-md-12">
                                <div class="contact_message contact_info">
                                    <h3>contact us</h3>    
                                    <p>Chắc chắn! ClothesShop là điểm đến lý tưởng cho các quý ông đam mê thể thao và phong cách. Với một bộ sưu tập đa dạng từ quần áo, giày dép đến phụ kiện thể thao, chúng tôi cam kết mang đến cho quý khách hàng những sản phẩm chất lượng và thời trang nhất. Từ những bài tập nhẹ nhàng đến những buổi tập thể dục mạnh mẽ, ClothesShop luôn có những lựa chọn phù hợp để bạn tỏa sáng và thoải mái. Hãy khám phá ngay để trải nghiệm sự đẳng cấp và phong cách tại ClothesShop!</p>
                                    <ul>
                                        <li><i class="fa fa-fax"></i>  Address : Số 600 Đường Nguyễn Văn Cừ (nối dài), P. An Bình, Q. Ninh Kiều, TP. Cần Thơ.</li>
                                        <li><i class="fa fa-envelope-o"></i> <a href="#">ClothesShopFUCT@fpt.edu.vn</a></li>
                                        <li><i class="fa fa-phone"></i> 0815255855</li>
                                    </ul>        
                                    <h3><strong>Working hours</strong></h3>
                                    <p><strong>Monday – Saturday</strong>:  08AM – 22PM</p>       
                                </div> 
                            </div>
                        </div>
                    </div>
                    <!--contact area end-->

                    <!--contact map start-->
                    <div class="contact_map">
                        <div class="row">
                            <div class="col-12">
                                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3929.0533542565!2d105.72986204057197!3d10.012451790134829!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31a0882139720a77%3A0x3916a227d0b95a64!2sFPT%20University!5e0!3m2!1svi!2s!4v1751246234067!5m2!1svi!2s" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                            </div>
                        </div>
                    </div>
                    <!--contact map end-->
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
