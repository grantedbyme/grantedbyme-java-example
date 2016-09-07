<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>GrantedBy.Me - JSP Demo - Account</title>
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
            postToken: '',
            initCall: 'getAccountToken',
            pollCall: 'getAccountState'
        };
    </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn-dev.grantedby.me/components/modal/css/grantedbyme.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lrsjng.jquery-qrcode/0.12.0/jquery.qrcode.min.js"></script>
    <script src="https://cdn-dev.grantedby.me/components/modal/js/grantedbyme.js"></script>
</head>
<body>
<!--GrantedByMe Component Start-->
<div id="GrantedByMe-Container"></div>
<div id="GrantedByMe-modal" class="GrantedByMe-modal">
    <div id="GrantedByMe-content"></div>
</div>
<!--GrantedByMe Component End-->
<a href="index.jsp">Home</a>
</body>
</html>