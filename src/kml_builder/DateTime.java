package kml_builder;

public class DateTime {
	public static String gYear(int Year) {
		String out = Integer.toString(Year);
		return out;
	}
	
	public static String gYearMonth(int Year, byte Month) {
		String out = String.format("%d-%d", Year,Month);
		return out;
	}
	
	public static String date(int Year, byte Month,byte Day) {
		String out = String.format("%d-%d-%d", Year,Month,Day);
		return out;
	}
	
	public static String dateTime(int Year, byte Month,byte Day,byte Hour, byte Minute,byte Second) {
		String out = Integer.toString(Year);
		return out;
	}
}
