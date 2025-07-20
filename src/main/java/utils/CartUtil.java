package utils;

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
public class CartUtil {

    private static HashMap<Integer, CartItem> listItemsInCart = new HashMap<>();

    public HashMap<Integer, CartItem> createCart(CartItem item) {
        listItemsInCart = new HashMap<>();
        listItemsInCart.put(item.getProduct().getId(), item);
        return listItemsInCart;
    }

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

    public boolean checkItemExist(ProductDTO product) {
        for (Integer id : listItemsInCart.keySet()) {
            if (product.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer, CartItem> removeItem(ProductDTO product) {
        listItemsInCart.remove(product.getId());
        return listItemsInCart;
    }

    // Xu li voi cookie
    public Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] arrCookies = request.getCookies();
        if (arrCookies != null) {
            for (Cookie cookie : arrCookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void saveCartToCookie(HttpServletRequest request,
            HttpServletResponse response, String strItemsInCart) {
        String cookieName = "Cart";

        Cookie cookieCart = getCookieByName(request, cookieName);

        if (cookieCart != null) {

            cookieCart.setValue(strItemsInCart);

        } else {

            cookieCart = new Cookie(cookieName, strItemsInCart);

        }

        cookieCart.setMaxAge(60 * 60 * 24 * 30 * 3);

        response.addCookie(cookieCart);

    }

    public String convertToString() {
        List<CartItem> list = new ArrayList<>(listItemsInCart.values());
        return list.toString();

    }

    public List<CartItem> getCartFromCookie(Cookie cookieCart) {
        ProductDAO pDao = new ProductDAO();
        List<CartItem> listItemsCart = new ArrayList<>();
        String inputString = cookieCart.getValue();
        
        // Check for null or empty cookie value
        if (inputString == null || inputString.trim().isEmpty()) {
            return listItemsCart;
        }
        
        if (inputString.startsWith("[") && inputString.endsWith("]")) {
            inputString = inputString.substring(1, inputString.length() - 1);
        }

        // Check again after removing brackets
        if (inputString.trim().isEmpty()) {
            return listItemsCart;
        }

        // Chia chuỗi thành các phần tử
        String[] elements = inputString.split(",");

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
        List<Integer> quantitys = new ArrayList<>();
        for (int i = 1; i < elements.length; i += 2) {
            // Validate element before parsing
            if (i < elements.length && elements[i] != null && !elements[i].trim().isEmpty()) {
                try {
                    quantitys.add(Integer.parseInt(elements[i].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid quantity in cart cookie: " + elements[i]);
                    quantitys.add(1); // Default quantity
                }
            }
        }
        
        // Lấy ra ListCartItem - only if we have valid products and quantities
        int minSize = Math.min(products.size(), quantitys.size());
        for (int i = 0; i < minSize; i++) {
            if (products.get(i) != null) { // Additional null check for product
                CartItem item = new CartItem(products.get(i), quantitys.get(i));
                listItemsCart.add(item);
            }
        }

        // add listItems to util
        for (CartItem cartItem : listItemsCart) {
            addItemToCart(cartItem);
        }
        return listItemsCart;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        CartUtil cartUtil = new CartUtil();
        ProductDAO pDao = new ProductDAO();
        ProductDTO product1 = pDao.getProductByID(1);
        ProductDTO product2 = pDao.getProductByID(2);
        CartItem item1 = new CartItem(product1, 2);
        CartItem item2 = new CartItem(product2, 2);

        HashMap<Integer, CartItem> carts = cartUtil.createCart(item1);
        carts = cartUtil.addItemToCart(item2);
        List<CartItem> list = new ArrayList<>(carts.values());
        System.out.println(list.toString());
    }
}
