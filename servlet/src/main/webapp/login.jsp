<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>GrantedBy.Me - JSP Demo - Login</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="GrantedByMe - Instant secure login without password">
    <meta name="author" content="grantedby.me">
    <script>
        grantedByMeConfig = {
            ajaxURL: 'ajax',
            redirectURL: '/example',
            csrfHeader: 'X-XSRF-TOKEN',
            challengeType: 'authenticate'
        };
    </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn-dev.grantedby.me/components/modal/css/grantedbyme.min.css">
    <style>
        body {
            margin: 10px;
        }
        #container {
            text-align: center;
        }
        #GrantedByMe-Container {
            padding: 10px;
        }
    </style>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js" integrity="sha384-6ePHh72Rl3hKio4HiJ841psfsRJveeS+aLoaEf3BWfS+gTF0XdAqku2ka8VddikM" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lrsjng.jquery-qrcode/0.12.0/jquery.qrcode.min.js" integrity="sha384-Y9GclwQi9a/Oe8KzZKzW6eyMiCa/WBMS3dkJabxbLG8hxqnb6XgE2qUTy5dQYUEL" crossorigin="anonymous"></script>
    <script src="https://cdn-dev.grantedby.me/components/modal/js/grantedbyme.js"></script>
</head>
<body>
<div id="container">
    <!--GrantedByMe Component Start-->
    <div id="GrantedByMe-Container"></div>
    <div id="GrantedByMe-modal" class="GrantedByMe-modal">
        <div id="GrantedByMe-content"></div>
    </div>
    <!--GrantedByMe Component End-->
    <a href="register.jsp">Register</a>
</div>
</body>
</html>
