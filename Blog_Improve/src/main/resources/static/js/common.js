// 把前端存入的token放入到请求header中：user_header_token 用ajaxsend
// 在ajax请求发送前，设置一下token
$(document).ajaxSend(function (e, xhr, opt) {
    // $("div").append("<p>Requesting: "+opt.url+"</p>");
    let token = localStorage.getItem("user_token");
    
    xhr.setRequestHeader("user_header_token", token);
});


// 统一设置发送请求返回error的结果
$(document).ajaxError(function(event,xhr,options,exc){
    console.log("test");
    
    if(xhr.status==401){
        console.log("weidengru");
        
        alert("用户未登入，请先登入");
        location.href="blog_login.html";
    }
    else if(xhr.status==404){
        
    }else{
        
    }
});


function getUserInfo(url){
    $.ajax({
        type:"get",
        url:url,
        success:function(result){
            if(result!=null&&result.code==200&&result.data!=null){
                let userInfo=result.data;
                $(".card h3 ").text(userInfo.userName);
                // 对链接的href属性进行赋值
                $(".card a ").attr("href",userInfo.githubUrl);
            }else{
                // 自行补充
            }
        }
    });
}


// 

function logout(){
    let logout=confirm("是否确认退出");
    if(logout){
        localStorage.removeItem("user_token");
        localStorage.removeItem("login_user_id");
        location.href="blog_login.html";
    }
    
}



