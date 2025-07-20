package dao;

import utils.DBContext;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import model.CartItem;
import model.CategoryDTO;
import model.ProductDTO;
import model.SupplierDTO;
import model.TypeDTO;

/**
 *
 * @author Group - 07
 */
public class ProductDAO extends DBContext {

    //private static final String GET_DATA = "SELECT * FROM Products WHERE status = 1";
    private static final String GET_DATA = "SELECT * FROM Products ORDER BY id ASC";

    private static final String GET_TOTAL_PRODUCTS = "SELECT COUNT(*) AS Total FROM Products WHERE status = 1";
    private static final String GET_QUANTITY_SOLD = "SELECT SUM(unitSold) AS Total from Products";
    private static final String GET_STOCK = "SELECT stock AS Total FROM Products WHERE id = ?";
    private static final String GET_PRODUCTS_LOW_QUANTITY = "SELECT COUNT(*) AS Total from Products WHERE Stock < 10 AND status = 1";
    private static final String GET_PRODUCTS_BY_ID = "SELECT * FROM Products WHERE id = ? AND status = 1";
    private static final String GET_PRODUCTS_BY_TYPE_ID = "SELECT * FROM Products WHERE typeid = ? AND status = 1";
    private static final String GET_PRODUCTS_BY_CATEGORY_ID = "SELECT * FROM Products WHERE categoryid = ? AND status = 1";
    private static final String GET_PRODUCTS_BY_SUPPLIER_ID = "SELECT * FROM Products WHERE supplierid = ? AND status = 1";
    private static final String GET_PRODUCTS_NEW_YEAR = "SELECT * from Products WHERE year(releasedate) = 2025 AND status = 1";
    private static final String GET_PRODUCTS_BEST_SELLER = "SELECT TOP(5) * from Products WHERE status = 1 order by unitSold desc";
    private static final String GET_PRODUCTS_BY_SEARCH = "SELECT * FROM Products WHERE productname LIKE ? AND status = 1";
    private static final String DELETE_PRODUCT = "UPDATE Products SET status = 0 WHERE id = ?";
    private static final String UPDATE_QUANTITY_PRODUCT = "UPDATE Products SET [stock] = ? WHERE id = ?";
    private static final String INSERT_PRODUCT = "INSERT INTO Products VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String RESTORE_PRODUCT = "UPDATE Products SET status = 1 WHERE id = ?";
    private static final String PERMANENTLY_DELETE_PRODUCT = "DELETE FROM Products WHERE id = ?";
    private static final String SEARCH_PRODUCTS = "SELECT * FROM Products WHERE (productname LIKE ? OR description LIKE ? OR id LIKE ?) ORDER BY status DESC, id ASC";

    private static final String PRICE_PATTERN = "^[0-9]+\\.?[0-9]*$"; // Non-negative real number
    private static final String STOCK_PATTERN = "^[0-9]+$"; // Non-negative integer
    private static final String DISCOUNT_PATTERN = "^[0-1](\\.[0-9]+)?$|^0$"; // Discount between 0 and 1

