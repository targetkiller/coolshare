/* ============================================================
 * 
 * ============================================================ */

!function ($) {
    $(function(){
        var btnStyles = ["btn-primary","btn-info","btn-success","btn-warning","btn-danger","btn-inverse","btn-link"];
        var btnStyle =btnStyles[0];
        var btnButton = $(".switchButton .btn");
        btnButton.each(function(){
            for(var i=0;i<btnStyles.length;i++){
                if($(this).attr("class").indexOf(btnStyles[i])!=-1){
                    btnStyle=btnStyles[i];
                    break;
                }
            }
        }).each(function(){
            if($(this).attr("class").indexOf(btnStyle)==-1)$(this).addClass("disabled").attr("disabled","");
        }).click(function(){
            $(this).siblings(".btn").addClass(btnStyle).removeClass("disabled").removeAttr("disabled");
            $(this).removeClass(btnStyle).addClass("disabled").attr("disabled","");
        });
    });
}(window.jQuery);