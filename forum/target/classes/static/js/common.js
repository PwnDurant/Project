// =================== 定义全局变量 ======================
let avatarUrl = 'image/avatar01.jpeg'; // 默认头像
let currentArticle; // 当前访问的帖子
let currentUserId;  // 当前登录用户
let profileUserId;  // 查看个人信息


// ============================ 处理导航激活效果 ===========================
function changeNavActive (boardItem) {
    // 判断当前是否为激活状态
    if (boardItem.hasClass('active') == false) {
      let activeLiEl = $('#topBoardList>.active');
      activeLiEl.removeClass('active');
      boardItem.addClass('active');
      // 请求版块中的帖子
      buildArticleList();
    }
}

// ============================ 删除导航激活效果 ===========================
function removeNavActive () {
    // 判断当前是否为激活状态
    let activeLiEl = $('#topBoardList>.active');
    if (activeLiEl) {
      activeLiEl.removeClass('active');
    }
}

//======================= 处理导航栏点击并获取帖子列表 ======================
function buildArticleList (){
  console.log('发送请求查询帖子列表');
  $('#bit-forum-content').load('article_list.html');
}

// 设置站内信接收用户信息
function setMessageReceiveUserInfo (userId, nickname) {
  console.log('userId = ' + userId);
  console.log('nickname = ' + nickname);
  $('#index_message_receive_user_id').val(userId);
  $('#index_message_receive_user_name').html('发送给: <strong>' + nickname + '</strong>');
  console.log('value = ' + $('#index_message_receive_user_id').val());
}
