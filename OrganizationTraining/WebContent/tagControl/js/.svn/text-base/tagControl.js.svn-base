(function($,window){

	$.TagControl = function(elm, opts) {
	
		this.KEY = {
			BACKSPACE: 8,
			TAB: 9,
			ENTER: 13,
			SPACE: 32
		};
	
		var $self = this;
		this.elm = elm;
		elm.addClass('tagControl');
        var opts = (this.opts = $.extend({},$.fn.TagControl.conf,opts));
		this.checkReg = /[^A-Za-z0-9_;. +\u4E00-\u9FA5]+/gi;
		this.$inputHtml = $("<input class='taginput' name='taginput' style='width:" + $self.opts.inputWidth + "px;height:" + $self.opts.inputHeight + "px;' type='text'/>");
		this.$inputHtml.bind("keydown blur click", function(event){
			if (event.keyCode == $self.KEY.ENTER || event.keyCode == $self.KEY.TAB || event.type === "blur") {
				event.preventDefault();
				event.stopPropagation();
				var inputObj = $(this);
				var value = $(this).val();
				
				var _tags = [];
				var _newTags = [];
				if (value) {
					value = value.replace(/^;+|;+$/gi, "");
					_tags = value.split(';');
					
					for(var i = 0; i < _tags.length; i++){
						_tags[i] = _tags[i].replace(/(^\s*)|(\s*$)/g, "");
						if (_tags[i] != "") {
							_newTags.push(_tags[i]);
						}
					}
				}
				var activeMaxCount = $self.opts.maxTagCount - $("span[name='tag']", $self.elm).length;
				_newTags = _newTags.length > activeMaxCount ? _newTags.slice(0, activeMaxCount) : _newTags;
				for(var i = 0; i < _newTags.length; i++){
					$self.checkAndDrawValue(_newTags[i], inputObj);
				}
				
			}
		}).bind("focus",function() {
			$self.hideErr();
		});
		elm.click(function() {
			$(".taginput", elm).each(function() {
				$(this).focus();
				return false;
			});
		}).hover(function(){
			elm.addClass('stopInputBlur-tag');
		}, function(){
			elm.removeClass('stopInputBlur-tag');
		});
		$self.elm.append($self.$inputHtml);
	}
	
	$.TagControl.prototype = {
		
		checkAndDrawValue : function(value, inputObj){
			var $self = this;
			$(".heightlight", $self.elm).removeClass('heightlight');
			if (value != "") {
				value = value.replace(/(^\s*)|(\s*$)/g, "");
				if (value.length > $self.opts.tagMaxLen) {
					$self.showMes(inputObj, $self.opts.lenVaildationMes.replace("{0}", $self.opts.tagMaxLen));
					return;
				}
				//var _match = value.match($self.checkReg);
				//if (!_match) {
					var flag = true;
					$("span[name=\"tag\"][class^=\"tag_radius\"]", $self.elm).each(function(){
						if ($(this).find('label').text() === value) {
							$(this).addClass('heightlight');
							inputObj.val('');
							flag = false;
						}
					}); 
					if(!flag) return;
					$self.drawTag(inputObj, value);
					if($(".tag_radius", $self.elm).length >= $self.opts.maxTagCount) {
						inputObj.hide();
					}
					
					inputObj.focus();
					$self.hideErr();
				//}
				 //else {
				//	$self.showMes(inputObj, $self.opts.illegalVaildationMes.replace("{0}", _match.join(" ")));
				//}
				
			}
		},
		
		addTags : function(tagStr) {
			var $self = this;
			if($("span[name='tag']", $self.elm).length  >= $self.opts.maxTagCount){
				return false;
			}
			
			var _tags = [];
			var _newTags = [];
			if (tagStr) {
				tagStr = tagStr.replace(/^;+|;+$/gi, "");
				_tags = tagStr.split(';');
				
				for(var i = 0; i < _tags.length; i++){
					_tags[i] = _tags[i].replace(/(^\s*)|(\s*$)/g, "");
					if (_tags[i] != "") {
						_newTags.push(_tags[i]);
					}
				}
			}
			var activeMaxCount = $self.opts.maxTagCount - $("span[name='tag']", $self.elm).length;
			_newTags = _newTags.length > activeMaxCount ? _newTags.slice(0, activeMaxCount) : _newTags;
			for(var i = 0; i < _newTags.length; i++){
				var inputObj = $("input[type='text'][name='taginput']:last", $self.elm);
				$self.checkAndDrawValue(_newTags[i], inputObj);
			}
		},
		
		drawTag : function(inputObj, value){
			var $self = this;
			var $_span = $("<span name='tag' class='tag_radius'><label>" + value + "</label><a class='deltag'>&times;</a></span>");
			inputObj.before($_span);
			inputObj.val('');
			$_span.find("a").click(function() {
				$(this).parent().remove();
				if($(".tag_radius", $self.elm).length === $self.opts.maxTagCount - 1) {
					inputObj.show();
				}
				$self.setInputValue();
			});
			$self.setInputValue();
		},
		
		showMes : function(inputObj, mes) {
			$self = this;
			var _offset = inputObj.offset();
			var _mesHtml = "<div id='errormes' class='radius_shadow' style='position:absolute;left:" + (_offset.left + $self.opts.tipLOffset) + "px;top:" + (_offset.top + $self.opts.inputHeight + $self.opts.tipTOffset) + "px;'>" + mes + "</div>";
			$("#errormes").remove();
			$("body").append(_mesHtml);
		},
		
		hideErr : function() {
			$("#errormes").hide();
		},
		
		showErr : function() {
			$("#errormes").show();
		},
		
		getTagVals: function() {
			var $self = this;
			var values = [];
			$("span[name=\"tag\"][class^=\"tag_radius\"]", $self.elm).find("label").text(function(index, text) {
				
				values.push(text.replace($self.checkReg, ""));
			});
			return values;
		},
		
		setInputValue : function(){
			var $self = this;
			if ($self.opts.valueObject) {
				$self.opts.valueObject.val($self.getTagVals().join(';'));
			}
		},
		
		initInputWidth: function() {
			var $self = this;
			var _offset = $self.$inputHtml.offset();
			console.log(_offset.left);
			var _elmOffset = $self.elm.offset();
			var currentWidth = _elmOffset.left + $self.elm.width() - _offset.left - 10
			console.log(currentWidth);
			setTimeout(function(){
				console.log($self.$inputHtml[0].style.width);
				$self.$inputHtml[0].style.width = currentWidth + "px";
			},0);
		},
		
		clearData : function() {
			var $self = this;
			$("span[name=\"tag\"][class^=\"tag_radius\"]", $self.elm).remove();
			$self.opts.valueObject.val('');
		}
	}
	

	$.fn.TagControl = function(opts){
		var list = [];
		this.each(function(){
			list.push(new $.TagControl($(this), opts));
		})
		return list;
	}
	
	$.fn.TagControl.conf = {
		maxTagCount: 10,
		tagMaxLen: 30,
		inputWidth: 150,
		inputHeight: 15,
		tipTOffset: 5,
		tipLOffset: 0,
		lenVaildationMes: "请输入1到{0}个字符长度的标签",
		illegalVaildationMes: "内容不能包含非法字符「{0}」！",
		valueObject: undefined
	}
})(jQuery,window)