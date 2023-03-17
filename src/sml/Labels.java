package sml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO: write a JavaDoc for the class

/**
 *
 * @author ...
 */
public final class Labels {
	private final Map<String, Integer> labels = new HashMap<>();

	/**
	 * Adds a label with the associated address to the map.
	 *
	 * @param label the label
	 * @param address the address the label refers to
	 */
	public void addLabel(String label, int address) {
		Objects.requireNonNull(label);
		// TODO: Add a check that there are no label duplicates.
		if (labels.containsKey(label)) {
			throw new IllegalArgumentException("Duplicate label: " + label);
		labels.put(label, address);
	}

	/**
	 * Returns the address associated with the label.
	 *
	 * @param label the label
	 * @return the address the label refers to
	 */
	public int getAddress(String label) {
		// TODO: Where can NullPointerException be thrown here?
		//       (Write an explanation.)
		//       Add code to deal with non-existent labels.
		/*
		 A NullPointerException could be thrown by the get() method of the labels map if the
		 label parameter in null or if there is no mapping given 'label' in the map.
	 	*/
		if (label == null || !labels.containsKey(label)) {
			throw new IllegalArgumentException("Label does not exist: " + label);
		}
		return labels.get(label);
	}

	/**
	 * representation of this instance,
	 * in the form "[label -> address, label -> address, ..., label -> address]"
	 *
	 * @return the string representation of the labels map
	 */
	@Override
	public String toString() {
		// TODO: Implement the method using the Stream API (see also class Registers).
		return Registers.entrySet().stream()
				.map(entry -> entry.getKey() + " = " + entry.getValue())
				.collect(Collectors.joining("\n"));;
	}

	// TODO: Implement equals and hashCode (needed in class Machine).
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Machine machine = (Machine) o;
			return id == machine.id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}
	/**
	 * Removes the labels
	 */
	public void reset() {
		labels.clear();
	}
}
