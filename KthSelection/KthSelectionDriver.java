//Program to select the kth lowest item in an unsorted array
import java.util.*;
import java.io.*;

public class KthSelectionDriver{
	public static void main(String[] args){
			int i = 100000;
			System.out.println("n is " + i);
			KthSelection sel = new KthSelection(i);
			int k = 1;
			System.out.println("k is 1");
			doEachAlg(sel, k);
			k = i / 4;
			System.out.println("k is n / 4");
			doEachAlg(sel,k);
			k = i / 2;
			System.out.println("k is n / 2");
			doEachAlg(sel,k);
			k = (3 * i) / 4;
			System.out.println("k is 3n / 4");
			doEachAlg(sel,k);
			k = i;
			System.out.println("k is n");
			doEachAlg(sel,k);
	}

	public static void doEachAlg(KthSelection selection, int k){
		long time = System.nanoTime();
		System.out.println(selection.selectKth1(4));
		System.out.println(System.nanoTime() - time);
		time = System.nanoTime();
		System.out.println(selection.selectKth2(4));
		System.out.println(System.nanoTime() - time);
		time = System.nanoTime();
		System.out.println(selection.selectKth3(4));
		System.out.println(System.nanoTime() - time);
		time = System.nanoTime();
		try{
			System.out.println(selection.selectKth4(4));
		}catch(StackOverflowError e){
			System.out.println("whoops");
		}
		System.out.println(System.nanoTime() - time);
		System.out.println("-------------------------------------------------");
	}
}