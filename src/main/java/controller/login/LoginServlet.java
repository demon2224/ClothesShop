/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.login;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import model.CartItem;
import model.ProductDTO;
import model.UserDTO;
import service.CartService;
import service.WishlistService;

/**
 *
 * @author Group - 07
 */
public class LoginServlet extends HttpServlet {

    private final String WELCOME = "home";
    private final String LOGIN = "WEB-INF/home/login.jsp";
    private final String ADMIN_DASHBOARD = "admin";

    // Xử lý HTTP GET request
    // kiem tra ma xac thuc tu google
    //nếu có mã , gọi handleGoogleLogin để xử lý đăng nhập với Google.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.getRequestDispatcher(LOGIN).forward(request, response);
        } catch (Exception ex) {
            log("LoginServlet error:" + ex.getMessage());
        }
    }

    // Xử lý HTTP POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");

            UserDAO userDAO = new UserDAO();
            String hashedPassword = encryptPassword(password);
            UserDTO user = userDAO.checkLogin(username, hashedPassword);

            if (user == null) {
                request.setAttribute("msg", "Tên người dùng hoặc mật khẩu không hợp lệ!");
                request.setAttribute("uName", username);
                request.getRequestDispatcher(LOGIN).forward(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("account", user);

                // Load user-specific wishlist and cart
                loadUserWishlist(request, response, user);
                loadUserCart(request, response, user);

                // Check user role and redirect
                if (user.getRoleID() == 1) {
                    response.sendRedirect("admin");
                } else {
                    response.sendRedirect("home");
                }
            }
        } catch (Exception e) {
            log("Error in LoginServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("msg", "An error occurred during login!");
            request.getRequestDispatcher(LOGIN).forward(request, response);
        }
    }

    // Method để load wishlist từ cookie khi user login
    private void loadUserWishlist(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
        try {
            WishlistService wishlistService = new WishlistService();
            String cookieName = "Wishlist_" + user.getId();
            List<ProductDTO> wishlistItems = wishlistService.getWishlistFromCookie(request, cookieName);
            if (wishlistItems != null) {
                HttpSession session = request.getSession();
                session.setAttribute("WISHLIST", wishlistItems);
            }
        } catch (Exception e) {
            log("Error loading user wishlist: " + e.getMessage());
        }
    }

    // Method để load cart từ cookie khi user login
    private void loadUserCart(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
        try {
            CartService cartService = new CartService();
            String cookieName = "Cart_" + user.getId();
            List<CartItem> cartItems = cartService.getCartFromCookie(request, cookieName);
            if (cartItems != null) {
                HttpSession session = request.getSession();
                session.setAttribute("CART", cartItems);
            }
        } catch (Exception e) {
            log("Error loading user cart: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }

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
}
