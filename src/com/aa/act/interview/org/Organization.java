package com.aa.act.interview.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public abstract class Organization {

	private Position root;
	
	public Organization() {
		root = createOrganization();
	}
	
	protected abstract Position createOrganization();
	
	//For storing the identifiers and their associated employees.
	private Map<Integer, String> hashedNames = new HashMap<Integer, String>();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {
		//your code here
		
		//Convert Name object into a name string.
		//Then generate a positive identifier from the name.
		//Finally, add both into a map for optional future retrieval.
		//Note: the identifiers may not be unique, but this possibility is extremely unlikely. :)
		String personName = person.toString();
		int identifier = Math.abs(personName.hashCode());
		hashedNames.put(identifier, person.toString());
		
		//Create a new employee.
		Employee emp = new Employee(identifier, person);
		
		//Use a breadth-first search to check whether a position with the given title exists in the organization hierarchy.
		//If the position exists, assign the employee to the position and then return the position option.
		//Otherwise, return an empty option.
		Queue<Position> queue = new LinkedList<Position>();
		ArrayList<Position> visited = new ArrayList<Position>();
		queue.add(root);
		while(!queue.isEmpty()){
			if (queue.peek().getTitle().equals(title)){
				//found
				queue.peek().setEmployee(Optional.of(emp));
				return Optional.of(queue.peek());
			}
			for (Position x : queue.remove().getDirectReports()){
				if (!visited.contains(x)){
					queue.add(x);
				}
			}
		}
		return Optional.empty();
	}
	
	//Given an identifier, return the associated name.
	public String getNameFromIdentifier(int identifier){
		return hashedNames.get(identifier);
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}
	
	
	
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
	
}
