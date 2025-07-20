package service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import model.ProductDTO;

/**
 *
 * @author Group - 07
 */
public interface IWishlistService {

    HashMap<Integer, ProductDTO> createWishlist(ProductDTO item);

    HashMap<Integer, ProductDTO> addItemToWishlist(ProductDTO item);

    boolean checkItemExist(ProductDTO product);

    HashMap<Integer, ProductDTO> removeItem(ProductDTO product);
    
    // New methods with HashMap parameter for stateless operations
    HashMap<Integer, ProductDTO> addItemToWishlist(HashMap<Integer, ProductDTO> currentWishlist, ProductDTO item);
    
    HashMap<Integer, ProductDTO> removeItemFromWishlist(HashMap<Integer, ProductDTO> currentWishlist, ProductDTO item);
    
    boolean checkItemExist(HashMap<Integer, ProductDTO> currentWishlist, ProductDTO product);
    
    String convertToString(HashMap<Integer, ProductDTO> wishlist);

    Cookie getCookieByName(HttpServletRequest request, String cookieName);

    void saveWishlistToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInWishList);

    void saveWishlistToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInWishList, String cookieName);

    String convertToString();

    List<ProductDTO> getWishlistFromCookie(Cookie cookieWishlist);
    
    List<ProductDTO> getWishlistFromCookie(HttpServletRequest request, String cookieName);
}
