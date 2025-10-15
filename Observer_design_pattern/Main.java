/* so this question is taken as suppose there is a thing to observe for example iphone, we can add many more
 to this, and whenever certain things change we have to send the notification via email or message.*/

package com.Observer_design_pattern;

import com.Observer_design_pattern.Observable.Observable;
import com.Observer_design_pattern.Observable.IphoneObservable;
import com.Observer_design_pattern.Observable.SamsungObservable;
import com.Observer_design_pattern.Observer.*;

public class Main {
    public static void main(String args[]) {
        Observable obj = new IphoneObservable();
        Observable obj1 = new SamsungObservable();
        /* we have added object of observable because */
        Observer observer1 = new EmailObserver("abc", obj);
        Observer observer2 = new EmailObserver("abce", obj);
        Observer observer3 = new EmailObserver("abcd", obj1);
        Observer observer4 = new SmsObsever("abcf", obj1);


        obj.add(observer1);
        obj.add(observer2);
        obj1.add(observer3);
        obj1.add(observer4);

        obj1.setStock(10);


    }
}

