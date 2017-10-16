package com.onemenu.driver;

public class OrderStatus {
    public static int    Invailid = 1;
    public static int    Finished = 2;
    public static int    WaitForCustomerPay = 3;
    public static int    PayFailed = 4;
    public static int    PaySuccessed = 5;
    public static int    PendingForRestaurant = 6;
    public static int    RestaurantConfirmedWithOneMenuDelivery = 7;
    public static int    RestaurantConfirmedWithSelfDelivery = 8;
    public static int    RestaurantCanceled = 9;
    public static int    RestaurantCanceledAfterDriverPickuped = 10;
    public static int    OneMenuCanceled = 11;
    public static int    CustomerCanceled = 12;
    public static int    RestaurantPendingOneMenuDelivery = 13;
    public static int    PendingForDriverAccept = 14;
    public static int    DriverAccepted = 15;
    public static int    DriverPickuped = 16;

    public static int    OrderOver = 99;
}
