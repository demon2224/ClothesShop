package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import model.OrderDTO;

/**
 *
 * @author Group - 07
 */
public class AdminServlet extends HttpServlet {

    private static final String ADMIN = "WEB-INF/admin/admin_home.jsp";
    private static final String ORDER_DETAIL_PAGE = "WEB-INF/admin/admin_order_detail.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO pDao = new ProductDAO();
        new OrderItemDAO();
        OrderDAO oDao = new OrderDAO();
        UserDAO uDao = new UserDAO();
        String url = ADMIN;

        try {
            double totalSale = oDao.getTotalSale();
            double totalSaleTD = oDao.getTotalSaleToday();
            int totalProducts = pDao.getTotalProducts();
            int numberProductsLowQuantity = pDao.getProductsLowQuantiry();
            int totalUsers = uDao.getTotalUsers();
            int totalOrders = oDao.getTotalOrders();
            List<OrderDTO> lastRecentOrders = oDao.getRecentOrders();
            request.setAttribute("TOTALSALE", totalSale);
            request.setAttribute("TOTALSALETODAY", totalSaleTD);
            request.setAttribute("TOTALPRODUCTS", totalProducts);
            request.setAttribute("PRODUCTSLOW", numberProductsLowQuantity);
            request.setAttribute("TOTALUSERS", totalUsers);
            request.setAttribute("TOTALORDERS", totalOrders);
            request.setAttribute("LAST_RECENT_ORDERS", lastRecentOrders);
            request.setAttribute("CURRENTSERVLET", "Dashboard");
            String action = request.getParameter("showdetail");
            if ("showdetail".equals(action)) {
                url = "manageorder?action=ShowDetail&bill_id=" + request.getParameter("bill_id");
                request.getRequestDispatcher(url).forward(request, response);
            }
        } catch (IOException | SQLException | ServletException var21) {
            Exception ex = var21;
            this.log("AdminServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(ADMIN).forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    public String getServletInfo() {
        return "AdminServlet for managing the admin dashboard and order details.";
    }
}
