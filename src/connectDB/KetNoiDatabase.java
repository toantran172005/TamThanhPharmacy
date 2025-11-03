package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoiDatabase {

	private static String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=QL_HieuThuoc_TamThanh;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";     
    private static final String PASSWORD = "md140705";

    private static Connection connection = null;

    public static Connection getConnection(){
    	connection = null;
        try {
        	 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        	 System.out.println("✅ Driver đã được nạp!");
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                System.out.println(" Kết nối SQL Server thành công!");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối CSDL: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Đã đóng kết nối CSDL.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
	
}
