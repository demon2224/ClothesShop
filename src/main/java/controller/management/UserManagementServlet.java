package controller.management;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import model.UserDTO;

/**
 *
 * @author Group - 07
 */
public class UserManagementServlet extends HttpServlet {

    private static final String MANAGE_USER_PAGE = "WEB-INF/admin/admin_users.jsp";
    private static final String INSERT_USER_PAGE = "WEB-INF/admin/admin_user_insert.jsp";
    private static final String EDIT_USER_PAGE = "WEB-INF/admin/admin_edit_user.jsp";
    private static final String UPLOAD_DIR = "assets/home/img/users/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = "view/jsp/admin/admin_users.jsp";
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
        userDao.deleteUser(uid);
        request.setAttribute("mess", "Xóa người dùng thành công!");
        return this.showUserList(request, userDao);
    }

    private String insertNewUser(HttpServletRequest request, UserDAO userDao) throws SQLException, Exception {
        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String avatar = request.getParameter("avatar");
        String firstName = fullName != null && !fullName.isEmpty() ? fullName.split(" ")[0] : "";
        String lastName = fullName != null && fullName.split(" ").length > 1 ? String.join(" ", (CharSequence[]) Arrays.copyOfRange(fullName.split(" "), 1, fullName.split(" ").length)) : "";
        int roleId = "admin".equals(role) ? 1 : 2;
        if (avatar != null && !avatar.isEmpty()) {
            avatar = UPLOAD_DIR + avatar;
        }

        UserDTO user = new UserDTO(0, firstName, lastName, email, avatar, username, password, address, phone, roleId, true);

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
        String avatar = request.getParameter("avatar");
        if (avatar != null && !avatar.isEmpty()) {
            avatar = UPLOAD_DIR + avatar;
        } else {
            UserDTO existingUser = userDao.getUserByName(username);
            avatar = existingUser != null ? existingUser.getAvatar() : "";
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
        userDao.permanentlyDeleteUser(uid);
        request.setAttribute("mess", "Xóa vĩnh viễn người dùng thành công!");
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
