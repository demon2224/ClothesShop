package controller.profile;

import dao.OrderDAO;
import dao.OrderItemDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.OrderDTO;
import model.OrderItem;
import model.UserDTO;

/**
 *
 * @author Group - 07
 */
public class ProfileServlet extends HttpServlet {

    private static final String PROFILE_PAGE = "WEB-INF/home/my-account.jsp";
    private OrderDAO orderDAO = new OrderDAO();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Get user from session
            HttpSession session = request.getSession();
            UserDTO user = (UserDTO) session.getAttribute("account");

            // Get the requested tab
            String tab = request.getParameter("tab");
            String orderIdStr = request.getParameter("orderID");

            // Nếu có orderID, hiển thị chi tiết order
            if (orderIdStr != null && !orderIdStr.isEmpty()) {
                int orderID = Integer.parseInt(orderIdStr);
                List<OrderItem> orderItems = orderItemDAO.getOrderItemByOrderId(orderID);
                request.setAttribute("ORDER_ITEMS", orderItems);
                request.setAttribute("ORDER_ID", orderID);
                request.setAttribute("SHOW_ORDER_DETAIL", true);
            } else {
                // Fetch user's orders
                List<OrderDTO> orders = orderDAO.getOrdersByUsername(user.getUserName());
                request.setAttribute("LISTORDERS", orders);
                if (tab != null) {
                    request.setAttribute("tab", tab);
                }
            }
        } catch (Exception ex) {
            log("Error in ProfileServlet: " + ex.getMessage());
        }

        // Forward to profile page
        request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
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
        return "Displays user profile and order history";
    }
}
