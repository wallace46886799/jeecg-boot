/**
 * 
 */
package com.dreamlabs.concurrent;

import org.junit.Test;


/**
 * @author Frank
 *
 */
public class ConcurrentTest {
	
	private int counter = 0;
	
	private synchronized int  netx(){
		return counter++;
	}
	
	@Test
	public void testNext(){
		
		Runnable r = new Runnable(){
			public void run() {
				System.out.println(netx());
			}
		};
		
		
		while(true){
			Thread t = new Thread(r);
			t.start();
		}
	}
}
