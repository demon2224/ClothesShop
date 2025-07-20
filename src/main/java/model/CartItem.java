package model;

/**
 *
 * @author Gruop - 07
 */
public class CartItem {
    private ProductDTO product;
    private int quantity;

    public CartItem() {
    }

    
    public CartItem(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return product.getId()+","+quantity+"";
    }
    
    
}
