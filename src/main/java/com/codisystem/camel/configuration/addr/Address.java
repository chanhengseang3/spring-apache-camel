package com.codisystem.camel.configuration.addr;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "address")
public class Address {

    private String name;

    private String street;

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    @XmlElement(name = "street")
    public void setStreet(String stree) {
        this.street = stree;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
