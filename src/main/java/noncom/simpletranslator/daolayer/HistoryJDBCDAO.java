package noncom.simpletranslator.daolayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import noncom.simpletranslator.model.HistoryRecord;


/**
 * DAO-класс для работы с базой данных
 * @author Waldemar
 *
 */
public class HistoryJDBCDAO {
	
	public final static Logger log = Logger.getLogger(HistoryJDBCDAO.class);
	public final static String sqlQuery = "INSERT INTO visithistory VALUES(?, ?, ?, ?, ?, ?);";
	
	public HistoryJDBCDAO() {
		//
	}
	
	
	private Connection getConnection() throws SQLException {
		Connection connection = ConnectionFactory.getInstance().getConnection();
		return connection;
	}
	
	public void update(HistoryRecord historyRecord) {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		try {
			
			connection = this.getConnection();
			
			log.info("Соединение с базой данных создано успешно!");
			
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, historyRecord.getDateTime());
			preparedStatement.setString(2, historyRecord.getIpAddress());
			preparedStatement.setString(4, historyRecord.getFromLang());
			preparedStatement.setString(3, historyRecord.getOriginalText());			
			preparedStatement.setString(5, historyRecord.getToLang());
			preparedStatement.setString(6, historyRecord.getTranslatedText());	
		    
		    log.info("preparedStatement создан успешно!");
		    
            result = preparedStatement.executeUpdate();
		    
		    log.info("Обнвлено записей в базе данных: " + result);
			
		} catch (SQLException e) {

			log.error("Ошибка! ", e);
			
		} finally {
			
			try {
				
				if (preparedStatement != null) {
					preparedStatement.close();
			    }
				
			} catch (SQLException ex) {
				log.error("Ошибка при поптыке закрыть preparedStatement");
			}
			
			try {
				
                if (connection != null) {
                	connection.close();
                }
                
            } catch (SQLException ex) {
				log.error("Ошибка при поптыке закрыть соединение");
            }
			
		}
	
	}

}
