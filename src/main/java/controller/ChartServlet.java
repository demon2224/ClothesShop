package controller;

import dao.OrderDAO;
import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Group - 07
 */
public class ChartServlet extends HttpServlet {

    private static final String CHART_PAGE = "WEB-INF/admin/admin_chart.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrderDAO oDao = new OrderDAO();
        ProductDAO pDao = new ProductDAO();
        // Biến kiểm tra có lỗi xảy ra trong quá trình xử lý không
        boolean hasError = false;

        try {
            // Lấy dữ liệu doanh thu theo năm
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int currentYear = cal.get(java.util.Calendar.YEAR);
            int startYear = currentYear - 4; // Lấy 5 năm gần nhất
            double[] yearlyData = new double[5];

            // Xử lý dữ liệu từng năm
            for (int i = 0; i < 5; i++) {
                try {
                    yearlyData[i] = oDao.getTotalMoneyByYear(startYear + i);
                    request.setAttribute("YEAR" + (i + 1), yearlyData[i]);
                } catch (Exception e) {
                    log("Lỗi khi lấy dữ liệu năm " + (startYear + i) + ": " + e.getMessage());
                    request.setAttribute("YEAR" + (i + 1), 0.0);
                    hasError = true;
                }
            }

            // Lấy dữ liệu doanh thu theo tháng trong năm hiện tại
            double[] monthlyData = new double[12];
            for (int month = 1; month <= 12; month++) {
                try {
                    monthlyData[month - 1] = oDao.getTotalMoneyByMonth(month, currentYear);
                    request.setAttribute("MONTH" + month, monthlyData[month - 1]);
                } catch (Exception e) {
                    log("Lỗi khi lấy dữ liệu tháng " + month + " năm " + currentYear + ": " + e.getMessage());
                    request.setAttribute("MONTH" + month, 0.0);
                    hasError = true;
                }
            }

            // Lấy danh sách top 5 khách hàng có doanh số cao nhất
            try {
                List<Object[]> topUsers = oDao.getTopUsersByTotalSpent(5);
                request.setAttribute("TOP_USERS", topUsers);
            } catch (Exception e) {
                log("Lỗi khi lấy danh sách top khách hàng: " + e.getMessage());
                request.setAttribute("TOP_USERS", new ArrayList<>());
                hasError = true;
            }

            // Lấy danh sách top 5 sản phẩm bán chạy nhất
            try {
                List<Object[]> topProducts = pDao.getTopProductsByOrderCount(5);
                request.setAttribute("TOP_PRODUCTS", topProducts);
            } catch (Exception e) {
                log("Lỗi khi lấy danh sách top sản phẩm: " + e.getMessage());
                request.setAttribute("TOP_PRODUCTS", new ArrayList<>());
                hasError = true;
            }

            request.setAttribute("CURRENTSERVLET", "Chart");

            // Hiển thị cảnh báo nếu có lỗi xảy ra
            if (hasError) {
                request.setAttribute("WARNING_MESSAGE",
                        "Một số dữ liệu không thể tải. Vui lòng kiểm tra log để biết chi tiết.");
            }

        } catch (Exception ex) {
            // Xử lý lỗi nghiêm trọng
            log("Lỗi nghiêm trọng trong ChartServlet: " + ex.getMessage());
            request.setAttribute("ERROR_MESSAGE",
                    "Lỗi nghiêm trọng khi tải dữ liệu thống kê. Chi tiết: " + ex.getMessage());
        }

        // Chuyển hướng đến trang hiển thị biểu đồ
        request.getRequestDispatcher(CHART_PAGE).forward(request, response);
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
}
