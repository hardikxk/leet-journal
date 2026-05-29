public class BinarySearch {
	public static void main(String[] args){
		int res = algo(new int[]{2, 3, 5, 9, 11, 34}, 3);
		if(res != -1){
			System.out.println("Target number found at index: " + res);
		}
		else{
			System.out.println("Number is not present in the array!!");
		}
	}
	public static int algo(int[] arr, int k){
		int low = 0;
		int high = arr.length;
		while(low < high){
			int mid = (low + high) / 2;
			if(k < arr[mid]){
				high = mid;
			}
			else if(k > arr[mid]){
				low = mid + 1;
			}
			else{
				return mid;
			}
		}
		return -1;
	}
}
