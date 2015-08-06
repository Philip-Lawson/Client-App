/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import java.util.List;

/**
 * @author Phil
 *
 */
public class Database {

	private static volatile Database uniqueInstance;

	private Database() {

	}

	public static Database getInstance() {

		if (uniqueInstance == null) {
			synchronized (Database.class) {
				if (uniqueInstance == null)
					uniqueInstance = new Database();
			}
		}

		return uniqueInstance;
	}
	
	public List<Integer> getIncompleteNetworkTasks(){
		return null;
	}
	
	public int getNumberOfCalculations(){
		return 0;
	}

}
