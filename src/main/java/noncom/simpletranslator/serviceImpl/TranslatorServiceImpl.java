package noncom.simpletranslator.serviceImpl;

import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

import noncom.simpletranslator.service.TranslatorService;
import noncom.simpletranslator.utils.AppHelper;
import noncom.simpletranslator.yatranslateapi.TranslateManager;
import noncom.simpletranslator.businesslayer.HistoryManager;


@WebService(endpointInterface = "noncom.simpletranslator.serviceImpl.TranslatorServiceImpl")
public class TranslatorServiceImpl implements TranslatorService {
	
	public final static Logger log = Logger.getLogger(TranslatorServiceImpl.class);
	
	private HistoryManager historyManager;
	
	@Resource
	WebServiceContext wsContext;
	
	
	public TranslatorServiceImpl() {
		historyManager = new HistoryManager();
	}
	
	
	@Override
	public String translate(String originalText, String fromLang, String toLang) {
		
		// Ответ сервиса
		String translatedText = null;
		
		// Адрес запроса и дата/время
		String ipAddress = AppHelper.getIpAddress(wsContext);
		String dateTime = AppHelper.getCurrentDateTime();
		
		// Если прилетели null, то сделаем запись в БД и вернём null-ответ
		if ((fromLang == null)  || (toLang == null) || (fromLang.isEmpty()) || (toLang.isEmpty())) {		
			historyManager.addRecord(dateTime, ipAddress, fromLang, originalText, toLang, translatedText);			
			return null;
		}
		
		// Залогируем всевозможные языки и их сокращения (для информации, чтобы удобнее было выбирать и тестировать)
		Map<String, String> languages = TranslateManager.getAllLanguages();
		StringBuilder strBuilder = new StringBuilder();
		for (String key : languages.keySet()) {
			String lang = languages.get(key);	
			strBuilder.append(key).append(" = ").append(lang).append("\n");
		}
		log.info(strBuilder.toString());
		
		/* Определим ключи полученых языков (если ключ не определился, то добавляем запись в БД и возвращаем null-ответ) */		
		String keyFromLang = TranslateManager.getKeyByLanguage(fromLang); 
		if ((keyFromLang == null) || (keyFromLang.isEmpty())) {
			log.error("Ошибка. Не определился ключ языка(исходный язык). keyFromLang is null");
			historyManager.addRecord(dateTime, ipAddress, fromLang, originalText, toLang, translatedText);
			return null;
		} else {
			log.info("Ключ языка(исходный язык): " + keyFromLang);
		}
		
		String keyToLang = TranslateManager.getKeyByLanguage(toLang);
		if ((keyToLang == null) || (keyToLang.isEmpty())) {
			log.error("Ошибка. Не определился ключ языка(язык перевода). keyFromLang is null");
			historyManager.addRecord(dateTime, ipAddress, fromLang, originalText, toLang, translatedText);
			return null;
		} else {
			log.info("Ключ языка(язык перевода): " + keyToLang);
		}
		
		// Функция перевода		
		translatedText  = TranslateManager.getFullTranslate(originalText, keyFromLang, keyToLang);
		historyManager.addRecord(dateTime, ipAddress, fromLang, originalText, toLang, translatedText);
		
		return translatedText;
	}
	
}
