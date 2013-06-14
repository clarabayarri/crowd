package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HomeControllerTest {

	private HomeController controller = new HomeController();
	
	@Test
	public void testLoadHomeHandleRequestView() {
		String result = controller.loadHome();
		
		assertEquals("index", result);
	}
	
	@Test
	public void testFindIconHandleRequestView() {
		String result = controller.findIcon();
		
		assertEquals("redirect:/resources/img/favicon.ico", result);
	}

}
