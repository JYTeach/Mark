document.getElementsByClassName("banner")[0].style.height=document.documentElement.clientWidth/1.875+'px';
document.getElementsByClassName("fill")[0].style.height=document.documentElement.clientWidth/1.875+'px';
var IMGS=[];
//电影图片默认占位问题问题
    function initImg(){
        var objs = document.getElementsByTagName("img");
        for(var i=0;i<objs.length;i++)
        {
        		if(objs[i].getAttribute('data-ke-src')){
        			//获取图片高度和宽度
        				IMGS.push(objs[i].getAttribute('data-ke-src'));
                    var height = objs[i].getAttribute('height');
                    var width = objs[i].getAttribute('width');
                    if(objs[i].style.width!=""){
                    		width = objs[i].style.width.replace('px','');
                    }
                    if(objs[i].style.height!=""){
                    		height = objs[i].style.height.replace('px','');
                    }

                    var className = objs[i].getAttribute('class');
                    if(!className){
                    		className = ';'
                    }
                		if(className.indexOf('scrollLoading')===-1){
                			//不是图片scrollLoading
	                		if(width>(document.documentElement.clientWidth-30)){
	                			height = ((document.documentElement.clientWidth-30)*height/width)+'px';
	                			width = '100%';
	                		}
	                		objs[i].style.height=height ? height:(document.documentElement.clientWidth-30)/2+'px';
	                		objs[i].style.width=width ? width:'100%';
                		}
                		objs[i].onclick=function(){
            				var json={};
            		        json.clicktype='stage_photo';
            		        json.img_url=this.getAttribute('data-ke-src');
            		        json.urls=IMGS;
            		        imgtext = JSON.stringify(json);
            		        JSInterface.click(imgtext);
            			}
        		}

        }
    }
    initImg();

    function jsonToString(json){
        var jStr = '{';

        for(var item in json){
            jStr +=  '"'+item+'":';

            if (typeof json[item] == 'number') {
                jStr += json[item]+',';
            }else{
                jStr += '"'+json[item]+'",';
            }
        }
        jStr=jStr.substr(0,jStr.length-1);
        jStr += '}';
        return jStr;
    }

    function stopClickPop(){
		e = window.event;
	    if (e.stopPropagation) {
	        e.stopPropagation();
	    } else {
	        e.cancelBubble = true;
	    }
    }

    function hasClass( elements,cName ){
        return !!elements.className.match( new RegExp( "(\\s|^)" + cName + "(\\s|$)") );
    };
    function addClass( elements,cName ){
        if( !hasClass( elements,cName ) ){
            elements.className += " " + cName;
        };
    };
    function removeClass( elements,cName ){
        if( hasClass( elements,cName ) ){
            elements.className = elements.className.replace( new RegExp( "(\\s|^)" + cName + "(\\s|$)" ), " " );
        };
    };

    var scrollLoad = (function (options) {
        var defaults = (arguments.length == 0) ? { src: 'data-ke-src', time: 300} : { src: options.src || 'data-ke-src', time: options.time ||300};
        var camelize = function (s) {
            return s.replace(/-(\w)/g, function (strMatch, p1) {
                return p1.toUpperCase();
            });
        };
        this.getStyle = function (element, property) {
            if (arguments.length != 2) return false;
            var value = element.style[camelize(property)];
            if (!value) {
                if (document.defaultView && document.defaultView.getComputedStyle) {
                    var css = document.defaultView.getComputedStyle(element, null);
                    value = css ? css.getPropertyValue(property) : null;
                } else if (element.currentStyle) {
                    value = element.currentStyle[camelize(property)];
                }
            }
            return value == 'auto' ? '' : value;
        };
        var _init = function () {
            var offsetPage = window.pageYOffset ? window.pageYOffset : window.document.documentElement.scrollTop,
                offsetwindow = offsetPage + Number(window.innerHeight ? window.innerHeight : document.documentElement.clientHeight),
                docImg = document.images,
                _len = docImg.length;
            if (!_len) return false;
            for (var i = 0; i < _len; i++) {
                var attrSrc = docImg[i].getAttribute(defaults.src),
                    o = docImg[i], tag = o.nodeName.toLowerCase();
                if (o) {
                    postPage = o.getBoundingClientRect().top + window.document.documentElement.scrollTop + window.document.body.scrollTop; postwindow = postPage + Number(this.getStyle(o, 'height').replace('px', ''));
                    if ((postPage > offsetPage && postPage < offsetwindow) || (postwindow > offsetPage && postwindow < offsetwindow)) {
                        if (tag === "img" && attrSrc !== null) {
                           o.setAttribute("src", attrSrc);
                        }
                        o = null;
                    }
                }
            };
            window.onscroll = function () {
                setTimeout(function () {
                    _init();
                }, defaults.time);
            }
        };
        return _init();
    });
     scrollLoad({
        src:'data-ke-src', //字符串型
        time: 1       //数字型
    })
    //a标签替换
    function replaceHref(){
        var objs = document.getElementsByTagName('a');
        for(var i=0;i<objs.length;i++){
            var url = objs[i].getAttribute('href');
            if (url) {
                objs[i].setAttribute('href',"javascript:hrefClick('"+url+"');");
            };
        }
    }
    replaceHref();

    function imgClick(movie_id){
        var json = getmoviestr(movie_id);
        json.clicktype='img';
        movietext = jsonToString(json);
        window.JSInterface.click(movietext);
    }

    function getmoviestr(movie_id){
    		var json = MOVIES;
        for(var item in json){
        		if(json[item].movie_id == movie_id){
        			return json[item];
        		}
        }
    }

    function btnLeftClick(movie_id){
    	    stopClickPop();
        var json = getmoviestr(movie_id);
        json.clicktype='leftbtn';
        movietext = jsonToString(json);
        window.JSInterface.click(movietext);

    }

    function btnRightClick(movie_id){
    		stopClickPop();
        var json = getmoviestr(movie_id);
        json.clicktype='rightbtn';
        movietext = jsonToString(json);
        window.JSInterface.click(movietext);
    }

    function hrefClick(url){
        var hreftext = '{"url":"'+url+'","clicktype":"href"}';
        window.JSInterface.click(hreftext);
    }

  	//设置电影按钮的状态
    function setMovieStatus(id,type){

    		var objs = document.getElementsByClassName("left");
    		var length = objs.length;
    		for(var i=0; i<length; i++){
    			var btn = objs[i];
    			//console.log(btn);
    			if(btn.getAttribute("id")=="leftbtn"+id){
    				var parentobj = btn.parentNode;
    		        var lineobjs = parentobj.getElementsByClassName('line');

    		        if (lineobjs.length>0) {
    		        		var lineobj = parentobj.getElementsByClassName('line')[0];
    		        		lineobj.parentNode.removeChild(lineobj);
    		        };

    		        removeClass(parentobj,'notadded');
    		        removeClass(parentobj,'added');
    		  		switch(type){
    		  		case -1:{
    		                btn.innerHTML="想看";
    		                //parentobj.setAttribute('class','btn-movie notadded');
    		                addClass(parentobj,'notadded');
    		                var line = document.createElement("span");
    		                line.setAttribute('class','line');
    		                btn.parentNode.insertBefore(line,btn.nextSibling);
    		            }break;//未添加
    		  		case 0:{
    		                btn.innerHTML='<span class="icon icon-ok ok">&#xe800;</span>想看';
    		                //parentobj.setAttribute('class','btn-movie added');
    		                addClass(parentobj,'added');
    		            }break;//想看
    		  		case 1:{
    		                btn.innerHTML='<span class="icon icon-ok ok">&#xe800;</span>已看';
    		                //parentobj.setAttribute('class','btn-movie added');
    		                addClass(parentobj,'added');
    		            }break;//已看
    		  		default:return 0;
    		  		}

    		  		var movie_id = id;
    		        for(var item in MOVIES){
    		        		if(MOVIES[item].movie_id == movie_id){
    		        			MOVIES[item].is_done=type;
    		        			break;
    		        		}
    		        }
    			}
        }
    		return 1;
    }

    function jsonToString(json){
        var jStr = '{';

        for(var item in json){
            jStr +=  '"'+item+'":';

            if (typeof json[item] == 'number') {
                jStr += json[item]+',';
            }else{
                jStr += '"'+json[item]+'",';
            }
        }
        jStr=jStr.substr(0,jStr.length-1);
        jStr += '}';
        return jStr;
    }