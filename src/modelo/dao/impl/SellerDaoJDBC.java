package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegritExeption;
import modelo.dao.SellerDao;
import modelo.entidades.Departamento;
import modelo.entidades.Seller;

public class SellerDaoJDBC implements SellerDao {
	private Connection com;

	public SellerDaoJDBC(Connection com) {
		this.com = com;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = com.prepareStatement(
					"INSERT INTO seller (Name,Email,BirthDate,BaseSalary,DepartmentId) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());

			int linhasAdd = ps.executeUpdate();
			if (linhasAdd > 0) {
				rs = ps.getGeneratedKeys();

				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
			} else {
				throw new DbException("Error inesperado! Nenhuma linha adicionada ");
			}
		} catch (SQLException e) {
			throw new DbException("Error: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Seller seller) {
		PreparedStatement ps = null;
		try {
			com.setAutoCommit(false);
			ps = com.prepareStatement("UPDATE seller SET "
					+ "Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			ps.setInt(6, seller.getId());
			ps.executeUpdate();
			com.commit();
			System.out.println("aki foi");
		} catch (Exception e) {
			try {
				com.rollback();
				throw new DbException("Error ao executar update: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbIntegritExeption("Erro ao voltar dados do banco " + e1.getMessage());
			}

		}

	}

	@Override
	public void deleteById(int id) {
		PreparedStatement ps = null;
		
		try {
			com.setAutoCommit(false);
			ps = com.prepareStatement("DELETE FROM seller WHERE Id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
			com.commit();
			
		} catch (SQLException e) {

			try {
				com.rollback();
				throw new DbIntegritExeption("Erro: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbIntegritExeption("Error ao voltar dados do banco: " + e1.getMessage());
			}

			
		}
	}

	@Override
	public Seller findById(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = com.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = departmentId " + "WHERE seller.Id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Departamento depart = instaciacaoDepartamento(rs);
				Seller seller = instaciacaoSeller(rs, depart);
				return seller;
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}

	private Seller instaciacaoSeller(ResultSet rs, Departamento depart) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(new java.util.Date(rs.getDate("BirthDate").getTime()));
		seller.setDepartment(depart);
		return seller;
	}

	private Departamento instaciacaoDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartmentID"));
		dep.setNome(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = com.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");
			rs = ps.executeQuery();
			List<Seller> listaVendedores = new ArrayList<Seller>();
			Map<Integer, Departamento> map = new HashMap<Integer, Departamento>();
			while (rs.next()) {
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instaciacaoDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instaciacaoSeller(rs, dep);
				
				listaVendedores.add(seller);
			}
			if (listaVendedores.isEmpty()) {
				return null;
			} else {
				return listaVendedores;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}

	@Override
	public List<Seller> findAllDepartmento(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = com.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			List<Seller> listaVendedores = new ArrayList<Seller>();
			Map<Integer, Departamento> map = new HashMap<Integer, Departamento>();
			while (rs.next()) {
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instaciacaoDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instaciacaoSeller(rs, dep);
				listaVendedores.add(seller);
			}
			if (listaVendedores.isEmpty()) {
				return null;
			} else {
				return listaVendedores;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}

}
