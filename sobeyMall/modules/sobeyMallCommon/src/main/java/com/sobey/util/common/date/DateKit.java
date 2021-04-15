package com.sobey.util.common.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 日期工具类
 Created by Rukiy on 2017-11-21
 */
public class DateKit {
		
		private DateKit(){
				// 静态类不可实例化
		}
		
		
		/**
		 获取YYYY格式
		 
		 @return
		 */
		public static String getYear(){
				return formatDate( new Date(), "yyyy" );
		}
		
		/**
		 获取YYYY格式
		 
		 @return
		 */
		public static String getYear( Date date ){
				return formatDate( date, "yyyy" );
		}
		
		/**
		 获取YYYY-MM-DD格式
		 
		 @return
		 */
		public static String getDay(){
				return formatDate( new Date(), "yyyy-MM-dd" );
		}
		
		/**
		 获取YYYY-MM-DD格式
		 
		 @return
		 */
		public static String getDay( Date date ){
				return formatDate( date, "yyyy-MM-dd" );
		}
		
		/**
		 获取YYYYMMDD格式
		 
		 @return
		 */
		public static String getDays(){
				return formatDate( new Date(), "yyyyMMdd" );
		}
		
		/**
		 获取YYYYMMDD格式
		 
		 @return
		 */
		public static String getDays( Date date ){
				return formatDate( date, "yyyyMMdd" );
		}
		
		/**
		 获取YYYY-MM-DD HH:mm:ss格式
		 
		 @return
		 */
		public static String getTime(){
				return formatDate( new Date(), "yyyy-MM-dd HH:mm:ss" );
		}
		
		/**
		 获取YYYY-MM-DD HH:mm:ss.SSS格式
		 
		 @return
		 */
		public static String getMsTime(){
				return formatDate( new Date(), "yyyy-MM-dd HH:mm:ss.SSS" );
		}
		
		/**
		 获取YYYYMMDDHHmmss格式
		 
		 @return
		 */
		public static String getAllTime(){
				return formatDate( new Date(), "yyyyMMddHHmmss" );
		}
		
		/**
		 获取YYYY-MM-DD HH:mm:ss格式
		 
		 @return
		 */
		public static String getTime( Date date ){
				return formatDate( date, "yyyy-MM-dd HH:mm:ss" );
		}
		
		/**
		 获取yyyy-MM-dd HH:mm:ss SSS格式
		 
		 @return
		 */
		public static String getTime( long timeMillis ){
				return formatDate( timeMillis, "yyyy-MM-dd HH:mm:ss SSS" );
		}
		
		public static String formatDate( Object dateOrTimeMillis, String pattern ){
				String formatDate = null;
				if ( StringUtils.isBlank( pattern ) ) {
						pattern = "yyyy-MM-dd";
				}
				if ( dateOrTimeMillis instanceof Date ) {
						formatDate = DateFormatUtils.format( (Date) dateOrTimeMillis, pattern );
				} else if ( dateOrTimeMillis instanceof Long ) {
						formatDate = DateFormatUtils.format( (Long) dateOrTimeMillis, pattern );
				}
				return formatDate;
		}
		
		/**
		 @param s
		 @param e
		 @return boolean
		 @throws
		 @Title: compareDate
		 @Description:(日期比较，如果s>=e 返回true 否则返回false)
		 @author luguosui
		 */
		public static boolean compareDate( String s, String e ){
				Date _s = parseDate( s );
				Date _e = parseDate( e );
				return compareTime( _s, _e );
		}
		
		/**
		 @param s
		 @param e
		 @return boolean
		 @throws
		 @Title: compareTime
		 @Description:(日期比较，如果s>=e 返回true 否则返回false)
		 @author luguosui
		 */
		public static boolean compareTime( String s, String e ){
				Date _s = parseTime( s );
				Date _e = parseTime( e );
				return compareTime( _s, _e );
		}
		
		/**
		 @param s
		 @param e
		 @return boolean
		 @throws
		 @Title: compareDate
		 @Description:(日期比较，如果s>=e 返回true 否则返回false)
		 @author luguosui
		 */
		public static boolean compareTime( Date s, Date e ){
				if ( s == null || e == null ) {
						return false;
				}
				return s.getTime() > e.getTime();
		}
		
		/**
		 格式化日期
		 
		 @return
		 */
		public static Date parseDate( String date ){
				return parse( date, "yyyy-MM-dd" );
		}
		
		/**
		 格式化日期
		 
		 @return
		 */
		public static Date parseTime( String date ){
				return parse( date, "yyyy-MM-dd HH:mm:ss" );
		}
		
		/**
		 格式化日期
		 
		 @return
		 */
		public static Date parse( String date, String pattern ){
				try {
						return DateUtils.parseDate( date, pattern );
				} catch ( ParseException e ) {
						e.printStackTrace();
						return null;
				}
		}
		
		/**
		 格式化日期带时区
		 
		 @return
		 */
		public static Date parse( String date, Locale locale, String pattern ){
				try {
						return DateUtils.parseDate( date, locale, pattern );
				} catch ( ParseException e ) {
						e.printStackTrace();
						return null;
				}
		}
		
		/**
		 格式化日期
		 
		 @return
		 */
		public static String format( Date date, String pattern ){
				return DateFormatUtils.format( date, pattern );
		}
		
		/**
		 把日期转换为Timestamp
		 
		 @param date
		 @return
		 */
		public static Timestamp format( Date date ){
				return new Timestamp( date.getTime() );
		}
		
