package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public abstract interface UnrecognizedElementHandler
{
  public abstract boolean parseUnrecognizedElement(Element paramElement, Properties paramProperties)
    throws Exception;
}


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/xml/UnrecognizedElementHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */