package com.codisystem.camel.configuration;

import org.apache.camel.Message;
import org.apache.camel.ValidationException;
import org.apache.camel.spi.DataType;
import org.apache.camel.spi.Validator;

public class MyValidator extends Validator {
    @Override
    public void validate(Message message, DataType type) throws ValidationException {
//        ValidatorBuilder v = validator().type("xml").withUri("validator:sample.xsd");
    }
}