		/**
		 校验日期是否合法
		 
		 @return
		 */
		public static boolean isValidDate( String s ){
				return parse( s, "yyyy-MM-dd HH:mm:ss" ) != null;
		}
		
		/**
		 校验日期是否合法
		 
		 @return
		 */
		public static boolean isValidDate( String s, String pattern ){
				return parse( s, pattern ) != null;
		}
		
		public static int getDiffYear( String startTime, String endTime ){
				DateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd" );
				try {
						int years = (int) ( ( ( fmt.parse( endTime ).getTime() - fmt.parse( startTime ).getTime() )/( 1000*60*60*24 ) )/365 );
						return years;
				} catch ( Exception e ) {
						// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
						return 0;
				}
		}
		
		/**
		 <li>功能描述：时间相减得到天数
		 
		 @param beginDateStr
		 @param endDateStr
		 @return long
		 @author Administrator
		 */
		public static long getDaySub( String beginDateStr, String endDateStr ){
				long day = 0;
				SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
				Date beginDate = null;
				Date endDate = null;
				
				try {
						beginDate = format.parse( beginDateStr );
						endDate = format.parse( endDateStr );
				} catch ( ParseException e ) {
						e.printStackTrace();
				}
				day = ( endDate.getTime() - beginDate.getTime() )/( 24*60*60*1000 );
				// System.out.println("相隔的天数="+day);
				
				return day;
		}
		
		/**
		 得到n天之后的日期
		 
		 @param days
		 @return
		 */
		public static String getAfterDayDate( String days ){
				int daysInt = Integer.parseInt( days );
				Calendar canlendar = Calendar.getInstance(); // java.util包
				canlendar.add( Calendar.DATE, daysInt ); // 日期减 如果不够减会将月变动
				Date date = canlendar.getTime();
				SimpleDateFormat sdfd = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				
				return sdfd.format( date );
		}
		
		public static String getAfterHour( int hour ){
			Calendar calendar = Calendar.getInstance(); // java.util包
			calendar.add( Calendar.HOUR_OF_DAY, hour  ); // 日期减 如果不够减会将月变动
			Date date = calendar.getTime();
			SimpleDateFormat sdfd = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			
			return sdfd.format( date );
		}
		
		public static String getAfterMinute( int hour ){
			Calendar calendar = Calendar.getInstance(); // java.util包
			calendar.add( Calendar.MINUTE, hour  ); // 日期减 如果不够减会将月变动
			Date date = calendar.getTime();
			SimpleDateFormat sdfd = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			
			return sdfd.format( date );
		}
		
		/**
		 得到n天之后是周几
		 
		 @param days
		 @return
		 */
		public static String getAfterDayWeek( String days ){
				int daysInt = Integer.parseInt( days );
				Calendar canlendar = Calendar.getInstance(); // java.util包
				canlendar.add( Calendar.DATE, daysInt ); // 日期减 如果不够减会将月变动
				Date date = canlendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat( "E" );
				return sdf.format( date );
		}
		
		
		private static Map<String, String> patternRegularMapper;
		
		static{
				patternRegularMapper = new HashMap<>();
				patternRegularMapper.put( "yyyyMMddHHmmss", "[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}" );
				patternRegularMapper.put( "yyyyMMdd", "[0-9]{4}[0-9]{2}[0-9]{2}" );
				patternRegularMapper.put( "yyyy-MM-dd HH:mm:ss",  "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}" );
				patternRegularMapper.put( "yyyy-MM-dd", "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}" );
				patternRegularMapper.put( "yyyy-MM-dd HH:mm", "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}" );
				patternRegularMapper.put( "yyyy MM dd HH mm", "[0-9]{4} [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}" );
				patternRegularMapper.put( "yyyy MM dd HH mm ss", "[0-9]{4} [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}" );
		}
		
		/**
		 从字符串中解析出日期字串
		 
		 @param str
		 @return
		 */
		public static Date analyzeDate( String str ){
				if ( str != null && !"".equals( str ) ) {
						for ( String pattern : patternRegularMapper.keySet() ) {
								Date date = analyzeDate( str, pattern );
								if ( date != null ) {
										return date;
								}
						}
				}
				return null;
		}
		/**
		 从字符串中解析出日期字串
		 
		 @param str
		 @return
		 */
		public static Date analyzeDate( String str, String patternStr ){
				if ( str != null && !"".equals( str ) && patternStr != null && !"".equals( patternStr ) ) {
						patternStr = patternStr.trim();
						String regular = patternRegularMapper.get( patternStr );
						if ( regular != null && !"".equals( regular ) ) {
								Pattern pattern = Pattern.compile( regular );
								Matcher matcher = pattern.matcher( str );
								if ( matcher.find() ) {
										String dateStr = matcher.group( 0 );
										return parse( dateStr, patternStr );
								}
						}
				}
				return null;
		}
		
		public static String getDayInMonth(int month,int day){
			String firstday, lastday;
			// 获取前月的第一天
			Calendar cale = Calendar.getInstance();
			cale.add(Calendar.MONTH, month);
			cale.set(Calendar.DAY_OF_MONTH, day);
			Date time = cale.getTime();
			String time2 = DateKit.getDay(time);
			return time2;
		}
		
		public static Long getDiffMinute(Long future){
			
			if(future == null || future == 0L) {
				throw new AppException(ExceptionType.SYS_RUNTIME);
			}
			
			long now = System.currentTimeMillis();
			Long rs = (future - now )/(60*1000);
			return rs;
		}
		
		
		
}
