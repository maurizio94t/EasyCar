package ddt.sms16.ivu.di.uniba.it.easycar;

import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;

/**
 * Created by Maurizio on 09/07/16.
 */
public class Group {

    private String Name;
    private List<Problema> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public List<Problema> getItems() {
        return Items;
    }

    public void setItems(List<Problema> Items) {
        this.Items = Items;
    }

}