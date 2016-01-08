( function( $, undefined ) {
	$.scrollbar_parameter = {
		axis             : 'y',
		showOnHover      : true,
		wheelStep        : 50,
		drapScrollSize   : 12,
		scroll			 : true,
		lockscroll       : true,
		viewPortWidth    : 500,
		viewPortHeight	 : 200
	};
	
	$.fn.scrollbar = function(parameter){
		var options =  $.extend({}, $.scrollbar_parameter, parameter);
		if (this.length > 0) {
			return this.each(function(){
			$(this).data('scrollbarObject', new ScrollBar($(this), options));
		});
		}
	}
	$.fn.scrollbar_update = function(sScroll) {
		$(this).data('scrollbarObject').update(sScroll);
	}
	
	function ScrollBar($elm, options){
		if (!$elm.parent().is('.custom-scrollbar-overview')) {
			$elm.wrap($('<div></div>')
				.attr({'class':'custom-scrollbar-container'})
				.css({'width':options.viewPortWidth + options.drapScrollSize +'px',
					  'max-height' :options.viewPortHeight + 'px'
				})
				.append('<div class="custom-scrollbar-viewport">' + 
				'<div class="custom-scrollbar-overview"></div>'+
				'</div>')
				.append('<div class="custom-scrollbar-vertical custom-scrollbar"><div class="custom-scrollbar-thumb"></div></div>')
				.append('<div class="custom-scrollbar-horizontal custom-scrollbar"><div class="custom-scrollbar-thumb"></div></div>')
			);
		}
		$container = $elm.parent().parent().parent();
		var oSelf        = this;
        var oViewport    = {obj:$('.custom-scrollbar-viewport', $container)};
        var oContent     = {obj:$('.custom-scrollbar-overview', $container)};
        var oScrollbar   = options.axis === 'x' ?  {obj:$('.custom-scrollbar-horizontal', $container)}: {obj:$('.custom-scrollbar-vertical', $container)};
        var oThumb       = {obj:$( '.custom-scrollbar-thumb', oScrollbar.obj)};
        var sAxis        = options.axis === 'x';
        var sDirection   = sAxis ? 'left' : 'top';
        var sSize        = sAxis ? 'Width' : 'Height';
        var iScroll      = 0;
        var iPosition    = { start: 0, now: 0};
        var iMouse       = {};
		oViewport.obj.css({
			'width': options.viewPortWidth,
			'height' : options.viewPortHeight
		});
		
		function init() {
			init_scrollbar();
		    showOnHover();
            oSelf.update();
            initEvent();
            return oSelf;
		}
		
		function showOnHover(){
			if (options.showOnHover) {
				oScrollbar.obj.hide();
			    $container.hover(function(){
					oScrollbar.obj.stop(true,true).fadeIn();
				}, function(){
					oScrollbar.obj.stop(true,true).fadeOut();
					
				});
			}
		
		}
		
		function init_scrollbar(){
			if (!sAxis) {
				var paneWidth  = $elm.innerWidth();
				var originalSidePaddingTotal = (parseInt($elm.css('paddingLeft')) || 0) + (parseInt($elm.css('paddingRight')) || 0);
				oScrollbar.obj.css(
					{'left':options.viewPortWidth - options.drapScrollSize,
					'width':options.drapScrollSize
					});
				oThumb.obj.css({
				'width':options.drapScrollSize
				});
			}else {
				oScrollbar.obj.css(
					{'top':options.viewPortHeight + options.drapScrollSize / 2,
					 'height':options.drapScrollSize
					});
				oThumb.obj.css({
					'height':options.drapScrollSize
				});
			
			}
		}
		this.update = function(sScroll) {
			oViewport[options.axis]  = oViewport.obj[0]['offset'+sSize];
			oContent[options.axis]   = oContent.obj[0]['scroll'+ sSize];
			oContent.ratio           = oViewport[ options.axis ] / oContent[ options.axis ];
			oScrollbar[options.axis] = 	oViewport[options.axis];
			oThumb[options.axis]     = Math.min(oScrollbar[options.axis], Math.max( 0, (oScrollbar[ options.axis ] * oContent.ratio )));
			oScrollbar.obj.toggleClass('custom-scrollbar-disable', oContent.ratio >= 1);
			oScrollbar.ratio = oContent[ options.axis ] / oScrollbar[ options.axis ]
			iScroll = (sScroll === 'relative' && oContent.ratio <= 1) ? Math.min((oContent[options.axis] - oViewport[options.axis]), Math.max(0, iScroll)) : 0;
            iScroll = (sScroll === 'bottom' && oContent.ratio <= 1) ? (oContent[options.axis] - oViewport[options.axis] ) : isNaN(parseInt( sScroll, 10 ))
						? iScroll : parseInt(sScroll, 10);
			setSize();
		}
		
		function setSize() {
			 var sCssSize = sSize.toLowerCase();
			 oThumb.obj.css(sDirection, iScroll / oScrollbar.ratio);
             oContent.obj.css(sDirection, -iScroll);
             iMouse.start = oThumb.obj.offset()[sDirection];
			 oScrollbar.obj.css( sCssSize, oScrollbar[options.axis]);
             oThumb.obj.css(sCssSize, oThumb[options.axis]);
		}
		
		function initEvent(){
			oThumb.obj.bind( 'mousedown.scrollbar', start);
            oScrollbar.obj.bind( 'mouseup.scrollbar', drag);
			if (options.scroll && window.addEventListener) {
				$container[0].addEventListener( 'DOMMouseScroll', wheel, false );
                $container[0].addEventListener( 'mousewheel', wheel, false );
			}else if(options.scroll){
				$container[0].onmousewheel = wheel;
			}
		}
		
		function start(event) {
			$('body').addClass('custom-scrollbar-noSelect');
			var thumbTop = parseInt(oThumb.obj.css(sDirection), 10);
			iMouse.start    = sAxis ? event.pageX : event.pageY;
            iPosition.start = thumbTop;
			$(document).bind('mousemove.scrollbar', drag);
			$(document).bind('mouseup.scrollbar', end);
			oThumb.obj.bind('mouseup.scrollbar', end);
		}
		
		function drag(event){
			if (oContent.ratio < 1){
				 iPosition.now = Math.min((oScrollbar[options.axis] - oThumb[options.axis]), 
					Math.max(0,(iPosition.start + ((sAxis ? event.pageX : event.pageY ) - iMouse.start))));
				iScroll = iPosition.now * oScrollbar.ratio;
                oContent.obj.css(sDirection, -iScroll);
                oThumb.obj.css(sDirection, iPosition.now);
			}
		
		}
		
		function end(){
			$('body').removeClass('custom-scrollbar-noSelect');
            $(document).unbind('mousemove.scrollbar', drag);
            $(document).unbind('mouseup.scrollbar', end);
            oThumb.obj.unbind('mouseup.scrollbar', end);
		}
 
		function wheel(event){
			if (oContent.ratio < 1){
				var oEvent = event || window.event;
                var iDelta = oEvent.wheelDelta ? oEvent.wheelDelta / 120 : -oEvent.detail / 3;
				iScroll -= iDelta * options.wheelStep;
                iScroll = Math.min((oContent[options.axis] - oViewport[options.axis] ), Math.max(0, iScroll));
				oThumb.obj.css(sDirection, iScroll / oScrollbar.ratio);
                oContent.obj.css(sDirection, -iScroll);
				if (options.lockscroll || (iScroll !== ( oContent[ options.axis ] - oViewport[ options.axis ] ) && iScroll !== 0)) {
					oEvent = $.event.fix(oEvent);
                    oEvent.preventDefault();
				}
			}	
		}
		return init();
	}
	
}(jQuery));