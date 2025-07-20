package controller.management;

import dao.CategoryDAO;
import dao.TypeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import model.CategoryDTO;
import model.TypeDTO;

/**
 *
 * @author Group - 07
 */
public class CategoryManagementServlet extends HttpServlet {

    private static final String MANAGE_CATEGORY_PAGE = "WEB-INF/admin/admin_categories.jsp";
    private static final String INSERT_CATEGORY_PAGE = "WEB-INF/admin/admin_categories_insert.jsp";
    private static final String EDIT_CATEGORY_PAGE = "WEB-INF/admin/admin_categories_edit.jsp";
    private static final String INSERT_ACTION = "Insert";
    private static final String UPDATE_ACTION = "Update";
    private static final String DELETE_ACTION = "Delete";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = MANAGE_CATEGORY_PAGE;
        String action = request.getParameter("action");

        try {
            CategoryDAO categoryDao = new CategoryDAO();
            TypeDAO typeDao = new TypeDAO();

            if (null == action) {
                url = showCategoryList(request, categoryDao, typeDao);
            } else {
                switch (action) {
                    case INSERT_ACTION:
                        url = showInsertCategoryPage(request, typeDao);
                        break;
                    case UPDATE_ACTION:
                        url = updateCategory(request, categoryDao);
                        break;
                    case DELETE_ACTION:
                        url = deleteCategory(request, categoryDao);
                        break;
                    case "Edit":
                        url = showEditCategoryPage(request, categoryDao, typeDao);
                        break;
                    case "InsertCategory":
                        url = insertNewCategory(request, categoryDao);
                        break;
                    case "Search":
                        url = searchCategories(request, categoryDao, typeDao);
                        break;
                    default:
                        url = showCategoryList(request, categoryDao, typeDao);
                        break;
                }
            }

        } catch (Exception ex) {
            log("CategoryManagementServlet error: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // Hiển thị danh sách danh mục
    private String showCategoryList(HttpServletRequest request, CategoryDAO categoryDao, TypeDAO typeDao)
            throws SQLException {
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LIST_CATEGORIES", categoryList);
        request.setAttribute("LIST_TYPES", typeList);
        request.setAttribute("CURRENTSERVLET", "Category");
        return MANAGE_CATEGORY_PAGE;
    }

    // Hiển thị trang thêm mới danh mục
    private String showInsertCategoryPage(HttpServletRequest request, TypeDAO typeDao)
            throws SQLException {
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LIST_TYPES", typeList);
        return INSERT_CATEGORY_PAGE;
    }

    // Hiển thị trang chỉnh sửa danh mục
    private String showEditCategoryPage(HttpServletRequest request, CategoryDAO categoryDao, TypeDAO typeDao)
            throws SQLException {
        String categoryId = request.getParameter("categoryId");
        CategoryDTO category = categoryDao.getCategoryById(Integer.parseInt(categoryId));
        List<TypeDTO> typeList = typeDao.getAllType();

        request.setAttribute("categoryId", category.getId());
        request.setAttribute("categoryName", category.getName());
        request.setAttribute("typeId", category.getType().getId());
        request.setAttribute("LIST_TYPES", typeList);
        return EDIT_CATEGORY_PAGE;
    }

    // Xóa danh mục
    private String deleteCategory(HttpServletRequest request, CategoryDAO categoryDao)
            throws SQLException {
        String categoryId = request.getParameter("categoryId");
        categoryDao.deleteCategory(categoryId);
        request.setAttribute("mess", "Xóa danh mục thành công!");
        List<CategoryDTO> categoryList = categoryDao.getData();
        request.setAttribute("LIST_CATEGORIES", categoryList);
        request.setAttribute("CURRENTSERVLET", "Category");
        return MANAGE_CATEGORY_PAGE;
    }

    // Cập nhật danh mục
    private String updateCategory(HttpServletRequest request, CategoryDAO categoryDao)
            throws SQLException {
        String categoryId = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        String typeId = request.getParameter("typeId");

        categoryDao.editCategory(categoryName, typeId, categoryId);
        request.setAttribute("mess", "Cập nhật danh mục thành công!");
        List<CategoryDTO> categoryList = categoryDao.getData();
        request.setAttribute("LIST_CATEGORIES", categoryList);
        request.setAttribute("CURRENTSERVLET", "Category");
        return MANAGE_CATEGORY_PAGE;
    }

    // Thêm mới danh mục
    private String insertNewCategory(HttpServletRequest request, CategoryDAO categoryDao)
            throws SQLException {
        String categoryName = request.getParameter("categoryName");
        String typeId = request.getParameter("typeId");

        if (categoryName != null && typeId != null) {
            categoryDao.insertCategory(categoryName, typeId);
            request.setAttribute("mess", "Thêm danh mục thành công!");
        }
        List<CategoryDTO> categoryList = categoryDao.getData();
        request.setAttribute("LIST_CATEGORIES", categoryList);
        request.setAttribute("CURRENTSERVLET", "Category");
        return MANAGE_CATEGORY_PAGE;
    }

    private String searchCategories(HttpServletRequest request, CategoryDAO categoryDao, TypeDAO typeDao)
            throws SQLException {
        String searchQuery = request.getParameter("search");
        List<CategoryDTO> categoryList;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            categoryList = categoryDao.searchCategories(searchQuery);
            request.setAttribute("searchQuery", searchQuery);
        } else {
            categoryList = categoryDao.getData();
        }

        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LIST_CATEGORIES", categoryList);
        request.setAttribute("LIST_TYPES", typeList);
        request.setAttribute("CURRENTSERVLET", "Category");
        return MANAGE_CATEGORY_PAGE;
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
        return "Category Management Servlet";
    }
}
