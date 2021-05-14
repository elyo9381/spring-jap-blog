let index = {
    init:function(){
        $("#btn-save").on("click",()=>{
            this.save();
        });

        $("#btn-update").on("click",()=>{
            this.update();
        });



    },

    save:function(){
        // alert('user의 save함수이다.');
        let data ={
            username:$("#username").val(),
            password:$("#password").val(),
            email:$("#email").val()
        };


        // ajax호출시 default가 비동기 호출
        // ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환해주네요
        $.ajax({
            type:"POST",
            url:"/auth/joinProc",
            data:JSON.stringify(data), // http body 데이터
            contentType:"application/json;charset=utf-8", // body data ? MINE
            dataType:"json" // 요청을 서버로 해서 응답이 왔을때 기본적으로 모든것이 문자열 (생긴게 json이라면) -> javascript
        }).done(function (resp){
            if(resp.status === 500){
                alert("회원가입이 실패 되었습니다. ");
            } else {
                alert("회원가입이 완료 되었습니다. ");
                location.href="/";
            }
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    update:function(){
        let data ={
            id:$("#id").val(),
            username:$("#username").val(),
            password:$("#password").val(),
            email:$("#email").val()
        };


        $.ajax({
            type:"PUT",
            url:"/user",
            data:JSON.stringify(data), // http body 데이터
            contentType:"application/json;charset=utf-8", // body data ? MINE
            dataType:"json" // 요청을 서버로 해서 응답이 왔을때 기본적으로 모든것이 문자열 (생긴게 json이라면) -> javascript
        }).done(function (resp){
            alert("회원 수정이 완료되었습니다. ");
            location.href="/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },


}

index.init();

// $("#btn-login").on("click",()=>{
//     this.login();
// });

// login:function(){
//     // alert('user의 save함수이다.');
//     let data ={
//         username:$("#username").val(),
//         password:$("#password").val()
//     };
//
//     $.ajax({
//         type:"POST",
//         url:"/api/user/login",
//         data:JSON.stringify(data), // http body 데이터
//         contentType:"application/json;charset=utf-8", // body data ? MINE
//         dataType:"json" // 요청을 서버로 해서 응답이 왔을때 기본적으로 모든것이 문자열 (생긴게 json이라면) -> javascript
//     }).done(function (resp){
//         alert("로그인이 완료되었습니다. ");
//         location.href="/";
//     }).fail(function (error){
//         alert(JSON.stringify(error));
//     });
// }

/**
 *
 * 회원가입시 ajax를 사용하는 2가지 이유
 *  1. 요청에 대한 응답을 html이 아닌 data를 받기 위하여!
 *   - 브라우즈는 html, 앱은 자바코드, 등 클라이언트별로 다르기 때문에 데이터 전송한다.
 *  2. 비동기 통신을 하기 위해서 (순서에 상관없이)
 *   -
 *
 */
