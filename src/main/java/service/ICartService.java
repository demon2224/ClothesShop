package service;

import java.util.HashMap;
import java.util.List;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CartItem;
import model.ProductDTO;

/**
 *
 * @author Group - 07
 */
public interface ICartService {

    HashMap<Integer, CartItem> createCart(CartItem item);

    HashMap<Integer, CartItem> addItemToCart(CartItem item);

    HashMap<Integer, CartItem> updateItemToCart(CartItem item);

    boolean checkItemExist(ProductDTO product);

    HashMap<Integer, CartItem> removeItem(ProductDTO product);

    Cookie getCookieByName(HttpServletRequest request, String cookieName);

    void saveCartToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInCart);

    String convertToString();

    List<CartItem> getCartFromCookie(Cookie cookieCart);
}
