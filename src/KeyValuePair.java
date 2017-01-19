public class KeyValuePair<Key,Value> {
	private Key keyVal;
	private Value val;
	
	public KeyValuePair( Key k, Value v ) {
		this.keyVal = k;
		this.val = v;
	}
	public Key getKey() {
		//returns the Key
		return this.keyVal;
	}
	
	public Value getValue()  {
	//return the value
		return this.val;
	}
	
	public void setValue( Value v ) {
	// 	set the value
		this.val = v;
	}
	
	public String toString() {
	//returns a string with the values
		String retVal = "Key = " + keyVal + " Value = " + val;
		return retVal;
	}
	
	public static void main (String[] args) {
		KeyValuePair KVP = new KeyValuePair(1,2);
		System.out.println(KVP.getKey());
		System.out.println(KVP.getValue());
		KVP.setValue(3);
		System.out.println(KVP.getValue());
		System.out.println(KVP.toString());
	}
}
