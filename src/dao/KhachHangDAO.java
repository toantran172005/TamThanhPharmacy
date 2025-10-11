package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import database.KetNoiDatabase;
import entity.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KhachHangDAO {
	
	public ObservableList<KhachHang> layListKhachHang() {
		
		ObservableList<KhachHang> list = FXCollections.observableArrayList();
		
		try (Connection con = KetNoiDatabase.getConnection();
	             Statement stmt = con.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT maKH, tenKH, sdt, tuoi, trangThai FROM KhachHang")) {

	            while (rs.next()) {
	            	list.add(new KhachHang(
	            		    rs.getString("maKH"),
	            		    rs.getString("tenKH"),
	            		    rs.getString("sdt"),
	            		    rs.getInt("tuoi"),
	            		    rs.getBoolean("trangThai")
	            		));
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
		return list;
		
		
	}
	
}
