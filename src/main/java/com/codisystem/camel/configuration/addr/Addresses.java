package com.codisystem.camel.configuration.addr;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "addresses")
public class Addresses {

    List<Address> addressList;

    public List<Address> getAddressList() {
        return addressList;
    }

    @XmlElement(name = "address")
    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
