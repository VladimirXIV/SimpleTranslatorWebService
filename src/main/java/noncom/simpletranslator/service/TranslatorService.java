package noncom.simpletranslator.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface TranslatorService {

	/**
	 * Функция перевода
	 * @param textToTranslate - оригинал текста
	 * @param fromLang - язык оригинального текста
	 * @param toLang - язык перевода
	 * @return - перевод текста
	 */
	@WebMethod
	public String translate(String originalText, String fromLang, String toLang);
}
