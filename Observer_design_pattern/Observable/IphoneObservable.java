package com.Observer_design_pattern.Observable;
import com.Observer_design_pattern.Observer.Observer;
import com.tictactoe.models.Symbol;

import java.util.ArrayList;
import java.util.List;

public class IphoneObservable implements Observable{
    StockName typeOfDevice = StockName.Iphone;
    public List <Observer> observerList = new ArrayList<>();
    int stock_count=0;
    public void add(Observer obv){
        observerList.add(obv);
    }
    public void remove(Observer obv){
        observerList.remove(obv);
    }
    public void notifySubscribers(int stock_count1){
        for(Observer obv:observerList)
        {
            obv.update(StockName.Iphone,stock_count1);   //since we
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
