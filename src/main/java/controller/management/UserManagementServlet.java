package controller.management;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import model.UserDTO;

/**
 *
 * @author Group - 07
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class UserManagementServlet extends HttpServlet {

    private static final String MANAGE_USER_PAGE = "WEB-INF/admin/admin_users.jsp";
    private static final String INSERT_USER_PAGE = "WEB-INF/admin/admin_user_insert.jsp";
    private static final String EDIT_USER_PAGE = "WEB-INF/admin/admin_edit_user.jsp";
    private static final String UPLOAD_DIR = "assets/home/img/users/";

    /**
     * Kiểm tra quyền truy cập admin
     */
    private boolean checkAdminAccess(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("account");

        if (user == null) {
            log("Unauthorized admin access attempt - not logged in from " + request.getRequestURI());
            response.sendRedirect("home?btnAction=Login");
            return false;
        }

        if (user.getRoleID() != 1) {
            log("Unauthorized admin access attempt by user: " + user.getUserName()
                    + " (ID: " + user.getId() + ") to " + request.getRequestURI());
            response.sendRedirect("home");
            return false;
        }

        return true;
    }

    /**
     * Xử lý upload file ảnh
     */
    private String handleFileUpload(HttpServletRequest request, String currentAvatar) throws IOException, ServletException {
        Part filePart = request.getPart("avatar");

        if (filePart == null || filePart.getSize() == 0) {
            return currentAvatar; // Không có file mới, giữ ảnh cũ
        }

        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || fileName.trim().isEmpty()) {
            return currentAvatar;
        }

        // Kiểm tra extension
        String fileExtension = "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            fileExtension = fileName.substring(lastDotIndex).toLowerCase();
        }

        // Chỉ cho phép ảnh
        if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|bmp)$")) {
            throw new ServletException("Chỉ được upload file ảnh (jpg, jpeg, png, gif, bmp)");
        }

        // Sử dụng tên file gốc (không mã hóa)
        String originalFileName = fileName;

        // Đường dẫn upload (relative to webapp)
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

        // Tạo thư mục nếu chưa có
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Lưu file với tên gốc
        Path filePath = Paths.get(uploadPath + originalFileName);
        Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return UPLOAD_DIR + originalFileName;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Kiểm tra quyền truy cập admin
        if (!checkAdminAccess(request, response)) {
            return; // Đã redirect, không cần xử lý thêm
        }

        String url = "WEB-INF/admin/admin_users.jsp";
        String action = request.getParameter("action");

        try {
            UserDAO userDao = new UserDAO();
            if ("Insert".equals(action)) {
                url = this.showInsertUserPage(request);
            } else if ("Edit".equals(action)) {
                url = this.showEditUserPage(request, userDao);
            } else if ("Update".equals(action)) {
                url = this.updateUser(request, userDao);
            } else if ("Delete".equals(action)) {
                url = this.deleteUser(request, userDao);
            } else if ("InsertUser".equals(action)) {
                url = this.insertNewUser(request, userDao);
            } else if ("Search".equals(action)) {
                url = this.searchUsers(request, userDao);
            } else if ("Restore".equals(action)) {
                url = this.restoreUser(request, userDao);
            } else if ("PermanentlyDelete".equals(action)) {
                url = this.permanentlyDeleteUser(request, userDao);
            } else {
                url = this.showUserList(request, userDao);
            }
        } catch (Exception ex) {
            this.log("UserManagementServlet error: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    private String showUserList(HttpServletRequest request, UserDAO userDao) throws SQLException {
        List<UserDTO> userList = userDao.getData();
        request.setAttribute("LISTUSERS", userList);
        request.setAttribute("CURRENTSERVLET", "User");
        return MANAGE_USER_PAGE;
    }

    private String showInsertUserPage(HttpServletRequest request) {
        return INSERT_USER_PAGE;
    }

    private String showEditUserPage(HttpServletRequest request, UserDAO userDao) throws SQLException {
        String username = request.getParameter("username");
        UserDTO user = userDao.getUserByName(username);
        if (user == null) {
            request.setAttribute("error", "Không tìm thấy người dùng!");
            return this.showUserList(request, userDao);
        } else {
            request.setAttribute("username", user.getUserName());
            request.setAttribute("firstname", user.getFirstName());
            request.setAttribute("lastname", user.getLastName());
            request.setAttribute("phone", user.getPhone());
            request.setAttribute("roleid", user.getRoleID());
            request.setAttribute("address", user.getAddress());
            request.setAttribute("email", user.getEmail());
            request.setAttribute("avatar", user.getAvatar());
            return EDIT_USER_PAGE;
        }
    }

    private String deleteUser(HttpServletRequest request, UserDAO userDao) throws SQLException {
        String uid = request.getParameter("uid");
        try {
            userDao.deleteUser(uid);
            request.setAttribute("mess", "Xóa người dùng thành công!");
        } catch (SQLException e) {
            // Nếu có lỗi từ DAO (ví dụ: có đơn hàng), hiển thị thông báo lỗi
            request.setAttribute("error", e.getMessage());
        }
        return this.showUserList(request, userDao);
    }

    private String insertNewUser(HttpServletRequest request, UserDAO userDao) throws SQLException, Exception {
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        int roleId = "admin".equals(role) ? 1 : 2;

        // Xử lý upload ảnh
        String avatar = "";
        try {
            avatar = handleFileUpload(request, "");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
            this.setUserAttributes(request, username, firstName, lastName, phone, email, address, "", role);
            return INSERT_USER_PAGE;
        }

        // Mã hóa mật khẩu trước khi lưu vào database
        String hashedPassword = encryptPassword(password);

        UserDTO user = new UserDTO(0, firstName, lastName, email, avatar, username, hashedPassword, address, phone, roleId, true);

        try {
            userDao.registerUser(user);
            request.setAttribute("mess", "Thêm người dùng thành công!");
            return this.showUserList(request, userDao);
        } catch (SQLException e) {
            this.setUserAttributes(request, username, firstName, lastName, phone, email, address, avatar, role);
            request.setAttribute("error", e.getMessage());
            return INSERT_USER_PAGE;
        }
    }

    private String updateUser(HttpServletRequest request, UserDAO userDao) throws SQLException, Exception {
        String username = request.getParameter("username");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        // Lấy ảnh hiện tại
        UserDTO existingUser = userDao.getUserByName(username);
        String currentAvatar = existingUser != null ? existingUser.getAvatar() : "";

        // Xử lý upload ảnh mới (nếu có)
        String avatar = "";
        try {
            avatar = handleFileUpload(request, currentAvatar);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
            this.setUserAttributes(request, username, firstname, lastname, phone, email, address, currentAvatar, role);
            return EDIT_USER_PAGE;
        }

        int roleId = "admin".equals(role) ? 1 : 2;

        try {
            userDao.updateUser(firstname, lastname, email, address, phone, username, avatar, roleId);
            request.setAttribute("mess", "Cập nhật người dùng thành công!");
            return this.showUserList(request, userDao);
        } catch (SQLException e) {
            this.setUserAttributes(request, username, firstname, lastname, phone, email, address, avatar, role);
            request.setAttribute("error", e.getMessage());
            return EDIT_USER_PAGE;
        }
    }

    private String searchUsers(HttpServletRequest request, UserDAO userDao) throws SQLException, Exception {
        String searchQuery = request.getParameter("search");
        List<UserDTO> userList = searchQuery != null && !searchQuery.trim().isEmpty() ? userDao.searchUsers(searchQuery) : userDao.getData();
        request.setAttribute("LISTUSERS", userList);
        request.setAttribute("CURRENTSERVLET", "User");
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            request.setAttribute("searchQuery", searchQuery);
        }

        return MANAGE_USER_PAGE;
    }

    private String restoreUser(HttpServletRequest request, UserDAO userDao) throws SQLException, Exception {
        String uid = request.getParameter("uid");
        userDao.restoreUser(uid);
        request.setAttribute("mess", "Khôi phục người dùng thành công!");
        return this.showUserList(request, userDao);
    }

    private String permanentlyDeleteUser(HttpServletRequest request, UserDAO userDao) throws SQLException, Exception {
        String uid = request.getParameter("uid");
        log("Attempting to permanently delete user with ID: " + uid);

        if (uid == null || uid.trim().isEmpty()) {
            log("Error: User ID is null or empty");
            request.setAttribute("error", "ID người dùng không hợp lệ!");
            return this.showUserList(request, userDao);
        }

        try {
            userDao.permanentlyDeleteUser(uid);
            log("Successfully permanently deleted user with ID: " + uid);
            request.setAttribute("mess", "Xóa vĩnh viễn người dùng thành công!");
        } catch (Exception e) {
            log("Error permanently deleting user: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi xóa vĩnh viễn người dùng: " + e.getMessage());
        }

        return this.showUserList(request, userDao);
    }

    private void setUserAttributes(HttpServletRequest request, String username, String firstname, String lastname, String phone, String email, String address, String avatar, String role) {
        request.setAttribute("username", username);
        request.setAttribute("firstname", firstname);
        request.setAttribute("lastname", lastname);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("avatar", avatar);
        request.setAttribute("role", role);
    }

    // Method để mã hóa mật khẩu MD5 giống như RegisterServlet
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log("MD5 Hashing Error:" + e.getMessage());
            return null;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    public String getServletInfo() {
        return "User Management Servlet";
    }
}
