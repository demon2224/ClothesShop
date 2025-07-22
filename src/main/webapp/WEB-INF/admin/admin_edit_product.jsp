<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Chỉnh sửa sản phẩm | Quản trị Admin</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="assets/admin/css/main.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
    </head>
    <body class="app sidebar-mini rtl">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>
        <main class="app-content">
            <div class="app-title">
                <ul class="app-breadcrumb breadcrumb">
                    <li class="breadcrumb-item"><a href="productmanagement">Danh sách sản phẩm</a></li>
                    <li class="breadcrumb-item active">Chỉnh sửa sản phẩm</li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <h3 class="tile-title">Chỉnh sửa thông tin sản phẩm</h3>
                        <div class="tile-body">
                            <h3 style="color: green; text-align: center; margin: 20px 0">${requestScope.mess}</h3>
                            <h3 style="color: red; text-align: center; margin: 20px 0">${requestScope.error}</h3>
                            <form class="row" action="productmanagement" method="post">
                                <input type="hidden" name="action" value="Update"> <!-- Thay đổi thành "Update" -->
                                <input type="hidden" name="productId" value="${productId}">
                                <div class="form-group col-md-6">
                                    <label class="control-label">Tên sản phẩm</label>
                                    <input class="form-control" type="text" name="name" value="${name}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Giá</label>
                                    <input class="form-control" type="number" step="0.01" min="0" name="price" value="${price}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Số lượng</label>
                                    <input class="form-control" type="number" min="0" name="stock" value="${stock}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Size</label>
                                    <input class="form-control" type="text" name="size" value="${size}">
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Màu</label>
                                    <input class="form-control" type="text" name="color" value="${color}">
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Danh mục</label>
                                    <select name="categoryId" class="form-control" required>
                                        <c:forEach items="${requestScope.LISTCATEGORIES}" var="cat">
                                            <option value="${cat.id}" ${cat.id == categoryId ? 'selected' : ''}>${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Nhà cung cấp</label>
                                    <select name="supplierId" class="form-control" required>
                                        <c:forEach items="${requestScope.LISTSUPPLIERS}" var="sup">
                                            <option value="${sup.id}" ${sup.id == supplierId ? 'selected' : ''}>${sup.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Loại</label>
                                    <select name="typeId" class="form-control" required>
                                        <c:forEach items="${requestScope.LISTTYPES}" var="type">
                                            <option value="${type.id}" ${type.id == typeId ? 'selected' : ''}>${type.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Ngày phát hành</label>
                                    <input class="form-control" type="date" name="releaseDate" value="${releaseDate}">
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Giảm giá</label>
                                    <input class="form-control" type="number" step="0.01" min="0" max="1" name="discount" value="${discount}">
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Ảnh sản phẩm</label>
                                    <c:if test="${not empty image}">
                                        <img src="${image.split(' ')[0]}" height="100" width="100" alt="Current Image" style="margin-bottom: 10px;">
                                    </c:if>
                                    <input type="file" name="image" accept="image/*" multiple />
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="control-label">Mô tả</label>
                                    <textarea class="form-control" name="description">${description}</textarea>
                                </div>
                                <div class="form-group col-md-12">
                                    <button class="btn btn-save" type="submit">Lưu lại</button>
                                    <a class="btn btn-cancel" href="productmanagement">Hủy bỏ</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>