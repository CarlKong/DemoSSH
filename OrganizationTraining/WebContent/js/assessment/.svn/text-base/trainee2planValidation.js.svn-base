      function trainee2planCommentValidanguage(){
        /** It important if you use CommentAPI*/
        validanguage.settings.commentDelimiter = "/>";
        validanguage.settings.loadCommentAPI = true;
        validanguage.settings.focusOnerror = false;
        validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
        validanguage.settings.onerror = 'validanguage.addErrorTitle';
        validanguage.settings.errorListText = "<strong>"+"User-defined filed:"+"</strong>";
		validanguage.populate.call(validanguage);
    }
     
      function showTraineeToPlanValidateDiv($validateDiv) {
    		$validateDiv.css({visibility:"visible"});
    		$validateDiv.show();
    		$validateDiv.children(".validateStar").each(function(){
    			$(this).css("opacity",0);
    		});
    		var children = $validateDiv.children(".validateStar");
    		$(children[0]).animate({opacity:1},100,function(){
    			$(children[1]).animate({opacity:1},100,function(){
    				$(children[2]).animate({opacity:1},100,function(){
    					$(children[3]).animate({opacity:1},100,function(){
    						$(children[4]).animate({opacity:1},100);
    					});
    				});
    			});
    		});
    	}