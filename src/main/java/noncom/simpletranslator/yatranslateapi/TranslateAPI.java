package noncom.simpletranslator.yatranslateapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class TranslateAPI {
	
	// logger
	private final static Logger log = Logger.getLogger(TranslateAPI.class);
	
	// developers key
	public static final String API_KEY = "trnsl.1.1.20170820T205801Z.3b8232fa62c70cd8.b8a5f56711b898a3653319522f00ef12d4147789";
	
	// константы, которые используются в формировании URLа для запроса
	public static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
	public static final String PARAM_API_KEY = "key=",
		                       PARAM_LANG_PAIR = "&lang=",
		                       PARAM_TEXT = "&text=",
		                       PARAM_UI_LANG = "&ui=en";
	
    public static final String ENCODING = "UTF-8";
	
	public static final int CONNECTION_SOCKET_TIMEOUT = 10000;
	public static final int CONNECTION_READ_TIMEOUT = 10000;
	
	
	/**
	 * Отправка запросов к сервису-переводчику
	 * @param URL
	 * @return
	 * @throws IOException
	 */
	private static String request(String URL) throws IOException {
		
		log.info("Сформированный URL запроса:   " + URL);
		
		URL url = new URL(URL);
		URLConnection urlConn = url.openConnection();
		urlConn.setConnectTimeout(CONNECTION_SOCKET_TIMEOUT);
		urlConn.setReadTimeout(CONNECTION_READ_TIMEOUT);
		urlConn.addRequestProperty("User-Agent", "Mozilla"); //< requests headers
		
		InputStream inStream = urlConn.getInputStream();		
		String recieved = new BufferedReader(new InputStreamReader(inStream)).readLine();
		log.info("Получен ответ:   " + recieved);
		
		inStream.close();
		
		return recieved;
	}
	
	/**
	 * Функция получения всех возможных сочетаний "ключ - язык"
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getLangs() throws IOException {

		String requestUrl = SERVICE_URL + "getLangs?" + PARAM_API_KEY + API_KEY + PARAM_UI_LANG;
		
		String langs = request(requestUrl);
		
		langs = langs.substring(langs.indexOf("langs")+7);
		langs = langs.substring(0, langs.length()-1);
		
		String[] splitLangs = langs.split(",");
		
		Map<String, String> languages = new HashMap<String, String>();
		for (String s : splitLangs) {
			String[] s2 = s.split(":");
			
			String key = s2[0].substring(1, s2[0].length()-1);
			String value = s2[1].substring(1, s2[1].length()-1);
			
			languages.put(key, value);
		}
		return languages;
	}
	
	/**
	 * Перевод текста
	 * @param text 
	 * @param sourceLang
	 * @param targetLang
	 * @return
	 * @throws IOException
	 */
	public static String translate(String text, String sourceLang, String targetLang) throws IOException {

		String requestUrl = SERVICE_URL + "translate?" + PARAM_API_KEY + API_KEY + PARAM_TEXT 
				                        + URLEncoder.encode(text, ENCODING) + PARAM_LANG_PAIR + sourceLang + "-" + targetLang;
		
		String response = request(requestUrl);
		
		/*
		 * Ответ от сервиса приходит в формате {"code":some_code"lang":"some_langs","text":["some_text"]}
		 * Для получения текста some_text как подстроки ответа:
		 *     находим позицию тэга "text" и отсчитываем ВОСЕМЬ символов, получаем начало подстроки
		 *     отсчитываемь ТРИ символа "]} с конца ответа, получаем позицию конца подстроки
		 */
		String translatedText = response.substring(response.indexOf("text") + 8, response.length() - 3);
		
		// результат необходимо перекодировать в UTF-8
		byte[] sourceBytes = translatedText.getBytes();
		translatedText = new String(sourceBytes , ENCODING);
		
		return translatedText;
	}
	
	/**
	 * Функция определения языка
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static String detectLanguage(String text) throws IOException {

		String requestUrl = SERVICE_URL + "detect?" + PARAM_API_KEY + API_KEY + PARAM_TEXT + URLEncoder.encode(text, ENCODING);
		
		String response = request(requestUrl);
		
		/*
		 * Ответ от сервиса приходит в формате {"code":some_code,"lang":"some_lang"}
		 * Для получения языка some_lang как подстроки ответа:
		 *     находим позицию тэга "lang" и отсчитываем СЕМЬ символов, получаем позицию начала подстроки
		 *     отсчитываем ДВА символа "} с конца ответа, получаем позицию конца подстроки
		 */
		return response.substring(response.indexOf("lang") + 7, response.length() - 2);
	}
	
}