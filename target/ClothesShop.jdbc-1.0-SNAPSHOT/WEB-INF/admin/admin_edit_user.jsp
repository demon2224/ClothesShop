<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Chỉnh sửa người dùng | Quản trị Admin</title>
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
                    <li class="breadcrumb-item"><a href="usermanagement">Danh sách người dùng</a></li>
                    <li class="breadcrumb-item active">Chỉnh sửa người dùng</li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <h3 class="tile-title">Chỉnh sửa thông tin người dùng</h3>
                        <div class="tile-body">
                            <h3 style="color: green; text-align: center; margin: 20px 0">${requestScope.mess}</h3>
                            <h3 style="color: red; text-align: center; margin: 20px 0">${requestScope.error}</h3>
                            <form class="row" action="usermanagement" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="action" value="Update">
                                <div class="form-group col-md-6">
                                    <label class="control-label">Tên đăng nhập</label>
                                    <input class="form-control" type="text" readonly name="username" value="${username}">
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Tên</label>
                                    <input class="form-control" type="text" name="firstname" value="${firstname}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Họ</label>
                                    <input class="form-control" type="text" name="lastname" value="${lastname}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Số điện thoại</label>
                                    <input class="form-control" type="tel" name="phone" value="${phone}" pattern="[0-9]{10}" title="Số điện thoại phải có 10 chữ số" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Email</label>
                                    <input class="form-control" type="email" name="email" value="${email}" placeholder="example@domain.com" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Địa chỉ</label>
                                    <input class="form-control" type="text" name="address" value="${address}">
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Quyền quản trị</label>
                                    <select name="role" class="form-control">
                                        <option value="admin" ${roleid == 1 ? 'selected' : ''}>Admin</option>
                                        <option value="user" ${roleid == 2 ? 'selected' : ''}>User</option>
                                    </select>
                                </div>
                                <div class="form-group col-md-6">
                                    <label class="control-label">Ảnh đại diện</label>
                                    <c:if test="${not empty avatar}">
                                        <img src="${avatar}" height="100" width="100" alt="Current Avatar" style="margin-bottom: 10px;">
                                    </c:if>
                                    <input type="file" name="avatar" accept="image/*" />
                                </div>
                                <div class="form-group col-md-12">
                                    <button class="btn btn-save" type="submit">Lưu lại</button>
                                    <a class="btn btn-cancel" href="usermanagement">Hủy bỏ</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>

    </body>
</html>