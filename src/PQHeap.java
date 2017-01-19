import java.util.ArrayList;
import java.util.Comparator;

public class PQHeap<T> {
	private ArrayList<T> heap;
	private int heapSize; 
	private Comparator<T> comp;
	
	public PQHeap(Comparator<T> comparator) {
		heap = new ArrayList<T>();
		heapSize = 0;
		comp = comparator;
	}
	
	public int size () {
		return this.heap.size();
	}
	
	private void swap(int idex1, int idex2) {
		//Swaps two indexes
		T tempVar = heap.get(idex1);     //Temporarily store the value at idex 1  
		heap.set(idex1, heap.get(idex2));  //Takes object at index 2 and puts it into index 1
		heap.set(idex2, tempVar);  //Set object at index2 to the tempVar, which is object from index 1
	}
	
	private void moveUp() {
		// Swaps the last added value till it is smaller than its parent ie the rule for the max heap 
		int parent = ((this.heapSize-1)/2);      //Parent formula
		int current = heapSize;
		int compared = comp.compare(heap.get(current), heap.get(parent));
		while (compared > 0 && current >0) {      //See if current node is smaller than parent node
			this.swap(parent, current);  //Perform swap
			current = parent;
			parent = (current -1)/2;  //resets current and parent indexes   //Memory would be lost here but since it is a primitive type int not class Integer it isnt. 
			compared = comp.compare(heap.get(current), heap.get(parent));
		}
	}
	
	public void add(T obj) {
		//Adds a new object to the heap
		heap.add(obj);
		this.moveUp();
		heapSize++;
	}
	
	private int getLargerChildIndex(int cur) {
		//Gets the larger child of the current indes
		int rC = 0;
		int lC = 0;
//		System.out.println("Current = " + cur + " , Heap size = " + heapSize + " ( Left = " + (2*cur + 1) + ")");
		if (2*cur+1 == heapSize-1) {    //Bounds checking for left child
			return 2*cur+1;
		}
		if (2*cur+2 < heapSize) {    //Bounds checking for right child
			rC = 2*cur+2;
		}
		lC = 2*cur+1;
//		System.out.println("Left = " + lC + " , Heap size = " + heapSize + " ( Right = " +  rC + ")");
		if (comp.compare(heap.get(lC), heap.get(rC)) > 0) {    //right is "larger" than left
			return lC;
		}
		else {   //otherwise the left one is larger
			return rC;
		}
		
	}
	
	private void moveDown() {
		//Swaps the top most object until it is in proper place(maintaining rule of a max heap (parent > child))
		int cur = 0;
		int larger;
		if (heapSize <= 1) {  //If heapsize if less than or equal to 1 exit
			return;
		}
		while (2*cur + 1 < heapSize) {  //While a left child exists check to see if parent is smaller than child
			larger = getLargerChildIndex(cur);
			if (comp.compare(heap.get(cur), heap.get(larger)) < 0) {
				this.swap(cur, larger);  //Swap if necessary
			}
			else { break; }
			cur = larger; //Memory would be lost here but since it is a primitive type int not class Integer it isnt. 
			}
		}

	
	public T remove() {
		//Removes the first element of the array
		T tempVar = heap.get(0);
		if (this.heapSize == 1) {
			this.heapSize = 0;
			heap.remove(0);
			return tempVar;
		}
		if (this.heapSize == 0) {
			return null;
		}
		heap.set(0, heap.get(heapSize-1));
		heap.remove(heapSize-1 );
		heapSize --;
		moveDown();  //Reorganising of the heap
		return tempVar;
	}
}
