package controller.login;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDTO;
import java.security.MessageDigest;
import java.math.BigInteger;

/**
 *
 * @author Group - 07
 */
public class RegisterServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "WEB-INF/home/login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String fName = request.getParameter("firstname");
            String lName = request.getParameter("lastname");
            String uName = request.getParameter("username");
            String uPass = request.getParameter("password");
            String confirmPass = request.getParameter("confirmPassword");
            String email = request.getParameter("email");
            String action = request.getParameter("action");

            // Validate required fields
            if (isEmpty(fName) || isEmpty(lName) || isEmpty(uName) || isEmpty(uPass) || isEmpty(confirmPass) || isEmpty(email)) {
                request.setAttribute("ERROR", "Tất cả các trường đều bắt buộc!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                return;
            }
            
            // Validate password confirmation
            if (!uPass.equals(confirmPass)) {
                request.setAttribute("ERROR", "Mật khẩu và xác nhận mật khẩu không khớp!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                return;
            }
            // Validate username format (allow letters, numbers, underscore, 3-20 chars)
            if (!uName.matches("^[a-zA-Z0-9_]{3,20}$")) {
                request.setAttribute("ERROR", "Tên người dùng phải dài từ 3-20 ký tự và chỉ chứa chữ cái, số và dấu gạch dưới!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                return;
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                request.setAttribute("ERROR", "Định dạng email không hợp lệ!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                return;
            }

            // Validate password (at least 6 chars)
            if (uPass.length() < 6) {
                request.setAttribute("ERROR", "Mật khẩu phải có ít nhất 6 ký tự!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                return;
            }

            UserDAO ud = new UserDAO();

            // Check if the action is to verify if the username is a duplicate
            if (action != null && action.equals("CheckDuplicate")) {
                PrintWriter out = response.getWriter();
                boolean isDuplicate = ud.checkUserNameDuplicate(uName);
                if (isDuplicate) {
                    out.println("<h6 style='color: red'>Username already exists!</h6>");
                } else {
                    out.println("<h6 style='color: green'>Username is available!</h6>");
                }
                return;
            }

            // Check duplicate username for form submission
            if (ud.checkUserNameDuplicate(uName)) {
                request.setAttribute("ERROR", "Tên người dùng đã tồn tại!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                return;
            }

            try {
                // Mã hóa mật khẩu MD5 trước khi lưu vào database
                String hashedPassword = getMd5(uPass);

                // Create new user with hashed password
                UserDTO user = new UserDTO(0, fName, lName, email, "assets/home/img/users/user.jpg",
                        uName, hashedPassword, "", "", 2, true);
                ud.registerUser(user);

                request.setAttribute("SUCCESS", "Đăng ký thành công. Vui lòng đăng nhập!");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);

            } catch (Exception e) {
                log("RegisterServlet DB Error:" + e.getMessage());
                request.setAttribute("ERROR", "Đăng ký không thành công. Vui lòng thử lại sau.");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            }

        } catch (Exception ex) {
            log("RegisterServlet error:" + ex.getMessage());
            request.setAttribute("ERROR", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Hàm mã hóa mật khẩu sử dụng MD5
    private String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            log("MD5 Hashing Error:" + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "RegisterServlet handles new user registration";
    }// </editor-fold>

}
