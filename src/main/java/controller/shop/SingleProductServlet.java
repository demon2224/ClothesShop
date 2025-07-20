package controller.shop;

import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.ProductDTO;

/**
 *
 * @author Group - 07
 */
public class SingleProductServlet extends HttpServlet {

    private static final String SINGLE_PRODUCT_PAGE = "WEB-INF/home/single-product.jsp";

    // Main method to handle both GET and POST requests
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String destination = SINGLE_PRODUCT_PAGE;

        try {
            // Get product by ID
            ProductDAO productDAO = new ProductDAO();
            String productId = request.getParameter("product_id");
            ProductDTO product = productDAO.getProductByID(Integer.parseInt(productId));

            // Get products in the same category
            List<ProductDTO> allProductsInCategory = productDAO.getProductByCategoryId(
                productDAO.getData(), 
                product.getCategory().getId()
            );

            // Pick up to 4 related products (excluding the current product)
            List<ProductDTO> relatedProducts = new ArrayList<>();
            int count = 0;
            for (ProductDTO item : allProductsInCategory) {
                if (item.getId() != product.getId()) {
                    relatedProducts.add(item);
                    count++;
                    if (count == 4) {
                        break;
                    }
                }
            }

            // Set data for the JSP page
            request.setAttribute("PRODUCT", product);
            request.setAttribute("LIST_PRODUCTS_SAME_CATEGORY", relatedProducts);

        } catch (Exception ex) {
            log("Error in SingleProductServlet: " + ex.getMessage());
        }

        // Forward to the JSP page
        request.getRequestDispatcher(destination).forward(request, response);
    }

    // Handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Displays a single product and related items";
    }// </editor-fold>

}
