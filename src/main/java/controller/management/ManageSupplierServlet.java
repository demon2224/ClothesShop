package controller.management;

import dao.SupplierDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import model.SupplierDTO;

/**
 *
 * @author Group - 07
 */
public class ManageSupplierServlet extends HttpServlet {

    private static final String MANAGE_SUPPLIER_PAGE = "WEB-INF/admin/admin_suppliers.jsp";
    private static final String INSERT_SUPPLIER_PAGE = "WEB-INF/admin/admin_suppliers_insert.jsp";
    private static final String EDIT_SUPPLIER_PAGE = "WEB-INF/admin/admin_suppliers_edit.jsp";
    private static final String INSERT_ACTION = "Insert";
    private static final String UPDATE_ACTION = "Update";
    private static final String DELETE_ACTION = "Delete";
    private static final String UPLOAD_DIR = "assets/home/img/suppliers/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = MANAGE_SUPPLIER_PAGE;
        String action = request.getParameter("action");

        try {
            SupplierDAO supplierDao = new SupplierDAO();

            if (null == action) {
                url = showSupplierList(request, supplierDao);
            } else {
                switch (action) {
                    case INSERT_ACTION:
                        url = showInsertSupplierPage(request);
                        break;
                    case UPDATE_ACTION:
                        url = updateSupplier(request, supplierDao);
                        break;
                    case DELETE_ACTION:
                        url = deleteSupplier(request, supplierDao);
                        break;
                    case "Edit":
                        url = showEditSupplierPage(request, supplierDao);
                        break;
                    case "InsertSupplier":
                        url = insertNewSupplier(request, supplierDao);
                        break;
                    case "Search":
                        url = searchSuppliers(request, supplierDao);
                        break;
                    default:
                        url = showSupplierList(request, supplierDao);
                        break;
                }
            }

        } catch (Exception ex) {
            log("ManageSupplierServlet error: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private String showSupplierList(HttpServletRequest request, SupplierDAO supplierDao)
            throws ServletException, IOException, SQLException {
        List<SupplierDTO> supplierList = supplierDao.getData();
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("CURRENTSERVLET", "Supplier");
        return MANAGE_SUPPLIER_PAGE;
    }

    private String showInsertSupplierPage(HttpServletRequest request)
            throws ServletException, IOException {
        return INSERT_SUPPLIER_PAGE;
    }

    private String showEditSupplierPage(HttpServletRequest request, SupplierDAO supplierDao)
            throws ServletException, IOException, SQLException {
        String supplierId = request.getParameter("supplierId");
        SupplierDTO supplier = supplierDao.getSupplierById(Integer.parseInt(supplierId));
        request.setAttribute("supplierId", supplier.getId());
        request.setAttribute("name", supplier.getName());
        request.setAttribute("image", supplier.getImage());
        return EDIT_SUPPLIER_PAGE;
    }

    private String deleteSupplier(HttpServletRequest request, SupplierDAO supplierDao)
            throws ServletException, IOException, SQLException {
        String supplierId = request.getParameter("supplierId");
        supplierDao.deleteSupplier(Integer.parseInt(supplierId));
        request.setAttribute("mess", "Xóa nhà cung cấp thành công!");
        List<SupplierDTO> supplierList = supplierDao.getData();
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("CURRENTSERVLET", "Supplier");
        return MANAGE_SUPPLIER_PAGE;
    }

    private String insertNewSupplier(HttpServletRequest request, SupplierDAO supplierDao)
            throws ServletException, IOException, SQLException {
        String name = request.getParameter("name");
        String image = request.getParameter("image");

        if (name != null && !name.isEmpty()) {
            if (image != null && !image.isEmpty()) {
                image = UPLOAD_DIR + image; // Thêm tiền tố UPLOAD_DIR vào tên file
            } else {
                image = ""; // Nếu không chọn file, để trống
            }
            supplierDao.insertSupplier(name, image);
            request.setAttribute("mess", "Thêm nhà cung cấp thành công!");
            List<SupplierDTO> supplierList = supplierDao.getData();
            request.setAttribute("LISTSUPPLIERS", supplierList);
            request.setAttribute("CURRENTSERVLET", "Supplier");
            return MANAGE_SUPPLIER_PAGE;
        } else {
            request.setAttribute("error", "Tên nhà cung cấp không được để trống!");
            setSupplierAttributes(request, name, image);
            return INSERT_SUPPLIER_PAGE;
        }
    }

    private String updateSupplier(HttpServletRequest request, SupplierDAO supplierDao)
            throws ServletException, IOException, SQLException {
        String supplierId = request.getParameter("supplierId");
        String name = request.getParameter("name");
        String image = request.getParameter("image");

        if (name != null && !name.isEmpty()) {
            if (image != null && !image.isEmpty()) {
                image = UPLOAD_DIR + image; // Thêm tiền tố UPLOAD_DIR vào tên file
            } else {
                image = supplierDao.getSupplierById(Integer.parseInt(supplierId)).getImage(); // Giữ ảnh cũ nếu không chọn file mới
            }
            supplierDao.updateSupplier(Integer.parseInt(supplierId), name, image);
            request.setAttribute("mess", "Cập nhật nhà cung cấp thành công!");
            List<SupplierDTO> supplierList = supplierDao.getData();
            request.setAttribute("LISTSUPPLIERS", supplierList);
            request.setAttribute("CURRENTSERVLET", "Supplier");
            return MANAGE_SUPPLIER_PAGE;
        } else {
            request.setAttribute("error", "Tên nhà cung cấp không được để trống!");
            setSupplierAttributes(request, name, image);
            request.setAttribute("supplierId", supplierId);
            return EDIT_SUPPLIER_PAGE;
        }
    }

    private String searchSuppliers(HttpServletRequest request, SupplierDAO supplierDao)
            throws ServletException, IOException, SQLException {
        String searchQuery = request.getParameter("search");
        List<SupplierDTO> supplierList;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            supplierList = supplierDao.searchSuppliers(searchQuery);
            request.setAttribute("searchQuery", searchQuery);
        } else {
            supplierList = supplierDao.getData();
        }

        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("CURRENTSERVLET", "Supplier");
        return MANAGE_SUPPLIER_PAGE;
    }

    private void setSupplierAttributes(HttpServletRequest request, String name, String image) {
        request.setAttribute("name", name);
        request.setAttribute("image", image);
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
        return "Manages supplier operations";
    }
}
