/**
 * Commons: commonTab can draw optional tab on the page, 
 * 			and bind mouse over and mouse out event(HOVER),
 * 			but you must bind click function by yourself.
 */

/**
 * Draw tab on page base on UX design.
 * @param totalWidth
 * 			Integer type, width of tab head.
 * @param tabWidth
 * 			Integer type, width of every tab.
 * @param tabObjs
 * 			Object Array type, tabObj of all tabs.
 * 			Object like {id:"planTab", name:"Plan"}
 * @param $commonMenuTab
 * 			content to draw optional tab.
 * @param defaultSelectedNum
 * 			0: no one is default selected
 * 			>0: default selected number, start from 1
 * @return
 */
function initTab(totalWidth, tabWidth, tabObjs, $commonMenuTab, defaultSelectedNum){
	//Param of commonTab is wrong!
	if (tabObjs.length * tabWidth > totalWidth) {
		return;
	}
	if (defaultSelectedNum && defaultSelectedNum > tabObjs.length) {
		return;
	}
	$commonMenuTab.css({"width":totalWidth+"px", "display":"inline-block", "height":"auto"});
	//draw background of left part.
	var tabBgWidth = (totalWidth - tabObjs.length * tabWidth) / 2;
	var $commomMenuBg = $("<div>", {"class":"common_menu_bg"}).appendTo($commonMenuTab);
	$commomMenuBg.width(tabBgWidth+"px");
	//draw every tab
	var $defaultSelectedTab = null;
	for (i=1; i<=tabObjs.length; i++) {
		var $commonTab = $("<div>", {"id":tabObjs[i-1].id, "class":"common_tab"}).appendTo($commonMenuTab);
		$("<div>", {"class":"unselectedBkg"}).appendTo($commonTab);
		$("<div>", {"class":"left_border"}).appendTo($commonTab);
		$("<div>", {"class":"mid_part"}).appendTo($commonTab).text(tabObjs[i-1].name);
		$("<div>", {"class":"right_border"}).appendTo($commonTab);
		$("<div>", {"class":"unselectedBkg"}).appendTo($commonTab);
		$("<div>", {"class":"clear"}).appendTo($commonTab);
		//default selected tab
		if (defaultSelectedNum && defaultSelectedNum > 0 && i == defaultSelectedNum) {
			$defaultSelectedTab = $commonTab;
		}
		//bind mouse over and mouse out event
		$commonTab.bind("mouseover", function(){
			_overOptionTab($(this));
		});
		$commonTab.bind("mouseout", function(){
			_outOptionTab($(this));
		});
	}
	//draw background of right part, the same with left.
	var $commomMenuBg = $("<div>", {"class":"common_menu_bg"}).appendTo($commonMenuTab);
	$commomMenuBg.width(tabBgWidth+"px");
	$("<div>", {"class":"clear"}).appendTo($commonMenuTab);
	//Select default tab or no tab default selected.
	if ($defaultSelectedTab) {
		_selectDefaultTab($defaultSelectedTab, $commonMenuTab);
	} else {
		_noTabSelected($commonMenuTab);
	}
}

/**
 * Change style of tab after click tab,
 * call it in bind click event function.
 * @param $clickObj
 * @return
 */
function clickOptionTab($clickObj, $commonMenuTab) {
	_noTabSelected($commonMenuTab);
	$clickObj.find(".left_border").addClass("selected_tab_left").removeClass("unselected_tab_left");
	$clickObj.find(".right_border").addClass("selected_tab_right").removeClass("unselected_tab_right");
	$clickObj.find(".mid_part").addClass("selected_mid_tab").removeClass("unselected_mid_tab");
	$clickObj.attr("result", "selected");
}

/**
 * Default to select one tab.
 * @param $$defaultTab
 * @param $commonMenuTab
 * @return
 */
function _selectDefaultTab($defaultTab, $commonMenuTab){
	clickOptionTab($defaultTab, $commonMenuTab);
	$defaultTab.find(".unselectedBkg").hide();
	$defaultTab.removeClass("unselectedTabBkg");
}

function _noTabSelected($commonMenuTab) {
	$commonMenuTab.find(".common_tab").addClass("unselectedTabBkg");
	$commonMenuTab.find(".common_tab").attr("result", "unselected");
	$commonMenuTab.find(".unselectedBkg").show();
	$commonMenuTab.find(".left_border").addClass("unselected_tab_left").removeClass("selected_tab_left");
	$commonMenuTab.find(".right_border").addClass("unselected_tab_right").removeClass("selected_tab_right");
	$commonMenuTab.find(".mid_part").addClass("unselected_mid_tab").removeClass("selected_mid_tab");
}

/**
 * Change style of tab after mouse over tab.
 * @param $overObj
 * @return
 */
function _overOptionTab($overObj) {
	$overObj.addClass("unselectedTabBkg");
	$overObj.find(".unselectedBkg").show();
	$overObj.find(".left_border").removeClass("selected_tab_left").removeClass("unselected_tab_left").addClass("tab_hover_left");
	$overObj.find(".right_border").removeClass("selected_tab_right").removeClass("unselected_tab_right").addClass("tab_hover_right");
	$overObj.find(".mid_part").removeClass("selected_mid_tab").removeClass("unselected_mid_tab").addClass("tab_hover_mid");
}

/**
 * Change style of tab after mouse out tab.
 * @param $overObj
 * @return
 */
function _outOptionTab($outObj) {
	if ($outObj.attr("result") == "selected") {
		$outObj.removeClass("unselectedTabBkg");
		$outObj.find(".unselectedBkg").hide();
		$outObj.find(".left_border").addClass("selected_tab_left").removeClass("tab_hover_left");
		$outObj.find(".right_border").addClass("selected_tab_right").removeClass("tab_hover_right");
		$outObj.find(".mid_part").addClass("selected_mid_tab").removeClass("tab_hover_mid");
	} else {
		$outObj.find(".left_border").addClass("unselected_tab_left").removeClass("tab_hover_left");
		$outObj.find(".right_border").addClass("unselected_tab_right").removeClass("tab_hover_right");
		$outObj.find(".mid_part").addClass("unselected_mid_tab").removeClass("tab_hover_mid");
	}
}