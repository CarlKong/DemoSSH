(function($){
	$.FilterBox = function(elm, opts) {
		this.opts = $.extend({}, $.fn.filterBox.defaults, opts);
		this.$elm = elm;
		this.$data = elm.children();
		this.isShowToolTip = false;
		this.init();
	};
	$.FilterBox.prototype = {
		init : function() {
			var $self = this;
			$(".single_condition:first", $self.$elm).addClass("first_single_condition");
			$(".first_single_condition", $self.$elm).removeClass("single_condition");
			$(".single_condition:last", $self.$elm).addClass("last_single_condition");
			$(".last_single_condition:last", $self.$elm).removeClass("single_condition");
			$(".existedFlag", $self.$elm).css("display","none");
			var filterBox_width = $(".filterBox", $self.$elm).css("width");
			var filterBox_padding_right = $(".filterBox", $self.$elm).css("padding-right");
			var left = filterBox_width.substring(0,filterBox_width.length - 2) 
				- filterBox_padding_right.substring(0,filterBox_padding_right.length - 2);
			$(".reset", $self.$elm).css("left", left+"px");
			
			$("input:checkbox", $self.$elm).each(function(i){
				var $checkbox = $(this);
				if($checkbox.next().get(0).nodeName === "LABEL") {
					var span_checkbox = "<span class='span_checkbox'></span>";
					$checkbox.hide();
					$checkbox.after(span_checkbox);
					if($checkbox.attr("class") === "checked_all") {
						$checkbox.next().addClass("span_checked_all");
					}else {
						$checkbox.next().bind("click",function(){
							if($(this).prev().get(0).checked === false) {
								$(this).prev().get(0).checked = true;
								$(this).addClass('span_checked');
							} else {
								$(this).prev().get(0).checked = false;
								if ($(this).parent().prevAll().find(".checked_all").get(0) != undefined) {
									$(this).parent().prevAll().find(".checked_all").get(0).checked = false;
									$(this).parent().prevAll().find(".span_checked_all").removeClass('span_checked');
								}
								$(this).removeClass('span_checked');
							}
							return false;
						});
					}
				}
			});
			$(".span_checked_all", $self.$elm).each(function(){
				$(this).bind("click",function(){
					if($(this).prev().get(0).checked === false) {
						$(this).prev().get(0).checked = true;
						$(this).addClass('span_checked');
						$(this).parent().nextAll().each(function(){
							$(this).find("input:checkbox").get(0).checked = true;
							if(!$(this).find(".span_checkbox").hasClass('span_checked')){
								$(this).find(".span_checkbox").addClass('span_checked');
							}
						});
					}else {
						$(this).prev().get(0).checked = false;
						$(this).removeClass('span_checked');
						$(this).parent().nextAll().each(function(){
							$(this).find("input:checkbox").get(0).checked = false;
							if($(this).find(".span_checkbox").hasClass('span_checked')){
								$(this).find(".span_checkbox").removeClass('span_checked');
							}
						});
					}
					return false;
				});
			});
			$(".filterBtn", $self.$elm).click(function(){
				if($(".filterBox", $self.$elm).css("display") == "none") {
					$(this).removeClass('filter_no_dropDown');
					$(this).addClass('filter_dropDown');
					$(".existedFlag", $self.$elm).css("display","none");
					$(".filterBox", $self.$elm).slideDown();
					$self.isShowToolTip = false;
					$self.$elm.poshytip("destroy");
				} else {
					$(this).removeClass('filter_dropDown');
					$(this).addClass('filter_no_dropDown');
					var flag = false;
					flag = $self.hasCondition();
					if(flag) {
						$(".existedFlag", $self.$elm).css("display","inline-block");
					}else {
						$(".existedFlag", $self.$elm).css("display","none");
					}
					$(".filterBox", $self.$elm).slideUp();
					$self.isShowToolTip = true;
					//set tooltip for exsitedFlag
					$self.showExsitedCondition();
				}
				return false;
			});
			$(".reset", $self.$elm).click(function(){
				$("input:checkbox",$self.$elm).each(function(){
					this.checked = false;
				});
				$("input:text",$self.$elm).each(function(){
					this.value = "";
				});
				$(".span_checkbox",$self.$elm).each(function(){
					$(this).removeClass('span_checked');
				});
			});
			$self.$elm.click(function() {
				return false;
			});
			$(document).click(function(e){
				if($('#ui-datepicker-div')[0] === undefined || !$.contains($('#ui-datepicker-div')[0], e.target)) {
					var a = $('.ui-datepicker-prev span', $('#ui-datepicker-div'))[0] !== undefined;
					if(a){
						var b = $('.ui-datepicker-prev span', $('#ui-datepicker-div')).attr('class') === $(e.target).attr('class') 
							|| $('.ui-datepicker-next span', $('#ui-datepicker-div')).attr('class') === $(e.target).attr('class');
						if(b) return false;
					}
					$(".filterBtn", $self.$elm).removeClass('filter_dropDown');
					$(".filterBtn", $self.$elm).addClass('filter_no_dropDown');
					$(this).removeClass('filter_dropDown');
					$(this).addClass('filter_no_dropDown');
					var flag = false;
					flag = $self.hasCondition();
					if(flag) {
						$(".existedFlag", $self.$elm).css("display","inline-block");
					}else {
						$(".existedFlag", $self.$elm).css("display","none");
					}
					$(".filterBox", $self.$elm).slideUp();
					$self.isShowToolTip = true;
					//set tooltip for exsitedFlag
					$self.showExsitedCondition();
				}
			});
		},
		
		hasCondition : function() {
			var $self = this;
			var $checked = $("input:checked",$self.$elm);
			var $inputs = $("input:text",$self.$elm);
			var flag = false;
			if($checked.length > 0) {
				flag = true;
				return flag;
			}
			$inputs.each(function(){
				if(this.value != "") {
					flag = true;
					return flag;
				}
			});
			return flag;
		},
		
		showExsitedCondition : function() {
			var $self = this;
			$self.$elm.poshytip("destroy");
			var $checked = $("input:checked",$self.$elm);
			var $inputs = $("input:text",$self.$elm);
			if($self.isShowToolTip) {
				var filterConditionStr = "";
				$checked.each(function(){
					if($(this).attr("content")) {
						filterConditionStr += $(this).attr("content") + ", ";
					}
				});
				$inputs.each(function(){
					if(this.value != "") {
						filterConditionStr += $(this).attr("content") + $(this).val()+ ", ";
					}
				});
				var filterConditionStr = filterConditionStr.substring(0, filterConditionStr.length - 2);
				if(filterConditionStr.length > 0) {
					$self.$elm.poshytip({
						className: 'tip-green',
						content: filterConditionStr
					});
				}
			}
		}
	};
	$.fn.filterBox = function(opts){
		if(this.size() > 1){
			throw new Error('The selector result size should be one');
		}
		return new $.FilterBox(this,opts);
	};
	$.fn.filterBox.defaults = {
			
	};
})(jQuery); 
