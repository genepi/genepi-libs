package genepi.db;

public interface IUpdateListener {

	public void beforeUpdate(Database database);
	
	public void afterUpdate(Database database);
	
}
