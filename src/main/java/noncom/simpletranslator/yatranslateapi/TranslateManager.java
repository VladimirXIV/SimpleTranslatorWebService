package noncom.simpletranslator.yatranslateapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;


/**
 * Класс - обёртка над вызовами методов сервиса-переводчика (Yandex Translate API)
 * 
 * @author Waldemar
 *
 */
public class TranslateManager {
	
	private final static Logger log = Logger.getLogger(TranslateManager.class);
	public final static int MAX_THREADS = 10;
	
	/**
	 * Функция сплитит строку и переводит каждое слово в отдельном потоке.
	 * Затем собирает переведенные слова в текст.
	 * @param originalText - оригинальный текст
	 * @param keyFromLang - ключ оригинальноо языка
	 * @param keyToLang   - ключ языка перевода
	 * @return переведенный по словам текст
	 */
	public static String getFullTranslate(String originalText, String keyFromLang, String keyToLang) {

		ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS); //< кол-во потоков
		List<Future<String>> futuresList = new ArrayList<>();                        //< лист futures

		// разбиваем на слова
		String[] words = originalText.split("\\s+");
		for (String word : words) {
			
			// каждое слово в поток на обработку
			TaskTranslate task = new TaskTranslate(word, keyFromLang, keyToLang); 
		    Future<String> future = executorService.submit(task);
		    
		    // добавлем в лист тасков
		    futuresList.add(future);
		}

	    // проходим по всему листу
		StringBuilder strBuilder = new StringBuilder();
		for (Future<String> future : futuresList) {
			
			String translatedWord;
		    try {
		    	
		    	// ждём(минуту) выполнения таска, вытаскиваем переведенное слово
		    	translatedWord = future.get(1, TimeUnit.MINUTES);
		    	
		    	// собираем перевод
		        strBuilder.append(translatedWord).append(" ");
		    	
		    } catch (InterruptedException | ExecutionException | TimeoutException e) {
		    	log.error("Ошибка во время перевода: ", e);
		    	future.cancel(true);
		    }
		    
		 }
		
		String translatedText = strBuilder.toString();
	    translatedText = translatedText.trim();
		
		return translatedText;
	}
	
	/**
	 * Функция возвращает все возможные значения "ключ - язык"
	 * @return 
	 */
	public static Map<String, String> getAllLanguages() {
		
		// Получим языки и их сокращения (для информации)
        Map<String, String> languages = null;
		try {
			languages = TranslateAPI.getLangs();
		} catch (IOException e) {
			log.error("Ошибка при запросе языков: ", e);
		}
		
		return languages;
	}
	
	/**
	 * Функция получает ключ по языку
	 * @param language - язык
	 * @return ключ языка
	 */
	public static String getKeyByLanguage(String language) {
		
		String key = null; //< искомый ключ
		
		Map<String, String> languages = getAllLanguages();
		
		for (String keyEntry : languages.keySet()) {
			if (languages.get(keyEntry).equalsIgnoreCase(language)) {
				key = keyEntry;
			}
		}
		
		return key;
	}

}
