// 把前端存入的token放入到请求header中：user_header_token 用ajaxsend
// 在ajax请求发送前，设置一下token
$(document).ajaxSend(function (e, xhr, opt) {
    // $("div").append("<p>Requesting: "+opt.url+"</p>");
    let token = localStorage.getItem("user_token");
    
    xhr.setRequestHeader("user_header_token", token);
});


$(document).ajaxError(function(event,xhr,options,exc){
    if(xhr.status==401){
        alert("用户未登入，请先登入");
        location.href="blog_login.html";
    }
});