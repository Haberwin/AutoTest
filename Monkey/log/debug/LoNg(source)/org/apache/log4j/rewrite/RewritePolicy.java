package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public abstract interface RewritePolicy
{
  public abstract LoggingEvent rewrite(LoggingEvent paramLoggingEvent);
}


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/rewrite/RewritePolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */