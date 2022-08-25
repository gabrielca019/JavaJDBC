package database;

public class DatabaseIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DatabaseIntegrityException(String msg) {
		super(msg);
	}

}