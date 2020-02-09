package db;

public class DbIntegritExeption extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DbIntegritExeption(String msg) {
		super(msg);
	}
}
