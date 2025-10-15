package com.Observer_design_pattern.Observer;
import com.Observer_design_pattern.Observable.Observable;
import com.Observer_design_pattern.Observable.StockName;

public class SmsObsever implements Observer{

    String UserName;
    Observable obv;

    public SmsObsever(String userName, Observable obv) {
        UserName = userName;
        this.obv = obv;
    }

    @Override
    public void update(StockName deviceType, int curr_stock) {
        System.out.println(deviceType+" is available" + " and the current stock is "+curr_stock);
    }
}
