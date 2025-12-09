package controller;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.DonViTinhDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.Thue;
import entity.Thuoc;
import gui.NhapThuoc_GUI;

public class NhapThuocCtrl {
	
	public NhapThuoc_GUI nhapThuoc_gui;
	public ToolCtrl tool = new ToolCtrl();
	public ThuocDAO thuocDAO = new ThuocDAO();
	public DonViTinhDAO dvtDAO = new DonViTinhDAO();
	public ThueDAO thueDAO = new ThueDAO();

	public NhapThuocCtrl(NhapThuoc_GUI ntGUI) {
		this.nhapThuoc_gui = ntGUI;
	}


	//mở hộp thoại chọn file
	public void chonFileExcel() {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn tệp Excel để nhập thuốc");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "File Excel (.xlsx, .xls)", "xlsx", "xls"));

        if (fileChooser.showOpenDialog(nhapThuoc_gui) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            docFileExcel(file);
        }
    }
	
	//Đọc file excel
	public void docFileExcel(File file) {
		DefaultTableModel model = (DefaultTableModel) nhapThuoc_gui.tblNhapThuoc.getModel();
		model.setRowCount(0);
		
		try (FileInputStream fis = new FileInputStream(file);
			 Workbook workbook = new XSSFWorkbook(fis);)
		{
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			boolean isFirstRow = true;
			int stt = 1;
			
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				if(isFirstRow) {
					isFirstRow = false;
					continue;
				}
				
				Object[] rowData = new Object[12];
				rowData[0] = stt++;
				
				for(int i = 1; i < 12; i++) {
					Cell cell = row.getCell(i);
					if(cell == null) {
						rowData[i] = "";
					} else {
						switch (cell.getCellType()) {
						case STRING: 
							rowData[i] = cell.getStringCellValue();
							break;
						case NUMERIC:
							if(DateUtil.isCellDateFormatted(cell)) {
								rowData[i] = cell.getDateCellValue();
							} else {
								rowData[i] = cell.getNumericCellValue();
							}
							break;
						case BOOLEAN:
							rowData[i] = cell.getBooleanCellValue();
							break;
						
						default:
							rowData[i] = "";
						}
					}
				}
				model.addRow(rowData);
			}
			
			tool.hienThiThongBao("Thông báo", "Nhập dữ liệu thành công", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			tool.hienThiThongBao("Lỗi", "Nhập dữ liệu không thành công", false);
		}
	}
	
	//Làm mới bảng
	public void lamMoiBang() {
		nhapThuoc_gui.model.setRowCount(0);
	}
	
	
	//Lưu dữ liệu
	public void luuDataTuTable() {
		DefaultTableModel model = (DefaultTableModel) nhapThuoc_gui.tblNhapThuoc.getModel();
		int rowCount = model.getRowCount();
		
		if(rowCount == 0) {
			tool.hienThiThongBao("Lỗi", "Không có dữ liệu trên bảng", false);
			return;
		}
		
		ArrayList<Thuoc> listThuoc = new ArrayList<Thuoc>();
		for(int i = 0; i< rowCount; i++) {
			String maThuoc = model.getValueAt(i, 1).toString();
	        String tenThuoc = model.getValueAt(i, 2).toString();
	        int soLo = (int) Double.parseDouble(model.getValueAt(i, 3).toString());
	        String dangThuoc = model.getValueAt(i, 4).toString();
	        String dvt = model.getValueAt(i, 5).toString();
	        LocalDate hanDung = tool.convertExcelDate(model.getValueAt(i, 6));
	        int soLuong = (int) Double.parseDouble(model.getValueAt(i, 7).toString());
	        double donGia = Double.parseDouble(model.getValueAt(i, 8).toString());
	        double tyLeThue = Double.parseDouble(model.getValueAt(i, 9).toString());
	        String loaiThue = model.getValueAt(i, 10).toString();
	        double thanhTien = Double.parseDouble(model.getValueAt(i, 11).toString());
	        
	        
	        Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, soLo, dangThuoc, new DonViTinh(null, dvt), hanDung,
					soLuong, donGia, new Thue(null, tyLeThue), loaiThue);
	        
	        listThuoc.add(thuoc);
		}
		
		String maPNT = tool.taoKhoaChinh("PNT");
		String maNCC = "TTNCC1";
		String maNV = "TTNV1";
		LocalDate ngayNhap = LocalDate.now();
		
		boolean res = thuocDAO.luuData(maPNT, maNCC, maNV, ngayNhap, listThuoc);
		
		if(res) {
			tool.hienThiThongBao("Thông báo", "Lưu thành công", true);
			lamMoiBang();
		} else {
			tool.hienThiThongBao("Lỗi", "Lưu thất bại", false);
		}
	}
	
	
}
