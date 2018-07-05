package org.apache.log4j.spi;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;

public abstract interface RendererSupport
{
  public abstract RendererMap getRendererMap();
  
  public abstract void setRenderer(Class paramClass, ObjectRenderer paramObjectRenderer);
}


/* Location:              /home/liuwenhua/tools/LoNg(Official)_ALPS/LoNg_v2.1710.5 (2)/LoNg.jar!/org/apache/log4j/spi/RendererSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */