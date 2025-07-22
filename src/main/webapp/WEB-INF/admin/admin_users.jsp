<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<%
    // Lấy danh sách user từ request scope
    int totalUsers = request.getAttribute("LISTUSERS") != null ? ((java.util.List) request.getAttribute("LISTUSERS")).size() : 0;
    int pageSize = 8;
    int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
    request.setAttribute("totalPages", totalPages);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Danh sách người dùng | Quản trị Admin</title>
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
            }
        </style>
    </head>
    <body class="app sidebar-mini rtl">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>
        <main class="app-content">
            <div class="app-title">
                <ul class="app-breadcrumb breadcrumb side">
                    <li class="breadcrumb-item active"><a href="usermanagement"><b>Danh sách người dùng</b></a></li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <div class="tile-body">
                            <div class="row element-button">
                                <div class="col-sm-2">
                                    <a class="btn btn-add btn-sm" href="usermanagement?action=Insert" title="Thêm">
                                        <i class="fas fa-plus"></i> Tạo mới tài khoản
                                    </a>
                                </div>
                                <div class="col-sm-4">
                                    <form class="search-form" action="usermanagement" method="get">
                                        <input type="hidden" name="action" value="Search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="search"
                                                   placeholder="Tìm theo tên, email, điện thoại..."
                                                   value="${requestScope.searchQuery}">
                                            <button class="btn btn-primary" type="submit">
                                                <i class="fas fa-search"></i> Tìm
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <h3 style="color: green; text-align: center; margin: 20px 0">${requestScope.mess}</h3>
                            <h3 style="color: red; text-align: center; margin: 20px 0">${requestScope.error}</h3>
                            <c:choose>
                                <c:when test="${not empty requestScope.LISTUSERS}">
                                    <c:set var="pageNum" value="${param.page ne null && param.page.matches('^[0-9]+$') ? param.page : '1'}" />
                                    <c:set var="currentPage" value="${pageNum > 0 ? (pageNum > totalPages ? totalPages : pageNum) : 1}" />
                                    <c:set var="start" value="${(currentPage - 1) * 8}" />
                                    <c:set var="end" value="${start + 8 > totalUsers ? totalUsers : start + 8}" />

                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>ID khách hàng</th>
                                                <th>Tên khách hàng</th>
                                                <th>Ảnh đại diện</th>
                                                <th>Email</th>
                                                <th>Địa chỉ</th>
                                                <th>Số điện thoại</th>
                                                <th>Role</th>
                                                <th>Trạng thái</th>
                                                <th width="100">Chức năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requestScope.LISTUSERS}" var="u" begin="${start}" end="${end - 1}">
                                                <tr>
                                                    <td>${u.id}</td>
                                                    <td>${u.firstName} ${u.lastName}</td>
                                                    <td><img src="${u.avatar}" width="50px" height="50px"></td>
                                                    <td>${u.email}</td>
                                                    <td>${u.address}</td>
                                                    <td>${u.phone}</td>
                                                    <td>${u.roleID == 1 ? "Admin" : "User"}</td>
                                                    <td>${u.status ? "Hoạt động" : "Đã xóa"}</td>
                                                    <td>
                                                        <c:set var="isActiveUser" value="${u.status}" />
                                                        <c:if test="${isActiveUser}">
                                                            <a class="btn btn-primary btn-sm trash" href="usermanagement?action=Delete&uid=${u.id}&page=${currentPage}${requestScope.searchQuery != null ? '&search=' + requestScope.searchQuery : ''}" onclick="return confirm('Bạn có chắc muốn xóa?')">
                                                                <i class="fas fa-trash-alt"></i>
                                                            </a>
                                                            <a class="btn btn-primary btn-sm edit" href="usermanagement?action=Edit&username=${u.userName}&page=${currentPage}${requestScope.searchQuery != null ? '&search=' + requestScope.searchQuery : ''}">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${!isActiveUser}">
                                                            <a class="btn btn-success btn-sm restore" href="usermanagement?action=Restore&uid=${u.id}&page=${currentPage}${requestScope.searchQuery != null ? '&search=' + requestScope.searchQuery : ''}" onclick="return confirm('Bạn có chắc muốn khôi phục?')">
                                                                <i class="fas fa-undo"></i>
                                                            </a>
                                                            <a class="btn btn-danger btn-sm delete" href="usermanagement?action=PermanentlyDelete&uid=${u.id}&page=${currentPage}${requestScope.searchQuery != null ? '&search=' + requestScope.searchQuery : ''}" onclick="return confirm('Bạn có chắc muốn xóa vĩnh viễn người dùng này?')">
                                                                <i class="fas fa-trash"></i>
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="pagination">
                                        <c:if test="${totalPages > 1}">
                                            <a href="usermanagement?page=1${requestScope.searchQuery != null ? '&action=Search&search=' + requestScope.searchQuery : ''}">Đầu</a>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <a href="usermanagement?page=${i}${requestScope.searchQuery != null ? '&action=Search&search=' + requestScope.searchQuery : ''}"
                                                   class="${i == currentPage ? 'active' : ''}">${i}</a>
                                            </c:forEach>
                                            <a href="usermanagement?page=${totalPages}${requestScope.searchQuery != null ? '&action=Search&search=' + requestScope.searchQuery : ''}">Cuối</a>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>ID khách hàng</th>
                                                <th>Tên khách hàng</th>
                                                <th>Ảnh đại diện</th>
                                                <th>Email</th>
                                                <th>Địa chỉ</th>
                                                <th>Số điện thoại</th>
                                                <th>Role</th>
                                                <th>Trạng thái</th>
                                                <th width="100">Chức năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr><td colspan="9" style="text-align: center;">Không tìm thấy người dùng nào.</td></tr>
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