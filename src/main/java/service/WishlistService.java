package service;

import dao.ProductDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import model.ProductDTO;

/**
 *
 * @author Group - 07
 */
public class WishlistService implements IWishlistService {

    // Current wishlist working data  
    private HashMap<Integer, ProductDTO> currentWishlistData = new HashMap<>();

    @Override
    public HashMap<Integer, ProductDTO> createWishlist(ProductDTO item) {
        currentWishlistData = new HashMap<>();
        currentWishlistData.put(item.getId(), item);
        return currentWishlistData;
    }

    @Override
    public HashMap<Integer, ProductDTO> addItemToWishlist(ProductDTO item) {
        if (!checkItemExist(item)) {
            currentWishlistData.put(item.getId(), item);
        }
        return currentWishlistData;
    }

    @Override
    public boolean checkItemExist(ProductDTO product) {
        return currentWishlistData.containsKey(product.getId());
    }

    @Override
    public HashMap<Integer, ProductDTO> removeItem(ProductDTO product) {
        if (currentWishlistData.containsKey(product.getId())) {
            currentWishlistData.remove(product.getId());
        }
        return currentWishlistData;
    }
    
    // Method to load existing wishlist data
    public void loadWishlistData(List<ProductDTO> existingWishlist) {
        currentWishlistData.clear();
        if (existingWishlist != null) {
            for (ProductDTO product : existingWishlist) {
                currentWishlistData.put(product.getId(), product);
            }
        }
    }

    @Override
    public Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] arrCookies = request.getCookies();
        System.out.println("DEBUG: Looking for cookie: " + cookieName);
        if (arrCookies != null) {
            System.out.println("DEBUG: Found " + arrCookies.length + " cookies");
            for (Cookie cookie : arrCookies) {
                System.out.println("DEBUG: Cookie name: " + cookie.getName() + ", value: " + cookie.getValue());
                if (cookie.getName().equals(cookieName)) {
                    System.out.println("DEBUG: Found matching cookie: " + cookieName);
                    return cookie;
                }
            }
        } else {
            System.out.println("DEBUG: No cookies found in request");
        }
        System.out.println("DEBUG: Cookie " + cookieName + " not found");
        return null;
    }

    // Updated method with custom cookie name parameter
    @Override
    public void saveWishlistToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInWishList, String cookieName) {
        try {
            Cookie cookieWishlist = getCookieByName(request, cookieName);

            if (cookieWishlist != null) {
                cookieWishlist.setValue(strItemsInWishList);
                System.out.println("DEBUG: Updated existing cookie " + cookieName + " with value: " + strItemsInWishList);
            } else {
                cookieWishlist = new Cookie(cookieName, strItemsInWishList);
                System.out.println("DEBUG: Created new cookie " + cookieName + " with value: " + strItemsInWishList);
            }

            cookieWishlist.setMaxAge(60 * 60 * 24 * 3); // 3 days
            cookieWishlist.setPath("/"); // Đảm bảo cookie có thể truy cập từ mọi nơi trong app
            response.addCookie(cookieWishlist);
            System.out.println("DEBUG: Cookie " + cookieName + " saved with path=/ and maxAge=" + (60 * 60 * 24 * 3));
        } catch (Exception e) {
            System.out.println("ERROR saving cookie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Original method for backward compatibility
    @Override
    public void saveWishlistToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInWishList) {
        saveWishlistToCookie(request, response, strItemsInWishList, "Wishlist");
    }
    
    @Override
    public String convertToString() {
        List<ProductDTO> list = new ArrayList<>(currentWishlistData.values());
        String result = "";
        for (ProductDTO productDTO : list) {
            result += productDTO.getId() + "_";
        }
        return result;
    }

    @Override
    public List<ProductDTO> getWishlistFromCookie(Cookie cookieWishlist) {
        ProductDAO pDao = new ProductDAO();
        List<ProductDTO> listItemsWishlist = new ArrayList<>();
        String inputString = cookieWishlist.getValue();
        
        // Check for empty or null value first
        if (inputString == null || inputString.trim().isEmpty()) {
            loadWishlistData(listItemsWishlist);
            return listItemsWishlist;
        }
        
        // Remove trailing underscore instead of comma
        if (inputString.endsWith("_")) {
            inputString = inputString.substring(0, inputString.length() - 1);
        }

        if (inputString.length() > 0) {
            String[] elements = inputString.split("_");
            for (String element : elements) {
                // Add null/empty check for each element
                if (element != null && !element.trim().isEmpty()) {
                    try {
                        ProductDTO product = pDao.getProductByID(Integer.parseInt(element.trim()));
                        if (product != null) {
                            listItemsWishlist.add(product);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Invalid product ID in cookie: " + element);
                    }
                }
            }
        }

        // Load to current working data
        loadWishlistData(listItemsWishlist);
        return listItemsWishlist;
    }
    
    // New method to get wishlist from cookie by name
    @Override
    public List<ProductDTO> getWishlistFromCookie(HttpServletRequest request, String cookieName) {
        Cookie cookieWishlist = getCookieByName(request, cookieName);
        if (cookieWishlist != null) {
            return getWishlistFromCookie(cookieWishlist);
        }
        
        // Return empty list if no cookie found
        currentWishlistData.clear();
        return new ArrayList<>();
    }

    // New stateless methods for better control
    @Override
    public HashMap<Integer, ProductDTO> addItemToWishlist(HashMap<Integer, ProductDTO> currentWishlist, ProductDTO item) {
        if (currentWishlist == null) {
            currentWishlist = new HashMap<>();
        }
        if (!checkItemExist(currentWishlist, item)) {
            currentWishlist.put(item.getId(), item);
        }
        return currentWishlist;
    }
    
    @Override
    public HashMap<Integer, ProductDTO> removeItemFromWishlist(HashMap<Integer, ProductDTO> currentWishlist, ProductDTO item) {
        if (currentWishlist != null && currentWishlist.containsKey(item.getId())) {
            currentWishlist.remove(item.getId());
        }
        return currentWishlist != null ? currentWishlist : new HashMap<Integer, ProductDTO>();
    }
    
    @Override
    public boolean checkItemExist(HashMap<Integer, ProductDTO> currentWishlist, ProductDTO product) {
        return currentWishlist != null && currentWishlist.containsKey(product.getId());
    }
    
    @Override
    public String convertToString(HashMap<Integer, ProductDTO> wishlist) {
        if (wishlist == null || wishlist.isEmpty()) {
            return "";
        }
        List<ProductDTO> list = new ArrayList<>(wishlist.values());
        String result = "";
        for (ProductDTO productDTO : list) {
            result += productDTO.getId() + "_";
        }
        return result;
    }

}
