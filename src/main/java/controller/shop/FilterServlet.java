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
public class FilterServlet extends HttpServlet {

    private static final String SHOP_LIST_PAGE = "WEB-INF/home/shop.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String targetPage = SHOP_LIST_PAGE;

        try {
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getData();
            List<ProductDTO> products = productDAO.getData();

            String sortValue = request.getParameter("valueSort");
            String[] checkboxIds = request.getParameterValues("id_filter");
            String queryString = request.getQueryString();

            Boolean[] categoryCheckboxes = new Boolean[categories.size() + 1];
            categoryCheckboxes[0] = true;

            if (checkboxIds != null && checkboxIds.length > 0) {
                int[] categoryIds = parseIntArray(checkboxIds);
                if (categoryIds[0] != 0) {
                    products = productDAO.searchByCheckBox(products, categoryIds);
                    categoryCheckboxes[0] = false;
                }
                for (int i = 0; i < categories.size(); i++) {
                    categoryCheckboxes[i + 1] = isChecked(categories.get(i).getId(), categoryIds);
                }
            }

            double priceFrom = parseDouble(request.getParameter("pricefrom"), 0);
            double priceTo = parseDouble(request.getParameter("priceto"), 0);
            if (priceFrom > 0 || priceTo > 0) {
                products = productDAO.searchByPrice(products, priceFrom, priceTo);
                request.setAttribute("price1", priceFrom);
                request.setAttribute("price2", priceTo);
            }

            String[] colors = request.getParameterValues("color");
            if (colors != null && colors.length > 0) {
                products = productDAO.searchByColors(products, colors);
                request.setAttribute("COLORS", colors);
            }

            String discount = request.getParameter("discount");
            if (discount != null) {
                switch (discount) {
                    case "dis25":
                        products = productDAO.searchByDiscount(products, 0.25);
                        break;
                    case "dis40":
                        products = productDAO.searchByDiscount(products, 0.4);
                        break;
                    case "dis75":
                        products = productDAO.searchByDiscount(products, 0.75);
                        break;
                }
                request.setAttribute("DISCOUNT", discount);
            }

            if (sortValue != null && !sortValue.isEmpty()) {
                products = productDAO.sortProduct(products, sortValue);
            }

            int itemsPerPage = 9;
            int totalItems = products.size();
            int totalPages = (totalItems + itemsPerPage - 1) / itemsPerPage;
            int currentPage = parseInt(request.getParameter("page"), 1);
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(currentPage * itemsPerPage, totalItems);
            List<ProductDTO> paginatedProducts = productDAO.getListByPage(products, startIndex, endIndex);

            request.setAttribute("DATA_FROM", "filter");
            request.setAttribute("NUMBERPAGE", totalPages);
            request.setAttribute("CURRENTPAGE", currentPage);
            request.setAttribute("chid", categoryCheckboxes);
            request.setAttribute("CURRENTSERVLET", "shop");
            request.setAttribute("LISTPRODUCTS", paginatedProducts);
            request.setAttribute("LISTCATEGORIES", categories);
            request.setAttribute("VALUESORT", sortValue);
            request.setAttribute("QUERYSTRING", queryString);

        } catch (Exception ex) {
            log("FilterServlet error: " + ex.getMessage());
        }
        request.getRequestDispatcher(targetPage).forward(request, response);
    }

    private int[] parseIntArray(String[] values) {
        if (values == null) {
            return new int[0];
        }
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && !values[i].trim().isEmpty()) {
                try {
                    result[i] = Integer.parseInt(values[i].trim());
                } catch (NumberFormatException e) {
                    result[i] = 0; // Default value for invalid input
                }
            } else {
                result[i] = 0; // Default value for null/empty input
            }
        }
        return result;
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean isChecked(int id, int[] checkedIds) {
        for (int checkedId : checkedIds) {
            if (checkedId == id) {
                return true;
            }
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for filtering and sorting products.";
    }// </editor-fold>

}
