package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import model.OrderDTO;
import model.UserDTO;
import utils.PriceFormatter;

/**
 *
 * @author Group - 07
 */
public class AdminServlet extends HttpServlet {

    private static final String ADMIN = "WEB-INF/admin/admin_home.jsp";
    private static final String ORDER_DETAIL_PAGE = "WEB-INF/admin/admin_order_detail.jsp";

    /**
     * Kiểm tra quyền truy cập admin
     *
     * @param request
     * @param response
     * @return true nếu user là admin, false nếu không
     */
    private boolean checkAdminAccess(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("account");

        // Kiểm tra nếu chưa đăng nhập
        if (user == null) {
            log("Unauthorized admin access attempt - not logged in from " + request.getRequestURI());
            response.sendRedirect("home?btnAction=Login");
            return false;
        }

        // Kiểm tra nếu không phải admin (roleID != 1)
        if (user.getRoleID() != 1) {
            // Log unauthorized access attempt
            log("Unauthorized admin access attempt by user: " + user.getUserName()
                    + " (ID: " + user.getId() + ") to " + request.getRequestURI());
            response.sendRedirect("home");
            return false;
        }

        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập admin
        if (!checkAdminAccess(request, response)) {
            return; // Đã redirect, không cần xử lý thêm
        }

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
            request.setAttribute("TOTALSALE", PriceFormatter.format(totalSale));
            request.setAttribute("TOTALSALETODAY", PriceFormatter.format(totalSaleTD));
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
        // Kiểm tra quyền truy cập admin
        if (!checkAdminAccess(request, response)) {
            return; // Đã redirect, không cần xử lý thêm
        }

        this.doGet(request, response);
    }

    public String getServletInfo() {
        return "AdminServlet for managing the admin dashboard and order details.";
    }
}
