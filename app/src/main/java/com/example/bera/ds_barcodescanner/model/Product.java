package com.example.bera.ds_barcodescanner.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by bera on 2015-05-31.
 * modified by hyewon on 2015-06-25
 */
@XStreamAlias("product")
public class Product implements Serializable {
    public String getUser(){return user;}

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    @XStreamAlias("user")
    private String user;
    @XStreamAlias("barcode")
    private String barcode;
    @XStreamAlias("name")
    private String name;

    public Product(String user, String barcode, String name) {
        this.user=user;
        this.barcode = barcode;
        this.name = name;}
}
