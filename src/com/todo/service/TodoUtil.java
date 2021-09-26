package com.todo.service;

import java.io.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {

	public static void createItem(TodoList list) {

		String title, desc;
		Scanner sc = new Scanner(System.in);

		System.out.print("< 항목 추가 >" + "\n제목을 입력해 주세요 =)");

		title = sc.next();
		if (list.isDuplicate(title)) {
			System.out.print("이미 동일 제목이 존재합니다 =(");
			return;
		}
		
		sc.nextLine();

		System.out.print("카테고리를 입력하세요 =) ");
		String category = sc.nextLine();
		
		System.out.print("설명을 입력하세요 =) ");
		desc = sc.nextLine();
		
		System.out.print("마감일자를 입력하세요 =)(yyyy/mm/dd) ");
		String due_date = sc.nextLine();
		
		TodoItem t = new TodoItem(category,title, desc,due_date);
		t.setCategory(category);
		t.setDue_date(due_date);
		list.addItem(t);
		System.out.println("내용이 추가되었습니다 =) ");
	}

	public static void deleteItem(TodoList l) {

		Scanner sc = new Scanner(System.in);
		System.out.print("< 항목 제거 >" + "\n제거할 항목의 번호를 입력하세요 =) ");
		String number  = sc.next();

		for (TodoItem item : l.getList()) {
			if (l.indexOf(item) + 1 == Integer.parseInt(number)) {
				l.deleteItem(item);
				System.out.println("내용이 삭제되었습니다 =) ");
				break;
			}
//			if (num.equals(item.getTitle())) {
//				l.deleteItem(item);
//				System.out.println("내용이 삭제되었습니다 =) ");
//				break;
//			}
		}
	}

	public static void updateItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.print("< 항목 수정 > \n" + "수정할 번호를 입력해주세요 =) ");
		
		int num = sc.nextInt();
		
		if (l.getList().size() < num) {
			System.out.println("해당번호가 없습니다.\n");
			return;
		}

		System.out.print("새로운 제목을 입력해주세요 =) ");
		String new_title = sc.nextLine().trim();
		if (l.isDuplicate(new_title)) {
			System.out.println("이런,,이미 동일 제목이 존재합니다 =(");
			return;
		}
		sc.nextLine();
		
		System.out.print("새로운 카테고리를 입력해주세요=) ");
		String new_category = sc.next();
		
		System.out.print("새로운 내용을 입력해주세요 =) ");
		String new_description = sc.nextLine().trim();
		
		sc.nextLine();
		System.out.print("새로운 마감일을 입력하세요 (yyyy/mm/dd) > ");
		String new_due_date = sc.nextLine();
		
		int index=0;
		
		for (TodoItem item : l.getList()) {
			if (num-1 == index){
				l.deleteItem(item);
				TodoItem t = new TodoItem(new_category,new_title, new_description,new_due_date);
				l.addItem(t);
				System.out.println("수정 완료~!");
			}
			
			index++;
		}

	}
	
	public static void find(TodoList l, String key) {
		System.out.println("<\""+key+"\"의 검색 결과>");
		int index = 0;
		int count = 0;
		
		for (TodoItem item : l.getList()) {
			index++;
			
			if(item.toString().substring(item.toString().indexOf("]")).contains(key)) {
				System.out.println(index+". "+item.toString());
				count++;
			}
		}
		System.out.printf("총 %d개의 항목을 찾았습니다 =)\n",count);
	}
	
	public static void find_cate(TodoList l, String key) {
		System.out.println("<\""+key+"\"의 검색 결과>");
		int index = 0;
		int count = 0;
		
		for (TodoItem item : l.getList()) {
			index++;
			if(item.toString().substring(item.toString().indexOf("["),item.toString().indexOf("]")).contains(key)) {
				System.out.println(index+". "+item.toString());
				count++;
			}
		}
		System.out.printf("총 %d개의 항목을 찾았습니다.\n",count);
	}

	public static void listAll(TodoList l) {
		int total = l.getSize();
		System.out.println("\n[ 전체목록, " + "총 "+ total +"개"+ " ]" + "\n" );
		int i = 1;
		for (TodoItem item : l.getList()) {
			System.out.println(i+". "+item.toString());
			i++;
		}
	}
	
	public static void listCate(TodoList l) {
		System.out.println("");
		System.out.println("<카테고리 목록>");
		
		List<String> cates = new ArrayList<String>();
		
		String cl;
		
		int count = 0;
		
		for (TodoItem item : l.getList()) {
			cl = item.getCategory();
			if(cates.contains(cl)) continue;
			else{
				cates.add(cl);
				count++;
			}
		}
		for (String cate : cates) {
			System.out.print(cate + " ");
			if(cate == cates.get(count-1)) continue;
			else System.out.print("/ ");
		}
		System.out.printf("총 %d개의 카테고리가 등록되어 있습니다.\n",count);
	}
	
	
	public static void loadList(TodoList l, String filename) {
		int count = 0;
		try {
			File fl = new File(filename);
			if (!fl.exists()) {
				System.out.println("파일이 존재하지 않습니다.");
				return;
			}
			else {
				BufferedReader b = new BufferedReader(new FileReader(filename));
				String s;
				while ((s = b.readLine()) != null) {
					count ++;
					StringTokenizer st = new StringTokenizer(s, "##");
					String title = st.nextToken();
					String category = st.nextToken();
					String desc = st.nextToken();
					String due_date = st.nextToken();
					String current_date = st.nextToken();
					
					TodoItem t = new TodoItem(category,title, desc, due_date);
					t.setCurrent_date(current_date);
					l.addItem(t);
				}
				b.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(count+"개의 줄을 읽었습니다.");
	}

	public static void saveList(TodoList l, String filename) {
		try {
			FileWriter f = new FileWriter(filename);
			for (TodoItem item : l.getList()) {
				f.write(item.toSaveString());
			}
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("파일 저장 완료\n");
	}
}

