package com.mine.spring.factory.domain;

/**
 * @author caoyang
 * @description 汽车类
 * @time 2023-04-22 10:30:35
 */
public class Car {
    private String brand;

    public Car() {

    }

    public Car(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car [brand=" + brand + "]";
    }
}
