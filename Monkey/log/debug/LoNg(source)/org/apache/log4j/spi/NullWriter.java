package org.apache.log4j.spi;

import java.io.Writer;

/**
 * @deprecated
 */
class NullWriter
  extends Writer
{
  public void close() {}
  
  public void flush() {}
  
  public void write(char[] cbuf, int off, int len) {}
}


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/spi/NullWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */