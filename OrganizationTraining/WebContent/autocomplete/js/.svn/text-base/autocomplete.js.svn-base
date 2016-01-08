(function( $ , undefined ){
	
  var default_options = {
		  
		  //Set style
		  width:400,
		  height: 200,
		  dropdownWidth: 100,
		  dropdownHeight:null,
		  searchDelay: 100,
		  minChars: 1,
		  maxRows:null,
		  source:null,
		  // Display delete button settings
		  deleteText: "&times;",
		  // Tokenization settings
		  tokenLimit: null,
		  tokenDelimiter: ",",
		  preventDuplicates: true,
		  idPrefix: "token-input-",
		  animateDropdown: true,
		  tokenValue:"value",
		  // Formatters
		  resultsFormatter: function(item){ return "<li>" + item.label+ "</li>" ;},
		  tokenFormatter: function(item) { return "<li><p>" + item.label + "</p></li>" ;},
		  // Callbacks
		  onResult: null,
		  onAdd: null,
		  onDelete: null,
		  onReady: null,
		  theme: "facebook",
		  checkedTokenUrl:null,
		  checkedParm:"names",
		  messageShow:null,
		  queryParam:"queryKey",
		  method:"POST",
		  dataType:"json",
		  inputSplit:";",
		  inputValueIsValidation:true
  };
  
// Default classes to use when theming
  
  var default_classes = {
   
      tokenList: "token-input-list",
   
      token: "token-input-token",
   
      tokenDelete: "token-input-delete-token",
   
      selectedToken: "token-input-selected-token",
   
      highlightedToken: "token-input-highlighted-token",
   
      dropdown: "token-input-dropdown",
   
      dropdownItem: "token-input-dropdown-item",
   
      dropdownItem2: "token-input-dropdown-item2",
   
      selectedDropdownItem: "token-input-selected-dropdown-item",
   
      inputToken: "token-input-input-token"
   
  };
  
// Input box position "enum"
  
  var POSITION = {
   
      BEFORE: 0,
   
      AFTER: 1,
   
      END: 2
   
  };
   
   
   
  // Keys "enum"
   
  var KEY = {
   
      BACKSPACE: 8,
   
      TAB: 9,
   
      ENTER: 13,
   
      ESCAPE: 27,
   
      SPACE: 32,
   
      PAGE_UP: 33,
   
      PAGE_DOWN: 34,
   
      END: 35,
   
      HOME: 36,
   
      LEFT: 37,
   
      UP: 38,
   
      RIGHT: 39,
   
      DOWN: 40,
   
      NUMPAD_ENTER: 108,
   
      COMMA: 188
   
  };
 $.fn.autoComplete = function( options ){
	 var settings = $.extend({}, default_options, options);
	 this.each(function () {
         $(this).data("tokenInputObject", new $.TokenList(this, settings));
 
     });
	 return this.data("tokenInputObject");
	  
  };
  
  $.fn.getAutoCompleteInstance = function(){
		 return $(this).data("tokenInputObject");
	  }
	  
  
  // TokenList class for each input
  $.TokenList = function(input, settings) {
	  if ( $.isArray(settings.source) ) {
		  settings.local_data = _normalize(settings.source);
	  }else if ( typeof settings.source === 'string' ) {
		  settings.url = settings.source;
	  }else {
		  showMessage("Source invalid!");
	  }
	  if (settings.crossDomain === undefined && settings.url) {
          if(url.indexOf("://") === -1) {
              settings.crossDomain = false;
          } else {
              settings.crossDomain = (location.href.split(/\/+/g)[1] !== settings.url.split(/\/+/g)[1]);
          }
 
      }
	  
	// Build class names
	  if (settings.classes) {
	      // Use custom class names
	      settings.classes = $.extend({}, DEFAULT_CLASSES, settings.classes);
	    } else if (settings.theme) {
	        // Use theme-suffixed default class names
	        settings.classes = {};
	        $.each(default_classes, function(key, value) {
	            settings.classes[key] = value + "-" + settings.theme;
	        });
	    } else {
	        settings.classes = default_classes;
	    }
	  
	  var saved_tokens = [];
	  // Keep track of the number of tokens in the list
	  var token_count = 0;
	  // Basic cache to save on db hits
	  var cache = new $.TokenList.Cache();
	  // Keep track of the timeout, old vals
	  var timeout;
	  var input_val;
	  // Create a new text input an attach keyup events
	  var input_box = $("<input type=\"text\"  autocomplete=\"off\">")
	        .css({
	            outline: "none"
	        })
	        .attr("id", settings.idPrefix + input.id)
	        .blur(function () {
	        	if($(this).val() == '') {
	        		hide_dropdown();
	        		return false;
	        	}
	            addItems(this);
	        })
	        .bind("keyup keydown blur update", resize_input)
	        .keydown(function (event) {
	            var previous_token;
	            var next_token;
	 
	            switch(event.keyCode) {
	 
	                case KEY.LEFT:
	 
	                case KEY.RIGHT:
	 
	                case KEY.UP:
	 
	                case KEY.DOWN:
	 
	                    if(!$(this).val()) {
	 
	                        previous_token = input_token.prev();
	 
	                        next_token = input_token.next();
	 
	                        if((previous_token.length && previous_token.get(0) === selected_token) || (next_token.length && next_token.get(0) === selected_token)) {
	 
	                            // Check if there is a previous/next token and it is selected
	 
	                            if(event.keyCode === KEY.LEFT || event.keyCode === KEY.UP) {
	 
	                                deselect_token($(selected_token), POSITION.BEFORE);
	 
	                            } else {
	 
	                                deselect_token($(selected_token), POSITION.AFTER);
	 
	                            }
	 
	                        } else if((event.keyCode === KEY.LEFT || event.keyCode === KEY.UP) && previous_token.length) {
	 
	                            // We are moving left, select the previous token if it exists
	 
	                            select_token($(previous_token.get(0)));
	 
	                        } else if((event.keyCode === KEY.RIGHT || event.keyCode === KEY.DOWN) && next_token.length) {
	 
	                            // We are moving right, select the next token if it exists
	 
	                            select_token($(next_token.get(0)));
	 
	                        }
	 
	                    } else {
	 
	                        var dropdown_item = null;
	                        if(event.keyCode === KEY.DOWN || event.keyCode === KEY.RIGHT) {
	 
	                            dropdown_item = $(selected_dropdown_item).next();
	 
	                        } else {
	 
	                            dropdown_item = $(selected_dropdown_item).prev();
	 
	                        }
	 
	                        if(dropdown_item.length) {
	 
	                            select_dropdown_item(dropdown_item);
	 
	                        }
	 
	                        return false;
	 
	                    }
	 
	                    break;
	 
	 
	 
	                case KEY.BACKSPACE:
	 
	                    previous_token = input_token.prev();
	                    if(!$(this).val().length) {
	 
	                        if(selected_token) {
	 
	                            delete_token($(selected_token));
	 
	                            hidden_input.change();
	 
	                        } else if(previous_token.length) {
	 
	                            select_token($(previous_token.get(0)));
	 
	                        }
	                        hide_dropdown();
	                        return false;
	 
	                    } else if($(this).val().length === 1) {
	 
	                        hide_dropdown();
	 
	                    } else {
	 
	                        // set a timeout just long enough to let this function finish.
	                        setTimeout(function(){do_search();}, 5);
	 
	                    }
	 
	                    break;
	                case KEY.TAB:
	 
	                case KEY.ENTER:
	 
	                case KEY.NUMPAD_ENTER:
	 
	                case KEY.COMMA:
	 
	                  if(selected_dropdown_item) {
	 
	                    add_token($(selected_dropdown_item).data("tokeninput"));
	 
	                    hidden_input.change();
	 
	                    return false;
	 
	                  }
	 
	                  addItems(input_box);
	 
	                  break;
	 
	                case KEY.ESCAPE:
	 
	                  hide_dropdown();
	 
	                  return true;
	                  
	                default:
	 
	                    if(String.fromCharCode(event.which)) {
	 
	                        // set a timeout just long enough to let this function finish.
	 
	                        setTimeout(function(){do_search();}, 5);
	 
	                    }
	 
	                    break;
	            }
	 
	        }).keyup(function (event){
				 switch(event.keyCode) {
					case KEY.SPACE:
	                	 var value =  $(input_box).val();
	                	 if (value.indexOf(settings.inputSplit) != -1){
	                		 var index = value.lastIndexOf(settings.inputSplit);
	                		 value = value.substring(0,index+1);
	                		 checkedToken = add_string_items(value);
	                		 defaultCheckTokens(checkedToken);
	                	 }
	                	break;
	                	
					default:
						break;
				 
				 }				 
			
			});
	  //Need to check tokens
	  var notExistLabel = [];
	 
	  /*
		 * Assume all items have the right format
		 */
	  function _normalize( items ) {
		  if ( items.length && items[0].label && items[0].value) {
			  return items;
		  }
		  return $.map( items, function(item) {
			  if ( typeof item === 'string' ) {
				  return {
					  label : item,
					  value : item
				  };
			  }
			  return $.extend({
				  label : item.label || item.value,
				  value : item.value || item.label
			  },item);
		  });
	  }
	  
  //Keep a reference to the original input box
  var hidden_input = $(input)
                         .hide()
                         .val("")
                         .focus(function () {
 
                             input_box.focus();
 
                         })
                         .blur(function () {
 
                             input_box.blur();
 
                         });
  // Keep a reference to the selected token and dropdown item
  var selected_token = null;
  var selected_token_index = 0;
  var selected_dropdown_item = null;
  // The list to store the token items in
  
  var token_list = $("<ul />")
      .addClass(settings.classes.tokenList)
      .css({"width":settings.width+"px","max-height":settings.height+"px",
    	  "min-height":settings.height+"px"})
      .click(function (event) {
          var li = $(event.target).closest("li");
          if(li && li.get(0) && $.data(li.get(0), "tokeninput")) {
              toggle_select_token(li);
          } else {
              // Deselect selected token
              if(selected_token) {
                  deselect_token($(selected_token), POSITION.END);
              }
              // Focus input box
              input_box.focus();
          }
      })
      .mouseover(function (event) {
          var li = $(event.target).closest("li");
          if(li && selected_token !== this) {
              li.addClass(settings.classes.highlightedToken);
          }
      })
      .mouseout(function (event) {
          var li = $(event.target).closest("li");
          if(li && selected_token !== this) {
              li.removeClass(settings.classes.highlightedToken);
          }
      }).focusout(function(event){
    		  if(selected_token) {
                  deselect_token($(selected_token), POSITION.END);
              }
    	  })
      .insertBefore(hidden_input);
  
  var input_token = $("<li />")
  
  .addClass(settings.classes.inputToken)
 
  .appendTo(token_list)
 
  .append(input_box);
 
 
 
  // The list to store the dropdown items in
 
  var dropdown = $("<div>")
  	.addClass(settings.classes.dropdown)
  	.css({"width":settings.dropdownWidth+"px"})
  	.hide();
  if(settings.dropdownWidth) {
	  dropdown.css({"max-height":settings.dropdownHeight+"px"});
  }
  dropdown.appendTo("body");
  
  // Magic element to help us resize the text input
  var input_resizer = $("<tester/>")
  .insertAfter(input_box)
  .css({
      position: "absolute",
      top: -9999,
      left: -9999,
      width: "auto",
      fontSize: input_box.css("fontSize"),
      fontFamily: input_box.css("fontFamily"),
      fontWeight: input_box.css("fontWeight"),
      letterSpacing: input_box.css("letterSpacing"),
      whiteSpace: "nowrap"
  });
  
  // Pre-populate list if items exist
  
  hidden_input.val("");
 
  var li_data = settings.prePopulate || hidden_input.data("pre");
 
  if(settings.processPrePopulate && $.isFunction(settings.onResult)) {
 
      li_data = settings.onResult.call(hidden_input, li_data);
 
  }
 
  if(li_data && li_data.length) {
 
      $.each(li_data, function (index, value) {
 
          insert_token(value);
 
          checkTokenLimit();
 
      });
 
  }
  
  // Initialization is done
  
  if($.isFunction(settings.onReady)) {
 
      settings.onReady.call();
 
  }
 
  //Public functions
 
  this.clear = function() {
 
      token_list.children("li").each(function() {
 
          if ($(this).children("input").length === 0) {
 
              delete_token($(this));
 
          }
 
      });
 
  };
  
  this.add = function(item) {
	  
      add_token(item);
 
  }
 
 
 
  this.remove = function(item) {
 
      token_list.children("li").each(function() {
 
          if ($(this).children("input").length === 0) {
 
              var currToken = $(this).data("tokeninput");
 
              var match = true;
 
              for (var prop in item) {
 
                  if (item[prop] !== currToken[prop]) {
 
                      match = false;
 
                      break;
 
                  }
 
              }
 
              if (match) {
 
                  delete_token($(this));
 
              }
 
          }
 
      });
 
  };
 
  this.getTokens = function() {
 
 		return saved_tokens;
 
 	};
 
  this.getTokenListObject = function() {
 
  	 return token_list;
 
  };
  
  this.getCheckResult = function() {
	  var success = true;
	  token_list.children().each(function () {
          var current_token = $(this);
          if(current_token.attr("error")) {
        	  success = false;
        	 return false;
          }

      });
	  return success;
  }
  
   this.addItemsByString = function(items) {
		 add_string_items(items);
   }
  
  
  // Private functions
  
  /*
   * Add a item, argument :input dom object
   * */
  function addItems(input) {
    	 var value =  $(input).val();
    	 var checkedToken = add_string_items(value);
    	 if (!settings.inputValueIsValidation){
    		 return false;
    	 }
    	 defaultCheckTokens(checkedToken);
    }
  
  function defaultCheckTokens(checkedToken) {
	  if (!checkedToken || !checkedToken.length) {
 		 return false;
 	 }
	  if (settings.checkedTokenUrl) {
    		 $.ajax({
    			 type:"POST",
    			 url:settings.checkedTokenUrl,
    			 data:settings.checkedParm +"="+checkedToken.join(","),
    			 datatype:"html",
    			 success:function(text){
    			 	if(text) {
    			 		return false;
    			 	}
    				var notRightNames = text.split(settings.tokenDelimiter);
    				showErrorLabels(notRightNames); 
    			  },
    			 error : function(){
    				  var message = "Request server fail.";
    				  showMessage(message);	
    			 }
    		 }
    		 );
    	
    	return false;
	  }else if (settings.local_data) {
		  var allValues = $.map(settings.local_data, function (key) {
              	 return key.label;
              });
		  for (var i in checkedToken) {
			  if ($.inArray(checkedToken[i],allValues) == -1) {
				  notExistLabel.push(checkedToken[i]);
			  }
		  }
		 showErrorLabels(notExistLabel); 
		  
	  }else {
		  var message = "You don't provide the check token url.";
		  showMessage(message);
	  }
  }
  
  function showMessage(message){
	  if (settings.messageShow && $.isFunction(settings.messageShow)) {
		  settings.messageShow.call(this, message);
		  return false;
	  }
	  alert(message);
  }
  
  function showErrorLabels(results) {
	  token_list.children().each(function () {
          var current_token = $(this);
          var current_data = $.data(current_token.get(0), "tokeninput");
          if(current_data && $.inArray(current_data.label,results) != -1 ) {
        	  current_token.css("border","1px solid red");
        	  current_token.attr("error","true");
          }

      });
	  
  }
  
   /*
	* Add a item, argument :string
      * */
  function add_string_items(string){
	  string = $.trim(string);
	  var checkedToken = [];
	  if (string && string.indexOf(settings.inputSplit) != -1) {
		  var array = string.split(settings.inputSplit);
	     	 for (var i in array) {
	     		 var value = $.trim(array[i]);
	     		 if (value) {
	     			 add_token({'label':value,'value':value});
	     			 checkedToken.push(value);
	     		 }
	     	 }
	     	 
	      return checkedToken;
	      
	  }else if (string && dropdown.get(0).style.display == 'none') {
		  add_token({'label':string,'value':string});
		  checkedToken.push(string);
		  return checkedToken;
	  }else {
		  return null;
	  }
  }
  
  function checkTokenLimit() {
 
      if(settings.tokenLimit !== null && token_count >= settings.tokenLimit) {
 
          input_box.hide();
 
          hide_dropdown();
 
          return;
 
      }
 
  }
 
  function resize_input() {
 
      if(input_val === (input_val = input_box.val())) {return;}
 
      // Enter new content into resizer and resize input accordingly
 
      var escaped = input_val.replace(/&/g, '&amp;').replace(/\s/g,' ').replace(/</g, '&lt;').replace(/>/g, '&gt;');
      input_resizer.html(escaped);
      input_box.width(input_resizer.width() + 40);
 
  }
 
  function is_printable_character(keycode) {
	  
      return ((keycode >= 48 && keycode <= 90) ||     // 0-1a-z
 
              (keycode >= 96 && keycode <= 111) ||    // numpad 0-9 + - / * .
 
              (keycode >= 186 && keycode <= 192) ||   // ; = , - . / ^
 
              (keycode >= 219 && keycode <= 222));    // ( ) '
 
  }
  // Inner function to a token to the list
  
  function insert_token(item) {
 
      var this_token = settings.tokenFormatter(item);
 
      this_token = $(this_token)
 
        .addClass(settings.classes.token)
 
        .insertBefore(input_token);
 
      // The 'delete token' button
 
      $("<span>" + settings.deleteText + "</span>")
 
          .addClass(settings.classes.tokenDelete)
 
          .appendTo(this_token)
 
          .click(function () {
 
              delete_token($(this).parent());
 
              hidden_input.change();
 
              return false;
 
          });
 
      // Store data on the token
 
      var token_data = {"label": item.label};
 
      token_data["value"] = item.value;
 
      $.data(this_token.get(0), "tokeninput", item);
 
      // Save this token for duplicate checking
 
      saved_tokens = saved_tokens.slice(0,selected_token_index).concat([token_data]).concat(saved_tokens.slice(selected_token_index));
 
      selected_token_index++;
 
      // Update the hidden input
 
      update_hidden_input(saved_tokens, hidden_input);
 
      token_count += 1;
 
      // Check the token limit
 
      if(settings.tokenLimit !== null && token_count >= settings.tokenLimit) {
 
          input_box.hide();
 
          hide_dropdown();
 
      }
 
      return this_token;
  }
  
//Add a token to the token list based on user input
  
  function add_token (item) {
      var callback = settings.onAdd;
 
      // See if the token already exists and select it if we don't want duplicates
 
      if(token_count > 0 && settings.preventDuplicates) {
 
          var found_existing_token = null;
 
          token_list.children().each(function () {
 
              var existing_token = $(this);
 
              var existing_data = $.data(existing_token.get(0), "tokeninput");
              if(existing_data && existing_data.label === item.label) {
 
                  found_existing_token = existing_token;
 
                  return false;
 
              }
 
          });
 
          if(found_existing_token) {
 
              select_token(found_existing_token);
 
              input_box.focus();
 
              return;
 
          }
 
      }
      // Insert the new tokens
 
      if(settings.tokenLimit == null || token_count < settings.tokenLimit) {
 
          insert_token(item);
 
          checkTokenLimit();
 
      }
 
      // Clear input box
 
      input_box.val("");
 
      // Don't show the help dropdown, they've got the idea
 
      hide_dropdown();
      
      // Execute the onAdd callback if defined
 
      if($.isFunction(callback)) {
 
          callback.call(hidden_input,item);
 
      }
  }
  
  //Select a token in the token list
  
  function select_token (token) {
 
      token.addClass(settings.classes.selectedToken);
 
      selected_token = token.get(0);
      
      // Hide input box
 
      input_box.val("");
 
      // Hide dropdown if it is visible (eg if we clicked to select token)
 
      hide_dropdown();
 
  };
  
  // Deselect a token in the token list
  
  function deselect_token (token, position) {
 
      token.removeClass(settings.classes.selectedToken);
 
      selected_token = null;
      
      if(position === POSITION.BEFORE) {
 
          //input_token.insertBefore(token);
 
          selected_token_index--;
 
      } else if(position === POSITION.AFTER) {
 
          //input_token.insertAfter(token);
 
          selected_token_index++;
 
      } else {
 
          input_token.appendTo(token_list);
 
          selected_token_index = token_count;
 
      }
 
      // Show the input box and give it focus again
 
      input_box.focus();
 
  }
  
  //Toggle selection of a token in the token list
  
  function toggle_select_token(token) {
 
      var previous_selected_token = selected_token;
 
      if(selected_token) {
 
          deselect_token($(selected_token), POSITION.END);
 
      }
 
      if(previous_selected_token === token.get(0)) {
 
          deselect_token(token, POSITION.END);
 
      } else {
 
          select_token(token);
 
      }
 
  }
  
  //Delete a token from the token list
  
  function delete_token (token) {
 
      // Remove the id from the saved list
 
      var token_data = $.data(token.get(0), "tokeninput");
 
      var callback = settings.onDelete;
 
      var index = token.prevAll().length;
 
      if(index > selected_token_index) index--;
 
      // Delete the token
 
      token.remove();
 
      selected_token = null;
 
      // Show the input box and give it focus again
 
      input_box.focus();
 
      // Remove this token from the saved list
 
      saved_tokens = saved_tokens.slice(0,index).concat(saved_tokens.slice(index+1));
 
      if(index < selected_token_index) selected_token_index--;
 
      // Update the hidden input
 
      update_hidden_input(saved_tokens, hidden_input);
 
      token_count -= 1;
 
      if(settings.tokenLimit !== null) {
 
          input_box
 
              .show()
 
              .val("")
 
              .focus();
 
      }
 
      // Execute the onDelete callback if defined
 
      if($.isFunction(callback)) {
 
          callback.call(hidden_input,token_data);
 
      }
 
  }
 
  // Update the hidden input box value
 
  function update_hidden_input(saved_tokens, hidden_input) {
 
      var token_values = $.map(saved_tokens, function (el) {
 
          return el[settings.tokenValue];
 
      });
      hidden_input.val(token_values.join(settings.tokenDelimiter));
  }
 
  // Hide and clear the results dropdown
 
  function hide_dropdown () {
 
      dropdown.hide().empty();
 
      selected_dropdown_item = null;
 
  }
 
  function show_dropdown() {
 
      dropdown
          .css({
 
              position: "absolute",
 
              top: $(input_box).offset().top + $(input_box).outerHeight(),
 
              left: $(input_box).offset().left,
 
              zindex: 999
 
          })
          .show();
  	$( document ).bind( 'mousedown', function( event ) {
  			if ( event.target !== dropdown.get(0) ) {
  					hide_dropdown();
  					//$(input_box).val('');
  			}
  	});
  }
 
  function show_dropdown_searching () {
 
      if(settings.searchingText) {
 
          dropdown.html("<p>"+settings.searchingText+"</p>");
 
          show_dropdown();
 
      }
 
  }
 
  function show_dropdown_hint () {
 
      if(settings.hintText) {
 
          dropdown.html("<p>"+settings.hintText+"</p>");
 
          show_dropdown();
 
      }
 
  }
 
  // Highlight the query part of the search term
 
  function highlight_term(value, term) {
 
      return value.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + term + ")(?![^<>]*>)(?![^&;]+;)", "i"), "<b>$1</b>");
 
  }
  
 
  function find_value_and_highlight_term(template, value, term) {
 
      return template.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + value + ")(?![^<>]*>)(?![^&;]+;)", "g"), highlight_term(value, term));
 
  }
 
  // Populate the results dropdown with some results
 
  function populate_dropdown (query, results) {
      if(results && results.length) {
          dropdown.empty();
 
          var dropdown_ul = $("<ul>")
              .appendTo(dropdown)
              .mouseover(function (event) {
 
                  select_dropdown_item($(event.target).closest("li"));
 
              })
              .mousedown(function (event) {
 
                  add_token($(event.target).closest("li").data("tokeninput"));
 
                  hidden_input.change();
 
                  return false;
 
              })
              .hide();
 
          if (settings.maxRows) {
        	  var rowCount = results.length > settings.maxRows ? settings.maxRows : results.length;
        	  results = results.slice(0,rowCount);
          }
          $.each(results, function(index, value) {
 
              var this_li = settings.resultsFormatter(value);
 
              this_li = find_value_and_highlight_term(this_li ,value.label, query);            
 
              this_li = $(this_li).appendTo(dropdown_ul);
 
              if(index % 2) {
 
                  this_li.addClass(settings.classes.dropdownItem);
 
              } else {
 
                  this_li.addClass(settings.classes.dropdownItem2);
 
              }
 
              if(index === 0) {
 
                  select_dropdown_item(this_li);
 
              }
 
              $.data(this_li.get(0), "tokeninput", value);
 
          });
 
          show_dropdown();
 
          if(settings.animateDropdown) {
 
              dropdown_ul.slideDown("fast");
 
          } else {
 
              dropdown_ul.show();
 
          }
 
      } else {
 
    	  hide_dropdown();
 
      }
 
  }
 
  // Highlight an item in the results dropdown
 
  function select_dropdown_item (item) {
 
      if(item) {
 
          if(selected_dropdown_item) {
 
              deselect_dropdown_item($(selected_dropdown_item));
 
          }
 
          item.addClass(settings.classes.selectedDropdownItem);
 
          selected_dropdown_item = item.get(0);
 
      }
 
  }
 
  // Remove highlighting from an item in the results dropdown
 
  function deselect_dropdown_item (item) {
 
      item.removeClass(settings.classes.selectedDropdownItem);
 
      selected_dropdown_item = null;
 
  }
 
  // Do a search and show the "searching" dropdown if the input is longer
 
  // than settings.minChars
 
  function do_search() {
 
      var query = input_box.val().toLowerCase();
 
      if(query && query.length) {
 
          if(selected_token) {
 
              deselect_token($(selected_token), POSITION.AFTER);
 
          }
 
          if(query.length >= settings.minChars) {
 
             // show_dropdown_searching();
 
              clearTimeout(timeout);
 
              timeout = setTimeout(function(){
 
                  run_search(query);
 
              }, settings.searchDelay);
 
          } else {
 
              hide_dropdown();
 
          }
 
      }
 
  }
 
  // Do the actual search
 
  function run_search(query) {
 
      var cache_key = query + computeURL();
 
      var cached_results = cache.get(cache_key);
 
      if(cached_results) {
 
          populate_dropdown(query, cached_results);
 
      } else {
 
          // Are we doing an ajax search or local data search?
 
          if(settings.url) {
 
              var url = computeURL();
 
              // Extract exisiting get params
 
              var ajax_params = {};
 
              ajax_params.data = {};
 
              if(url.indexOf("?") > -1) {
 
                  var parts = url.split("?");
 
                  ajax_params.url = parts[0];
 
                  var param_array = parts[1].split("&");
 
                  $.each(param_array, function (index, value) {
 
                      var kv = value.split("=");
 
                      ajax_params.data[kv[0]] = kv[1];
 
                  });
 
              } else {
 
                  ajax_params.url = url;
 
              }
 
              // Prepare the request
              ajax_params.data[settings.queryParam] = query;
 
              ajax_params.type = settings.method;
 
              ajax_params.dataType = settings.dataType;
 
              if(settings.crossDomain) {
 
                  ajax_params.dataType = "jsonp";
 
              }
 
              // Attach the success callback
 
              ajax_params.success = function(results) {
 
                if($.isFunction(settings.onResult)) {
 
                    results = settings.onResult.call(hidden_input, results);
 
                }
                normalize(results);
 
                cache.add(cache_key, settings.jsonContainer ? results[settings.jsonContainer] : results);
 
                // only populate the dropdown if the results are associated with the active search query
 
                if(input_box.val().toLowerCase() === query) {
 
                    populate_dropdown(query, settings.jsonContainer ? results[settings.jsonContainer] : results);
 
                }
 
              };
 
              // Make the request
 
              $.ajax(ajax_params);
 
          } else if(settings.local_data) {
 
              // Do the search through local data
        	  
          	 var matcher = new RegExp( '^'+escapeRegex(query), "i" );
              var results = $.grep(settings.local_data, function (key) {
            	  
              	 return matcher.test(key.label.toLowerCase());
 
              });
 
              if($.isFunction(settings.onResult)) {
 
                  results = settings.onResult.call(hidden_input, results);
 
              }
              cache.add(cache_key, results);
 
              populate_dropdown(query, results);
 
          }
 
      }
 
  }
 
  // compute the dynamic URL
 
  function computeURL() {
 
      var url = settings.url;
 
      if(typeof settings.url == 'function') {
 
          url = settings.url.call();
 
      }
 
      return url;
 
  }
 
  function escapeRegex( value ) {
	  return value.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
 
	}
};
 
//Really basic cache for the results
 
$.TokenList.Cache = function (options) {
 
  var settings = $.extend({
 
      max_size: 500
 
  }, options);
 
  var data = {};
 
  var size = 0;
 
  var flush = function () {
 
      data = {};
 
      size = 0;
 
  };
 
  this.add = function (query, results) {
 
      if(size > settings.max_size) {
 
          flush();
 
      }
 
      if(!data[query]) {
 
          size += 1;
 
      }
 
      data[query] = results;
 
  };
 
  this.get = function (query) {
 
      return data[query];
 
  };
 
};
}( jQuery ));