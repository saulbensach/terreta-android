package bensach.saul.terreta;

import java.io.Serializable;

/**
 * Created by saul on 10/06/17.
 */

public class Payments implements Serializable{

    private int price;
    private String date;

    public Payments(int price, String date){
        this.price = price;
        this.date = date;
    }

    @Override
    public String toString() {
        return "\n\t\tPRICE: "+price+"\n\t\tDATE: "+date;
    }
}
