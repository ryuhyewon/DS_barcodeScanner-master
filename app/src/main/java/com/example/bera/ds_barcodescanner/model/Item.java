package com.example.bera.ds_barcodescanner.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bera on 2015-05-31.
 *  modified by hyewon on 2015-06-25
 */
@XStreamAlias("root")
public class Item {
    @XStreamImplicit(itemFieldName = "products")
    private List<Product> products = new ArrayList();

    public void add(Product product){
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }
}
