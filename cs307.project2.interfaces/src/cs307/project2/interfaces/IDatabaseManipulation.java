package cs307.project2.interfaces;

public interface IDatabaseManipulation {
	// Your implemented class shall have constructor like:
	//		Constructor(String database, String root, String pass)
	// See project description for more details.

	void $import(String recordsCSV, String staffsCSV);
}
