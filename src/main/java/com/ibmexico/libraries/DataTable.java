package com.ibmexico.libraries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;


public class DataTable<T> {
	
	private long total = 0L;
	private List<T> rows;
	
	public DataTable() {}
	
	public DataTable(List<T> rows, long total) {
		this.total = total;
		this.rows = rows;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	public List<T> getRows() {
		return rows;
	}
	
	public static int getPage(int offset, int limit) {
		int page = 1;
		
		if(limit > 0 ) {
			page = offset/limit;
		}
		
		return page;
	}
	
	public static LocalDateTime getInitialLocalDateTime(LocalDate ldtInitialDate) {
		
		LocalDateTime objReturn = null;
		if(ldtInitialDate != null) {
			objReturn = LocalDateTime.of(ldtInitialDate.getYear(), ldtInitialDate.getMonthValue(), ldtInitialDate.getDayOfMonth(), 0, 0, 0);
		}
		
		return objReturn;
	}
	
	public static LocalDateTime getFinalLocalDateTime(LocalDate ldtFinalDate) {
		
		LocalDateTime objReturn = null;
		if(ldtFinalDate != null) {
			LocalDateTime.of(ldtFinalDate.getYear(), ldtFinalDate.getMonthValue(), ldtFinalDate.getDayOfMonth(), 23, 59, 59);
		}
		
		return objReturn;
	}
	
	public static int getPageSize(int limit) {
		int pageSize = 50; 
		if (limit > 0) {
			pageSize = limit;
		}
		return pageSize;
	}
	
	public static PageRequest getPageRequest(int offset, int limit) {
		int page = DataTable.getPage(offset, limit);
		int pageSize = DataTable.getPageSize(limit);
		
		PageRequest objPageRequest = new PageRequest(page, pageSize);
		return objPageRequest;
	}
	
	public static LocalDate getDate(String stringDate) {
		if(stringDate != null) {
			
			/*

		      // Create a Pattern object
		      Pattern r = Pattern.compile(pattern);

		      // Now create matcher object.
		      Matcher m = r.matcher(line);
		      if (m.find( )) {
		         System.out.println("Found value: " + m.group(0) );
		         System.out.println("Found value: " + m.group(1) );
		         System.out.println("Found value: " + m.group(2) );
		      }else {
		         System.out.println("NO MATCH");
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MMM-dd");
			final LocalDate dt = dtf.parseLocalDate(yourinput);
			*/
		}
		return null;
	}

	@Override
	public String toString() {
		return "DataTable [total=" + total + ", rows=" + rows.size() + "]";
	}
}
