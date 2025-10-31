package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.KhachHang;
import entity.NhanVien;
import entity.Thue;
import entity.Thuoc;


public class HoaDonDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();
	NhanVienDAO nvDAO = new NhanVienDAO();
	KhachHangDAO khDAO = new KhachHangDAO();
	ThuocDAO thuocDAO = new ThuocDAO();

    // ================= LẤY DANH SÁCH HÓA ĐƠN CÒN HIỆU LỰC =================
    public ArrayList<Object[]> layListHoaDon() {
    	ArrayList<Object[]> listHD = new ArrayList<>();

        String sql = """
                SELECT hd.maHD, hd.maKH, kh.tenKH, hd.maNV, nv.tenNV, hd.loaiTT,
                       hd.ngayLap, hd.diaChiHT, hd.tenHT, hd.ghiChu, hd.hotline, hd.tienNhan, hd.trangThai
                FROM HoaDon hd
                JOIN NhanVien nv ON hd.maNV = nv.maNV
                JOIN KhachHang kh ON hd.maKH = kh.maKH
                WHERE hd.trangThai = 1
                ORDER BY TRY_CAST(SUBSTRING(hd.maHD, 5, LEN(hd.maHD)) AS INT)
                """;

        try (Connection con = KetNoiDatabase.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] hd = {
                    rs.getString("maHD"),
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("loaiTT"),
                    rs.getDate("ngayLap").toLocalDate(),
                    rs.getString("diaChiHT"),
                    rs.getString("tenHT"),
                    rs.getString("ghiChu"),
                    rs.getString("hotline"),
                    rs.getDouble("tienNhan"),
                    rs.getBoolean("trangThai")
                };
                listHD.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listHD;
    }

	 // ================= LẤY DANH SÁCH HÓA ĐƠN ĐÃ XÓA =================
    public ArrayList<Object[]> layListHDDaXoa() {
    	ArrayList<Object[]> listHDDaXoa = new ArrayList<>();

        String sql = """
                SELECT hd.maHD, hd.maKH, kh.tenKH, hd.maNV, nv.tenNV, hd.loaiTT,
                       hd.ngayLap, hd.diaChiHT, hd.tenHT, hd.ghiChu, hd.hotline, hd.tienNhan, hd.trangThai
                FROM HoaDon hd
                JOIN NhanVien nv ON hd.maNV = nv.maNV
                JOIN KhachHang kh ON hd.maKH = kh.maKH
                WHERE hd.trangThai = 0
                ORDER BY TRY_CAST(SUBSTRING(hd.maHD, 5, LEN(hd.maHD)) AS INT)
                """;

        try (Connection con = KetNoiDatabase.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] hd = {
                    rs.getString("maHD"),
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("loaiTT"),
                    rs.getDate("ngayLap").toLocalDate(),
                    rs.getString("diaChiHT"),
                    rs.getString("tenHT"),
                    rs.getString("ghiChu"),
                    rs.getString("hotline"),
                    rs.getDouble("tienNhan"),
                    rs.getBoolean("trangThai")
                };
                listHDDaXoa.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listHDDaXoa;
    }
	
	  public boolean themHoaDonBangObject(Object[] hd, String maNV) {
	        String sql = """
	                INSERT INTO HoaDon (maHD, maKH, maNV, loaiTT, ngayLap, diaChiHT, tenHT, ghiChu, hotline, tienNhan, trangThai)
	                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	                """;
	        try (Connection con = KetNoiDatabase.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, hd[0].toString());
	            ps.setString(2, hd[1].toString());
	            ps.setString(3, hd[2].toString());
	            ps.setString(4, hd[3].toString());
	            ps.setDate(5, java.sql.Date.valueOf(hd[4].toString()));
	            ps.setString(6, hd[5].toString());
	            ps.setString(7, hd[6].toString());
	            ps.setString(8, hd[7] != null ? hd[7].toString() : "");
	            ps.setString(9, hd[8].toString());
	            ps.setDouble(10, Double.parseDouble(hd[9].toString()));
	            ps.setString(11, hd[10].toString());

	            return ps.executeUpdate() > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

    public String layMaHoaDonMoiNhat() {
        String sql = "SELECT TOP 1 hd.maHD\r\n"
        		+ "FROM HoaDon hd\r\n"
        		+ "ORDER BY TRY_CAST(SUBSTRING(hd.maHD, 5, LEN(hd.maHD)) AS INT) DESC;";
        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maHD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themChiTietHoaDonBangObject(Object[] ct) {
        String sql = "INSERT INTO CT_HoaDon (maHD, maThuoc, soLuong, donGia, maDVT) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct[0].toString());
            ps.setString(2, ct[1].toString());
            ps.setInt(3, Integer.parseInt(ct[2].toString()));
            ps.setDouble(4, Double.parseDouble(ct[3].toString()));
            ps.setString(5, ct[4].toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



	public int layTongDonHang(String maKH) {
		String sql = "SELECT COUNT(DISTINCT maHD) FROM HoaDon WHERE maKH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKH);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double layTongTien(String maKH) {
		String sql = """
				SELECT SUM(
					CASE 
						WHEN km.loaiKM = N'Giảm giá' THEN cthd.soLuong * cthd.donGia * (1 - km.mucKM / 100.0)
						WHEN km.loaiKM = N'Mua tặng' THEN 
                		(cthd.soLuong - FLOOR(cthd.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * cthd.donGia
					ELSE 
                		cthd.soLuong * cthd.donGia
					END
					)
				FROM HoaDon HD
				JOIN CT_HoaDon cthd ON HD.maHD = cthd.maHD
				JOIN Thuoc t ON cthd.maThuoc = t.maThuoc
				LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				WHERE HD.maKH = ?
				""";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKH);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double layTongTienTheoSanPham(String maHD, String maSP) {
		String sql = """
				SELECT SUM(
						CASE 
				         WHEN km.loaiKM = N'Giảm giá' THEN cthd.soLuong * cthd.donGia * (1 - km.mucKM / 100.0)
				         WHEN km.loaiKM = N'Mua tặng' THEN 
				             (cthd.soLuong - FLOOR(cthd.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * cthd.donGia
				         ELSE 
				             cthd.soLuong * cthd.donGia
						END) 
				FROM CT_HoaDon cthd
				 JOIN Thuoc t ON cthd.maThuoc = t.maThuoc
				 LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				WHERE cthd.maHD = ? AND cthd.maThuoc = ?
							""";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			ps.setString(2, maSP);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double tinhTongTienTheoHoaDon(String maHD) {
		String sql = """
				SELECT SUM(
					CASE 
						WHEN km.loaiKM = N'Giảm giá' THEN cthd.soLuong * cthd.donGia * (1 - km.mucKM / 100.0)
						WHEN km.loaiKM = N'Mua tặng' THEN 
                		(cthd.soLuong - FLOOR(cthd.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * cthd.donGia
					ELSE 
                		cthd.soLuong * cthd.donGia
					END
					) AS tongTien
				FROM CT_HoaDon cthd
				JOIN Thuoc t ON cthd.maThuoc = t.maThuoc
				LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				WHERE maHD = ?
					""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("tongTien");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Map<LocalDate, Double> layDoanhThuTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
		Map<LocalDate, Double> map = new LinkedHashMap<>();
		String sql = """
				SELECT hd.ngayLap, SUM(
					CASE 
						WHEN km.loaiKM = N'Giảm giá' THEN cthd.soLuong * cthd.donGia * (1 - km.mucKM / 100.0)
						WHEN km.loaiKM = N'Mua tặng' THEN 
                		(cthd.soLuong - FLOOR(cthd.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * cthd.donGia
					ELSE 
                		cthd.soLuong * cthd.donGia
					END
					) AS tongTien
				FROM HoaDon hd
				JOIN CT_HoaDon cthd ON hd.maHD = cthd.maHD
				JOIN Thuoc t ON cthd.maThuoc = t.maThuoc
				LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				WHERE hd.ngayLap BETWEEN ? AND ? AND hd.trangThai = 1
				GROUP BY hd.ngayLap
				ORDER BY hd.ngayLap
				    """;
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setDate(1, java.sql.Date.valueOf(ngayBD));
			pstmt.setDate(2, java.sql.Date.valueOf(ngayKT));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getDate("ngayLap").toLocalDate(), rs.getDouble("tongTien"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public ArrayList<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		ArrayList<KhachHang> list = new ArrayList<>();

		String query = """
				SELECT maKH, tenKH, tuoi, sdt
				FROM (
				    SELECT DISTINCT KH.maKH, KH.tenKH, KH.tuoi, KH.sdt,
				           TRY_CAST(REPLACE(KH.maKH, 'TTKH', '') AS INT) AS maSo
				    FROM KhachHang KH
				    JOIN HoaDon HD ON KH.maKH = HD.maKH
				    WHERE HD.ngayLap BETWEEN ? AND ? AND hd.trangThai = 1
				) AS t
				ORDER BY t.maSo;
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setDate(1, java.sql.Date.valueOf(ngayBD));
			pstmt.setDate(2, java.sql.Date.valueOf(ngayKT));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getInt("tuoi"),
						rs.getString("sdt")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Object[]> layDanhSachThuocTheoHoaDon(String maHD) {
		ArrayList<Object[]> list = new ArrayList<>();
		String sql = """
				    SELECT ct.maHD, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT, ct.donGia, SUM(
							CASE 
								WHEN km.loaiKM = N'Giảm giá' THEN ct.soLuong * ct.donGia * (1 - km.mucKM / 100.0)
								WHEN km.loaiKM = N'Mua tặng' THEN 
				            		(ct.soLuong - FLOOR(ct.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * ct.donGia
							ELSE 
				            		ct.soLuong * ct.donGia
							END
							) AS thanhTien
					FROM CT_HoaDon ct
					JOIN Thuoc t ON ct.maThuoc = t.maThuoc
					JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
					LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
					WHERE maHD = ?
					GROUP BY ct.maHD, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT, ct.donGia
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maHD);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maHD"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT"), rs.getDouble("donGia"), rs.getDouble("thanhTien") };
				list.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// ========== TÌM KHÁCH HÀNG THEO MÃ ==========
	public Object[] timHoaDonTheoMa(String maHD) {
		for (Object[] row : layListHoaDon()) {
			if (row[0].toString().equalsIgnoreCase(maHD))
				return row;
		}
		return null;
	}

	public boolean xoaHD(String maHD) {
		String sql = "UPDATE HoaDon SET trangThai = 0 WHERE maHD = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean khoiPhucHD(String maHD) {
		String sql = "UPDATE HoaDon SET trangThai = 1 WHERE maHD = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String layMaThuocTheoTenVaHoaDon(String maHD, String tenThuoc) {
	    String sql = """
	        SELECT t.maThuoc
	        FROM CT_HoaDon ct
	        JOIN Thuoc t ON ct.maThuoc = t.maThuoc
	        WHERE ct.maHD = ? AND t.tenThuoc = ?
	    """;
	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maHD);
	        ps.setString(2, tenThuoc);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getString("maThuoc");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public String layMaDVTTheoThuoc(String maThuoc) {
	    String sql = "SELECT maDVT FROM Thuoc WHERE maThuoc = ?";
	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maThuoc);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getString("maDVT");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}


}
