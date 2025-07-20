package dao;

import utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SupplierDTO;

/**
 *
 * @author Group - 07
 */

public class SupplierDAO extends DBContext {
    private static final String GETDATA = "SELECT * FROM Suppliers";
    private static final String GETSUPPLIERBYID = "SELECT * FROM Suppliers WHERE supplierid = ?";
    private static final String INSERT_SUPPLIER = "INSERT INTO Suppliers (suppliername, supplierimage) VALUES (?, ?)";
    private static final String UPDATE_SUPPLIER = "UPDATE Suppliers SET suppliername = ?, supplierimage = ? WHERE supplierid = ?";
    private static final String DELETE_SUPPLIER = "DELETE FROM Suppliers WHERE supplierid = ?";

    public List<SupplierDTO> getData() throws SQLException {
        List<SupplierDTO> suppliers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GETDATA);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int supplierId = rs.getInt("supplierid");
                    String supplierName = rs.getString("suppliername");
                    String supplierImage = rs.getString("supplierimage");
                    suppliers.add(new SupplierDTO(supplierId, supplierName, supplierImage));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return suppliers;
    }

    public SupplierDTO getSupplierById(int id) throws SQLException {
        SupplierDTO supplier = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GETSUPPLIERBYID);
                ptm.setInt(1, id);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    int supplierId = rs.getInt("supplierid");
                    String supplierName = rs.getString("suppliername");
                    String supplierImage = rs.getString("supplierimage");
                    supplier = new SupplierDTO(supplierId, supplierName, supplierImage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return supplier;
    }

    public void insertSupplier(String name, String image) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(INSERT_SUPPLIER);
                ptm.setString(1, name);
                ptm.setString(2, image);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
    }

    public void updateSupplier(int id, String name, String image) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_SUPPLIER);
                ptm.setString(1, name);
                ptm.setString(2, image);
                ptm.setInt(3, id);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
    }

    public void deleteSupplier(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(DELETE_SUPPLIER);
                ptm.setInt(1, id);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
    }
    public List<SupplierDTO> searchSuppliers(String searchQuery) throws SQLException {
        List<SupplierDTO> suppliers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn != null) {
                String query = "SELECT * FROM Suppliers WHERE suppliername LIKE ?";
                ptm = conn.prepareStatement(query);
                String searchPattern = "%" + searchQuery + "%";
                ptm.setString(1, searchPattern);

                rs = ptm.executeQuery();
                while (rs.next()) {
                    int supplierId = rs.getInt("supplierid");
                    String supplierName = rs.getString("suppliername");
                    String supplierImage = rs.getString("supplierimage");
                    suppliers.add(new SupplierDTO(supplierId, supplierName, supplierImage));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return suppliers;
    }
    
//    public static void main(String[] args) throws SQLException {
//        SupplierDAO dao = new SupplierDAO();
//        List<SupplierDTO> list = dao.getData();
//        for (SupplierDTO supplier : list) {
//            System.out.println(supplier.getName());
//        }
//    }
}