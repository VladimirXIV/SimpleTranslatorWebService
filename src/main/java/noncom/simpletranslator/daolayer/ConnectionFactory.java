package noncom.simpletranslator.daolayer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionFactory {
	
	private final static Logger log = Logger.getLogger(ConnectionFactory.class);
	private static ConnectionFactory connectionFactory = null;
	
	private Properties properties;

	private ConnectionFactory() {
		
		// Подгрузим файл database.properties
		properties = new Properties();		 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			log.error("Ошибка загрузки параметров");
		}
		
		// Драйвер БД
		String driverClassName = properties.getProperty("jdbc.driver");		
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			log.error("Не найден класс драйвера базы данных");
		}
	}

	public Connection getConnection() throws SQLException {
				
		Connection connection = null;
		
		String connectionUrl = properties.getProperty("jdbc.url");
		connection = DriverManager.getConnection(connectionUrl);
		
		return connection;
	}

	public static ConnectionFactory getInstance() {
		
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory();
		}
		return connectionFactory;
	}

}
