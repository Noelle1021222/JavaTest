package test.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class test {

	public static void main(String[] args) {

		// original: int changeNeed = (totalIPNImpact/200)+1;

		String[] tokens = { "A","B","C"};
		
		Set<String> itemNumberSet = new TreeSet<>();

		List<String> tokenList = Arrays.asList(tokens);

		
		ArrayList<String> unique = new ArrayList<String>();
		for (String a : tokenList) {
			if (itemNumberSet.add(a.toUpperCase())) {
				unique.add(a.toUpperCase());
			}
				
		}
		
		List<String> unique1 = tokenList.stream().map(String::toUpperCase).distinct().collect(Collectors.toList());

		
		System.out.println("tokenList size:"+tokenList.size());
		System.out.println("uniqueList size:"+unique.size());
		System.out.println("unique1List size:"+unique1.size());

		
		System.out.println("tokenList:"+tokenList);
		System.out.println("uniqueList:"+unique);
		System.out.println("unique1List:"+unique1);


		
	       List<String> list = new ArrayList<>();
	        list.add("张三");
	        list.add("张三");
	        list.add("张飞");
	        list.add("张二河");
	        list.add("张二狗");
	        list.add("张二河");
	        List<String> listStr = new ArrayList<>();
	        Map<String,Integer> map = new HashMap<>();
	        int i = 0;
	        for (String str : list) {
	            map.put(str, i+1);
	            i++;
	        }
	        Set<String> sets = map.keySet();
	        for (String string : sets) {
	            listStr.add(string);
	        }
	        for (String string : listStr) {
	            System.out.println(string);
	        }
	    

		


	}

}
