/**
 * Spinner model which wraps around at the end of the list (in the same way that rotors would)
 * 
 * Based on example code at javaworld.com's spinner tutorial:
 * 		http://www.javaworld.com/javaworld/jw-08-2005/jw-0829-jspinner.html?page=4
 * 
 * getNextValue() and getPreviousValue() have been reversed for more browsing of alphabets
 */
import java.util.List;
import javax.swing.SpinnerListModel;

public class RolloverSpinnerListModel extends SpinnerListModel {
	private static final long serialVersionUID = 1L;

	public RolloverSpinnerListModel(List<?> values) {
		super(values);
	}

	public RolloverSpinnerListModel(Object[] values) {
		super(values);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SpinnerListModel#getNextValue()
	 */
	public Object getNextValue() {
		Object returnValue = super.getPreviousValue();
		if (returnValue == null) {
			List<?> list = getList();
			returnValue = list.get(list.size() - 1);
		}
		return returnValue;		
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerListModel#getPreviousValue()
	 */
	public Object getPreviousValue() {
		Object returnValue = super.getNextValue();
		if (returnValue == null) {
			returnValue = getList().get(0);
		}
		return returnValue;
	}
}