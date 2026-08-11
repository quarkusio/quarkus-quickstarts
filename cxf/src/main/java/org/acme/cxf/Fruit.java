package org.acme.cxf;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(FruitAdapter.class)
public interface Fruit {

    String getName();
    
    String getDescription();
}
