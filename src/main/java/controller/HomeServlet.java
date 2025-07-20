package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import dao.TypeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.CategoryDTO;
import model.ProductDTO;
import model.SupplierDTO;
import model.TypeDTO;
import model.UserDTO;
import service.CartService;
import service.IWishlistService;
import service.WishlistService;

/**
 * HomeServlet - Servlet chính điều khiển luồng xử lý của ứng dụng Xử lý các
 * request từ người dùng và điều hướng đến các trang/servlet tương ứng
 *
 * @author Group - 07
 */
public class HomeServlet extends HttpServlet {

    // Định nghĩa các đường dẫn trang và servlet
    private final String HOME_PAGE = "WEB-INF/home/home.jsp";
    private final String LOGIN_SERVLET = "login";
    private final String WISHLIST_SERVLET = "wishlist";
    private final String REGISTER_SERVLET = "register";
    private final String SEARCH_SERVLET = "search";

    // Định nghĩa các action từ button
    private final String LOGIN_ACTION = "Login";
    private final String SEARCH_ACTION = "Search";
    private final String LOGOUT_ACTION = "Logout";
    private final String REGISTER_ACTION = "Register";
    private final String ADD_TO_WISHLIST_ACTION = "AddToWishList";

    /**
     * Phương thức xử lý request chính. Xác định action và điều hướng đến
     * trang/servlet tương ứng
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập encoding UTF-8 cho response
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String destination = HOME_PAGE; // Trang mặc định là trang chủ
        boolean shouldForward = true; // Flag để kiểm soát việc forward

        try {
            String buttonClicked = request.getParameter("btnAction");
            HttpSession session = request.getSession();

            // Xử lý các action khác nhau
            if (buttonClicked == null) {
                // Không có action -> hiển thị trang chủ
                loadHomePageData(request, response);
                request.setAttribute("CURRENTSERVLET", "Home");
            } else if (buttonClicked.equals(LOGOUT_ACTION)) {
                // Xử lý đăng xuất
                try {
                    // Lưu cart và wishlist vào cookie trước khi logout
                    UserDTO account = (UserDTO) session.getAttribute("account");
                    if (account != null) {
                        log("DEBUG: Logout for user ID: " + account.getId() + ", username: " + account.getUserName());
                        
                        // Lưu Cart - Always save (even if empty) to update cookie
                        List<CartItem> currentCart = (List<CartItem>) session.getAttribute("CART");
                        CartService cartService = new CartService();
                        String cartString = cartService.convertCartToString(currentCart);
                        // REPLACE COMMA WITH DASH TO AVOID COOKIE ISSUE
                        cartString = cartString.replace(",", "-");
                        String cartCookieName = "Cart_" + account.getId();
                        
                        try {
                            // Create cookie manually
                            Cookie cartCookie = new Cookie(cartCookieName, cartString);
                            cartCookie.setMaxAge(60 * 60 * 24 * 30 * 3); // 3 months
                            cartCookie.setPath("/ClothesShop.jdbc/");
                            response.addCookie(cartCookie);
                        } catch (Exception saveError) {
                            saveError.printStackTrace();
                        }
                        
                        // Lưu Wishlist
                        List<ProductDTO> currentWishlist = (List<ProductDTO>) session.getAttribute("WISHLIST");
                        if (currentWishlist != null && !currentWishlist.isEmpty()) {
                            // Convert wishlist to string using underscore separator (comma not allowed in cookies)
                            String wishlistString = "";
                            for (ProductDTO product : currentWishlist) {
                                wishlistString += product.getId() + "_";
                            }
                            
                            // Save to user-specific cookie TRƯỚC KHI invalidate session
                            IWishlistService wishlistService = new WishlistService();
                            String cookieName = "Wishlist_" + account.getId();
                            
                            try {
                                wishlistService.saveWishlistToCookie(request, response, wishlistString, cookieName);
                            } catch (Exception saveError) {
                                saveError.printStackTrace();
                            }
                        }
                    }
                    
                    // Invalidate session SAU KHI đã lưu wishlist
                    session.invalidate();
                    
                    // Redirect về home thay vì forward để tránh session cũ
                    response.sendRedirect("home");
                    shouldForward = false; // Không forward vì đã redirect
                    return; // Quan trọng: return để không tiếp tục xử lý
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    // Vẫn tiến hành logout dù có lỗi
                    try {
                        if (session != null) {
                            session.invalidate();
                        }
                    } catch (Exception ex) {
                        // Ignore
                    }
                    response.sendRedirect("home");
                    shouldForward = false; // Không forward vì đã redirect
                    return;
                }
            } else if (buttonClicked.equals(LOGIN_ACTION)) {
                // Chuyển đến trang đăng nhập
                destination = LOGIN_SERVLET;
            } else if (buttonClicked.equals(REGISTER_ACTION)) {
                // Chuyển đến trang đăng ký
                destination = REGISTER_SERVLET;
            } else if (buttonClicked.equals(SEARCH_ACTION)) {
                // Chuyển đến trang tìm kiếm
                destination = SEARCH_SERVLET;
            } else if (buttonClicked.equals(ADD_TO_WISHLIST_ACTION)) {
                // Chuyển đến trang wishlist
                destination = WISHLIST_SERVLET;
            } else if (buttonClicked.equals("Chat")) {
                // Xử lý chat dựa vào role của user
                if (session.getAttribute("account") != null) {
                    UserDTO user = (UserDTO) session.getAttribute("account");
                    destination = user.getRoleID() == 1 ? "chat_admin.jsp" : "chat_user.jsp";
                } else {
                    destination = LOGIN_SERVLET;
                }
            }
        } catch (Exception error) {
            log("Error in DispatchServlet: " + error.getMessage());
        } finally {
            // Chỉ forward nếu chưa redirect
            if (shouldForward) {
                request.getRequestDispatcher(destination).forward(request, response);
            }
        }
    }

    /**
     * Phương thức tải dữ liệu cho trang chủ. Lấy danh sách sản phẩm, danh mục,
     * nhà cung cấp và loại sản phẩm
     */
    protected void loadHomePageData(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Khởi tạo các DAO
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            SupplierDAO supplierDAO = new SupplierDAO();
            TypeDAO typeDAO = new TypeDAO();

            // Lấy dữ liệu từ database
            List<ProductDTO> allProducts = productDAO.getData();
            List<CategoryDTO> categories = categoryDAO.getData();
            List<SupplierDTO> suppliers = supplierDAO.getData();
            List<ProductDTO> newProducts = productDAO.getProductNew();
            List<ProductDTO> bestSellers = productDAO.getProductsBestSeller();
            List<TypeDTO> types = typeDAO.getAllType();

            // Debug log cho suppliers
//            log("Number of suppliers loaded: " + (suppliers != null ? suppliers.size() : 0));
//            if (suppliers != null) {
//                for (SupplierDTO supplier : suppliers) {
//                    log("Supplier data - ID: " + supplier.getId() + 
//                        ", Name: " + supplier.getName() + 
//                        ", Image path: " + supplier.getImage() + 
//                        ", Full URL: " + request.getContextPath() + "/" + supplier.getImage());
//                }
//            }

            request.setAttribute("LIST_PRODUCTS", allProducts);
            request.setAttribute("LIST_TYPES", types);
            request.setAttribute("LIST_CATEGORIESS", categories);
            request.setAttribute("LIST_SUPPLIERS", suppliers);
            request.setAttribute("LIST_PRODUCTS_NEW", newProducts);
            if (bestSellers != null) {
                log("Found " + bestSellers.size() + " best seller products");
            } else {
                log("bestSellers is null");
            }
            request.setAttribute("LIST_PRODUCTS_SELLER", bestSellers);
        } catch (Exception error) {
            log("Error loading home page data: " + error.getMessage());
            error.printStackTrace();
//            error.printStackTrace(); // In stack trace để debug
        }
    }

    /**
     * Xử lý GET request. Kiểm tra và tải giỏ hàng và wishlist từ session hoặc
     * cookie
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CartService cartService = new CartService();
        IWishlistService wishlistService = new WishlistService();

        try {
            HttpSession session = request.getSession();
            List<CartItem> cartItems = null;
            List<ProductDTO> wishlistItems = null;

            // Kiểm tra và tải giỏ hàng từ session hoặc cookie
            UserDTO account = (UserDTO) session.getAttribute("account");
            
            // Only load cart from cookie if session doesn't have it OR if it's empty
            List<CartItem> existingCart = (List<CartItem>) session.getAttribute("CART");
            if (existingCart == null || existingCart.isEmpty()) {
                if (account != null) {
                    // User đã đăng nhập - tải cart theo user ID
                    String userCartCookieName = "Cart_" + account.getId();
                    log("Loading cart from cookie for user " + account.getId() + " (session cart was " + (existingCart == null ? "null" : "empty") + ")");
                    cartItems = cartService.getCartFromCookie(request, userCartCookieName);
                    if (cartItems != null && !cartItems.isEmpty()) {
                        log("Loaded " + cartItems.size() + " items from user cart cookie");
                    } else {
                        log("No cart items found in user cookie");
                        cartItems = new ArrayList<>();
                    }
                } else {
                    // Guest user - tải cart chung
                    Cookie cartCookie = cartService.getCookieByName(request, "Cart");
                    if (cartCookie != null) {
                        cartItems = cartService.getCartFromCookie(cartCookie);
                        log("Loaded cart from guest cookie");
                    } else {
                        cartItems = new ArrayList<>();
                    }
                }
            } else {
                cartItems = existingCart;
                log("Using existing cart from session: " + cartItems.size() + " items");
            }

            // Kiểm tra và tải wishlist từ session hoặc cookie (chỉ cho user đã đăng nhập)
            if (account != null && session.getAttribute("WISHLIST") == null) {
                String userWishlistCookieName = "Wishlist_" + account.getId();
                log("Loading wishlist from cookie for user " + account.getId());
                wishlistItems = wishlistService.getWishlistFromCookie(request, userWishlistCookieName);
                if (wishlistItems != null && !wishlistItems.isEmpty()) {
                    log("Loaded " + wishlistItems.size() + " items from wishlist cookie");
                } else {
                    log("No wishlist items found in cookie");
                }
            } else if (account != null) {
                wishlistItems = (List<ProductDTO>) session.getAttribute("WISHLIST");
                log("Using wishlist from session: " + (wishlistItems != null ? wishlistItems.size() : 0) + " items");
            } else {
                // Guest không có wishlist
                wishlistItems = new ArrayList<>();
                log("Guest user - no wishlist");
            }

            // Cập nhật session với giỏ hàng và wishlist
            session.setAttribute("CART", cartItems);
            log("HomeServlet doGet: Set CART in session with " + (cartItems != null ? cartItems.size() : 0) + " items");
            if (account != null) {
                session.setAttribute("WISHLIST", wishlistItems);
            }

        } catch (Exception error) {
            log("Error in doGet: " + error.getMessage());
        }

        processRequest(request, response);
    }

    /**
     * Xử lý POST request. Chuyển tiếp đến processRequest
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Main home controller for clothing store";
    }// </editor-fold>

}
