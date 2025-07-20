/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
public class SearchServlet extends HttpServlet {

    private static final String SEARCH_PAGE = "WEB-INF/home/shop.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String destination = SEARCH_PAGE;

        try {
            // Get parameters
            String searchText = request.getParameter("txtSearch");
            String sortValue = request.getParameter("valueSort");
            String pageParam = request.getParameter("page");
            String[] categoryIds = request.getParameterValues("id_filter");
            String[] colors = request.getParameterValues("color");
            String priceFromParam = request.getParameter("pricefrom");
            String priceToParam = request.getParameter("priceto");
            String discountParam = request.getParameter("discount");

            // Validate searchText
            if (searchText == null || searchText.trim().isEmpty()) {
                request.getRequestDispatcher("ShopServlet").forward(request, response);
                return;
            }

            // Fetch initial search results
            ProductDAO productDAO = new ProductDAO();
            List<ProductDTO> products = productDAO.getProductBySearch(searchText);

            // Apply filters if provided
            if (categoryIds != null && categoryIds.length > 0) {
                int[] ids = new int[categoryIds.length];
                for (int i = 0; i < categoryIds.length; i++) {
                    ids[i] = Integer.parseInt(categoryIds[i]);
                }
                products = productDAO.searchByCheckBox(products, ids);
            }

            if (colors != null && colors.length > 0) {
                products = productDAO.searchByColors(products, colors);
            }

            double priceFrom = (priceFromParam != null && !priceFromParam.isEmpty()) ? Double.parseDouble(priceFromParam) : 0;
            double priceTo = (priceToParam != null && !priceToParam.isEmpty()) ? Double.parseDouble(priceToParam) : 0;
            if (priceFrom > 0 || priceTo > 0) {
                products = productDAO.searchByPrice(products, priceFrom, priceTo);
            }

            if (discountParam != null && !discountParam.isEmpty()) {
                switch (discountParam) {
                    case "dis25": products = productDAO.searchByDiscount(products, 0.25); break;
                    case "dis40": products = productDAO.searchByDiscount(products, 0.4); break;
                    case "dis75": products = productDAO.searchByDiscount(products, 0.75); break;
                }
            }

            // Apply sorting if sortValue is provided
            if (sortValue != null && !sortValue.isEmpty()) {
                products = productDAO.sortProduct(products, sortValue);
            }

            // Pagination logic
            int itemsPerPage = 9;
            int totalItems = products.size();
            int totalPages = (totalItems + itemsPerPage - 1) / itemsPerPage;
            int currentPage = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;
            int start = (currentPage - 1) * itemsPerPage;
            int end = Math.min(currentPage * itemsPerPage, totalItems);
            List<ProductDTO> paginatedProducts = productDAO.getListByPage(products, start, end);

            // Fetch categories
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getData();

            // Set checkbox states for categories
            Boolean[] categoryCheckboxes = new Boolean[categories.size() + 1];
            categoryCheckboxes[0] = (categoryIds == null || categoryIds.length == 0 || categoryIds[0].equals("0"));
            for (int i = 0; i < categories.size(); i++) {
                categoryCheckboxes[i + 1] = isChecked(categories.get(i).getId(), categoryIds);
            }

            // Set attributes
            request.setAttribute("DATA_FROM", "SearchServlet");
            request.setAttribute("CURRENTPAGE", currentPage);
            request.setAttribute("NUMBERPAGE", totalPages);
            request.setAttribute("LISTPRODUCTS", paginatedProducts);
            request.setAttribute("LISTCATEGORIES", categories);
            request.setAttribute("VALUESORT", sortValue);
            request.setAttribute("txtSearch", searchText);
            request.setAttribute("chid", categoryCheckboxes);
            request.setAttribute("COLORS", colors);
            request.setAttribute("price1", priceFrom > 0 ? priceFrom : null);
            request.setAttribute("price2", priceTo > 0 ? priceTo : null);
            request.setAttribute("DISCOUNT", discountParam);

        } catch (Exception ex) {
            log("Error in SearchServlet: " + ex.getMessage());
            ex.printStackTrace();
        }

        request.getRequestDispatcher(destination).forward(request, response);
    }
    
    private boolean isChecked(int id, String[] checkedIds) {
        if (checkedIds == null) return false;
        for (String checkedId : checkedIds) {
            if (Integer.parseInt(checkedId) == id) return true;
        }
        return false;
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
        return "Handles product search, filtering, sorting, and pagination";
    }// </editor-fold>

}
