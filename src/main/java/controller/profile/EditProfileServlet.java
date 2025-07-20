package controller.profile;

import dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;

/**
 *
 * @author Group - 07
 */
public class EditProfileServlet extends HttpServlet {

    private final String PROFILE_PAGE = "WEB-INF/home/my-account.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form chỉnh sửa
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("account");

        if (user != null) {
            request.setAttribute("EDIT_MODE", true); // Đánh dấu đang ở chế độ chỉnh sửa
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } else {
            response.sendRedirect("login"); // Redirect nếu chưa đăng nhập
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try {
            // Get current user from session
            HttpSession session = request.getSession(false);
            UserDTO currentUser = (UserDTO) session.getAttribute("account");

            if (currentUser == null) {
                response.sendRedirect("login");
                return;
            }

            // Get user input from the form
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String avatar = request.getParameter("avatar");

            // Validate input
            if (firstName == null || lastName == null || email == null
                    || firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty()) {
                request.setAttribute("ERROR", "All fields are required!");
                request.setAttribute("EDIT_MODE", true);
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                return;
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                request.setAttribute("ERROR", "Invalid email format!");
                request.setAttribute("EDIT_MODE", true);
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                return;
            }

            // Validate phone number (if provided)
            if (phone != null && !phone.trim().isEmpty() && !phone.matches("\\d{10,12}")) {
                request.setAttribute("ERROR", "Invalid phone number format!");
                request.setAttribute("EDIT_MODE", true);
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                return;
            }

            // Update user in database
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.updateUser(
                firstName, 
                lastName, 
                email, 
                address, 
                phone, 
                currentUser.getUserName(),
                avatar != null ? avatar : currentUser.getAvatar(),
                currentUser.getRoleID()
            );

            if (success) {
                // Update session object
                currentUser.setFirstName(firstName);
                currentUser.setLastName(lastName);
                currentUser.setEmail(email);
                currentUser.setAddress(address);
                currentUser.setPhone(phone);
                if (avatar != null) {
                    currentUser.setAvatar(avatar);
                }
                session.setAttribute("account", currentUser);
                
                request.setAttribute("STATUS", "Profile updated successfully!");
            } else {
                request.setAttribute("ERROR", "Failed to update profile! Please try again.");
                request.setAttribute("EDIT_MODE", true);
            }
            
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);

        } catch (SQLException ex) {
            log("EditProfileServlet SQL Error: " + ex.getMessage());
            request.setAttribute("ERROR", "Database error occurred. Please try again later.");
            request.setAttribute("EDIT_MODE", true);
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } catch (Exception e) {
            log("EditProfileServlet Error: " + e.getMessage());
            request.setAttribute("ERROR", "An unexpected error occurred. Please try again later.");
            request.setAttribute("EDIT_MODE", true);
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user profile updates";
    }
}
