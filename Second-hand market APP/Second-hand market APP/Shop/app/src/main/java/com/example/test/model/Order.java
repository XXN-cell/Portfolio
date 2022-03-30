package com.example.test.model;

public class Order {
    int _id;
    String order_name;
    String time;
    String productStr;
    Product product;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductStr() {
        return productStr;
    }

    public void setProductStr(String productStr) {
        this.productStr = productStr;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
