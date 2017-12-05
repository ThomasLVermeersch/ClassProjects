import java.util.*;

public class KthSelection{
	private static int[] a;
	private static int length;

	//Constructor initializes array and populates it
	public KthSelection(int n){
		this.length = n;
		this.a = new int[n];
		randomPopulate();

	}
	//return length of array
	public int getLength(){
		return this.length;
	}

	//Get kth element by simply sorting and returning kth element
	public int selectKth1(int k){
		//Copy array over so we dont modify the OG
		int[] arr = new int[this.length];
		for(int i = 0; i < this.length; i++ ) arr[i] = this.a[i];
		//Sort this array
		arr = mergeSort(arr);
		//Return kth element
		return arr[k - 1];
	}

	//Get kth element with iterative partitioning
	public int selectKth2(int k){
		int[] arr = new int[this.length];
		for(int i = 0; i < this.length; i++ ){
			arr[i] = this.a[i];
		}
		return partitionIteration(arr, k);
	}

	//Get kth element with recursive partitioning
	public int selectKth3(int k){
		int[] arr = new int[this.length];
		for(int i = 0; i < this.length; i++ ){
			arr[i] = this.a[i];
		}
		return partitionRecursive(arr, 0, arr.length - 1, k);
	}

	//Get kth element with mm method
	public int selectKth4(int k){
		int[] arr = new int[this.length];
		for(int i = 0; i < this.length; i++ ){
			arr[i] = this.a[i];
		}
		return partitionMM(arr, k);
	}

	int partitionIteration(int[] arr, int k){
		int low = 0;
		int high = arr.length - 1;
		int pivotIndex = -1;
		while(pivotIndex != k-1){
			pivotIndex = partition(arr, low, high);
			if(pivotIndex == k-1) return arr[k-1];
			else if(k-1 < pivotIndex) high = pivotIndex - 1;
			else low = pivotIndex + 1;
		}
		return -1;
	}

	int partitionRecursive(int[] arr, int low, int high, int k){
		int pivotIndex = partition(arr, low, high);
		if(pivotIndex == k - 1) return arr[k-1];
		else if(k-1 < pivotIndex) return partitionRecursive(arr, low, --pivotIndex, k);
		else return partitionRecursive(arr, ++pivotIndex, high, k);
	}

	int partitionMM(int[] arr, int k){
		if(a.length <= 100) return selectKth1(k);
		int subsets = a.length/100;
		int[] medians = new int[subsets];
		for(int i = 0; i < subsets; i++){
			int lowRange = i*100;
			int[] subset = takeSubset(arr, lowRange, lowRange + 100);
			medians[i] = partitionMM(subset, 50);
		}
		int pivot = partitionMM(medians, subsets/2);
		int pivotIndex = 0;
		while(pivotIndex != arr[pivot]) pivotIndex++;
		swap(arr, pivotIndex, arr.length-1);
		pivotIndex = partition(arr, 0, arr.length - 1);

		if(k-1 == pivotIndex) return arr[pivotIndex];
		else if(k-1 < pivotIndex){
			int[] lowerSubset = takeSubset(arr, 0, pivotIndex - 1);
			return partitionMM(lowerSubset, k);
		}
		int[] subset = takeSubset(arr, pivotIndex + 1, arr.length - 1);
		return partitionMM(subset, k);
	}

	int[] takeSubset(int[] arr, int low, int high){
		int[] temp = new int[100];
		for(int i = 0; i < temp.length; i++) temp[i] = arr[i + low];
		return temp;
	}

	int partition(int[] arr, int low, int high){
		if(low == high) return low;
		int pivot = arr[high];
		int fin = low;
		for(int i = low; i < high; i++){
			if(arr[i] <= pivot) swap(arr, i, fin++);
		}
		swap(arr,fin,high);
		return fin;
	}

	void swap(int[] arr, int b, int c){
		int temp = arr[b];
		arr[b] = arr[c];
		arr[c] = temp;
	}

	//Randomly populate our Array
	void randomPopulate(){
		Random rng = new Random();
		for(int i = 0; i < this.length; i++){
			int n = rng.nextInt(length);
			this.a[i] = n;
		}
	}

	//merge sort function
	int[] mergeSort(int[] arr){
		//Base case: return when array is length 1
		int length = arr.length;
		if( length <= 1) return arr;
		//Initialize arrays for each half(partition)
		int[] arr1 = new int[length/2];
		int[] arr2 = new int[length - length/2];
		for(int i = 0; i < arr1.length; i++) arr1[i] = arr[i];
		for(int i = 0; i < arr2.length; i++) arr2[i] = arr[i + length/2];
		//Merge two arrays
		return merge(mergeSort(arr1), mergeSort(arr2));
	}

	//Merge function for merge sort
	int[] merge(int[] arr1, int[] arr2){
		int[] arr = new int[arr1.length + arr2.length];
		//Keep track of indices for array1 and array2
		int i = 0;
		int j = 0;
		//Merge based on values at indices of arrays
		for(int k = 0; k < arr.length; k++){
			if(i >= arr1.length) arr[k] = arr2[j++];
			else if (j >= arr2.length) arr[k] = arr1[i++];
			else if (arr1[i] <= arr2[j]) arr[k] = arr1[i++];
			else arr[k] = arr2[j++];
		}
		return arr;
	}
}




















