(function($,window){
	I18N = {
			ZH : {
				all : '全选',
				perPage : '每页',
				of : '总',
				save : '保存',
				cancel : '取消',
				please_input_a_positive_integer : '请输入正整数',
				viewAll : '查看全部'
			},
			EN : {
				all : 'All',
				perPage : 'per page',
				of : 'of',
				save : 'save',
				cancel : 'cancel',
				please_input_a_positive_integer : 'please input a positive integer',
				viewAll : 'view all'
			}
		}
	$.DataList = function(elm,opts) {
		this.commonName = elm.attr('class').replace(/\s/g,'-') + "-child";
		this.$widthTool = $('<div class="'+this.commonName+'-dataList-tool-getWidth dataList-tool-getWidth"></div>');
		this.$heightTool = $('<div class="'+this.commonName+'-dataList-tool-getHeight dataList-tool-getHeight"></div>');
		var document = window.document;
		
		this.criteria = opts.criteria;
		if(!this.criteria.pageSize){
			this.criteria.pageSize = 10;
		}
		if(!this.criteria.pageNum){
			this.criteria.pageNum = 1;
		}
		if(!this.criteria.sortSign){
			this.criteria.sortSign = 'desc';
		}
		this.opts = $.extend({}, $.fn.DataList.defaults, opts);
		if(this.opts.hasAddedIds === null) {
			this.opts.hasAddedIds = [];
		}
		this.params = $.extend({}, $.fn.DataList.params);
		this.opts.language = this.opts.language.toUpperCase();
		this.criteria.sortName = this.opts.columns[0].sortName;
		if(!this.opts.url){
			throw new Error('The url cann\'t be empty');
		}
		if(this.opts.hasCheckbox && this.opts.hasAddIcon && this.opts.hasRadio){
			throw new Error('hasCheckbox, hasAddIcon and hasRadio can not be true All.');
		}
		
		$(['<div class="'+this.commonName+'"><div class="dataList-div-loader"></div></div>'
						,'<div class="'+this.commonName+'">'
							,'<div class="dataList-div-nineSqurt">'
								,'<div class="dataList-img-nineSqurt w-h-26"></div>'
				       		    ,'<div class="dataList-div-fields"></div>'
			       			,'</div>'
			       			,'<div class="clear"></div>'
			       		,'</div>'
				        ,'<div class="'+this.commonName+' dataList-div-data">'
				        	,'<div class="dataList-div-head"></div>'
				        	,'<div class="dataList-div-body-left-corner"></div>'
				        	,'<div class="dataList-div-body-right-corner"></div>'
				        	,'<div class="dataList-div-body"></div>'
			        		,'<div class="dataList-div-hrizon-slide" unselectable="on">'
			        			,'<div class="dataList-div-hrizon-slideDiv" unselectable="on"></div>'
			        		,'</div>'
			        		,'<div class="dataList-div-vertical-slide" unselectable="on">'
			        			,'<div class="dataList-div-vertical-slideDiv" unselectable="on"></div>'
			        		,'</div>'
				        ,'</div>'
				        ,'<div class="'+this.commonName+' dataList-div-pagination">'
				        	,'<div class="dataList-div-perPage"></div>'
				        	,'<div class="dataList-div-pageSize-wrapper"></div>'
				        	,'<div class="dataList-div-page"></div>'
				        	,'<div class="dataList-div-go"></div>'
				        ,'</div>'
				        ,'<div class="clear">'
				        ,'</div>'].join('')).appendTo(elm);
		this.$widthTool.appendTo(document.body);
		this.$heightTool.appendTo(document.body);
		this.$data = elm.find("."+this.commonName);
		this.$body = this.$data.find('div.dataList-div-body');
		this.$first = $('<span></span>').text(0);
		this.$last = this.$first.clone();
		this.$count = this.$first.clone();
		this.$page = this.$data.find('div.dataList-div-page');
		this.$head  = this.$data.find('div.dataList-div-head');
		this.$hrizon_slide = this.$data.find('div.dataList-div-hrizon-slideDiv');
		this.$vertical_slide = this.$data.find('div.dataList-div-vertical-slideDiv');
		this.$loader = this.$data.find('div.dataList-div-loader');
		this.$go = this.$data.find('div.dataList-div-go');
		this.init();
	}
	$.DataList.prototype = {
		init : function() {
			var $self = this;
			var $fields = this.$data.find('div.dataList-div-fields');
			var $head = this.$data.find('div.dataList-div-head');
			var $pagination = this.$data.find('div.dataList-div-pagination');
			var $perPage = this.$data.find('div.dataList-div-perPage');
			var $pageSize = this.$data.find('div.dataList-div-pageSize-wrapper');
			$(document).bind({
                'mousedown.mike' : function(e){
                    if(!$.contains($fields[0], e.target)){
                    	$fields.slideUp();
                    }
                }
            });
			if(!this.opts.hasPageSize){
				$pageSize.remove();
			}else{
				$(document).bind({
					'mousedown.mike' : function(e){
						if(!$.contains($('.dataList-div-pageSize',$self.$data)[0], e.target)){
							$('.dataList-a-pageSize',$self.$data).prev().slideUp();
						}
					}
				});
			}
			//int the div-data height
			if(this.opts.height){	
				$head.parent().css('height',!!this.opts.height ? this.opts.height + 30 : 'auto' );
			}else{
				this.$body.css('min-height', this.opts.minHeight);
			}
			//init the fields module.
			var fields = [];
			var all = 'dataList-checkbox-active';
			for(var i = 0; i < this.opts.columns.length; i++){
				var column = this.opts.columns[i];
				var clz = 'checkbox-' + this.opts.columns[i].EN.replace(/\s/g,'-');
				if(column.isMustShow){
					clz = clz + ' dataList-a-field-checkbox-active-disable';
				}else{
					clz = clz + ' dataList-a-field-checkbox'
					all = ' ';
				}
				fields.push('<div><a href="javascript:;" en="' + this.opts.columns[i].EN + '" index=' + i + ' class="' + clz + '"></a><span>' + column[this.opts.language] + '</span></div>');
			}
			fields.unshift('<div><a href="javascript:;"  class="dataList-a-field-checkbox_all ' + all + '"></a><span>' + I18N[this.opts.language].all + '</span></div>');
			fields.push('<a class="dataList-a-field-save" href="javascript:;">' + I18N[this.opts.language].save + '</a>');
			fields.push('<a class="dataList-a-field-cancel" href="javascript:;">' + I18N[this.opts.language].cancel + '</a>');
			$fields.html(fields.join(''));


			// //init the thing about column
			// because the setShow must to be invoke.
			// this.refreshColumn();

			//init the perPage
			$perPage.append(this.$first).append(' - ');
			$perPage.append(this.$last).append(' ' + I18N[this.opts.language].of + ' ');
			$perPage.append(this.$count);
			var $selectPageSize = $('<div class="dataList-div-pageSize">');
			$selectPageSize.css('top', -1 * (this.opts.pageSizes.length * 24 + 2) );
			for(var i = 0 ; i < this.opts.pageSizes.length ; i++){
				$selectPageSize.append($('<div>').text(this.opts.pageSizes[i]));
			}
			$pageSize.append($selectPageSize);
			$pageSize.append($('<a>',{'class':'dataList-a-pageSize w40-h24','href':'javascript:;'}).text(this.criteria.pageSize));
			$pageSize.append(' ' + I18N[this.opts.language].perPage + ' ');
			

			//init the go 
			this.$go.append($("<input>",{type:'text','class':'dataList-input-go w30-h24'}).keydown(function(){
				if(event.keyCode == 13) {
					var val =  $(this).val();
					var patrn=/^[0-9]*[1-9][0-9]*$/;
					if(patrn.test(val) && $self.$go.data('totalPage')){
						if(val < 1 || val > $self.$go.data('totalPage')){
							$(this).addClass('dataList-error');
						}else{
							$self.criteria.pageNum = val ; 
							$(this).removeClass('dataList-error').val('');
							$self.search();
						}
					}else{
						$(this).addClass('dataList-error');
					}
				}
			}));
			this.$go.append($("<a>",{href:'javascript:;','class':'dataList-button-go w-h-24'}).click(function(){
				var val =  $(this).prev().val();
				var patrn=/^[0-9]*[1-9][0-9]*$/;
				if(patrn.test(val) && $self.$go.data('totalPage')){
					if(val < 1 || val > $self.$go.data('totalPage')){
						$(this).prev().addClass('dataList-error');
					}else{
						$self.criteria.pageNum = val ; 
						$(this).prev().removeClass('dataList-error').val('');
						$self.search();
					}
				}else{
					$(this).prev().addClass('dataList-error');
				}
			}));
			setTimeout(function(){
				//add the events
				$('.dataList-img-nineSqurt',$self.$data).click(function(){
					var next = $(this).next();
					if(next.is(':visible')){
						$('.dataList-a-field-cancel',$self.$data).click();
					}else{
						var selectColumns = [];
						$(this).next().find('.dataList-a-field-checkbox').each(function(){
							if($(this).hasClass('dataList-checkbox-active'))
								selectColumns.push($(this).attr('en'));
						})
						next.data('selectColumns',selectColumns);
						next.slideDown();
					}
				});
				$('.dataList-a-field-cancel',$self.$data).click(function(){
					$(this).parent().slideUp();
					$('.dataList-a-field-checkbox',$self.$data).removeClass('dataList-checkbox-active');
					var selectColumns = $(this).parent().data('selectColumns');
					for(var i = 0 ; i < selectColumns.length ; i++){
						$('.checkbox-' + selectColumns[i]).addClass('dataList-checkbox-active');
					}
					$self._checkAllForNineSqurt();
				});
				$('.dataList-a-field-save',$self.$data).click(function(){
					$('.dataList-a-field-checkbox',$self.$data).each(function(){
						var show = false;
						if($(this).hasClass('dataList-checkbox-active')){
							show = true;
						}
						$self.opts.columns[$(this).attr('index')].isShow = show;
						if(show){
							$('.' + $self.opts.columns[$(this).attr('index')].EN.replace(/\s/g,'-')).show();
						}else{
							$('.' + $self.opts.columns[$(this).attr('index')].EN.replace(/\s/g,'-')).hide();
						}
						
					});
					$self.refreshColumn();
					$(this).parent().slideUp();
					if($self.opts.updateShowField.url && $self.opts.updateShowField.callback){
						var customization = [];
						$('.dataList-checkbox-active', $self.$data).each(function(){
							if(!$(this).hasClass('dataList-a-field-checkbox_all')) 
								customization.push($(this).attr('en'));
						});
						$.ajax({
							url : $self.opts.updateShowField.url,
							dataType : 'json',
							data : {
								'customization' : customization.join(',')
							},
							type : 'GET',
							success : function(data){
								$self.opts.updateShowField.callback(data);
							}
						});
					}
				});
				$('.dataList-a-field-checkbox_all',$self.$data).click(function(){
					if($(this).hasClass('dataList-checkbox-active')){
						$(this).add($('.dataList-a-field-checkbox',$self.$data)).removeClass('dataList-checkbox-active');
					}else{
						$(this).add($('.dataList-a-field-checkbox',$self.$data)).addClass('dataList-checkbox-active');
					}
				});
				$('.dataList-a-field-checkbox',$self.$data).click(function(){
					if($(this).hasClass('dataList-checkbox-active')){
						$(this).add($('.dataList-a-field-checkbox_all',$self.$data)).removeClass('dataList-checkbox-active');
					}else{
						$(this).addClass('dataList-checkbox-active');
						$self._checkAllForNineSqurt();
					}
				});
				$('.dataList-div-hrizon-slideDiv',$self.$data).mousedown(function(e){
					var _$self = $(this);
					var x = e.clientX;
					var y = e.clientY;
					var left = this.offsetLeft;
					$(document).bind({
						'mousemove.mike' : function(e){
							var _left = e.clientX - x + left;
							if(_left < 0){
								_left = 0;
							}
							if(_left + _$self.width() > _$self.parent().width() ){
								_left = _$self.parent().width() - _$self.width();
							}
	                     	_$self.css('left', _left);
	                     	$self.$body.add($head).css('margin-left', (-1) *  _left * $self.$head.width() / _$self.parent().width() );
						}
					});
	                $(document).bind({
	                	'mouseup.mike' : function(){
	                		$(document).unbind('mousemove.mike').unbind('mouseup.mike');
	                	}
	                });
            		return false;
				});
				if($self.opts.height){
					$('.dataList-div-vertical-slideDiv',$self.$data).mousedown(function(e){
						var _$self = $(this);
						var x = e.clientX;
						var y = e.clientY;
						var top = this.offsetTop;
						$(document).bind({
							'mousemove.mike' : function(e){
								var _top = e.clientY - y + top;
								if(_top < 0){
									_top = 0;
								}
								if( _top + _$self.height() > $self.opts.height ){
									_top = $self.opts.height - _$self.height();
								}
								_$self.css('top', _top);
								$self.$body.css('margin-top', (-1) *  _top * $self.$body.children('div').length * 30 / $self.opts.height );
							}
						});
						$(document).bind({
							'mouseup.mike' : function(){
								$(document).unbind('mousemove.mike').unbind('mouseup.mike');
							}
						});
            			return false;
					});
				}
				$('.dataList-a-pageSize',$self.$data).click(function(){
					var prev = $(this).prev();
					if(prev.is(':visible')){
						prev.fadeOut();
					}else{
						prev.fadeIn();
					}
				});
				$('.dataList-div-pageSize > div',$self.$data).click(function(){
					$(this).parent().next().text($(this).text());
					var pageSize = $(this).text();
					$self.criteria.pageSize = parseInt(pageSize);
					$self.criteria.pageNum = 1;
					$self.search();
					$(this).parent().hide();
					if($self.opts.updateShowSize.url && $self.opts.updateShowSize.callback){
						var data = {};
						data[$self.opts.backendFieldName + '.' + 'pageSize'] =  pageSize;
						$.ajax({
							url : $self.opts.updateShowSize.url,
							dataType : 'json',
							data : data ,
							type : 'GET',
							success : function(data){
								$self.opts.updateShowSize.callback(data)
							}
						});
					}
				});
				$self.$body.parent().hover(function(){
					if($self.params.vSlideShow){
						$(".dataList-div-vertical-slide",$self.$data).fadeIn();
					}
					if($self.params.hSlideShow){
						$(".dataList-div-hrizon-slide",$self.$data).fadeIn();
					}
				},function(){
					$(".dataList-div-vertical-slide",$self.$data).stop(true,true).fadeOut();
					$(".dataList-div-hrizon-slide",$self.$data).stop(true,true).fadeOut();
				});
			},200);
			
		},
		refreshColumn : function(){
			var _width = 60;
			var $self = this;
			//init the data head
			var heads = [];
			if(this.opts.hasStatus){
				heads.push('<div class="w-10"></div>');
				_width += 10;
			}
			if(this.opts.hasCheckbox){
				heads.push('<div class="dataList-div-checkbox-all w-30"></div><div class="w-30"></div>');
			}else if(this.opts.hasAddIcon){
				heads.push('<div class="w-30"></div><div class="w-30"></div>');
			}else if(this.opts.hasRadio){
			    heads.push('<div class="w-30 dataList-div-radio-header"></div><div class="w-30"></div>');
			}else{
				heads.push('<div class="w-30"></div>');
			}
			
			
			for(var i =0; i < this.opts.columns.length; i++){
				if(this.opts.columns[i].isMustShow || this.opts.columns[i].isShow){
					_width = _width + 2 + parseFloat(this.opts.columns[i].width);
				}
			}
			if(this.opts.hasAttachmentIcon){
				heads.push('<div class="w-30"></div>');
				_width += 30;
			}
			var _dataWidth = this.$body.parent().width();
			$self.params.avg = 0;
			if( _dataWidth > _width){
				var sum = 0;
				for(var i =0; i < this.opts.columns.length; i++){
					if(this.opts.columns[i].isMustShow || this.opts.columns[i].isShow){
						if(this.opts.columns[i].autoWidth){
							sum++;
						}
					}
				}
				if(!sum){
					sum = 1;
				}
				$self.params.avg = ( _dataWidth - _width ) / sum;
				_width = _dataWidth;
				$self.params.hSlideShow = false;
			}else{
				$self.params.hSlideShow = true;
				this.$hrizon_slide.width( this.$hrizon_slide.parent().width() * this.$body.parent().width() / _width ).css('left',0);
			}
			this.$head.add(this.$body).width(_width).css('margin-left',0);
			for(var i =0; i < this.opts.columns.length; i++){
				var sort = this.opts.columns[i].sortName ? '</span><span class="dataList-span-sort ' + this.opts.columns[i].sortName + '-sort"></span>' : '</span>';
				var pointer = this.opts.columns[i].sortName ? 'pointer' : 'default';
				if(this.opts.columns[i].isMustShow || this.opts.columns[i].isShow){
					heads.push('<div class="dataList-div-listLine w-2"></div>');
					if(this.opts.columns[i].autoWidth){
						heads.push('<div style="width:' + ( this.opts.columns[i].width + $self.params.avg ) + 'px;cursor:' + pointer + '"><span en="' + this.opts.columns[i].sortName + '">' + this.opts.columns[i][this.opts.language] + sort + '</div>');
						$('.' + this.opts.columns[i].EN, this.$data).width(this.opts.columns[i].width + $self.params.avg);
					}else{
						heads.push('<div style="width:' + this.opts.columns[i].width + 'px;cursor:' + pointer + '"><span en="' + this.opts.columns[i].sortName + '">' + this.opts.columns[i][this.opts.language] + sort + '</div>');
					}
					
				}else{
					heads.push('<div class="dataList-div-listLine w-2 hide"></div>');
					if(this.opts.columns[i].autoWidth){
						heads.push('<div class="hide" style="width:' + ( this.opts.columns[i].width + $self.params.avg ) + 'px;cursor:' + pointer + '"><span en="' + this.opts.columns[i].sortName + '">' + this.opts.columns[i][this.opts.language] + sort + '</div>');
					}else{
						heads.push('<div class="hide" style="width:' + this.opts.columns[i].width + 'px;cursor:' + pointer +  '"><span en="' + this.opts.columns[i].sortName + '">' + this.opts.columns[i][this.opts.language] + sort + '</div>');
					}
					
				}
			}
			this.$head.html(heads.join(''));
			$('.' + this.criteria.sortName + '-sort', $self.$data).parent().addClass('dataList-div-sort-style');
			$('.' + this.criteria.sortName + '-sort').addClass('dataList-desc');
			
			//init the all checkbox
			var all = true;
			var execute = false;
			$('.dataList-div-checkbox',$self.$data).each(function(){
				execute = true;
				if(!$(this).hasClass('dataList-checkbox-active')){
					return (all = false);
				}
			});
			if(all && execute){
				$('.dataList-div-checkbox-all',$self.$data).addClass('dataList-checkbox-active');
			}
			$('.dataList-span-sort',$self.$data).parent().click(function(){
				var sort = $(this).children(':eq(0)');
				$self.criteria.sortName = sort.attr('en');
				var sortImgSpan = $(this).children(':eq(1)');
				if(sortImgSpan.hasClass('dataList-desc')){
					$self.criteria.sortSign = 'asc';
					$('.dataList-span-sort',$self.$data).removeClass('dataList-desc').removeClass('dataList-asc');
					sortImgSpan.removeClass('dataList-desc').addClass('dataList-asc');
				}else{
					$self.criteria.sortSign = 'desc';
					$('.dataList-span-sort',$self.$data).removeClass('dataList-desc').removeClass('dataList-asc');
					sortImgSpan.removeClass('dataList-asc').addClass('dataList-desc');
				}
				$(this).addClass('dataList-div-sort-style').siblings().removeClass('dataList-div-sort-style');
				if($self.opts.sortHander){
					$self.opts.sortHander();
				}
				$self.criteria.pageNum = 1;
				$self.search();
			});
			$('.dataList-div-checkbox-all',$self.$data).click(function(){
				if($(this).hasClass('dataList-checkbox-active')){
					$(this).removeClass('dataList-checkbox-active');
					$('.dataList-div-checkbox',$self.$data).removeClass('dataList-checkbox-active');
				}else{
					$(this).addClass('dataList-checkbox-active');
					$('.dataList-div-checkbox',$self.$data).addClass('dataList-checkbox-active');
				}
			});
		},
		_checkAllForNineSqurt : function(){
			var all = true;
			$('.dataList-a-field-checkbox',this.$data).each(function(){
				if(!$(this).hasClass('dataList-checkbox-active')){
					all = false;
					return false;
				}
			});
			if(all){
				$('.dataList-a-field-checkbox_all',this.$data).addClass('dataList-checkbox-active');
			}else{
				$('.dataList-a-field-checkbox_all',this.$data).removeClass('dataList-checkbox-active');
			}
		},
		search : function() {
			var $self = this;
			$self.$loader.show();
			var data = {};
			var criteria = this.criteria ;
			if(!criteria.sortName){
				$('.dataList-div-sort-style').removeClass('dataList-div-sort-style');
			}
			for(var key in criteria){
				data[this.opts.backendFieldName + '.' + key] = criteria[key];
			}
			$('.dataList-input-go',$self.$data).removeClass('dataList-error').val('');
			$.ajax({
				type : 'GET',
				dataType : 'json',
				url: this.opts.url,
				data : data,
				success : function(data){
					$self.refresh(data);
				}
			});

		},
		refresh : function(data) {
			var $self =  this;
			//remove the select all row checkbox active status
			$('.dataList-div-checkbox-all',this.$data).removeClass('dataList-checkbox-active');
			$('.' + $self.criteria.sortName + '-sort', $self.$data).parent().addClass('dataList-div-sort-style');
			$('.dataList-span-sort', this.$data).removeClass('dataList-desc').removeClass('dataList-asc');
			$('.' + $self.criteria.sortName + '-sort').addClass('dataList-' + $self.criteria.sortSign);
			//destroy tooltip object
			$self.params.$tips.each(function(){
				$(this).poshytip("destroy");
			});
			$self.params.$tips = $();

			
			var pageNum = parseInt(this.criteria.pageNum);
			var pageSize = parseInt(this.criteria.pageSize);
			
			// this.$first.text(data.)
			if(data.count){
				this.$first.text((pageNum - 1) * pageSize + 1);
            	this.$last.text(pageNum * pageSize > data.count ? data.count : pageNum * pageSize);
			}else{
				this.$first.text(0);
            	this.$last.text(0);
			}
            this.$count.text(data.count);
            var bodys = [];
            
            for(var i = 0; i < data.fieldsData.length; i++){
            	var increment = 0;
            	bodys.push("<div>");
            	if(this.opts.hasStatus){
            		increment++;
					bodys.push('<div class="w-10 dataList-div-status-icon">' + data.fieldsData[i][0] + '</div>');
				}
				if(this.opts.hasAttachmentIcon){
					increment++;
				}
            	if(this.opts.hasCheckbox){
            		bodys.push('<div class="dataList-div-checkbox w-30" pk=' + data.fieldsData[i][0 + increment] + '></div><div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
            	}else if(this.opts.hasRadio){
                    bodys.push('<div class="dataList-div-radio w-30" pk=' + data.fieldsData[i][0 + increment] + '></div><div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
				}else if(this.opts.hasAddIcon){
            		if(!$self.opts.isRepeat) {
            			if($.inArray(parseInt(data.fieldsData[i][0]), this.opts.hasAddedIds) == -1){
            				bodys.push('<div class="dataList-div-addIcon w-30" pk=' + data.fieldsData[i][0] + '></div><div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
            			}else{
            				bodys.push('<div class="dataList-div-addedIcon w-30" pk=' + data.fieldsData[i][0] + '><span class="addIcon-hasSelected"></span></div><div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
            			}
            		} else {
            			if($.inArray(parseInt(data.fieldsData[i][0]), this.opts.hasAddedIds) == -1){
            				bodys.push('<div class="dataList-div-addIcon w-30" pk=' + data.fieldsData[i][0] + '></div><div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
            			}else{
            				bodys.push('<div class="dataList-div-addIcon w-30" pk=' + data.fieldsData[i][0] + '><span class="addIcon-hasSelected"></span></div><div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
            			}
            		}
            	}else{
            		bodys.push('<div class="w-30">' + ((pageNum - 1) * pageSize + 1 + i) + '</div>');
            	}
            	if(this.opts.hasAttachmentIcon){
            		bodys.push('<div class="dataList-attachment-icon w-30">' + data.fieldsData[i][0 + increment] + '</div>');
            	}
            	for(var j =0; j < data.fieldsData[i].length - 1 - increment; j++){
            		if(this.opts.columns[j].isMustShow || this.opts.columns[j].isShow){
            			if(this.opts.columns[j].autoWidth){
            				bodys.push('<div class="ml-2 ' + this.opts.columns[j].EN.replace(/\s/g,'-') + '" style="width:' + (this.opts.columns[j].width + $self.params.avg ) + 'px;text-align:' + (this.opts.columns[j].align || 'center') + '">' + this.opts.contentHandler(data.fieldsData[i][j + 1 + increment]) + '</div>');
            			}else{
            				bodys.push('<div class="ml-2 ' + this.opts.columns[j].EN.replace(/\s/g,'-') + '" style="width:' + this.opts.columns[j].width + 'px;text-align:' + (this.opts.columns[j].align || 'center') + '">' + this.opts.contentHandler(data.fieldsData[i][j + 1 + increment]) + '</div>');
            			}
            		}else{
            			if(this.opts.columns[j].autoWidth){
            				bodys.push('<div class="ml-2 hide ' + this.opts.columns[j].EN.replace(/\s/g,'-') + '" style="width:' + ( this.opts.columns[j].width + $self.params.avg) + 'px;text-align:' + (this.opts.columns[j].align || 'center') + '">' + this.opts.contentHandler(data.fieldsData[i][j + 1 + increment]) + '</div>');
            			}else{
            				bodys.push('<div class="ml-2 hide ' + this.opts.columns[j].EN.replace(/\s/g,'-') + '" style="width:' +  this.opts.columns[j].width + 'px;text-align:' + (this.opts.columns[j].align || 'center') + '">' + this.opts.contentHandler(data.fieldsData[i][j + 1 + increment]) + '</div>');
            			}
            		}
            	}
            	bodys.push('</div>');
            }
            this.$vertical_slide.css('top', 0 );
            this.$body.css('margin-top', 0).html(bodys.join(''));
			if(this.opts.height){
				//set the data body height
				$self.$body.height( this.opts.height );
				// init the slide div
				if( data.fieldsData.length * 30 <= this.opts.height ){
					$self.params.vSlideShow = false;
				}else{
					$self.params.vSlideShow = true;
					this.$vertical_slide.height( this.opts.height * this.opts.height / data.fieldsData.length / 30 ).parent().height(this.opts.height);
				}
			}
            this.$page.empty();
            // init the page links
            if(1 == pageNum){
                this.$page.append($("<a>", {'href':'javascript:;','class':'dataList-a-first-disable w-h-24'}))
                     .append($("<a>",{'href':'javascript:;','class':'dataList-a-previous-disable w-h-24'}));
            }else{
                this.$page.append($('<a>', {'href':'javascript:;','class':'dataList-a-first w-h-24'}).click(function(){
                    $self.criteria.pageNum  = 1;
                    $self.search();
                })).append($('<a>', {'href':'javascript:;','class':'dataList-a-previous w-h-24'}).click(function(){
                    $self.criteria.pageNum-- ;
                    $self.search();
                }));
            }
            var itemSize = parseInt( this.opts.pageItemSize / 2 );
            // default value
            var startIndex = 1 ;
            var endIndex = data.totalPage ;
            if( data.totalPage > this.opts.pageItemSize ){
            	if( pageNum <= itemSize){
            		startIndex = 1;
            		endIndex = this.opts.pageItemSize;
            	}else if ( data.totalPage - pageNum <= itemSize ){
            		endIndex = data.totalPage;
            		startIndex = data.totalPage - this.opts.pageItemSize + 1;
            	}else{
            		startIndex = pageNum - itemSize;
            		endIndex = pageNum + itemSize;
            	}
            }
            for(var i = startIndex ; i <= endIndex ; i++){
                if( i == pageNum){
                    this.$page.append($('<a>',{'href':'javascript:;','class':'dataList-a-page currentPage'}).text(i));
                }else{
                    this.$page.append($('<a>',{'href':'javascript:;','class':'dataList-a-page'}).text(i).click(function(){
                        $self.criteria.pageNum  = $(this).text();
                        $self.search();
                    }));
                }
            }
            if(data.totalPage < 1 || data.totalPage == pageNum){
                this.$page.append($('<a>',{'href':'javascript:;','class':'dataList-a-next-disable w-h-24'}))
                     .append($('<a>',{'href':'javascript:;','class':'dataList-a-last-disable w-h-24'}));
            }else{
                this.$page.append($('<a>',{'href':'javascript:;','class':'dataList-a-next w-h-24'}).click(function(){
                    $self.criteria.pageNum ++ ;
                    $self.search();
                })).append($('<a>',{'href':'javascript:;','class':'dataList-a-last w-h-24'}).click(function(){
                    $self.criteria.pageNum = data.totalPage ;
                    $self.search();
                }));
            }
            this.$loader.hide();
            $('.dataList-div-checkbox',$self.$data).click(function(){
				if($(this).hasClass('dataList-checkbox-active')){
					$(this).removeClass('dataList-checkbox-active');
					$('.dataList-div-checkbox-all',$self.$data).removeClass('dataList-checkbox-active');
				}else{
					$(this).addClass('dataList-checkbox-active');
					var all = true;
					$('.dataList-div-checkbox',$self.$data).each(function(){
						if(!$(this).hasClass('dataList-checkbox-active')){
							all = false;
							return false;
						}
					});
					if(all){
						$('.dataList-div-checkbox-all',$self.$data).addClass('dataList-checkbox-active');
					}
				}
			});
			$('.dataList-div-radio',$self.$data).click(function(){
                $('.dataList-div-radio',$self.$data).each(function(){
                    if ($(this).hasClass('dataList-radio-active')){
                        $(this).removeClass('dataList-radio-active');
                    }
                });
                $(this).addClass('dataList-radio-active');
                $('.dataList-div-radio-header', $self.$data).data('radio-pk',$(this).attr('pk'));
            });
			if(this.opts.hasAddIcon && this.opts.addIconHandler){
				$('.dataList-div-addIcon',$self.$data).click(function(){
					if($(this).hasClass('dataList-div-addIcon')){
						$self.opts.addIconHandler($(this).parent());
						if($self.opts.hasAddedIds != undefined){
							if($.inArray(parseInt($(this).attr('pk')), $self.opts.hasAddedIds) == -1) {
								$(this).append("<span class='addIcon-hasSelected'></span>");
								$self.opts.hasAddedIds.push(parseInt($(this).attr('pk')));
							}
						}
						if(!$self.opts.isRepeat)
							$(this).removeClass('dataList-div-addIcon').addClass('dataList-div-addedIcon');
					}
				})
			}
			$('.dataList-div-body > div',$self.$data).each(function(){
				$(this).find('div').each(function(){
					$self.$widthTool.text($(this).text());
					if($self.$widthTool.width() > $(this).width()){
						if($self.$widthTool.width() > $(this).width() - 18){
							$self.params.$tips = $self.params.$tips.add($(this));
						}
					}
				});
			});
			var arrEntities={'<':'&lt;','>':'&gt;','"':'&quot;','®':'&reg;','©':'&copy;'};//实体
			var regEntities=/[<>"®©]/g;
			$self.params.$tips.poshytip({
				className : 'tip-green',
				allowTipHover : true ,
				content : function(){
					//var title;
					var text = $(this).text().replace(regEntities,function(c){return arrEntities[c];});
					$self.$heightTool.text(text);
//					if($self.$heightTool.height() > 120 ){
//						title = '<div class="dataList-div-content">' +  text + '</div><div><a class="dataList-a-viewAll" href="javascript:;" onclick="$.DataList.viewAll(this);">[' + I18N[$self.opts.language].viewAll + ']<a></div>' ; 
//					}else{
//						title = '<div class="dataList-div-content">' +  text + '</div>' ; 
//					}
					while ($self.$heightTool.height() > 120) {
						$self.$heightTool.text($self.$heightTool.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
     			    };
     			   	return $self.$heightTool.text();
					//return title;
				}
			});
			this.$go.data('totalPage', data.totalPage);
		},
		setShow : function(str){
			var columns = this.opts.columns;
			var a = str.split(',');
			for(var i = 0 ; i < a.length ; i++){
				var checkbox = $('.checkbox-' + a[i].replace(/\s/g,'-'));
				if(!checkbox.hasClass('dataList-a-field-checkbox-active-disable')){
					$('.checkbox-' + a[i].replace(/\s/g,'-')).addClass('dataList-checkbox-active');
					for(var j = 0 ; j < columns.length ; j++){
						if(columns[j].EN == a[i]){
							columns[j].isShow = true;
						}
					}
				}
			}
			this._checkAllForNineSqurt();
			this.refreshColumn();
			$('.dataList-span-sort', this.$data).removeClass('dataList-desc').removeClass('dataList-asc');
			$('.' + this.criteria.sortName + '-sort', this.$data).parent().removeClass('dataList-div-sort-style');
		},
		getDeleteIds : function(){
			var ids = [];
			$('.dataList-div-checkbox',this.$data).each(function(){
				if($(this).hasClass('dataList-checkbox-active')){
					ids.push($(this).attr('pk'));
				}
			});
			return ids.join(',');
		},
		getRadioSelectedId : function(){
		    pk = $('.dataList-div-radio-header', this.$data).data('radio-pk');
		    return pk;
        },
		setHasAddedIds : function(arg){
			this.opts.hasAddedIds  = arg;
		},
		delIdFromHasAddedIds : function(arg){
			var ids = this.opts.hasAddedIds;
			for(var i = 0 ; i < ids.length ; i++){
				if(arg == ids[i]){
					ids.splice(i,1);
					break;
				}
			}
			$('div.dataList-div-addedIcon:[pk=' + arg + ']').removeClass('dataList-div-addedIcon').addClass('dataList-div-addIcon');
		},
		cleanAddIcon : function(arg){
			if($.inArray(parseInt(arg), this.opts.hasAddedIds) == -1) {
				alert('this id is not existed in hasAddedIds');
			} else {
				for(var i in this.opts.hasAddedIds) {
					if(this.opts.hasAddedIds[i] === parseInt(arg)) {
						delete this.opts.hasAddedIds[i];
					}
				}

				if($(".dataList-div-addIcon:[pk='"+arg+"']", this.$data)[0] != undefined){
					$(".dataList-div-addIcon:[pk='"+arg+"']", this.$data).find('.addIcon-hasSelected').remove();
				}
				if($(".dataList-div-addedIcon:[pk='"+arg+"']", this.$data)[0] != undefined){
					$(".dataList-div-addedIcon:[pk='"+arg+"']", this.$data).find('.addIcon-hasSelected').remove();
					$(".dataList-div-addedIcon:[pk='"+arg+"']", this.$data).removeClass('dataList-div-addedIcon').addClass('dataList-div-addIcon');
				}
						
			}
		},
		cleanAllAddIcon : function(){
			for(var i in this.opts.hasAddedIds) {
				if($(".dataList-div-addIcon:[pk='"+this.opts.hasAddedIds[i]+"']", this.$data)[0] != undefined){
					$(".dataList-div-addIcon:[pk='"+this.opts.hasAddedIds[i]+"']", this.$data).find('.addIcon-hasSelected').remove();
				}
				if($(".dataList-div-addedIcon:[pk='"+this.opts.hasAddedIds[i]+"']", this.$data)[0] != undefined){
					$(".dataList-div-addedIcon:[pk='"+this.opts.hasAddedIds[i]+"']", this.$data).find('.addIcon-hasSelected').remove();
					$(".dataList-div-addedIcon:[pk='"+this.opts.hasAddedIds[i]+"']", this.$data).removeClass('dataList-div-addedIcon').addClass('dataList-div-addIcon');
				}
				delete this.opts.hasAddedIds[i];
			}
		}
	}
	$.DataList.viewAll = function(self){
		var _self = $(self).parent().prev();
		var _height = _self.height();
		_self.height("auto").css({
			"overflow-y" : "auto"
		});
		var height = _self.height();
		var top = _self.parent().parent().offset().top;
		_self.parent().parent().css( 'top', top - height + _height + 16 );
		$(self).remove();
	}
	$.fn.DataList = function(opts){
		if(this.size() > 1){
			throw new Error('The selector result size should be one');
		}
		return new $.DataList(this,opts);
	};
	$.fn.DataList.defaults = {
        columns:[

        ],
        updateShowField : {
        	url : undefined,
        	callback : undefined
	    },
	    updateShowSize : {
        	url : undefined,
        	callback : undefined
	    },
	    contentHandler : function(str){
	    	return str;
	    },

	    backendFieldName : 'criteria',
	    hasPageSize : true,
        pageSizes : [],
        language : 'ZH',
        url : undefined,
        hasCheckbox : false,
		hasRadio: false,
        hasAddIcon : false,
        hasAddedIds : null,
        hasAttachmentIcon : false,
        hasStatus : false,
        addIconHandler : undefined,
        isRepeat : false,
        pageItemSize : 9,
        minHeight : 150,
        sortHander: undefined
        // height : 270
    };
	$.fn.DataList.params = {
			avg : 0,
		    hSlideShow : false,
		    vSlideShow : false,
		    $tips : $()
	};
})(jQuery,window)
