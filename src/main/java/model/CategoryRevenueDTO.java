package model;

/**
 *
 * @author Group - 07
 */
public class CategoryRevenueDTO {

    private String categoryName;
    private double revenue;
    private int totalOrders;

    public CategoryRevenueDTO(String categoryName, double revenue, int totalOrders) {
        this.categoryName = categoryName;
        this.revenue = revenue;
        this.totalOrders = totalOrders;
    }

    // Getters and setters
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
}