    // Validation methods
    private boolean isValidPrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return false;
        }
        try {
            return Pattern.matches(PRICE_PATTERN, price) && Double.parseDouble(price) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidStock(String stock) {
        if (stock == null || stock.trim().isEmpty()) {
            return false;
        }
        try {
            return Pattern.matches(STOCK_PATTERN, stock) && Integer.parseInt(stock) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDiscount(String discount) {
        if (discount == null || discount.trim().isEmpty()) {
            return true; // Allow null/empty (defaults to 0)
        }
        try {
            return Pattern.matches(DISCOUNT_PATTERN, discount) && Double.parseDouble(discount) >= 0 && Double.parseDouble(discount) <= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<ProductDTO> getData() throws SQLException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_DATA);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    int id = rs.getInt("id");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return products;
    }

    public ProductDTO getProductByID(int id) {
        ProductDTO product = null;
        Connection con = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            if (con != null) {
                ptm = con.prepareStatement(GET_PRODUCTS_BY_ID);
                ptm.setInt(1, id);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;

    }

    public List<ProductDTO> getProductByTypeId(int typeId) {
        List<ProductDTO> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            if (con != null) {
                ptm = con.prepareStatement(GET_PRODUCTS_BY_TYPE_ID);
                ptm.setInt(1, typeId);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(typeId);
                    int id = rs.getInt("id");
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;

    }

    public List<ProductDTO> getProductByCategoryId(List<ProductDTO> list, int categoryid) {
        if (categoryid == 0) {
            return list;
        }
        List<ProductDTO> rs = new ArrayList<>();
        for (ProductDTO productDTO : list) {
            if (productDTO.getCategory().getId() == categoryid) {
                rs.add(productDTO);
            }
        }
        return rs;
    }

    public List<ProductDTO> getProductSupplierId(int supplierid) {
        List<ProductDTO> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            if (con != null) {
                ptm = con.prepareStatement(GET_PRODUCTS_BY_SUPPLIER_ID);
                ptm.setInt(1, supplierid);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    SupplierDTO supplier = sDao.getSupplierById(supplierid);
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    int id = rs.getInt("id");
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    boolean status = rs.getBoolean("status");
                    double price = rs.getDouble("price");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;

    }

    public int getTotalProducts() throws SQLException {
        int result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_PRODUCTS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public int getQuantitySold() throws SQLException {
        int result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_QUANTITY_SOLD);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public int getStock(int id) throws SQLException {
        int result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_STOCK);
                ptm.setInt(1, id);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public int getProductsLowQuantiry() throws SQLException {
        int result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_PRODUCTS_LOW_QUANTITY);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public List<ProductDTO> getListByPage(List<ProductDTO> list, int start, int end) {
        ArrayList<ProductDTO> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(list.get(i));
        }
        return arr;
    }

    public List<ProductDTO> getProductNew() throws SQLException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_PRODUCTS_NEW_YEAR);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    int id = rs.getInt("id");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return products;
    }

    public List<ProductDTO> getProductsBestSeller() throws SQLException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            // Kiểm tra số lượng sản phẩm active và đã bán
            conn = getConnection();
            if (conn != null) {
                // Thực hiện query chính
                ptm = conn.prepareStatement(GET_PRODUCTS_BEST_SELLER);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    int id = rs.getInt("id");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return products;
    }

    public List<ProductDTO> getProductBySearch(String txtSearch) throws SQLException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_PRODUCTS_BY_SEARCH);
                ptm.setString(1, "%" + txtSearch + "%");
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    int id = rs.getInt("id");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return products;
    }

    public List<ProductDTO> sortProduct(List<ProductDTO> list, String value) throws SQLException {
        List<ProductDTO> result = new ArrayList<>(list);
        switch (value) {
            case "0": // Nổi bật (sắp xếp theo ngày phát hành mới nhất)
                Collections.sort(result, new Comparator<ProductDTO>() {
                    @Override
                    public int compare(ProductDTO p1, ProductDTO p2) {
                        // Sort by release date, newest first (descending)
                        return p2.getReleasedate().compareTo(p1.getReleasedate());
                    }
                });
                break;
            case "1": // Giá: Thấp đến Cao
                Collections.sort(result, new Comparator<ProductDTO>() {
                    @Override
                    public int compare(ProductDTO p1, ProductDTO p2) {
                        // Sort by sale price, lowest first (ascending)
                        return Double.compare(p1.getSalePrice(), p2.getSalePrice());
                    }
                });
                break;
            case "2": // Giá: Cao đến Thấp
                Collections.sort(result, new Comparator<ProductDTO>() {
                    @Override
                    public int compare(ProductDTO p1, ProductDTO p2) {
                        // Sort by sale price, highest first (descending)
                        return Double.compare(p2.getSalePrice(), p1.getSalePrice());
                    }
                });
                break;
            case "3": // Tên: A-Z
                Collections.sort(result, new Comparator<ProductDTO>() {
                    @Override
                    public int compare(ProductDTO p1, ProductDTO p2) {
                        // Sort by name, A-Z (case-insensitive)
                        return p1.getName().compareToIgnoreCase(p2.getName());
                    }
                });
                break;
            default:
                // No sorting applied if value doesn't match a case
                break;
        }
        return result;
    }

    public void editProduct(int id, String name, String description, int stock, String image,
            String color, String size, String releasedate, double discount, double price, int categoryId, int supplierId, int typeId) throws SQLException, Exception {
        String priceStr = String.valueOf(price);
        String stockStr = String.valueOf(stock);
        String discountStr = String.valueOf(discount);

        if (!isValidPrice(priceStr)) {
            throw new SQLException("Giá không hợp lệ hoặc nhỏ hơn 0!");
        }
        if (!isValidStock(stockStr)) {
            throw new SQLException("Số lượng không hợp lệ hoặc nhỏ hơn 0!");
        }
        if (!isValidDiscount(discountStr)) {
            throw new SQLException("Giảm giá không hợp lệ (phải từ 0 đến 1)!");
        }

        String sql = "UPDATE [dbo].[Products] SET [productname] = ?, [supplierid] = ?, [categoryid] = ?, [size] = ?, [stock] = ?, [description] = ?";
        if (!image.isEmpty()) {
            sql += ", [images] = ?";
        }
        sql += ", [colors] = ?, [releasedate] = ?, [discount] = ?, [price] = ?, [typeid] = ? WHERE [id] = ?";

        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, name);
                ptm.setInt(2, supplierId);
                ptm.setInt(3, categoryId);
                ptm.setString(4, size);
                ptm.setInt(5, stock);
                ptm.setString(6, description);
                int paramIndex = 7;
                if (!image.isEmpty()) {
                    ptm.setString(paramIndex++, image);
                }
                ptm.setString(paramIndex++, color);
                ptm.setString(paramIndex++, releasedate);
                ptm.setDouble(paramIndex++, discount);
                ptm.setDouble(paramIndex++, price);
                ptm.setInt(paramIndex++, typeId);
                ptm.setInt(paramIndex, id);
                ptm.executeUpdate();
            }
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void deleteProduct(String pid) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(DELETE_PRODUCT);
                ptm.setString(1, pid);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void updateQuanityProduct(CartItem item) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_QUANTITY_PRODUCT);
                ptm.setInt(1, (item.getProduct().getStock() - item.getQuantity()));
                ptm.setInt(2, item.getProduct().getId());
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void insertProduct(String name, int cId, int sId, int tId, double price, double discount, String size, String color, int stock,
            String date, String images, String description) throws SQLException, Exception {
        String priceStr = String.valueOf(price);
        String stockStr = String.valueOf(stock);
        String discountStr = String.valueOf(discount);

        if (!isValidPrice(priceStr)) {
            throw new SQLException("Giá không hợp lệ hoặc nhỏ hơn 0!");
        }
        if (!isValidStock(stockStr)) {
            throw new SQLException("Số lượng không hợp lệ hoặc nhỏ hơn 0!");
        }
        if (!isValidDiscount(discountStr)) {
            throw new SQLException("Giảm giá không hợp lệ (phải từ 0 đến 1)!");
        }

        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(INSERT_PRODUCT);
                ptm.setString(1, name);
                ptm.setInt(2, sId);
                ptm.setInt(3, cId);
                ptm.setString(4, size);
                ptm.setInt(5, stock);
                ptm.setString(6, description);
                ptm.setString(7, images);
                ptm.setString(8, color);
                ptm.setString(9, date);
                ptm.setDouble(10, discount);
                ptm.setInt(11, 0); // unitSold
                ptm.setDouble(12, price);
                ptm.setInt(13, 1); // status
                ptm.setInt(14, tId);
                ptm.executeUpdate();
            }
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public List<ProductDTO> searchByPrice(List<ProductDTO> list, double priceFrom, double priceTo) {
        List<ProductDTO> rs = new ArrayList<>();
        for (ProductDTO productDTO : list) {
            if (priceFrom != 0) {
                if (priceTo != 0) {
                    if (productDTO.getSalePrice() >= priceFrom && productDTO.getSalePrice() <= priceTo) {
                        rs.add(productDTO);
                    }
                } else if (productDTO.getSalePrice() >= priceFrom) {
                    rs.add(productDTO);
                }
            }
        }
        return rs;
    }

    public List<ProductDTO> searchByColors(List<ProductDTO> list, String[] colors) {
        List<ProductDTO> rs = new ArrayList<>();
        for (ProductDTO product : list) {
            for (String color : colors) {
                for (String productColor : product.getColors()) {
                    if (productColor.trim().equalsIgnoreCase(color.trim())) {
                        rs.add(product);
                        break; // Thoát vòng lặp màu của sản phẩm nếu đã tìm thấy
                    }
                }
                if (rs.contains(product)) {
                    break; // Thoát vòng lặp colors nếu sản phẩm đã được thêm
                }
            }
        }
        return rs;
    }

    public List<ProductDTO> searchByDiscount(List<ProductDTO> list, double discount) {
        List<ProductDTO> rs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDiscount() > discount) {
                rs.add(list.get(i));
            }
        }
        return rs;
    }

    public List<ProductDTO> searchByCheckBox(List<ProductDTO> list, int[] cid) throws Exception {

        List<ProductDTO> result = new ArrayList<>();
        if (cid[0] == 0) {
            return list;
        }

        for (ProductDTO productDTO : list) {
            for (int i = 0; i < cid.length; i++) {
                if (productDTO.getCategory().getId() == cid[i]) {
                    result.add(productDTO);
                }
            }
        }

        return result;
    }

    public void restoreProduct(String pid) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(RESTORE_PRODUCT);
                ptm.setString(1, pid);
                ptm.executeUpdate();
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void permanentlyDeleteProduct(String pid) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(PERMANENTLY_DELETE_PRODUCT);
                ptm.setString(1, pid);
                ptm.executeUpdate();
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public List<ProductDTO> searchProducts(String searchQuery) throws SQLException, Exception {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(SEARCH_PRODUCTS);
                String searchPattern = "%" + searchQuery + "%";
                ptm.setString(1, searchPattern);
                ptm.setString(2, searchPattern);
                ptm.setString(3, searchPattern);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    CategoryDAO cDao = new CategoryDAO();
                    SupplierDAO sDao = new SupplierDAO();
                    TypeDAO tDao = new TypeDAO();
                    String productname = rs.getString("productname");
                    int id = rs.getInt("id");
                    SupplierDTO supplier = sDao.getSupplierById(rs.getInt("supplierid"));
                    CategoryDTO category = cDao.getCategoryById(rs.getInt("categoryid"));
                    TypeDTO type = tDao.getTypeById(rs.getInt("typeid"));
                    int stock = rs.getInt("stock");
                    String description = rs.getString("description");
                    Date date = rs.getDate("releasedate");
                    double discount = rs.getDouble("discount");
                    int unitSold = rs.getInt("unitSold");
                    double price = rs.getDouble("price");
                    boolean status = rs.getBoolean("status");
                    String colors[] = rs.getString("colors").split(",");
                    String images[] = rs.getString("images").split(" ");
                    String sizes[] = rs.getString("size").split(",");
                    ProductDTO product = new ProductDTO(id, productname, description, stock, unitSold, images, colors, sizes, date, discount, price, status, category, supplier, type);
                    products.add(product);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return products;
    }

    // MĐH
    public List<Object[]> getTopProductsByOrderCount(int limit) throws SQLException {
        List<Object[]> topProducts = new ArrayList();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = this.getConnection();
            if (conn != null) {
                String query = "SELECT p.id, p.productname, COUNT(oi.order_id) as orderCount FROM Products p LEFT JOIN OrderItem oi ON p.id = oi.product_id WHERE p.status = 1 GROUP BY p.id, p.productname ORDER BY orderCount DESC";
                ptm = conn.prepareStatement(query);
                rs = ptm.executeQuery();

                while (rs.next()) {
                    Object[] productData = new Object[]{rs.getInt("id"), rs.getString("productname"), rs.getInt("orderCount")};
                    topProducts.add(productData);
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (ptm != null) {
                ptm.close();
            }

            if (conn != null) {
                conn.close();
            }

        }

        return topProducts;
    }
}
