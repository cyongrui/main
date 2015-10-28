package doordonote.commandfactory;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.CalendarSource;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateParser {
	protected Parser natty = null;
	protected Date defaultDate = null;

	public DateParser() {
		natty = new Parser();
		defaultDate = parse("today 8am");
	}
	
	public Date parse(String input) {
		// input should always be checked to be no null
		assert(input != null);
		CalendarSource.setBaseDate(defaultDate);
		List<DateGroup> baseDateList = natty.parse(input);
		try {
			DateGroup baseDateGroup = baseDateList.get(0);
			List<Date> dateList = baseDateGroup.getDates();
			return dateList.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
