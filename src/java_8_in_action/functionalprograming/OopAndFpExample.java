package java_8_in_action.functionalprograming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OopAndFpExample {
	static final List<Integer> list = Arrays.asList(1, 4, 9);
	public static void main(String[] args) {
		List<List<Integer>> result = subsets(list);
		System.out.println(result);
	}
	
	static List<List<Integer>> subsets (List<Integer> list) {
		if (list.isEmpty()) {
			List<List<Integer>> result = new ArrayList<>();
			result.add(Collections.emptyList());
			return result;
		}
		int first = list.get(0);
		List<Integer> rest = list.subList(1, list.size());
		
		List<List<Integer>> subans = subsets(rest);
		List<List<Integer>> subans2 = insertAll(first, subans);
		return concat(subans, subans2);
	}
	
	static List<List<Integer>> insertAll (int first, List<List<Integer>> lists) {
		List<List<Integer>> result = new ArrayList<>();
		for(List<Integer> list : lists) {
			List<Integer> copyList = new ArrayList<>();
			copyList.add(first);
			copyList.addAll(list);
			result.add(copyList);
		}
		return result;
	}
	
	static List<List<Integer>> concat (List<List<Integer>> subans, List<List<Integer>> subans2) {
		List<List<Integer>> result = new ArrayList<>(subans);
		result.addAll(subans2);
		return result;
	}
}
