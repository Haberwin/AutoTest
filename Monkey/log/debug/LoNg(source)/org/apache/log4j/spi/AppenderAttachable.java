package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;

public abstract interface AppenderAttachable
{
  public abstract void addAppender(Appender paramAppender);
  
  public abstract Enumeration getAllAppenders();
  
  public abstract Appender getAppender(String paramString);
  
  public abstract boolean isAttached(Appender paramAppender);
  
  public abstract void removeAllAppenders();
  
  public abstract void removeAppender(Appender paramAppender);
  
  public abstract void removeAppender(String paramString);
}


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/spi/AppenderAttachable.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */