package com.inventoryManagement;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum ProductType{
    iphone,
    samsung,
}


class Product {
    private int stock;
    private ProductType typeOfProduct;
    List<EmailObserver> email = new ArrayList<>();
    List<SmsObserver> sms = new ArrayList<>();

    Product (int stock, ProductType typeOfProduct,List<EmailObserver>email, List<SmsObserver>sms){
        this.stock = stock;
        this.typeOfProduct = typeOfProduct;
        this.email = email;
        this.sms = sms;
    }

    public int getStock(){
        return stock;
    }
    public ProductType getTypeOfProduct(){
        return typeOfProduct;
    }
    public void setStock(int stock) {
        if(this.stock == 0) {
            notifyAll(this.typeOfProduct,stock);
        }
        this.stock += stock;
    }
    public String useStock(int stock){
        if(this.stock<stock)
        {
            return "Available Stock less, only " + Integer.toString(this.stock) + "left";
        }
        this.stock -= stock;
        if(this.stock == 0)
        {
            notifyAll(this.typeOfProduct,this.stock);
            return "Stock N/A";
        }
        return "Stock deducted";
    }
    public void notifyAll(ProductType productType, int stock){
        for(EmailObserver emailtype : email){
            emailtype.update(productType,stock);
        }
        for(SmsObserver smstype : sms){
            smstype.update(productType,stock);
        }
    }
}



class EmailObserver{
    private String name;
    private String emailId;
    EmailObserver(String name, String emailId){
        this.emailId = emailId;
        this.name = name;
    }
    public void update(ProductType productType,int stock){
        System.out.println("Hey! "  +this.name + " " + productType + " is having just " + stock + "left products  through email");
    }
}
class SmsObserver{
    private String name;
    private String Number;
    SmsObserver(String name, String Number){
        this.Number = Number;
        this.name = name;
    }
    void update(ProductType productType,int stock){
        System.out.println("Hey! "+ this.name + " " + productType + " is having just " + stock + "left products  through sms");
    }
}


class InventoryOrchestrator{

    public void addStock(Product product,int stock){
        product.setStock(stock);
    }
    public void removeStock(Product product,int stock){
        product.useStock(stock);
    }


}


public class InventoryManagement {

    public static void main(String []args){
        InventoryOrchestrator inventoryOrchestratorManager = new InventoryOrchestrator();
        List<EmailObserver>emailObservers = new ArrayList<>();
        List<SmsObserver>smsObservers = new ArrayList<>();
        EmailObserver obj1 = new EmailObserver("jay","abcd@gmail.com");
        EmailObserver obj2 = new EmailObserver("Jiya","jiya@gmail.com");
        SmsObserver obj3 = new SmsObserver("Dad","234234");
        SmsObserver obj4 = new SmsObserver("MOM","23443556");
        emailObservers.add(obj1);
        emailObservers.add(obj2);
        smsObservers.add(obj3);
        smsObservers.add(obj4);
        int i=0;
        Product iphone = new Product(0, ProductType.iphone,emailObservers,smsObservers);
        Product samsung = new Product(0,ProductType.samsung,emailObservers,smsObservers);
        while(i<50){
            System.out.println("Press 1 to add product or 2 to remove the product");
            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();
            if(num == 1)
            {
                System.out.println("press 1 to add iphone or 2 for samsung");
                int productType = sc.nextInt();
                if(productType == 1 )
                {
                    int stockCount = sc.nextInt();
                    inventoryOrchestratorManager.addStock(iphone,stockCount);
                }
                else if (productType ==2) {
                    int stockCount = sc.nextInt();
                    inventoryOrchestratorManager.addStock(samsung,stockCount);
                }
                else {
                    System.out.println("Enter correct product");
                }
                i++;
            }
            else if(num ==2)
            {
                System.out.println("press 1 to remove iphone or 2 for samsung");
                int productType = sc.nextInt();
                if(productType == 1 )
                {
                    int stockCount = sc.nextInt();
                    inventoryOrchestratorManager.removeStock(iphone,stockCount);
                }
                else if (productType ==2) {
                    int stockCount = sc.nextInt();
                    inventoryOrchestratorManager.removeStock(samsung,stockCount);
                }
                else {
                    System.out.println("Enter correct product");
                }
                i++;
            }
            else {
                System.out.println("Try again");

            }
        }
    }
}
