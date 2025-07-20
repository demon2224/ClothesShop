package controller.management;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import dao.TypeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import model.CategoryDTO;
import model.ProductDTO;
import model.SupplierDTO;
import model.TypeDTO;

/**
 *
 * @author Group - 07
 */
public class ProductManagementServlet extends HttpServlet {

    private static final String MANAGE_PRODUCT_PAGE = "WEB-INF/admin/admin_products.jsp";
    private static final String INSERT_PRODUCT_PAGE = "WEB-INF/admin/admin_products_insert.jsp";
    private static final String EDIT_PRODUCT_PAGE = "WEB-INF/admin/admin_edit_product.jsp";
    private static final String UPLOAD_DIR = "assets/home/img/products/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = MANAGE_PRODUCT_PAGE;
        String action = request.getParameter("action");

        try {
            ProductDAO productDao = new ProductDAO();
            CategoryDAO categoryDao = new CategoryDAO();
            SupplierDAO supplierDao = new SupplierDAO();
            TypeDAO typeDao = new TypeDAO();
            if ("Insert".equals(action)) {
                url = this.showInsertProductPage(request, categoryDao, supplierDao, typeDao);
            } else if ("Update".equals(action)) {
                url = this.updateProduct(request, productDao);
            } else if ("Delete".equals(action)) {
                url = this.deleteProduct(request, productDao);
            } else if ("Edit".equals(action)) {
                url = this.showEditProductPage(request, productDao, categoryDao, supplierDao, typeDao);
            } else if ("InsertProduct".equals(action)) {
                url = this.insertNewProduct(request, productDao);
            } else if ("Search".equals(action)) {
                url = this.searchProducts(request, productDao);
            } else if ("Restore".equals(action)) {
                url = this.restoreProduct(request, productDao);
            } else if ("PermanentlyDelete".equals(action)) {
                url = this.permanentlyDeleteProduct(request, productDao);
            } else {
                url = this.showProductList(request, productDao);
            }
        } catch (Exception var12) {
            Exception ex = var12;
            this.log("ProductManagementServlet error: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    private String showProductList(HttpServletRequest request, ProductDAO productDao) throws SQLException {
        List<ProductDTO> productList = productDao.getData();
        Iterator var4 = productList.iterator();

        while (var4.hasNext()) {
            ProductDTO product = (ProductDTO) var4.next();
            product.setSize(product.getSize() != null ? product.getSize() : new String[0]);
            product.setColors(product.getColors() != null ? product.getColors() : new String[0]);
            product.setImages(product.getImages() != null ? product.getImages() : new String[0]);
        }

        request.setAttribute("LISTPRODUCTS", productList);
        request.setAttribute("CURRENTSERVLET", "Product");
        return MANAGE_PRODUCT_PAGE;
    }

    private String showInsertProductPage(HttpServletRequest request, CategoryDAO categoryDao, SupplierDAO supplierDao, TypeDAO typeDao) throws SQLException {
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<SupplierDTO> supplierList = supplierDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LISTCATEGORIES", categoryList);
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("LISTTYPES", typeList);
        return INSERT_PRODUCT_PAGE;
    }

    private String showEditProductPage(HttpServletRequest request, ProductDAO productDao, CategoryDAO categoryDao, SupplierDAO supplierDao, TypeDAO typeDao) throws SQLException {
        String productId = request.getParameter("productId");
        ProductDTO product = productDao.getProductByID(Integer.parseInt(productId));
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<SupplierDTO> supplierList = supplierDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("productId", product.getId());
        request.setAttribute("name", product.getName());
        request.setAttribute("price", product.getPrice());
        request.setAttribute("stock", product.getStock());
        request.setAttribute("size", String.join(",", product.getSize() != null ? product.getSize() : new String[0]));
        request.setAttribute("color", String.join(",", product.getColors() != null ? product.getColors() : new String[0]));
        request.setAttribute("description", product.getDescription());
        request.setAttribute("image", String.join(" ", product.getImages() != null ? product.getImages() : new String[0]));
        request.setAttribute("categoryId", product.getCategory().getId());
        request.setAttribute("supplierId", product.getSupplier().getId());
        request.setAttribute("typeId", product.getType().getId());
        request.setAttribute("releaseDate", product.getReleasedate());
        request.setAttribute("discount", product.getDiscount());
        request.setAttribute("LISTCATEGORIES", categoryList);
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("LISTTYPES", typeList);
        return EDIT_PRODUCT_PAGE;
    }

    private String deleteProduct(HttpServletRequest request, ProductDAO productDao) throws SQLException {
        String productId = request.getParameter("productId");
        productDao.deleteProduct(productId);
        request.setAttribute("mess", "Xóa sản phẩm thành công!");
        return this.showProductList(request, productDao);
    }

    private String insertNewProduct(HttpServletRequest request, ProductDAO productDao) throws SQLException, Exception {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String[] images = request.getParameterValues("image");
        String description = request.getParameter("description");
        String categoryId = request.getParameter("categoryId");
        String supplierId = request.getParameter("supplierId");
        String typeId = request.getParameter("typeId");
        String releaseDate = request.getParameter("releaseDate");
        String discount = request.getParameter("discount");
        String image = "";
        if (images != null && images.length > 0 && !images[0].isEmpty()) {
            String[] var16 = images;
            int var17 = images.length;

            for (int var18 = 0; var18 < var17; ++var18) {
                String img = var16[var18];
                image = image + UPLOAD_DIR + img + " ";
            }
        }

        try {
            productDao.insertProduct(name, Integer.parseInt(categoryId), Integer.parseInt(supplierId), Integer.parseInt(typeId), Double.parseDouble(price), discount.isEmpty() ? 0.0 : Double.parseDouble(discount), size, color, Integer.parseInt(stock), releaseDate, image.trim(), description);
            request.setAttribute("mess", "Thêm sản phẩm thành công!");
            return this.showProductList(request, productDao);
        } catch (SQLException var23) {
            this.setProductAttributes(request, name, price, stock, size, color, description, image, categoryId, supplierId, typeId, releaseDate, discount);
            request.setAttribute("error", var23.getMessage());
            CategoryDAO categoryDao = new CategoryDAO();
            SupplierDAO supplierDao = new SupplierDAO();
            TypeDAO typeDao = new TypeDAO();
            List<CategoryDTO> categoryList = categoryDao.getData();
            List<SupplierDTO> supplierList = supplierDao.getData();
            List<TypeDTO> typeList = typeDao.getAllType();
            request.setAttribute("LISTCATEGORIES", categoryList);
            request.setAttribute("LISTSUPPLIERS", supplierList);
            request.setAttribute("LISTTYPES", typeList);
            return INSERT_PRODUCT_PAGE;
        }
    }

    private String updateProduct(HttpServletRequest request, ProductDAO productDao) throws SQLException, Exception {
        String productId = request.getParameter("productId");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String[] images = request.getParameterValues("image");
        String description = request.getParameter("description");
        String categoryId = request.getParameter("categoryId");
        String supplierId = request.getParameter("supplierId");
        String typeId = request.getParameter("typeId");
        String releaseDate = request.getParameter("releaseDate");
        String discount = request.getParameter("discount");
        String image = "";
        if (images != null && images.length > 0 && !images[0].isEmpty()) {
            String[] var17 = images;
            int var18 = images.length;

            for (int var19 = 0; var19 < var18; ++var19) {
                String img = var17[var19];
                image = image + UPLOAD_DIR + img + " ";
            }
        } else {
            image = productDao.getProductByID(Integer.parseInt(productId)).getImages().length > 0 ? String.join(" ", productDao.getProductByID(Integer.parseInt(productId)).getImages()) : "";
        }

        try {
            productDao.editProduct(Integer.parseInt(productId), name, description, Integer.parseInt(stock), image.trim(), color, size, releaseDate, discount.isEmpty() ? 0.0 : Double.parseDouble(discount), Double.parseDouble(price), Integer.parseInt(categoryId), Integer.parseInt(supplierId), Integer.parseInt(typeId));
            request.setAttribute("mess", "Cập nhật sản phẩm thành công!");
            return this.showProductList(request, productDao);
        } catch (SQLException var21) {
            this.setProductAttributes(request, name, price, stock, size, color, description, image, categoryId, supplierId, typeId, releaseDate, discount);
            request.setAttribute("error", var21.getMessage());
            this.setEditPageAttributes(request, productDao);
            return EDIT_PRODUCT_PAGE;
        }
    }

    private String searchProducts(HttpServletRequest request, ProductDAO productDao) throws SQLException, Exception {
        String searchQuery = request.getParameter("search");
        List<ProductDTO> productList = searchQuery != null && !searchQuery.trim().isEmpty() ? productDao.searchProducts(searchQuery) : productDao.getData();
        Iterator var5 = productList.iterator();

        while (var5.hasNext()) {
            ProductDTO product = (ProductDTO) var5.next();
            product.setSize(product.getSize() != null ? product.getSize() : new String[0]);
            product.setColors(product.getColors() != null ? product.getColors() : new String[0]);
            product.setImages(product.getImages() != null ? product.getImages() : new String[0]);
        }

        request.setAttribute("LISTPRODUCTS", productList);
        request.setAttribute("CURRENTSERVLET", "Product");
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            request.setAttribute("searchQuery", searchQuery);
        }

        return MANAGE_PRODUCT_PAGE;
    }

