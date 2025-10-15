package com.Delhivery;
import java.util.*;

// Need to design meet scheduler service
//u1, u2,u3 are users
//working hours 9AM-6PM
// schedule a meeting with n numbers
// one function for getting all available slot of a member
// one function to schedudle the meeting of n number
// if any person not avaiable, give the next available slot - print starting time and meeting length

class User{
    private String name;
    private String Id;
    private TreeMap<Integer,Integer> slot = new TreeMap<>();

    public User(String name, String id) {
        this.name = name;
        Id = id;
        for(int time = 6;time<=21;time++)
        {
            slot.put(time,0);
        }
    }

    public String getName() {
        return name;
    }
    public String getId(){
        return Id;
    }
    public void setSlot(Integer startTIme, Integer duration)
    {
        int finalTime = startTIme + duration;
        if(startTIme<9 && startTIme>18)
        {
            System.out.println("Out of Available time slot");
        }
        else if(finalTime>6)
        {
            System.out.println("Out of Available time slot");
        }
        else {
            for(int time = startTIme;time<finalTime;time++ )
            {
                slot.put(time,1);
            }
        }
    }
    public void getAvailableSlots()
    {
        for(int time = 6;time<=21;time++)
        {
            if(slot.get(time)==0)
            {
                System.out.println("Time available from" + time + " to " + time + 1);
            }
        }
    }
    public boolean checkAvailability(int startTime, int duration)
    {
        for(int time = startTime;time <startTime+duration;time++)
        {
            if(slot.get(time)==1)
            {
                return false;
            }
        }
        return true;
    }
}



class Meeting{
    private int numberOfUser = 0;
    private List<User> userList = new ArrayList<>();
    public void addMember(User user,int startTime,int duration)
    {
        int finalTime = startTime + duration;
        boolean flag_checker = true;
        if(user.checkAvailability(startTime,duration)==false) {
            flag_checker = false;
        }
    }
}

class Manager{
    Scanner sc = new Scanner(System.in);

}


public class Delhivery {
    public static void main(String args[])
    {
        System.out.println("hi");
    }
}
