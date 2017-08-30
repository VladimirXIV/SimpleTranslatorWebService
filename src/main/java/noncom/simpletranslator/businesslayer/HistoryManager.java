package noncom.simpletranslator.businesslayer;

import noncom.simpletranslator.daolayer.HistoryJDBCDAO;
import noncom.simpletranslator.model.HistoryRecord;

public class HistoryManager {
	
	private HistoryJDBCDAO historyJDBCDAO;
	
	
	public HistoryManager() {
		historyJDBCDAO = new HistoryJDBCDAO();
	}
	
	
	/**
	 * Функция делает запись в БД о посещении.
	 * @param ipAddress - адрес
	 * @param fromLang - язык оригинала
	 * @param originalText - оригинальный текст
	 * @param toLang - язык перевода
	 * @param translatedText - переведенный текст
	 */
	public void addRecord(String dateTime,
			              String ipAddress, 
			              String fromLang, 
			              String originalText,
			              String toLang,
			              String translatedText) {
		
		/* собираем bean */
		HistoryRecord historyRecord = new HistoryRecord();
					
		historyRecord.setDateTime(dateTime);		
		historyRecord.setIpAddress(ipAddress);
		historyRecord.setFromLang(fromLang);
		historyRecord.setOriginalText(originalText);
		historyRecord.setToLang(toLang);
		historyRecord.setTranslatedText(translatedText);
		
		// передаём bean на уровень DAO
		historyJDBCDAO.update(historyRecord);		
	}
}
