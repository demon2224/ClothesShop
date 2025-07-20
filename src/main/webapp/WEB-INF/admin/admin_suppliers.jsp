<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<%
    int totalSuppliers = request.getAttribute("LISTSUPPLIERS") != null ? ((java.util.List) request.getAttribute("LISTSUPPLIERS")).size() : 0;
    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) totalSuppliers / pageSize);
    request.setAttribute("totalPages", totalPages);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Danh sách nhà cung cấp | Quản trị Admin</title>
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
            .input-group {
                display: flex;
            }
            .input-group input {
                flex-grow: 1;
                margin-right: 10px;
            }
        </style>
    </head>
    <body class="app sidebar-mini rtl">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>
        <main class="app-content">
            <div class="app-title">
                <ul class="app-breadcrumb breadcrumb">
                    <li class="breadcrumb-item active"><a href="#">Danh sách nhà cung cấp</a></li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <div class="tile-body">
                            <div class="row element-button">
                                <div class="col-sm-2">
                                    <a class="btn btn-add btn-sm" href="managesupplier?action=Insert" title="Thêm">
                                        <i class="fas fa-plus"></i> Tạo mới nhà cung cấp</a>
                                </div>
                                <div class="col-sm-4">
                                    <form class="search-form" action="managesupplier" method="get">
                                        <input type="hidden" name="action" value="Search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="search"
                                                   placeholder="Tìm theo tên nhà cung cấp..."
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
                                <c:when test="${not empty requestScope.LISTSUPPLIERS}">
                                    <c:set var="pageNum" value="${param.page != null && param.page.matches('^[0-9]+$') ? param.page : '1'}" />
                                    <c:set var="currentPage" value="${pageNum}" />
                                    <c:if test="${currentPage < 1}">
                                        <c:set var="currentPage" value="1" />
                                    </c:if>
                                    <c:if test="${currentPage > totalPages}">
                                        <c:set var="currentPage" value="${totalPages}" />
                                    </c:if>

                                    <c:set var="start" value="${(currentPage - 1) * 10}" />
                                    <c:set var="end" value="${start + 10 > totalSuppliers ? totalSuppliers : start + 10}" />

                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Mã nhà cung cấp</th>
                                                <th>Tên nhà cung cấp</th>
                                                <th>Ảnh</th>
                                                <th>Chức năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requestScope.LISTSUPPLIERS}" var="s" begin="${start}" end="${end - 1}">
                                                <tr>
                                                    <td>${s.id}</td>
                                                    <td>${s.name}</td>
                                                    <td><img src="${s.image}" alt="" width="100px;"></td>
                                                    <td>
                                                        <a class="btn btn-primary btn-sm trash"
                                                           href="managesupplier?action=Delete&supplierId=${s.id}&page=${currentPage}${requestScope.searchQuery != null ? '&search=' += requestScope.searchQuery : ''}"
                                                           onclick="return confirm('Bạn có chắc muốn xóa nhà cung cấp này ?')">
                                                            <i class="fas fa-trash-alt"></i>
                                                        </a>
                                                        <a class="btn btn-primary btn-sm edit"
                                                           href="managesupplier?action=Edit&supplierId=${s.id}">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="pagination">
                                        <c:if test="${totalPages > 1}">
                                            <a href="managesupplier?page=1${requestScope.searchQuery != null ? '&action=Search&search=' += requestScope.searchQuery : ''}">Đầu</a>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <a href="managesupplier?page=${i}${requestScope.searchQuery != null ? '&action=Search&search=' += requestScope.searchQuery : ''}"
                                                   class="${i == currentPage ? 'active' : ''}">${i}</a>
                                            </c:forEach>
                                            <a href="managesupplier?page=${totalPages}${requestScope.searchQuery != null ? '&action=Search&search=' += requestScope.searchQuery : ''}">Cuối</a>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Mã nhà cung cấp</th>
                                                <th>Tên nhà cung cấp</th>
                                                <th>Ảnh</th>
                                                <th>Chức năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr><td colspan="4" style="text-align: center;">Không tìm thấy nhà cung cấp nào.</td></tr>
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