package controller.shop;

import dao.CategoryDAO;
import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.CategoryDTO;
import model.ProductDTO;

/**
 *
 * @author Group - 07
 */
public class ShopServlet extends HttpServlet {

    private static final String SHOP_PAGE = "WEB-INF/home/shop.jsp";
    private static final String SORT_PAGE = "WEB-INF/ajax/sortproducts.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String destinationPage = SHOP_PAGE;

        try {
            ProductDAO productDao = new ProductDAO();
            CategoryDAO categoryDao = new CategoryDAO();
            List<CategoryDTO> categories = categoryDao.getData();
            List<ProductDTO> products;
            if (request.getAttribute("LISTPRODUCTS") == null) {
                products = productDao.getData();
            } else {
                products = (List<ProductDTO>) request.getAttribute("LISTPRODUCTS");
            }

            // Handle sorting
            String sortOption = request.getParameter("valueSort");
            if (sortOption != null && !sortOption.isEmpty()) {
                products = productDao.sortProduct(products, sortOption);
                destinationPage = SORT_PAGE;
            }

            int itemsPerPage = 9;
            int totalProducts = products.size();
            int totalPages = (totalProducts % itemsPerPage == 0) ? (totalProducts / itemsPerPage) : (totalProducts / itemsPerPage) + 1;
            String pageParam = request.getParameter("page");
            int currentPage = (pageParam == null) ? 1 : Integer.parseInt(pageParam);
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(currentPage * itemsPerPage, totalProducts);
            List<ProductDTO> productsForPage = productDao.getListByPage(products, startIndex, endIndex);

            request.setAttribute("DATA_FROM", "shop");
            request.setAttribute("NUMBERPAGE", totalPages);
            request.setAttribute("CURRENTPAGE", currentPage);
            request.setAttribute("LISTPRODUCTS", productsForPage);
            request.setAttribute("LISTCATEGORIES", categories);
            request.setAttribute("VALUESORT", sortOption);
            request.setAttribute("CURRENTSERVLET", "Shop");

        } catch (Exception ex) {
            log("ShopServlet error: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(destinationPage).forward(request, response);
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
        return "Handles shop page functionality including product listing, sorting, and pagination";
    }// </editor-fold>

}
