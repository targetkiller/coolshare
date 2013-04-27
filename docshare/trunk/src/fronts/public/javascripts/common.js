if (typeof SUDOCN == "undefined" || !SUDOCN) {
    var SUDOCN = {};
}
SUDOCN.namespace = function(){
    var currentNS=null,levels;
    for (var i=0; i<arguments.length; ++i) {
        levels=(""+arguments[i]).split(".");
        currentNS=SUDOCN;
        for(var j = (levels[0] == "SUDOCN") ? 1 : 0 ; j<levels.length; ++j){ 
            currentNS[levels[j]] = currentNS[levels[j]] || {}; 
            currentNS = currentNS[levels[j]]; 
        } 
    }
    return currentNS; 
};
SUDOCN.namespace("common");
SUDOCN.namespace("orgcenter");
SUDOCN.namespace("usercenter");
SUDOCN.namespace("userIndex");
SUDOCN.common={
    modalLoadContent:function(url,id){
        $('#'+id).find('.modal-body').load(url);
        $('#'+id).modal('show');
    },
    copyText:function(text){
        if (window.clipboardData) {
            window.clipboardData.setData("Text",text);
            alert("复制成功！");
        } else {
            window.prompt("Sorry，您的浏览器暂时不支持自动复制，请按(Ctrl+C)复制",text)
        }
    },
    selectText:function(input){
        var text = input.createTextRange();
        text.moveStart("character",0);
        text.moveStart("character",input.value.length);
        text.select();
    },
    addCssByLink:function(url){
        var doc=document;
        var link=doc.createElement("link");
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", url);
        var heads = doc.getElementsByTagName("head");
        if(heads.length)
            heads[0].appendChild(link);
        else
            doc.documentElement.appendChild(link);
    },
    addJsByLink:function (url){
        var doc=document;
        var link=doc.createElement("script");
        link.setAttribute("type", "text/javascript");
        link.setAttribute("src", url);
        var heads = doc.getElementsByTagName("body");
        if(heads.length)
            heads[0].appendChild(link);
        else
            doc.documentElement.appendChild(link);
    },
    initSudoHelp:function(){
        if($(".sudocn-help").length>0){
            SUDOCN.common.addCssByLink('http://cdn.jingkao.net/feedback/public/stylesheets/sudocn-help.less');
            SUDOCN.common.addJsByLink('http://cdn.jingkao.net/feedback/public/javascripts/sudocn-help.js');
            setTimeout(function (){
                if($.sudocnHelp)$.sudocnHelp.init({});
            }, 1000);
        }
    },
    fixSubNav:function(){
        var $win = $(window)
        , $nav = $('.subnav')
        , navTop = $('.subnav').length && $('.subnav').offset().top
        , isFixed = 0
        processScroll();
        $win.on('scroll', processScroll)
        function processScroll() {
            var i, scrollTop = $win.scrollTop()
            if (scrollTop >= navTop && !isFixed) {
                isFixed = 1
                $nav.addClass('subnav-fixed')
            } else if (scrollTop <= navTop && isFixed) {
                isFixed = 0
                $nav.removeClass('subnav-fixed')
            }
        } 
    } ,
    scrolltotop:{
        //startline: Integer. Number of pixels from top of doc scrollbar is scrolled before showing control
        //scrollto: Keyword (Integer, or "Scroll_to_Element_ID"). How far to scroll document up when control is clicked on (0=top).
        setting: {
            startline:1, 
            scrollto: 0, 
            scrollduration:1000, 
            fadeduration:[500, 100]
        },
        controlHTML: '<img src="http://www.jingkao.net/public/images/gotop.gif" style="width:33px; height:35px" />',
        controlattrs: {
            offsetx:15, 
            offsety:80
        }, //offset of control relative to right/ bottom of window corner
        anchorkeyword: '#top', //Enter href value of HTML anchors on the page that should also act as "Scroll Up" links
        state: {
            isvisible:false, 
            shouldvisible:false
        },

        scrollup:function(){
            if (!this.cssfixedsupport) //if control is positioned using JavaScript
                this.$control.css({
                    opacity:0
                }) //hide control immediately after clicking it
            var dest=isNaN(this.setting.scrollto)? this.setting.scrollto : parseInt(this.setting.scrollto)
            if (typeof dest=="string" && jQuery('#'+dest).length==1) //check element set by string exists
                dest=jQuery('#'+dest).offset().top
            else
                dest=0
            this.$body.animate({
                scrollTop: dest
            }, this.setting.scrollduration);
        },

        keepfixed:function(){
            var $window=jQuery(window)
            var controlx=$window.scrollLeft() + $window.width() - this.$control.width() - this.controlattrs.offsetx
            var controly=$window.scrollTop() + $window.height() - this.$control.height() - this.controlattrs.offsety
            this.$control.css({
                left:controlx+'px', 
                top:controly+'px'
            })
        },

        togglecontrol:function(){
            var scrolltop=jQuery(window).scrollTop()
            if (!this.cssfixedsupport)
                this.keepfixed()
            this.state.shouldvisible=(scrolltop>=this.setting.startline)? true : false
            if (this.state.shouldvisible && !this.state.isvisible){
                this.$control.stop().animate({
                    opacity:1
                }, this.setting.fadeduration[0])
                this.state.isvisible=true
            }
            else if (this.state.shouldvisible==false && this.state.isvisible){
                this.$control.stop().animate({
                    opacity:0
                }, this.setting.fadeduration[1])
                this.state.isvisible=false
            }
        },
	
        init:function(){
            jQuery(document).ready(function($){
                var mainobj=SUDOCN.common.scrolltotop;
                var iebrws=document.all;
                mainobj.cssfixedsupport=!iebrws || iebrws && document.compatMode=="CSS1Compat" && window.XMLHttpRequest //not IE or IE7+ browsers in standards mode
                mainobj.$body=(window.opera)? (document.compatMode=="CSS1Compat"? $('html') : $('body')) : $('html,body')
                mainobj.$control=$('<div id="topcontrol">'+mainobj.controlHTML+'</div>')
                .css({
                    position:mainobj.cssfixedsupport? 'fixed' : 'absolute', 
                    bottom:mainobj.controlattrs.offsety, 
                    right:mainobj.controlattrs.offsetx, 
                    width:$(mainobj.controlHTML).width(),//fix ie8 width=0 bug
                    height:$(mainobj.controlHTML).height(),//fix ie8 height=0 bug
                    opacity:0, 
                    cursor:'pointer'
                })
                .attr({
                    title:'回到顶部'
                })
                .click(function(){
                    mainobj.scrollup();
                    return false
                })
                .appendTo('body')
                if (document.all && !window.XMLHttpRequest && mainobj.$control.text()!='') //loose check for IE6 and below, plus whether control contains any text
                    mainobj.$control.css({
                        width:mainobj.$control.width()
                    }) //IE6- seems to require an explicit width on a DIV containing text
                mainobj.togglecontrol()
                $('a[href="' + mainobj.anchorkeyword +'"]').click(function(){
                    mainobj.scrollup()
                    return false
                })
                $(window).bind('scroll resize', function(e){
                    mainobj.togglecontrol()
                })
            })
        }
    },
    initHelpLink:function(text,func){
        $("body").append($('<div class="help-link-box"><a href="javascript:void(0)" title="'+text+'">'+text+'</a></div>').click(function(){
            func();
        }));
    }
} 
SUDOCN.namespace("SUDOCN.usercenter.account");
SUDOCN.usercenter.account={
    config:{
        accountValidateUrl:"",
        sendCodeUrl:""
    },
    setPswd:function(url){
        $.post($("#setPswd-form").attr("action"),{
            "userId":$("#userId").val(),
            "email":$("#email").val(),
            "password":$("#password").val(),
            "passwordConfirmation":$("#passwordConfirmation").val()
        },function(rs){
            if(""==rs){
                alert('修改密码成功，将跳回登录页面！');
                window.location=url;
            }else{
                alert(rs);
            }
        },"text");
    },
    _showError:function(obj, msg) {
        obj.parents(".controls").append("<span class='help-block'>" + msg + "</span>");
        obj.parents(".control-group").addClass("error");
    },
    _checkPhone:function(phone) {
        if (phone.val() == "") {
            SUDOCN.usercenter.account._showError(phone, "请输入手机号码");
            return false;
        }
        if ((/^\d{11}$/).test(phone.val()) != true) {
            SUDOCN.usercenter.account._showError(phone, "手机号码不合法");
            return false;
        }
        return true;
    },
    _checkCode:function() {
        if ($("#code").val() == "") {
            SUDOCN.usercenter.account._showError($("#code"), "请输入验证码");
            return false;
        }
        return true;
    },
    boundPhone:function() {
        $("#validatePhone").button('loading');
        var data={};
        $("#boundPhoneForm input").each(function(){
            data[$(this).attr("name")]=$(this).val();//serialize()会忽略disabled的input
        });
        $.post($("#boundPhoneForm").attr("action"),data, function(rs) {
            if (rs == "") {
                alert("绑定成功");
                window.location.href = SUDOCN.usercenter.account.config.accountValidateUrl;
            } else {
                alert(rs);
            }
            $("#validatePhone").button('reset');
        }, "text");
    },
    firstBoundPhone:function(){
        if (SUDOCN.usercenter.account._checkPhone($("#cellphone")) == false)
            return;
        if (SUDOCN.usercenter.account._checkCode() == false) {
            return;
        }
        SUDOCN.usercenter.account.boundPhone();
    },
    changePhone:function(){
        if (SUDOCN.usercenter.account._checkPhone($("#oldCellphone")) == false)
            return;
        if (SUDOCN.usercenter.account._checkPhone($("#newCellphone")) == false)
            return;
        if (SUDOCN.usercenter.account._checkCode() == false) {
            return;
        }
        SUDOCN.usercenter.account.boundPhone();
    },
    //发送验证码
    sendCode:function(){
        var btn = $("#sendCodeBtn");
        var phone = btn.siblings("input");
        if (SUDOCN.usercenter.account._checkPhone(phone) == false)return;
        $.post( SUDOCN.usercenter.account.config.sendCodeUrl, {
            cellphone: phone.val()
        }, function(rs) {
            if ("" == rs)
                alert("验证码已经成功发送，请注意查收");
            else
                alert(rs);
        }, "text");
        //延时
        var count = 60;
        var text = btn.val();
        btn.attr("disabled", "").addClass("disabled");
        phone.attr("disabled", "");
        btn.val(text + "(" + (count--) + ")");
        var i = setInterval(function() {
            if (count == 0) {
                window.clearInterval(i);
                btn.val(text);
                btn.removeAttr("disabled").removeClass("disabled");
                phone.removeAttr("disabled");
            } else {
                btn.val(text + "(" + (count--) + ")");
            }
        }, "1000");
    },
    updateAvatar:{
        init:function(){
            //记得放在jQuery(window).load(...)内调用，否则Jcrop无法正确初始化
            $("#avatar-img").Jcrop({
                onChange: showAllPreview,
                onSelect: showAllPreview,
                maxSize:[300,300],
                minSize:[30,30],
                aspectRatio: 1,
                bgColor:'white',
                bgOpacity:.7,
                setSelect: [0, 0, 80, 80] //setSelect是Jcrop插件内部已定义的运动方法
            });
            //简单的事件处理程序，响应自onChange,onSelect事件，按照上面的Jcrop调用
            function showAllPreview(coords) {
                $("#avatarX").val(coords.x);
                $("#avatarY").val(coords.y);
                $("#avatarW").val(coords.w);
                $("#avatarH").val(coords.h);
                showOnePreview(coords, "large-preview");
                showOnePreview(coords, "middle-preview");
                showOnePreview(coords, "small-preview");
            }
            function showOnePreview(coords, key) {
                if (parseInt(coords.w) > 0) {
                    //计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到
                    var rx = $("." + key + "-box").width() / coords.w;
                    var ry = $("." + key + "-box").height() / coords.h;
                    //通过比例值控制图片的样式与显示
                    $("#" + key + "-img").css({
                        width: Math.round(rx * $("#avatar-img").width()) + "px", //预览图片宽度为计算比例值与原图片宽度的乘积
                        height: Math.round(rx * $("#avatar-img").height()) + "px", //预览图片高度为计算比例值与原图片高度的乘积
                        marginLeft: "-" + Math.round(rx * coords.x) + "px",
                        marginTop: "-" + Math.round(ry * coords.y) + "px"
                    });
                }
            }
        },
        setImgWH :function() {
            $('#imgW').val($('#avatar-img').width());
            $('#imgH').val($('#avatar-img').height());
            return true;
        }
    }
}
SUDOCN.namespace("SUDOCN.orgcenter.member");
SUDOCN.orgcenter.member={
    queryAdmin:function(url,orgId){
        var key = $("#queryKey").val();
        if(key==""){
            $("#errorInfo").html("请输入邮箱或者昵称");
            $("#queryKey-btn").hide();
            return;
        }
        $.post(url,{
            "key":key,
            "orgId":orgId
        },function(rs){
            if(!rs){
                $("#errorInfo").html("您输入的昵称或者邮箱不是您机构的成员，请重新输入！");
                $("#queryKey-btn").hide();
            }else{
                $("#addAdminForm").html(rs);
                $("#addAdminAction").show();
                $("#queryKey-btn").hide();
            }
        });
    },
    goBackAdminList:function(){
        $('#addAdminAction').hide();
        $('#addAdminForm').html('');
        $('#queryKey-btn').show();
    }
}
SUDOCN.namespace("SUDOCN.orgcenter.orgInfo");
SUDOCN.orgcenter.orgInfo={
    checkForm:function(){
        if ($('#realName').val() == '') {
            $('#realNameErrorInfo').html('机构实名不能为空').parent().parent().addClass('error');
            return false;
        }
        return true;
    } 
}
SUDOCN.namespace("SUDOCN.orgcenter.announcement");
SUDOCN.orgcenter.announcement={
    init:function(){
        KindEditor.ready(function(K) {
            SUDOCN.orgcenter.announcement.editor = K.create('textarea[name="content"]', {
                resizeType : 1,
                allowPreviewEmoticons : false,
                allowImageUpload : false,
                items : [
                'undo', 'redo', '|','fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline','removeformat','/',
                'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist', 'insertunorderedlist', '|','table','hr','link', 'unlink']
            });
        });
    },
    check:function(){
        var editor =  SUDOCN.orgcenter.announcement.editor;
        var flag = true;
        if($("#announcementTitle").val()==""){
            alert("公告的标题不能为空");
            $("#announcementTitle").focus(); 
            return false;
        }
        if(editor.isEmpty()){
            alert("公告的内容不能为空");
            editor.focus();
            return false;
        }
        return flag;
    }
}
//个人首页
SUDOCN.userIndex={
    baseUrl:"",
    init:function(ulId){
        $("#"+ulId+" li a").click(function(){
            $("#"+ulId+" li").each(function(){
                $(this).removeClass("active");
            });
            $(this).parent().addClass("active");
        });
    }
}
//页面初始化
!function ($) {
    $(function(){
        SUDOCN.common.fixSubNav();
        SUDOCN.common.initSudoHelp();
        SUDOCN.common.scrolltotop.init();
    });
}(window.jQuery)

var MessageAlert={
    
    showInfo : function(content){
        $("#msg-area").removeClass("alert-success");
        $("#msg-area").addClass("alert-success");
        $("#msg-area").removeClass("alert-error");
        $("#msg-area").children("p").text(content);
        $("#msg-area").fadeIn("slow").fadeTo(3000, 0.99).fadeOut("slow");
    },
    
    showError : function(content){
        $("#msg-area").removeClass("alert-success");
        $("#msg-area").removeClass("alert-error");
        $("#msg-area").addClass("alert-error");
        $("#msg-area").children("p").text(content);
        $("#msg-area").fadeIn("slow");
    }
}
