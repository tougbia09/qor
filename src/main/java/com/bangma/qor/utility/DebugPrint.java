package com.bangma.qor.utility;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DebugPrint {
	public static void printSet(Set<Integer> set) {
		Iterator<Integer> iter = set.iterator();
		String toPrint = "[";
		while(iter.hasNext()) {
			toPrint += iter.next() + ", ";
		}
		System.out.println(toPrint.substring(0, toPrint.length() - 2) + "]");
	}
	public static void printDeque(Deque<Integer> deque) {
		Iterator<Integer> iter = deque.iterator();
		String toPrint = "[";
		while(iter.hasNext()) {
			toPrint += iter.next() + ", ";
		}
		System.out.println(toPrint.substring(0, toPrint.length() - 2) + "]");
	}
	public static void printList(List<Integer> list) {
		Iterator<Integer> iter = list.iterator();
		String toPrint = "[";
		while(iter.hasNext()) {
			toPrint += iter.next() + ", ";
		}
		System.out.println(toPrint.substring(0, toPrint.length() - 2) + "]");
		
	}
}
