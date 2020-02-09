package modelo.dao;

import db.DB;
import modelo.dao.impl.DepartmentJDBC;
import modelo.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConection());
	}
	
	public static DepartamentoDao createDepartDao() {
		return new DepartmentJDBC(DB.getConection());
	}
}
