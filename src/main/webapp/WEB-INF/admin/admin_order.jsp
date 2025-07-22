<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<%
    int totalOrders = request.getAttribute("LIST_ORDERS") != null ? ((java.util.List) request.getAttribute("LIST_ORDERS")).size() : 0;
    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
    request.setAttribute("totalPages", totalPages);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Danh sách đơn hàng | Quản trị Admin</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="assets/admin/css/main.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
        <style>
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 20px;
            }
            .pagination a {
                padding: 8px 16px;
                text-decoration: none;
                color: #007bff;
                border: 1px solid #ddd;
                margin: 0 4px;
            }
            .pagination a.active {
                background-color: #007bff;
                color: white;
                border: 1px solid #007bff;
            }
            .pagination a:hover:not(.active) {
                background-color: #ddd;
            }
            .search-form {
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 15px;
            }
            .input-group {
                display: flex;
                align-items: center;
            }
            .input-group input {
                margin-right: 10px;
            }
            .select-group {
                display: flex;
                align-items: center;
            }
        </style>
    </head>
    <body class="app sidebar-mini rtl">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>
        <main class="app-content">
            <div class="app-title">
                <ul class="app-breadcrumb breadcrumb">
                    <li class="breadcrumb-item active"><a href="#">Danh sách đơn hàng</a></li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <div class="tile-body">
                            <form class="search-form" action="manageorder" method="get">
                                <input type="hidden" name="action" value="Search">
                                <div class="input-group">
                                    <input type="text" class="form-control" name="searchUsername"
                                           placeholder="Tìm theo tên khách hàng..."
                                           value="${requestScope.searchUsername}">
                                </div>
                                <div class="select-group">
                                    <select class="form-control" name="statusFilter">
                                        <option value="all" ${requestScope.statusFilter == null || requestScope.statusFilter == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
                                        <option value="delivered" ${requestScope.statusFilter == 'delivered' ? 'selected' : ''}>Đã giao</option>
                                        <option value="pending" ${requestScope.statusFilter == 'pending' ? 'selected' : ''}>Chưa giao</option>
                                    </select>
                                </div>
                                <button class="btn btn-primary" type="submit">
                                    <i class="fas fa-search"></i> Tìm
                                </button>
                            </form>

                            <h3 style="color: green; text-align: center; margin: 20px 0">${requestScope.mess}</h3>
                            <h3 style="color: red; text-align: center; margin: 20px 0">${requestScope.error}</h3>

                            <c:choose>
                                <c:when test="${not empty requestScope.LIST_ORDERS}">
                                    <c:set var="pageNum" value="${param.page != null && param.page.matches('^[0-9]+$') ? param.page : '1'}" />
                                    <c:set var="currentPage" value="${pageNum}" />
                                    <c:if test="${currentPage < 1}">
                                        <c:set var="currentPage" value="1" />
                                    </c:if>
                                    <c:if test="${currentPage > totalPages}">
                                        <c:set var="currentPage" value="${totalPages}" />
                                    </c:if>

                                    <c:set var="start" value="${(currentPage - 1) * 10}" />
                                    <c:set var="end" value="${start + 10 > totalOrders ? totalOrders : start + 10}" />

                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>ID đơn hàng</th>
                                                <th>Khách hàng</th>
                                                <th>Số điện thoại</th>
                                                <th>Địa chỉ</th>
                                                <th>Ngày mua</th>
                                                <th>Tổng tiền</th>
                                                <th>Thanh toán</th>
                                                <th>Trạng thái</th>
                                                <th>Tính năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requestScope.LIST_ORDERS}" var="b" begin="${start}" end="${end - 1}">
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
                                                            <a href="manageorder?action=ChangeStatus&id=${b.orderID}&page=${currentPage}${requestScope.searchUsername != null ? '&searchUsername=' += requestScope.searchUsername : ''}${requestScope.statusFilter != null ? '&statusFilter=' += requestScope.statusFilter : ''}"
                                                               style="margin-left: 20px; color: green;">
                                                                <i class="fa-solid fa-check"></i>
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <a style="color: rgb(245 157 57); background-color: rgb(251 226 197); padding: 5px; border-radius: 5px; margin-right: 5px;"
                                                           href="manageorder?action=ShowDetail&bill_id=${b.orderID}">
                                                            <i class="fa fa-eye"></i> Chi tiết
                                                        </a>
                                                        <a style="color: rgb(220 53 69); background-color: rgb(248 215 218); padding: 5px; border-radius: 5px;"
                                                           href="manageorder?action=DeleteOrder&id=${b.orderID}&page=${currentPage}${requestScope.searchUsername != null ? '&searchUsername=' += requestScope.searchUsername : ''}${requestScope.statusFilter != null ? '&statusFilter=' += requestScope.statusFilter : ''}"
                                                           onclick="return confirm('Bạn có chắc chắn muốn xóa đơn hàng #${b.orderID}? Hành động này không thể hoàn tác.')">
                                                            <i class="fa fa-trash"></i> Xóa
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="pagination">
                                        <c:if test="${totalPages > 1}">
                                            <a href="manageorder?page=1${requestScope.searchUsername != null ? '&action=Search&searchUsername=' += requestScope.searchUsername : ''}${requestScope.statusFilter != null ? '&statusFilter=' += requestScope.statusFilter : ''}">Đầu</a>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <a href="manageorder?page=${i}${requestScope.searchUsername != null ? '&action=Search&searchUsername=' += requestScope.searchUsername : ''}${requestScope.statusFilter != null ? '&statusFilter=' += requestScope.statusFilter : ''}"
                                                   class="${i == currentPage ? 'active' : ''}">${i}</a>
                                            </c:forEach>
                                            <a href="manageorder?page=${totalPages}${requestScope.searchUsername != null ? '&action=Search&searchUsername=' += requestScope.searchUsername : ''}${requestScope.statusFilter != null ? '&statusFilter=' += requestScope.statusFilter : ''}">Cuối</a>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>ID đơn hàng</th>
                                                <th>Khách hàng</th>
                                                <th>Số điện thoại</th>
                                                <th>Địa chỉ</th>
                                                <th>Ngày mua</th>
                                                <th>Tổng tiền</th>
                                                <th>Thanh toán</th>
                                                <th>Trạng thái</th>
                                                <th>Tính năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr><td colspan="9" style="text-align: center;">Không tìm thấy đơn hàng nào.</td></tr>
                                        </tbody>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>