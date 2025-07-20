package controller.cart_wishlist;

import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.ProductDTO;
import model.UserDTO;
import service.IWishlistService;
import service.WishlistService;

/**
 *
 * @author Group - 07
 */
public class WishlistServlet extends HttpServlet {

    private static final String WISHLIST_PAGE = "WEB-INF/home/wishlist.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Default URL is for AJAX requests
        String destination = WISHLIST_PAGE;
        boolean shouldForward = true; // Flag để kiểm soát việc forward

        // Initialize tools and variables
        ProductDAO productDAO = new ProductDAO();
        IWishlistService wishlistService = new WishlistService(); // Use interface and implementation
        HttpSession session = request.getSession();
        UserDTO account = (UserDTO) session.getAttribute("account");
        
        // Check if user is logged in
        if (account == null) {
            response.sendRedirect("home?btnAction=Login");
            return;
        }
        
        List<ProductDTO> wishlist = null;
        HashMap<Integer, ProductDTO> wishlistItems = null;

        try {
            // Get the action parameter (Add, Delete, or null)
            String action = request.getParameter("action");

            // If no action, show the wishlist page
            if (action == null) {
                destination = WISHLIST_PAGE;
                
                // Kiểm tra session trước, nếu có rồi thì dùng session
                wishlist = (List<ProductDTO>) session.getAttribute("WISHLIST");
                
                if (wishlist == null) {
                    // Nếu session không có, mới load từ cookie
                    String cookieName = "Wishlist_" + account.getId(); // User-specific cookie
                    log("No wishlist in session, loading from cookie: " + cookieName);
                    wishlist = wishlistService.getWishlistFromCookie(request, cookieName);
                    session.setAttribute("WISHLIST", wishlist);
                    log("Loaded " + (wishlist != null ? wishlist.size() : 0) + " items from cookie");
                } else {
                    log("Using existing wishlist from session: " + wishlist.size() + " items");
                }
                
                // Save back to cookie to refresh expiry time và đồng bộ
                if (wishlist != null && !wishlist.isEmpty()) {
                    String wishlistString = "";
                    for (ProductDTO product : wishlist) {
                        wishlistString += product.getId() + ",";
                    }
                    String cookieName = "Wishlist_" + account.getId();
                    wishlistService.saveWishlistToCookie(request, response, wishlistString, cookieName);
                }
            } else {
                // Get product ID and fetch the product
                String productId = request.getParameter("product_id");
                if (productId == null || productId.trim().isEmpty()) {
                    log("WishlistServlet: Invalid product_id parameter");
                    destination = WISHLIST_PAGE;
                } else {
                    try {
                        ProductDTO product = productDAO.getProductByID(Integer.parseInt(productId));

                // Handle "Add" action
                if ("Add".equals(action)) {
                    // Load existing wishlist from session or cookie first
                    wishlist = (List<ProductDTO>) session.getAttribute("WISHLIST");
                    if (wishlist == null) {
                        // Load from cookie if not in session
                        String cookieName = "Wishlist_" + account.getId();
                        wishlist = wishlistService.getWishlistFromCookie(request, cookieName);
                    }
                    
                    // Convert List to HashMap
                    HashMap<Integer, ProductDTO> currentWishlistMap = new HashMap<>();
                    if (wishlist != null) {
                        for (ProductDTO p : wishlist) {
                            currentWishlistMap.put(p.getId(), p);
                        }
                    }
                    
                    // Add new product using stateless method
                    wishlistItems = wishlistService.addItemToWishlist(currentWishlistMap, product);
                    
                    // Update wishlist in session
                    wishlist = new ArrayList<>(wishlistItems.values());
                    session.setAttribute("WISHLIST", wishlist);

                    // Save user-specific wishlist to cookie
                    String wishlistString = wishlistService.convertToString(wishlistItems);
                    String cookieName = "Wishlist_" + account.getId(); // User-specific cookie
                    wishlistService.saveWishlistToCookie(request, response, wishlistString, cookieName);
                    
                    // Redirect back to the referring page instead of wishlist
                    String referer = request.getHeader("Referer");
                    if (referer != null && !referer.isEmpty()) {
                        response.sendRedirect(referer);
                    } else {
                        response.sendRedirect("shop"); // Fallback to shop page
                    }
                    shouldForward = false; // Không forward vì đã redirect
                    return;
                }
                // Handle "Delete" action
                else if ("Delete".equals(action)) {
                    // Load existing wishlist from session first
                    wishlist = (List<ProductDTO>) session.getAttribute("WISHLIST");
                    
                    // Convert List to HashMap
                    HashMap<Integer, ProductDTO> currentWishlistMap = new HashMap<>();
                    if (wishlist != null) {
                        for (ProductDTO p : wishlist) {
                            currentWishlistMap.put(p.getId(), p);
                        }
                    }
                    
                    // Remove product using stateless method
                    wishlistItems = wishlistService.removeItemFromWishlist(currentWishlistMap, product);
                    
                    // Update wishlist in session
                    wishlist = new ArrayList<>(wishlistItems.values());
                    session.setAttribute("WISHLIST", wishlist);

                    // Save user-specific wishlist to cookie
                    String wishlistString = wishlistService.convertToString(wishlistItems);
                    String cookieName = "Wishlist_" + account.getId(); // User-specific cookie
                    wishlistService.saveWishlistToCookie(request, response, wishlistString, cookieName);
                    
                        // Redirect to avoid resubmission and show updated wishlist
                        response.sendRedirect("wishlist");
                        shouldForward = false; // Không forward vì đã redirect
                        return;
                    }
                    
                    // This part should not be reached after Add or Delete actions
                    } catch (NumberFormatException e) {
                        log("WishlistServlet: Invalid number format in product_id: " + productId);
                        destination = WISHLIST_PAGE;
                    } catch (Exception e) {
                        log("WishlistServlet: Error processing product: " + e.getMessage());
                        destination = WISHLIST_PAGE;
                    }
                }
            }

        } catch (Exception ex) {
            log("WishlistServlet error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Chỉ forward nếu chưa redirect
            if (shouldForward) {
                request.getRequestDispatcher(destination).forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Handles wishlist actions like adding or removing products.";
    }
}
