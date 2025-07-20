package controller.management;

import dao.OrderDAO;
import dao.OrderItemDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import model.OrderDTO;
import model.OrderItem;

/**
 *
 * @author Group - 07
 */
public class ManageOrderServlet extends HttpServlet {

    private static final String ORDER_PAGE = "WEB-INF/admin/admin_order.jsp";
    private static final String ORDER_DETAIL_PAGE = "WEB-INF/admin/admin_order_detail.jsp";
    private static final String CHANGE_STATUS_ACTION = "ChangeStatus";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = ORDER_PAGE;
        String action = request.getParameter("action");

        try {
            OrderDAO orderDao = new OrderDAO();
            OrderItemDAO orderItemDao = new OrderItemDAO();

            if ("ShowDetail".equals(action)) {
                url = showOrderDetail(request, orderDao, orderItemDao);
            } else if (CHANGE_STATUS_ACTION.equals(action)) {
                url = changeOrderStatus(request, orderDao);
            } else if ("Search".equals(action)) {  // Add search action
                url = searchOrders(request, orderDao);
            } else {
                url = showOrderList(request, orderDao);
            }

        } catch (Exception ex) {
            log("Error in ManageOrderServlet: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // Hiển thị danh sách đơn hàng
    private String showOrderList(HttpServletRequest request, OrderDAO orderDao)
            throws ServletException, IOException, SQLException {
        List<OrderDTO> orders = orderDao.getAllOrders();
        request.setAttribute("LIST_ORDERS", orders);
        request.setAttribute("CURRENTSERVLET", "Order");
        return ORDER_PAGE;
    }

    // Hiển thị chi tiết đơn hàng
    private String showOrderDetail(HttpServletRequest request, OrderDAO orderDao, OrderItemDAO orderItemDao)
            throws ServletException, IOException, SQLException {
        String billId = request.getParameter("bill_id");
        OrderDTO order = orderDao.getOrdersByID(billId);
        List<OrderItem> orderItems = orderItemDao.getOrderItemByOrderId(order.getOrderID());
        request.setAttribute("LIST_PRODUCTS_IN_ORDER", orderItems);
        request.setAttribute("CURRENTSERVLET", "Order");
        return ORDER_DETAIL_PAGE;
    }

    // Thay đổi trạng thái đơn hàng
    private String changeOrderStatus(HttpServletRequest request, OrderDAO orderDao)
            throws ServletException, IOException, SQLException {
        String orderId = request.getParameter("id");
        orderDao.UpdateStatus(orderId);
        request.setAttribute("mess", "Cập nhật trạng thái thành công!");
        List<OrderDTO> orders = orderDao.getAllOrders();
        request.setAttribute("LIST_ORDERS", orders);
        request.setAttribute("CURRENTSERVLET", "Order");
        return ORDER_PAGE;
    }

    private String searchOrders(HttpServletRequest request, OrderDAO orderDao)
            throws ServletException, IOException, SQLException {
        String searchUsername = request.getParameter("searchUsername");
        String statusFilter = request.getParameter("statusFilter");

        List<OrderDTO> orders = orderDao.searchOrders(searchUsername, statusFilter);

        request.setAttribute("LIST_ORDERS", orders);
        request.setAttribute("CURRENTSERVLET", "Order");
        request.setAttribute("searchUsername", searchUsername);
        request.setAttribute("statusFilter", statusFilter);
        return ORDER_PAGE;
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
        return "Manages admin order operations";
    }
}
