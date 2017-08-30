package noncom.simpletranslator.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * Утилитный класс
 * 
 * @author Waldemar
 *
 */
public class AppHelper {
	
	public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * Функция возвращает IP-адресс, с которого было обращение
	 * @param wsContext
	 * @return String ipAddress
	 */
	public static String getIpAddress(WebServiceContext wsContext) {
		
		MessageContext msgx = wsContext.getMessageContext();
		HttpServletRequest req = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
		String ipAddress = req.getRemoteAddr();		
		
		return ipAddress;		
	}
	
	/**
	 * Функция возвращает текущую дату и время в формате String
	 * @return String дата-время
	 */
	public static String getCurrentDateTime() {
		
		Date date = new Date();		
		String dateTime = dateFormat.format(date);
		
		return dateTime;
	}

}
