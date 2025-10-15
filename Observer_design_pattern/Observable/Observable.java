package com.Observer_design_pattern.Observable;

import com.Observer_design_pattern.Observer.Observer;

public interface Observable {
    public void add(Observer obv);
    public void remove(Observer obv);
    public void notifySubscribers(int stockNum);
    public void setStock(int Stock_update);
    public int getStock();
}
