package ddt.sms16.ivu.di.uniba.it.easycar;

import java.util.ArrayList;

/**
 * Created by Maurizio on 09/07/16.
 */
public class Group {

    private String Name;
    private ArrayList<Child> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> Items) {
        this.Items = Items;
    }

}