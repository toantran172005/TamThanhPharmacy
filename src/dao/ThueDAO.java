package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.KetNoiDatabase;
import entity.KeThuoc;
import entity.Thue;

public class ThueDAO {
	public ArrayList<Thue> listThue = new ArrayList<>();

	public ArrayList<Thue> layListThue() {
		listThue.clear();

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT * FROM Thue ")) {

			while (rs.next()) {

				Thue thue = new Thue(
						rs.getString("maThue"), 
						rs.getString("loaiThue"),
						rs.getDouble("tyLeThue"),
						rs.getString("moTa")
						
						);
						
				listThue.add(thue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listThue;
	}
	
	public Thue timTheoTen(Double tyLeThue) {
	    String sql = "SELECT * FROM Thue WHERE tyLeThue = ?";
	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setDouble(1, tyLeThue);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new Thue(
	                rs.getString("maThue"),
	                rs.getString("loaiThue"),
	                rs.getDouble("tyLeThue"),
	                rs.getString("moTa")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