    private String restoreProduct(HttpServletRequest request, ProductDAO productDao) throws SQLException, Exception {
        String productId = request.getParameter("productId");
        productDao.restoreProduct(productId);
        request.setAttribute("mess", "Khôi phục sản phẩm thành công!");
        return this.showProductList(request, productDao);
    }

    private String permanentlyDeleteProduct(HttpServletRequest request, ProductDAO productDao) throws SQLException, Exception {
        String productId = request.getParameter("productId");
        productDao.permanentlyDeleteProduct(productId);
        request.setAttribute("mess", "Xóa vĩnh viễn sản phẩm thành công!");
        return this.showProductList(request, productDao);
    }

    private void setProductAttributes(HttpServletRequest request, String name, String price, String stock, String size, String color, String description, String image, String categoryId, String supplierId, String typeId, String releaseDate, String discount) {
        request.setAttribute("name", name);
        request.setAttribute("price", price);
        request.setAttribute("stock", stock);
        request.setAttribute("size", size);
        request.setAttribute("color", color);
        request.setAttribute("description", description);
        request.setAttribute("image", image);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("supplierId", supplierId);
        request.setAttribute("typeId", typeId);
        request.setAttribute("releaseDate", releaseDate);
        request.setAttribute("discount", discount);
        request.setAttribute("productId", request.getParameter("productId"));
    }

    private void setEditPageAttributes(HttpServletRequest request, ProductDAO productDao) throws SQLException {
        CategoryDAO categoryDao = new CategoryDAO();
        SupplierDAO supplierDao = new SupplierDAO();
        TypeDAO typeDao = new TypeDAO();
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<SupplierDTO> supplierList = supplierDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LISTCATEGORIES", categoryList);
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("LISTTYPES", typeList);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    public String getServletInfo() {
        return "Product Management Servlet";
    }
}
