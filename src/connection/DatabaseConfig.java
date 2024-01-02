package connection;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseConfig {
	public static DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/pbl4.distantcontrolhub?useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("postgres");
        dataSource.setPassword("23022003");
        return dataSource;
    }
}
