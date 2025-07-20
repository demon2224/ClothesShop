package controller.cart_wishlist;

import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.CartItem;
import model.ProductDTO;
import model.UserDTO;
import service.CartService;
import service.IWishlistService;
import service.WishlistService;

/**
 *
 * @author Group - 07
 */
public class CartServlet extends HttpServlet {

    private static final String CART_PAGE = "WEB-INF/home/cart.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = CART_PAGE;
        boolean shouldForward = true; // Flag để kiểm soát việc forward
        ProductDAO pDao = new ProductDAO();
        CartService cartService = new CartService();  // Use CartService instead of CartUtil
        List<CartItem> carts = null;
        HashMap<Integer, CartItem> listItem = null;

        IWishlistService wishlistService = new WishlistService(); // Thay WishlistUtil bằng IWishlistService
        List<ProductDTO> wishlists = null;
        HashMap<Integer, ProductDTO> listWishlist = null;

        try {
            HttpSession session = request.getSession();
            String action = request.getParameter("action");

            // Load existing cart into CartService before processing actions
            carts = (List<CartItem>) session.getAttribute("CART");
            if (carts != null) {
                cartService.loadCartData(carts);
            }

            if (action == null) {
                url = CART_PAGE;
            } else {
                String product_id = request.getParameter("product_id");
                if (product_id == null || product_id.trim().isEmpty()) {
                    log("CartServlet: Invalid product_id parameter");
                    url = CART_PAGE;
                } else {
                    try {
                        ProductDTO product = pDao.getProductByID(Integer.parseInt(product_id));

                        if ("Add".equals(action)) {
                            String quantity = request.getParameter("quantity");
                            if (quantity == null || quantity.trim().isEmpty()) {
                                quantity = "1"; // Default quantity
                            }
                            CartItem item = new CartItem(product, Integer.parseInt(quantity));

                            // Existing cart is already loaded, just add the item
                            if (carts == null || carts.isEmpty()) {
                                listItem = cartService.createCart(item);
                            } else {
                                listItem = cartService.addItemToCart(item);
                            }

                            // Update session with cart data immediately
                            if (listItem != null) {
                                carts = new ArrayList<>(listItem.values());
                                session.setAttribute("CART", carts);
                            }

                            // Save cart to cookie immediately after adding
                            String strCarts = cartService.convertCartToString(carts);
                            Object accountObj = session.getAttribute("account");
                            if (accountObj != null) {
                                model.UserDTO user = (model.UserDTO) accountObj;
                                String cartCookieName = "Cart_" + user.getId();
                                String cookieValue = strCarts.replace(",", "-");
                                cartService.saveCartToCookie(request, response, cookieValue, cartCookieName);
                            }

                            // Redirect back to the referring page instead of cart page
                            String referer = request.getHeader("Referer");
                            if (referer != null && !referer.isEmpty()) {
                                response.sendRedirect(referer);
                            } else {
                                response.sendRedirect("shop"); // Fallback to shop page
                            }
                            shouldForward = false; // Không forward vì đã redirect
                            return;

                        } else if ("Delete".equals(action)) {
                            String curPage = request.getParameter("curPage");
                            if ("cart.jsp".equals(curPage) || "header.jsp".equals(curPage)) {
                                url = CART_PAGE;
                            }
                            listItem = cartService.removeItem(product);
                        } else if ("Update".equals(action)) {
                            url = CART_PAGE;
                            String quantity = request.getParameter("quantity");
                            if (quantity == null || quantity.trim().isEmpty()) {
                                quantity = "1"; // Default quantity
                            }
                            CartItem item = new CartItem(product, Integer.parseInt(quantity));
                            listItem = cartService.updateItemToCart(item);
                        }
                    } catch (NumberFormatException e) {
                        log("CartServlet: Invalid number format in parameters: " + e.getMessage());
                        url = CART_PAGE;
                    } catch (Exception e) {
                        log("CartServlet: Error processing product: " + e.getMessage());
                        url = CART_PAGE;
                    }
                }
            }

            // Update session with cart data
            if (listItem != null) {
                carts = new ArrayList<>(listItem.values());
            } else {
                // Keep existing cart if no action was performed
                carts = (List<CartItem>) session.getAttribute("CART");
                if (carts == null) {
                    carts = new ArrayList<>();
                }
            }
            session.setAttribute("CART", carts);

            // Save cart with user-specific cookie name - Use stateless method
            String strCarts = cartService.convertCartToString(carts);
            log("CartServlet: About to save cart with " + (carts != null ? carts.size() : 0) + " items");
            log("CartServlet: Converted cart string: '" + strCarts + "'");

            HttpSession session2 = request.getSession();
            Object accountObj = session2.getAttribute("account");

            // Always save cookie for logged-in users (even if cart is empty to clear old data)
            if (accountObj != null) {
                model.UserDTO user = (model.UserDTO) accountObj;
                String cartCookieName = "Cart_" + user.getId();

                // Convert commas to dashes for cookie compatibility
                String cookieValue = strCarts.replace(",", "-");
                cartService.saveCartToCookie(request, response, cookieValue, cartCookieName);
                log("CartServlet: Saved cart to user-specific cookie: " + cartCookieName + " with value: " + cookieValue);
                log("CartServlet: Original cart string was: '" + strCarts + "'");
            } else {
                log("CartServlet: No user logged in, skipping cart cookie save");

                // Fallback for guest users - save to generic cart cookie only if we have a cart
                if (carts != null && !carts.isEmpty()) {
                    cartService.saveCartToCookie(request, response, strCarts);
                    log("CartServlet: Saved cart to generic cookie for guest user");
                }
            }

            // Save wishlist with user-specific cookie name
            String strWishlist = wishlistService.convertToString();
            if (accountObj != null) {
                model.UserDTO user = (model.UserDTO) accountObj;
                String wishlistCookieName = "Wishlist_" + user.getId();
                wishlistService.saveWishlistToCookie(request, response, strWishlist, wishlistCookieName);
                log("CartServlet: Saved wishlist to user-specific cookie: " + wishlistCookieName);
            } else {
                // Fallback for guest users - don't save generic wishlist cookie
                log("CartServlet: No user logged in, skipping wishlist cookie save");
            }

        } catch (Exception ex) {
            log("CartServlet error: " + ex.getMessage());
        } finally {
            // Chỉ forward nếu chưa redirect
            if (shouldForward) {
                request.getRequestDispatcher(url).forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
        return "Servlet xử lý giỏ hàng và danh sách yêu thích";
    }// </editor-fold>

}
