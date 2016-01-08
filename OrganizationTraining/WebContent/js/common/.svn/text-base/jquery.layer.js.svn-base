function showLayer(obj){
        obj.fadeIn("fast");
        //获取页面文档的高度
        var docheight = $(document).height();
        var docwidth = $(document).width();
        //追加一个层，使背景变灰
        $("body").append("<div id='greybackground'></div>");
        $("#greybackground").css({"position":"absolute","z-index":"50"});
        $("#greybackground").css({"opacity":"0.25","height":docheight
        ,"width":docwidth,"background-color":"#555555"
        ,"left":"0px","top":"0px"});
        };
function closeLayer(obj){
    	obj.hide();
        //删除变灰的层
        $("#greybackground").remove();
    }