<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <title>Admin</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="assets/admin/css/main.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.css">
    </head>

    <body class="app sidebar-mini rtl">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>
        <main class="app-content">
            <div class="row">
                <div class="col-md-12">
                    <div class="app-title">
                        <ul class="app-breadcrumb breadcrumb">
                            <li class="breadcrumb-item"><a href="#"><b>Bảng điều khiển</b></a></li>
                        </ul>
                        <div id="clock"><%= new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy").format(new java.util.Date())%></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="widget-small primary coloured-icon"><i class='icon bx bxs-user-account fa-3x'></i>
                        <div class="info">
                            <h4>Tổng khách hàng</h4>
                            <p><b>${requestScope.TOTALUSERS} khách hàng</b></p>
                            <p class="info-tong">Tổng số khách hàng được quản lý.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="widget-small info coloured-icon"><i class='icon bx bxs-data fa-3x'></i>
                        <div class="info">
                            <h4>Tổng sản phẩm</h4>
                            <p><b>${requestScope.TOTALPRODUCTS} sản phẩm</b></p>
                            <p class="info-tong">Tổng số sản phẩm được quản lý.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="widget-small warning coloured-icon"><i class='icon bx bxs-shopping-bags fa-3x'></i>
                        <div class="info">
                            <h4>Tổng đơn hàng</h4>
                            <p><b>${requestScope.TOTALORDERS} đơn hàng</b></p>
                            <p class="info-tong">Tổng số hóa đơn bán hàng trong tháng.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="widget-small danger coloured-icon"><i class='icon bx bxs-error-alt fa-3x'></i>
                        <div class="info">
                            <h4>Sắp hết hàng</h4>
                            <p><b>${requestScope.PRODUCTSLOW} sản phẩm</b></p>
                            <p class="info-tong">Số sản phẩm cảnh báo hết cần nhập thêm.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="widget-small primary coloured-icon"><i class="icon bx bxs-bank fa-3x"></i>
                        <div class="info">
                            <h4>Tổng doanh thu</h4>
                            <p><b>${requestScope.TOTALSALE} VNĐ</b></p>
                            <p class="info-tong">Tổng doanh thu từ tất cả các đơn hàng.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="widget-small primary coloured-icon"><i class='icon bx bxs-calendar-check fa-3x'></i>
                        <div class="info">
                            <h4>Doanh thu hôm nay</h4>
                            <p><b>${requestScope.TOTALSALETODAY} VNĐ</b></p>
                            <p class="info-tong">Tổng doanh thu từ đơn hàng hôm nay.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="tile">
                        <h3 class="tile-title">Đơn hàng gần đây</h3>
                        <table class="table table-hover table-bordered">
                            <thead>
                                <tr>
                                    <th>ID đơn hàng</th>
                                    <th>Khách hàng</th>
                                    <th>Số điện thoại</th>
                                    <th>Địa chỉ</th>
                                    <th>Ngày mua</th>
                                    <th>Tổng tiền</th>
                                    <th>Thanh Toán</th>
                                    <th>Trạng thái</th>
                                    <th>Chức năng</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${LAST_RECENT_ORDERS}" var="b">
                                    <tr>
                                        <td>${b.orderID}</td>
                                        <td>${b.user.userName}</td>
                                        <td>(+84) ${b.user.phone}</td>
                                        <td>${b.user.address}</td>
                                        <td>${b.orderDate}</td>
                                        <td>${b.getFormattedTotalPrice()}đ</td>
                                        <td><span class="badge bg-success">${b.paymentMethod.paymentMethod}</span></td>
                                        <td>
                                            ${b.status ? "Đã giao" : "Chưa giao"}
                                            <c:if test="${!b.status}">
                                                <a href="manageorder?action=ChangeStatus&id=${b.orderID}"
                                                   style="margin-left: 20px; color: green;">
                                                    <i class="fa-solid fa-check"></i>
                                                </a>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a style="color: rgb(245, 157, 57); background-color: rgb(251, 226, 197); padding: 5px; border-radius: 5px;"
                                               href="manageorder?action=ShowDetail&bill_id=${b.orderID}">
                                                <i class="fa fa-eye"></i> Chi tiết
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>
    </body>

</html>