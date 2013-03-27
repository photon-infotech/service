/*
 * Service Web Archive
 *
 * Copyright (C) 1999-2013 Photon Infotech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// JavaScript Document
$(window).resize(function() {
	$(window).bind("resize", resizeWindow);
	var heightDiff_MainWrpr = $(window).height()- $(".main_wrapper").height();
	var heightDiff_Wrpr = $(".main_wrapper").height()- $(".wrapper").height();
	var heightDiff_Content = $(".wrapper").height()- $(".container").height();
	
	function resizeWindow(e) {
		var newHeight_Window = $(window).height();
		$(".main_wrapper").css("height", newHeight_Window - heightDiff_MainWrpr);
		var newHeight_MainWrpr = $(".main_wrapper").height();
		$(".wrapper").css("height", newHeight_MainWrpr - heightDiff_Wrpr);
		
		var newHeight_content = $(".wrapper").height();
		$(".container").css("height", newHeight_content - heightDiff_Content);
		
		var newHeight_subcont = $(".container").height();
		$("#subcontainer").css("height", newHeight_subcont - 25);
		
		var newHeight_form = $("#subcontainer").height();
		$(".form-horizontal").css("height", newHeight_form - 5);
		$(".table_div").css("height",newHeight_form - 50);
		
		var newHeight_tableDiv = $(".table_div").height();
		$(".fixed-table-container-inner").css("height", newHeight_tableDiv - 30);
		
		var newHeight_tableContainerInner = $(".fixed-table-container-inner").height();
		$(".scroll-line").css("height",newHeight_tableContainerInner - 70);
		$(".scroll-bar").css("height",newHeight_tableContainerInner - 10); 
		
		var newHeight_contentAdder = $(".content_adder").height();
		$(".scroll-line").css("height", newHeight_contentAdder - 70);
		$(".scroll-bar").css("height", newHeight_contentAdder - 10);
		
		$(".multilist-scroller .scroll-line").css("position", "static");
		$(".multilist-scroller .scroll-bar").css("height", "82px");
		$(".multilist-scroller .scroll-bar").css("top", "7px");

		
		var newHeight_formHoriz = $(".form-horizontal").height();
		$(".content_adder").css("height", newHeight_formHoriz - 70);
		
		var newHeight_content = $(".form-horizontal").height();
		$(".content_feature").css("height", newHeight_content - 95);
		
		var newWidth_button = $(".form-horizontal").width();
		$(".bottom_button").css("width", newWidth_button - 25);		
	}
});
