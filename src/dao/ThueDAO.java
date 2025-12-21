package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.KetNoiDatabase;
import entity.Thue;

public class ThueDAO {
    
    // Lấy toàn bộ danh sách thuế
    public ArrayList<Thue> layListThue() {
        ArrayList<Thue> listThue = new ArrayList<>();
        try (Connection con = KetNoiDatabase.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Thue WHERE trangThai = 1")) {

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

    // Thêm thuế mới
    public boolean themThue(Thue t) {
        String sql = "INSERT INTO Thue (maThue, loaiThue, tyLeThue, moTa) VALUES (?, ?, ?, ?)";
        try (Connection con = KetNoiDatabase.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, t.getMaThue());
            ps.setString(2, t.getLoaiThue());
            ps.setDouble(3, t.getTiLeThue());
            ps.setString(4, t.getMoTa());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin thuế
    public boolean capNhatThue(Thue t) {
        String sql = "UPDATE Thue SET loaiThue = ?, tyLeThue = ?, moTa = ? WHERE maThue = ?";
        try (Connection con = KetNoiDatabase.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, t.getLoaiThue());
            ps.setDouble(2, t.getTiLeThue());
            ps.setString(3, t.getMoTa());
            ps.setString(4, t.getMaThue());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa thuế 
    public boolean xoaThue(String maThue) throws SQLException {
        String sql = "UPDATE Thue SET trangThai = 0 WHERE maThue = ?";
        try (Connection con = KetNoiDatabase.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maThue);
            return ps.executeUpdate() > 0;
        }
    }

    // Tìm kiếm theo tên hoặc mã
    public ArrayList<Thue> timKiem(String tuKhoa) {
        ArrayList<Thue> list = new ArrayList<>();
        String sql = "SELECT * FROM Thue WHERE (maThue LIKE ? OR loaiThue LIKE ?) AND trangThai = 1";
        
        try (Connection con = KetNoiDatabase.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String query = "%" + tuKhoa + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Thue thue = new Thue(
                    rs.getString("maThue"),
                    rs.getString("loaiThue"),
                    rs.getDouble("tyLeThue"),
                    rs.getString("moTa")
                );
                list.add(thue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}