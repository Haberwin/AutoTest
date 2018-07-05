package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public abstract interface HierarchyEventListener
{
  public abstract void addAppenderEvent(Category paramCategory, Appender paramAppender);
  
  public abstract void removeAppenderEvent(Category paramCategory, Appender paramAppender);
}


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/spi/HierarchyEventListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */