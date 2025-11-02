package controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.record.RKRecord;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.List;

import dao.ThuocDAO;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.ThongKeThuoc_GUI;
import gui.TimKiemThuoc_GUI;

public class ThuocCtrl {
	
	private ThuocDAO thuocDAO = new ThuocDAO();
	private ToolCtrl tool = new ToolCtrl();
	private ThongKeThuoc_GUI tkThuoc;
	private TimKiemThuoc_GUI thuoc;
	private ChiTietThuoc_GUI ctThuoc;
	
	public ThuocCtrl(ThongKeThuoc_GUI tkThuoc) {
		this.tkThuoc = tkThuoc;
	}
	
	public ThuocCtrl(ChiTietThuoc_GUI ctThuoc) {
		this.ctThuoc = ctThuoc;
	}
	
	public ThuocCtrl(TimKiemThuoc_GUI thuoc) {
		this.thuoc = thuoc;
	}

	public ThuocCtrl() {
		super();
		// TODO Auto-generated constructor stub
	}


	//chọn file 
	public File chonFile() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	

	//Thống kê thuốc
	public ArrayList<Thuoc> layListThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		ArrayList<Thuoc> listTK = new  ArrayList<Thuoc>();
		try {
			listTK = thuocDAO.layListTHThongKe(ngayBD, ngayKT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listTK;
	}
	
	
	//Lấy list thuốc
	public ArrayList<Thuoc> layListThuoc() {	
		ArrayList<Thuoc> listTH = new ArrayList<Thuoc>();
		try {
			listTH = thuocDAO.layListThuocHoanChinh();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listTH;
	}
	
	
	//xoá thuốc
	public boolean xoaThuoc(String maTH) {
		return thuocDAO.xoaThuoc(maTH);
	}
	
	//đổ dữ liệu lên bảng tìm kiếm thuốc
	public void setDataChoTable() {
		ArrayList<Thuoc> listThuoc = layListThuoc();
		for(Thuoc t : listThuoc) {
			thuoc.model.addRow(new Object[] {
					t.getMaThuoc(),
					t.getTenThuoc(),
					t.getKeThuoc().getLoaiKe(),
					t.getGiaBan(),
					t.getSoLuong(),
					t.getDvt().getTenDVT(),
					t.getHanSuDung()
			});
		}
	}
	
	//xử lý thống kê
	public void onThongKe() {
		 LocalDate bd = tool.utilDateSangLocalDate(tkThuoc.dpNgayBD.getDate());
	     LocalDate kt = tool.utilDateSangLocalDate(tkThuoc.dpNgayKT.getDate());
	     ArrayList<Thuoc> listTK = layListThongKe(bd, kt);
	     int stt = 1;
	     
	     if(bd == null || kt == null) {
			tool.hienThiThongBao("Lỗi", "Vui lòng không để trống ngày bắt đầu và ngày kết thúc", false);
		} else {
			tkThuoc.dataset.clear();
			for(Thuoc t : listTK) {
				tkThuoc.dataset.addValue(t.getSoLuongBan(), "Số lượng bán", t.getTenThuoc());
			}
		}
	    
	    tkThuoc.pnlChart.repaint();
	    
	    for(Thuoc t : listTK) {
	    	tkThuoc.model.addRow(new Object[] {
	    			stt++,
	    			t.getMaThuoc(),
	    			t.getTenThuoc(),
	    			t.getSoLuongBan(),
	    			t.getDoanhThu(),
	    	});
	    }
	}
	
	//Làm mới thống kê
	public void onLamMoi() {
        tkThuoc.dpNgayBD.setDate(null);
        tkThuoc.dpNgayKT.setDate(null);
        ((DefaultTableModel) tkThuoc.tblThongKe.getModel()).setRowCount(0);
    }
	
	//Lưu thống kê
	public void luuThongKe() {
		
	}
	
	//xử lý xem chi tiết
	public void xemChiTiet() {
		int selectedRow = thuoc.tblThuoc.getSelectedRow();
		if(selectedRow == -1) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 thuốc để xem chi tiết", false);
			return;
		}
		
		int modelRow = thuoc.tblThuoc.convertRowIndexToModel(selectedRow);
		String maThuoc = thuoc.tblThuoc.getModel().getValueAt(selectedRow, 0).toString();
		
		ChiTietThuoc_GUI ctThuoc_GUI = new ChiTietThuoc_GUI(maThuoc);
		tool.doiPanel(thuoc, ctThuoc_GUI);
	}
}
