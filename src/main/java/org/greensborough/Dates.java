package org.greensborough;

public class Dates
{
  public static Ranges<Range<java.util.Date>> dateRanges()
  {
    return new DateRangesImpl();
  }

  public static Range<java.util.Date> dateRange( java.util.Date start, java.util.Date finish)
  {
    return new DateRangeBasicImpl( start, finish );
  }

  public static Date at( int year, int month, int day )
  {
    return Date.at( year, month, day );
  }

  public static Date at( java.util.Date date )
  {
    return Date.at( date );
  }

  public static final class Date
    extends java.util.Date
  {
    private static final Integer[] FIELDS = new Integer[] {
      java.util.Calendar.YEAR,
      java.util.Calendar.MONTH,
      java.util.Calendar.DATE,
      java.util.Calendar.HOUR_OF_DAY,
      java.util.Calendar.MINUTE,
      java.util.Calendar.SECOND,
      java.util.Calendar.MILLISECOND
    };

    public Date( java.util.Date date )
    {
      super( date.getTime() );
    }

    public String format( final String template )
    {
      return String.format( template, new java.text.SimpleDateFormat( "EEEE dd" ).format( this ) );
      //      return String.format(template, java.text.DateFormat.getDateInstance( java.text.DateFormat.LONG ).format( this ));
    }

    public static Date at( java.util.Date date )
    {
      return new Date( date );
    }

    public static Date at( int year, int month, int day )
    {
      return Date.at( year, month, day, 0, 0, 0, 0 );
    }

    public static Date at( int... values )
    {

      java.util.Calendar calendar = java.util.Calendar.getInstance();
      for ( int i = 0; i < FIELDS.length && i < values.length; i++ )
      {
        calendar.set( FIELDS[i], values[i] );
      }

      return new Date( calendar.getTime() );
    }
  }

  private static abstract class RangesAbstractImpl<T>
    extends java.util.ArrayList<T>
    implements Ranges<T>
  {

  }


  private static final class DateRangesImpl
    //extends RangesAbstractImpl<DateRange>
    extends RangesAbstractImpl<Range<java.util.Date>>
    //implements DateRanges
    implements Ranges<Range<java.util.Date>>
  {
    /*
    @Override
    public <S extends DateRange> Ranges<DateRange> plus( final S range )
    {
      add( range );
      return this;
    }
    */

    @Override
    public <S extends Range<java.util.Date>> Ranges<Range<java.util.Date>> plus( final S range )
    {
      add( range );
      return this;
    }

    @Override
    public java.util.List<Range<java.util.Date>> gaps()
    {
      java.util.List<Range<java.util.Date>> gaps = new java.util.ArrayList<Range<java.util.Date>>();
      java.util.List<Range<java.util.Date>> sorted = sorted();

      for ( int i = 0, j = 1; j < sorted.size(); i++, j++ )
      {
        if ( sorted.get( i ).end().before( sorted.get( j ).start() ) )
        {
          gaps.add( new DateRangeBasicImpl( sorted.get( i ).end(), sorted.get( j ).start() ) );
        }
      }

      return gaps;
    }

    private java.util.List<Range<java.util.Date>> sorted()
    {
      final java.util.List<Range<java.util.Date>> other = new java.util.ArrayList<Range<java.util.Date>>();

      other.addAll( this );

      java.util.Collections.sort( other, new java.util.Comparator<Range<java.util.Date>>()
      {
        @Override
        public int compare( Range<java.util.Date> o1, Range<java.util.Date> o2 )
        {
          return o1.start().compareTo( o1.start() );
        }
      } );

      return other;
    }
  }

  private static abstract class IntegerRangeAbstractImpl
    implements IntegerRange
  {
    @Override
    public java.util.Iterator<java.lang.Integer> iterator()
    {
      return new java.util.Iterator<java.lang.Integer>()
      {
        private java.lang.Integer current = start();

        @Override
        public boolean hasNext()
        {
          return current < end();
        }

        @Override
        public java.lang.Integer next()
        {
          return current++;
        }

        @Override
        public void remove()
        {
        }
      };
    }
  }

  private static abstract class DateRangeAbstractImpl
    implements DateRange
  {
    @Override
    public java.util.Iterator<java.util.Date> iterator()
    {
      return new java.util.Iterator<java.util.Date>()
      {
        private int field = java.util.Calendar.DAY_OF_YEAR;
        private java.util.Date next = start();

        @Override
        public boolean hasNext()
        {
          return next.before( end() );
        }

        @Override
        public java.util.Date next()
        {
          java.util.Calendar calendar = java.util.Calendar.getInstance();
          calendar.setTime( next );

          calendar.roll( field, 1 );
          next = calendar.getTime();

          calendar.roll( field, -1 );
          return calendar.getTime();
        }

        @Override
        public void remove()
        {
        }

      };
    }
  }

  private static final class IntegerRangeBasicImpl
    extends IntegerRangeAbstractImpl
    implements IntegerRange
  {
    private final java.lang.Integer start;
    private final java.lang.Integer end;

    public IntegerRangeBasicImpl( java.lang.Integer start, java.lang.Integer end )
    {
      this.start = start;
      this.end = end;
    }

    @Override
    public java.lang.Integer start()
    {
      return start;
    }

    @Override
    public java.lang.Integer end()
    {
      return end;
    }
  }

  private static final class DateRangeBasicImpl
    extends DateRangeAbstractImpl
    implements DateRange
  {
    private final java.util.Date start;
    private final java.util.Date end;

    public DateRangeBasicImpl( java.util.Date start, java.util.Date end )
    {
      this.start = start;
      this.end = end;
    }

    @Override
    public java.util.Date start()
    {
      return start;
    }

    @Override
    public java.util.Date end()
    {
      return end;
    }

    @Override
    public String toString()
    {
      return String.format( "DateRangeBasicImpl [ %s -> %s ]",
                            new Date( start ).format( "%s" ),
                            new Date( end ).format( "%s" ) );
    }
  }

  private static final class DateRangeRawImpl
  {
    @Start
    private final java.util.Date a;

    @End
    private final java.util.Date b;

    public DateRangeRawImpl( java.util.Date a, java.util.Date b )
    {
      this.a = a;
      this.b = b;
    }

    public java.util.Date getA()
    {
      return a;
    }

    public java.util.Date getB()
    {
      return b;
    }
  }

}
