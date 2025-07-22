<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Tạo mới tài khoản | Quản trị Admin</title>
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
                    <li class="breadcrumb-item active">Tạo mới tài khoản</li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="tile">
                        <h3 class="tile-title">Tạo mới tài khoản</h3>
                        <div class="tile-body">
                            <h3 style="color: green; text-align: center; margin: 20px 0">${requestScope.mess}</h3>
                            <h3 style="color: red; text-align: center; margin: 20px 0">${requestScope.error}</h3>
                            <form class="row" action="usermanagement" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="action" value="InsertUser">

                                <div class="form-group col-md-3">
                                    <label class="control-label">Ảnh đại diện</label>
                                    <input type="file" name="avatar" accept="image/*">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Tên</label>
                                    <input class="form-control" required name="firstname" type="text" value="${requestScope.firstname}">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Họ</label>
                                    <input class="form-control" required name="lastname" type="text" value="${requestScope.lastname}">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Tên người dùng</label>
                                    <input class="form-control" required name="username" type="text" value="${requestScope.username}">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Mật khẩu</label>
                                    <input class="form-control" required name="password" type="password" value="${requestScope.password}">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Email</label>
                                    <input class="form-control" type="email" name="email" value="${requestScope.email}" placeholder="example@domain.com">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Địa chỉ</label>
                                    <input class="form-control" required name="address" type="text" value="${requestScope.address}">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Số điện thoại</label>
                                    <input class="form-control" required name="phone" type="tel" pattern="[0-9]{10}" title="Số điện thoại phải có 10 chữ số" value="${requestScope.phone}">
                                </div>

                                <div class="form-group col-md-3">
                                    <label class="control-label">Quyền quản trị</label>
                                    <select name="role" class="form-control">
                                        <option value="admin" ${requestScope.role == 'admin' ? 'selected' : ''}>Admin</option>
                                        <option value="user" ${requestScope.role == 'user' ? 'selected' : ''}>User</option>
                                    </select>
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