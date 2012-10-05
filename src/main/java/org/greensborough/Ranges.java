package org.greensborough;

public interface Ranges<T>
  extends Iterable<T>
{
  <S extends T> Ranges<T> plus( S range );

  java.util.List<T> gaps();
}
