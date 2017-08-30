package noncom.simpletranslator.yatranslateapi;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

/**
 * 
 * @author Waldemar
 *
 */
public class TaskTranslate implements Callable<String> {
	
	public final static Logger log = Logger.getLogger(TaskTranslate.class);
	
	private String text;
	private String fromLang;
	private String toLang;
	
	public TaskTranslate(String text, String fromLang, String toLang) {
        this.text = text;
        this.fromLang = fromLang;
        this.toLang   = toLang;
    }

    @Override
    public String call() {

        String translatedText = null;
        try {
        	
            translatedText = TranslateAPI.translate(text, fromLang, toLang);           
            
            long threadId = Thread.currentThread().getId();
            log.info("Thread # " + threadId + " has translated the word: " + text + " -----> " + translatedText);
            
        } catch (IOException e) {
        	log.error("Ошибка во время перевода: ", e);
        }
        
        return translatedText;
    }

}
