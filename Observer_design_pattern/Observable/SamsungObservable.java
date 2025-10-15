package com.Observer_design_pattern.Observable;

import com.Observer_design_pattern.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class SamsungObservable implements Observable{
    StockName typeOfDevice = StockName.Samsung;
    public List<Observer> observerList = new ArrayList<>();
    int stock_count=0;
    public void add(Observer obv){
        observerList.add(obv);
    }
    public void remove(Observer obv){
        observerList.remove(obv);
    }
    public void notifySubscribers(int stock_update){
        for(Observer obv:observerList)
        {
            obv.update(StockName.Samsung,stock_update);   //since we
        }
    }
    public void setStock(int stock_update) {
        if(stock_count==0)
        {
            notifySubscribers(stock_update);
        }
        stock_count+=stock_update;
    }
    public int getStock(){
        return stock_count;
    }
}
