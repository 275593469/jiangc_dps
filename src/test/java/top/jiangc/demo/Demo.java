package top.jiangc.demo;

import org.junit.Test;

public class Demo {
	
	@Test
	public void test1(){
		String a = "123";
		String b = "123";
		String c = new String("123");
		System.out.println(a==b);
		System.out.println(a.equals(b));
		System.out.println(a==c);
		System.out.println(a.equals(c));
		
	}
	
	public void test2(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		t.start();
		
	}
	
	@Test
	public void test3(){
	
		while(true){
			
		int num = (int) (Math.random()*10)+1;
		System.out.println(num);
		}
	}

	
}
