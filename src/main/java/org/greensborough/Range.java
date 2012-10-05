package org.greensborough;

public interface Range<T>
  extends Iterable<T>
{
  T start();

  T end();
}
