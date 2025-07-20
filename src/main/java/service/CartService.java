package service;

import dao.ProductDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.CartItem;
import model.ProductDTO;

/**
 *
 * @author Group - 07
 */
public class CartService implements ICartService {

    private HashMap<Integer, CartItem> listItemsInCart = new HashMap<>();

    @Override
    public HashMap<Integer, CartItem> createCart(CartItem item) {
        listItemsInCart = new HashMap<>();
        listItemsInCart.put(item.getProduct().getId(), item);
        return listItemsInCart;
    }

    @Override
    public HashMap<Integer, CartItem> addItemToCart(CartItem item) {
        if (checkItemExist(item.getProduct())) {
            CartItem itemExist = listItemsInCart.get(item.getProduct().getId());
            itemExist.setQuantity(itemExist.getQuantity() + item.getQuantity());
            listItemsInCart.put(itemExist.getProduct().getId(), itemExist);
        } else {
            listItemsInCart.put(item.getProduct().getId(), item);
        }
        return listItemsInCart;
    }

    @Override
    public HashMap<Integer, CartItem> updateItemToCart(CartItem item) {
        if (checkItemExist(item.getProduct())) {
            CartItem itemExist = listItemsInCart.get(item.getProduct().getId());
            itemExist.setQuantity(item.getQuantity());
            listItemsInCart.put(itemExist.getProduct().getId(), itemExist);
        } else {
            listItemsInCart.put(item.getProduct().getId(), item);
        }
        return listItemsInCart;
    }

    @Override
    public boolean checkItemExist(ProductDTO product) {
        for (Integer id : listItemsInCart.keySet()) {
            if (product.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Integer, CartItem> removeItem(ProductDTO product) {
        listItemsInCart.remove(product.getId());
        return listItemsInCart;
    }

    @Override
    public Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] arrCookies = request.getCookies();
        System.out.println("=== DEBUG getCookieByName ===");
        System.out.println("Looking for cookie: " + cookieName);
        System.out.println("Total cookies in request: " + (arrCookies != null ? arrCookies.length : 0));

        if (arrCookies != null) {
            for (Cookie cookie : arrCookies) {
                System.out.println("Found cookie: name='" + cookie.getName() + "', value='" + cookie.getValue() + "', path='" + cookie.getPath() + "'");
                if (cookie.getName().equals(cookieName)) {
                    System.out.println("MATCH FOUND! Returning cookie: " + cookieName);
                    return cookie;
                }
            }
        }
        System.out.println("NO MATCH FOUND for cookie: " + cookieName);
        System.out.println("=== END DEBUG getCookieByName ===");
        return null;
    }

    @Override
    public void saveCartToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInCart) {
        String cookieName = "Cart";
        Cookie cookieCart = getCookieByName(request, cookieName);

        if (cookieCart != null) {
            cookieCart.setValue(strItemsInCart);
        } else {
            cookieCart = new Cookie(cookieName, strItemsInCart);
        }

        cookieCart.setMaxAge(60 * 60 * 24 * 3); // 3 days
        cookieCart.setPath("/ClothesShop.jdbc/"); // Match application context path
        response.addCookie(cookieCart);
    }

    // New method to save cart with custom cookie name (for user-specific carts)
    public void saveCartToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInCart, String cookieName) {
        System.out.println("@@@ ENTERING saveCartToCookie method @@@ VERSION 3.0");
        System.out.println("@@@ Method called with cookieName: " + cookieName + ", value: " + strItemsInCart);
        System.out.println("@@@ TIMESTAMP: " + System.currentTimeMillis());
        try {
            System.out.println("=== DEBUG saveCartToCookie ===");
            System.out.println("Cookie name: " + cookieName);
            System.out.println("Cookie value to save: '" + strItemsInCart + "'");

            // Always create/update cookie even if value is empty (to clear old data)
            Cookie cookieCart = new Cookie(cookieName, strItemsInCart != null ? strItemsInCart : "");
            System.out.println("DEBUG: Created cart cookie " + cookieName + " with value: " + cookieCart.getValue());

            cookieCart.setMaxAge(60 * 60 * 24 * 30 * 3); // 3 months
            cookieCart.setPath("/ClothesShop.jdbc/"); // Match application context path
            response.addCookie(cookieCart);
            System.out.println("DEBUG: Cart cookie " + cookieName + " saved with path=/ClothesShop.jdbc/ and maxAge=" + (60 * 60 * 24 * 30 * 3));
            System.out.println(">>> CART COOKIE SAVE STACK TRACE:");
            for (StackTraceElement elem : Thread.currentThread().getStackTrace()) {
                if (elem.getClassName().contains("controller") || elem.getClassName().contains("Servlet")) {
                    System.out.println("    " + elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber());
                }
            }
            System.out.println("=== END DEBUG saveCartToCookie ===");
        } catch (Exception e) {
            System.out.println("ERROR saving cart cookie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String convertToString() {
        List<CartItem> list = new ArrayList<>(listItemsInCart.values());
        System.out.println("DEBUG convertToString: listItemsInCart size = " + listItemsInCart.size());
        String result = "";
        for (CartItem item : list) {
            System.out.println("DEBUG: Converting item - ProductID: " + item.getProduct().getId() + ", Quantity: " + item.getQuantity());
            result += item.getProduct().getId() + "," + item.getQuantity() + ",";
        }
        // Remove trailing comma
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        System.out.println("DEBUG convertToString result: " + result);
        return result;
    }

    // Stateless method to convert cart list to string
    public String convertCartToString(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return "";
        }

        String result = "";
        for (CartItem item : cartItems) {
            System.out.println("DEBUG: Converting item - ProductID: " + item.getProduct().getId() + ", Quantity: " + item.getQuantity());
            result += item.getProduct().getId() + "," + item.getQuantity() + ",";
        }
        // Remove trailing comma
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        System.out.println("DEBUG convertCartToString result: " + result);
        return result;
    }

    @Override
    public List<CartItem> getCartFromCookie(Cookie cookieCart) {
        ProductDAO pDao = new ProductDAO();
        List<CartItem> listItemsCart = new ArrayList<>();
        String inputString = cookieCart.getValue();

        System.out.println("=== DEBUG getCartFromCookie ===");
        System.out.println("Raw cookie value: " + inputString);

        // Check for null or empty cookie value
        if (inputString == null || inputString.trim().isEmpty()) {
            System.out.println("Cookie value is null or empty, returning empty list");
            return listItemsCart;
        }

        if (inputString.startsWith("[") && inputString.endsWith("]")) {
            inputString = inputString.substring(1, inputString.length() - 1);
            System.out.println("Removed brackets: " + inputString);
        }

        // Check again after removing brackets
        if (inputString.trim().isEmpty()) {
            System.out.println("After removing brackets, string is empty");
            return listItemsCart;
        }

        // CONVERT DASH BACK TO COMMA FOR PROCESSING
        inputString = inputString.replace("-", ",");
        System.out.println("Converted dashes to commas: " + inputString);

        String[] elements = inputString.split(",");
        System.out.println("Split into " + elements.length + " elements");

        for (int i = 0; i < elements.length; i++) {
            System.out.println("Element[" + i + "] = '" + elements[i] + "'");
        }

        List<ProductDTO> products = new ArrayList<>();
        for (int i = 0; i < elements.length; i += 2) {
            // Validate element before parsing
            if (i < elements.length && elements[i] != null && !elements[i].trim().isEmpty()) {
                try {
                    ProductDTO product = pDao.getProductByID(Integer.parseInt(elements[i].trim()));
                    products.add(product);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid product ID in cart cookie: " + elements[i]);
                }
            }
        }

        List<Integer> quantities = new ArrayList<>();
        for (int i = 1; i < elements.length; i += 2) {
            // Validate element before parsing
            if (i < elements.length && elements[i] != null && !elements[i].trim().isEmpty()) {
                try {
                    quantities.add(Integer.parseInt(elements[i].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid quantity in cart cookie: " + elements[i]);
                    quantities.add(1); // Default quantity
                }
            }
        }

        // Create CartItems only if we have valid products and quantities
        int minSize = Math.min(products.size(), quantities.size());
        System.out.println("Creating " + minSize + " cart items from " + products.size() + " products and " + quantities.size() + " quantities");
        for (int i = 0; i < minSize; i++) {
            if (products.get(i) != null) { // Additional null check for product
                CartItem item = new CartItem(products.get(i), quantities.get(i));
                listItemsCart.add(item);
                System.out.println("Created cart item: ProductID=" + products.get(i).getId() + ", Quantity=" + quantities.get(i));
            }
        }

        // Load to current working data
        loadCartData(listItemsCart);
        System.out.println("Final cart size: " + listItemsCart.size());
        System.out.println("=== END DEBUG getCartFromCookie ===");
        return listItemsCart;
    }

    // New method to get cart from cookie by name (for user-specific carts)
    public List<CartItem> getCartFromCookie(HttpServletRequest request, String cookieName) {
        Cookie cookieCart = getCookieByName(request, cookieName);
        if (cookieCart != null) {
            return getCartFromCookie(cookieCart);
        }

        // Return empty list if no cookie found
        listItemsInCart.clear();
        return new ArrayList<>();
    }

    // Method to load existing cart data (similar to WishlistService)
    public void loadCartData(List<CartItem> existingCart) {
        listItemsInCart.clear();
        System.out.println("DEBUG loadCartData: Input cart size = " + (existingCart != null ? existingCart.size() : 0));
        if (existingCart != null) {
            for (CartItem item : existingCart) {
                System.out.println("DEBUG: Loading item - ProductID: " + item.getProduct().getId() + ", Quantity: " + item.getQuantity());
                listItemsInCart.put(item.getProduct().getId(), item);
            }
        }
        System.out.println("DEBUG loadCartData: Final listItemsInCart size = " + listItemsInCart.size());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        CartService cartService = new CartService();
        ProductDAO pDao = new ProductDAO();
        ProductDTO product1 = pDao.getProductByID(1);
        ProductDTO product2 = pDao.getProductByID(2);
        CartItem item1 = new CartItem(product1, 2);
        CartItem item2 = new CartItem(product2, 2);

        HashMap<Integer, CartItem> carts = cartService.createCart(item1);
        carts = cartService.addItemToCart(item2);
        List<CartItem> list = new ArrayList<>(carts.values());
        System.out.println(list.toString());
    }
}
