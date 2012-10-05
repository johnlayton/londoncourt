package org.greensborough;

import org.greensborough.Dates;
import org.greensborough.Range;
import org.greensborough.Ranges;
import org.testng.annotations.Test;

import java.util.Date;

public class DatesTest
{
  @Test
  public void testShouldCalculateGaps()
  {
    final Ranges<Range<Date>> ranges = Dates.dateRanges();

    ranges
      .plus( Dates.dateRange( Dates.at( 2012, 1, 1 ), Dates.at( 2012, 1, 2 ) ) )
      .plus( Dates.dateRange( Dates.at( 2012, 1, 3 ), Dates.at( 2012, 1, 4 ) ) )
      .plus( Dates.dateRange( Dates.at( 2012, 1, 5 ), Dates.at( 2012, 1, 6 ) ) );

    for ( Range<java.util.Date> gap : ranges.gaps() )
    {
      System.out.println( gap );
    }
  }

  @Test
  public void testShouldProduceNextYearsRoster()
  {
    Range<java.util.Date> dates = Dates.dateRange( Dates.at( 2013, 0, 1 ), Dates.at( 2013, 11, 31 ) );

    java.util.Calendar calendar = java.util.Calendar.getInstance();

    String header =
      n( "<html><head><style>" ) +
      n( "@media all {" ) +
      "\t.page-break  { display:none; }\n" +
      "}\n" +
      "@media print {\n" +
      "\t.page-break { " +
      "\t\tdisplay:block; " +
      "\t\tpage-break-before:always; " +
      "\t}\n" +
      "}\n" +
      "body {\n" +
      "\tfont-family: Arial,Helvetica,sans-serif;\n" +
      "}\n" +
      "table, th, td {\n" +
      "\tborder: 1px solid black;\n" +
      "}\n" +
      "td {\n" +
      "\twidth: 200px;\n" +
      "}\n" +
      "table > caption {\n" +
      "\tfont-weight: bold;\n" +
      "}\n" +
      ".short {\n" +
      "\tbackground: #CCC;\n" +
      "}\n" +
      "</style><head><body><table><caption>January</caption>";

    System.out.println( header );

    int month = 0;

    for ( java.util.Date date : dates )
    {
      calendar.setTime( date );
      int day = calendar.get( java.util.Calendar.DAY_OF_WEEK );
      if ( day == 1 || day == 3 || day == 5 )
      {
        if ( month != calendar.get( java.util.Calendar.MONTH ) )
        {
          month = calendar.get( java.util.Calendar.MONTH );
          System.out.println( "</table>" );

          if ( month > 0 && ( month % 2 ) == 0 )
          {
            System.out.println( "<div class=\"page-break\">&nbsp;</div>" );
          }
          System.out.println( "<table>" );
          System.out
            .println( "<caption>" +
                      calendar.getDisplayName( java.util.Calendar.MONTH,
                                               java.util.Calendar.LONG,
                                               java.util.Locale.UK ) +
                      "</caption>" );
        }
        System.out
          .println( Dates.at( date ).format( "<tr><td>%s</td><td>&nbsp;</td><td>&nbsp;</td><td " +
                                             ( ( day == 5 ) ? " class=\"short\" " : "" ) +
                                             ">&nbsp;</td></tr>" ) );
      }
    }
    System.out.println( "</table></body></html>" );
  }


  private static String n( final String text )
  {
    return text + "\n";
  }

  private static String t( final String text )
  {
    return "\t" + text;
  }

}
