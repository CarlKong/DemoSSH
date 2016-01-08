 (function( $ , undefined ){
  var default_options = {
		  //Set style
		  width:400,
		  height: 200,
		  // Display delete button settings
		  deleteText: "&times;",
		  // Tokenization settings
		  tokenLimit: 5,
		  tokenDelimiter: ";",
		  preventDuplicates: true,
		  idPrefix: "token-input-",
		  tokenValue:"value",
		  tokenFormatter: function(item) { return "<li><p style='height:30px;'>" + item.label + "</p></li>" ;},
		  // Callbacks
		  onAdd: null,
		  onDelete: null,
		  onGtTokenLimit:null,
		  theme: "tag",
		  splitChar:";"
  };
  
// Default classes to use when theming
  
  var default_classes = {
      tokenList: "token-input-list",
      token: "token-input-token",
      tokenDelete: "token-input-delete-token",
      selectedToken: "token-input-selected-token",
      highlightedToken: "token-input-highlighted-token",
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
 $.fn.tagToken = function( options ){
	 var settings = $.extend({}, default_options, options);
	 this.each(function () {
         $(this).data("tokenInputObject", new $.TokenList(this, settings));
     });
	 return this.data("tokenInputObject");
  };
  
  $.fn.getTagTokenInstance = function(){
		 return $(this).data("tokenInputObject");
  };
  
  // TokenList class for each input
  $.TokenList = function(input, settings) {
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
	  var input_val;
	  // Create a new text input an attach keyup events
	  var input_box = $("<input type=\"text\">")
	        .css({
	            outline: "none"
	        })
	        .attr("id", settings.idPrefix + input.id)
	        .blur(function () {
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
	                        return false;
	                    }	 
	                    break;
	                case KEY.TAB:
	                case KEY.ENTER:
	                case KEY.NUMPAD_ENTER:
	                case KEY.COMMA:
	                  addItems(input_box);
	                  break;
	            }
	 
	        }).keyup(function (event){
				 switch(event.keyCode) {
					case KEY.SPACE:
	                	 var value =  $(input_box).val();
	                	 if (value.indexOf(settings.splitChar) != -1){
	                		 var index = value.lastIndexOf(settings.splitChar);
	                		 value = value.substring(0,index+1);
	                		 add_string_items(value);
	                	 }
	                	break;
				 
				 }				 
			
			});
	  
  //Keep a reference to the original input box
  var hidden_input = $(input).hide().val("")
                         .focus(function () {
                             input_box.focus();
                         })
                         .blur(function () {
                             input_box.blur();
                         });
  // Keep a reference to the selected token and dropdown item
  var selected_token = null;
  var selected_token_index = 0;
  // The list to store the token items in
  
  var token_list = $("<ul/>")
      .addClass(settings.classes.tokenList)
      .css({"width":settings.width+"px","max-height":settings.height+"px",
    	  "min-height":settings.height+"px"})
      .click(function (event) {
          var li = $(event.target).closest("li");
          if(li && li.get(0) && $.data(li.get(0), "tokeninput")) {
              toggle_select_token(li);
          } else {
              // Focus input box
              input_box.focus();
          }
      })
      .insertBefore(hidden_input);
  
  var input_token = $("<li />")
  .addClass(settings.classes.inputToken)
  .appendTo(token_list)
  .append(input_box);
 
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
  
  //Public functions
  this.addStringItems = function(string) {
	  checkTokenLimit();
	  add_string_items(string);
  };
  
  this.clear = function() {
      token_list.children("li").each(function() {
          if ($(this).children("input").length === 0) {
              delete_token($(this));
          }
      });
  };
  this.add = function(item) {
      add_token(item);
  };
  
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
  
  // Private functions
  function checkTokenLimit() {
      if(settings.tokenLimit !== null && token_count >= settings.tokenLimit) {
    	  if (settings.onGtTokenLimit && $.isFunction(settings.onGtTokenLimit)) {
    		  settings.onGtTokenLimit.call(this,settings.tokenLimit);
    	  }
          input_box.hide();
          return;
      }
  }
 
  /*
   * Add a item, argument :input dom object
   * */
  function add_string_items(string){
	  string = $.trim(string);
	  if (string && string.indexOf(settings.splitChar) != -1) {
		  var array = string.split(settings.splitChar);
	     	 for (var i in array) {
	     		 var value = $.trim(array[i]);
	     		 if (value) {
	     			 add_token({'label':value,'value':value});
	     		 }
	     	 } 
	  }else if (string) {
		  add_token({'label':string,'value':string}); 
	  }
  }
  function addItems(input) {
    	 var value =  $(input).val();
    	 add_string_items(value);
    	 $(input).val("");
    }
  
  function resize_input() {
      if(input_val === (input_val = input_box.val())) {return;}
      // Enter new content into resizer and resize input accordingly
      var escaped = input_val.replace(/&/g, '&amp;').replace(/\s/g,' ').replace(/</g, '&lt;').replace(/>/g, '&gt;');
      input_resizer.html(escaped);
      input_box.width(input_resizer.width() + 30);
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
              $(document).bind('mousedown',function(){
            	  token_list.children().each(function () {
                      $(this).removeClass(settings.classes.selectedToken);
                  });
              });
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
  }
  
  //Select a token in the token list
  function select_token (token) {
      token.addClass(settings.classes.selectedToken);
      selected_token = token.get(0);
      // Hide input box
      input_box.val("");
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
          input_box.show().val("").focus();
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
};
 
}( jQuery ));