<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<%
    int totalCategories = request.getAttribute("LIST_CATEGORIES") != null ? ((java.util.List) request.getAttribute("LIST_CATEGORIES")).size() : 0;
    int pageSize = 8;
    int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
    request.setAttribute("totalPages", totalPages);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Danh sách danh mục | Quản trị Admin</title>
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
                    <li class="breadcrumb-item active"><a href="#">Danh sách danh mục</a></li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <div class="tile-body">
                            <div class="row element-button">
                                <div class="col-sm-2">
                                    <a class="btn btn-add btn-sm" href="categorymanagement?action=Insert" title="Thêm">
                                        <i class="fas fa-plus"></i> Tạo mới danh mục</a>
                                </div>
                                <div class="col-sm-4">
                                    <form class="search-form" action="categorymanagement" method="get">
                                        <input type="hidden" name="action" value="Search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="search"
                                                   placeholder="Tìm theo tên danh mục..."
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
                                <c:when test="${not empty requestScope.LIST_CATEGORIES}">
                                    <c:set var="pageNum" value="${param.page != null && param.page.matches('^[0-9]+$') ? param.page : '1'}" />
                                    <c:set var="currentPage" value="${pageNum}" />
                                    <c:if test="${currentPage < 1}">
                                        <c:set var="currentPage" value="1" />
                                    </c:if>
                                    <c:if test="${currentPage > totalPages}">
                                        <c:set var="currentPage" value="${totalPages}" />
                                    </c:if>

                                    <c:set var="start" value="${(currentPage - 1) * 8}" />
                                    <c:set var="end" value="${start + 8 > totalCategories ? totalCategories : start + 8}" />

                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Mã danh mục</th>
                                                <th>Tên danh mục</th>
                                                <th>Loại</th>
                                                <th>Chức năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requestScope.LIST_CATEGORIES}" var="c" begin="${start}" end="${end - 1}">
                                                <tr>
                                                    <td>${c.id}</td>
                                                    <td>${c.name}</td>
                                                    <td>${c.type.name}</td>
                                                    <td>
                                                        <a class="btn btn-primary btn-sm trash" href="categorymanagement?action=Delete&categoryId=${c.id}&page=${currentPage}${requestScope.searchQuery != null ? '&search=' += requestScope.searchQuery : ''}"
                                                           onclick="return confirm('Bạn có chắc muốn xóa?')">
                                                            <i class="fas fa-trash-alt"></i>
                                                        </a>
                                                        <a class="btn btn-primary btn-sm edit" href="categorymanagement?action=Edit&categoryId=${c.id}">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="pagination">
                                        <c:if test="${totalPages > 1}">
                                            <a href="categorymanagement?page=1${requestScope.searchQuery != null ? '&action=Search&search=' += requestScope.searchQuery : ''}">Đầu</a>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <a href="categorymanagement?page=${i}${requestScope.searchQuery != null ? '&action=Search&search=' += requestScope.searchQuery : ''}"
                                                   class="${i == currentPage ? 'active' : ''}">${i}</a>
                                            </c:forEach>
                                            <a href="categorymanagement?page=${totalPages}${requestScope.searchQuery != null ? '&action=Search&search=' += requestScope.searchQuery : ''}">Cuối</a>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <table class="table table-hover table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Mã danh mục</th>
                                                <th>Tên danh mục</th>
                                                <th>Loại</th>
                                                <th>Chức năng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr><td colspan="4" style="text-align: center;">Không tìm thấy danh mục nào.</td></tr>
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