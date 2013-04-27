var hash={
		'.doc'  : 1,
		'.docx' : 1,
		'.ppt'  : 1,
		'.pptx' : 1,
		'.txt'  : 1,
		'.pdf'  : 1,
		'.gif'  : 1,
		'.jpg'  : 1,
		'.png'  : 1,
	}
	function check_document(filename){
		var re = /\..+$/;
	    var ext = filename.match(re);
	    if(hash[ext]){
	    	document.getElementById("fileform").submit();
	    }else{
	    	alert("您上传的文件不合法！");
	    }
	}