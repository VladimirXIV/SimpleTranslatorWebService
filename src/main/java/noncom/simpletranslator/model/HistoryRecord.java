package noncom.simpletranslator.model;

public class HistoryRecord {
	
	private String originalText;
	private String fromLang;
	private String toLang;
	private String translatedText;
	private String dateTime;
	private String ipAddress;
	
	
	public HistoryRecord() {
		
	}

	public HistoryRecord(String originalText,
                         String fromLang, 
                         String toLang, 
                         String translatedText, 
                         String dateTime,
                         String ipAddress) {
		super();
		this.originalText = originalText;
		this.fromLang = fromLang;
		this.toLang = toLang;
		this.translatedText = translatedText;
		this.dateTime = dateTime;
		this.ipAddress = ipAddress;
	}
	

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getFromLang() {
		return fromLang;
	}

	public void setFromLang(String fromLang) {
		this.fromLang = fromLang;
	}

	public String getToLang() {
		return toLang;
	}

	public void setToLang(String toLang) {
		this.toLang = toLang;
	}

	public String getTranslatedText() {
		return translatedText;
	}

	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	

	@Override
	public String toString() {
		return "HistoryRecord [originalText=" + originalText + ", fromLang=" + fromLang + ", toLang=" + toLang
				+ ", translatedText=" + translatedText + ", dateTime=" + dateTime + ", ipAddress=" + ipAddress + "]";
	}
}
