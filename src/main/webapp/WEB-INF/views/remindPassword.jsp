<!DOCTYPE html>

<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8"/>
    <title>Logowanie</title>

    <style type="text/css">
        body {
            background-color: #e6e6e6;
            margin: 0;
        }

        .Main {
            padding: 16px;
        }

        .Kafelek {
            float: left;
            margin: 16px;
            padding: 5px;
            width: 320px;
            height: 360px;
            box-shadow: 0px 0px 8px #404040;
            background-color: white;
            text-align: center;
        }

        p {
            margin-bottom: 0;
        }

        h1 {
            margin-bottom: 32px;
        }

        .Error {
            margin: 16px;
            color: red;
        }

        .TextBox {
            width: 160px;
            height: 16px;
        }

        .Send {
            width: 80px;
            height: 32px;
            margin-top: 48px;
        }

        .ZbiorKafelkow {
            display: table;
            margin: auto;
        }
    </style>
</head>
<body>
<div class="Main">
    <div class="ZbiorKafelkow">
        <div class="Kafelek">
            <h1>Przypomnienie hasła</h1>
            Na podany adress zostanie wysłana instrukcja odzyskiwania hasła.
            <form action="remindPassword" method="post" id="form">
                <p>Login:</p>
                <input type="text" class="TextBox" name="login" id="login"/>
                <p>Email:</p>
                <input type="text" class="TextBox" name="password" id="password"/>
                <div>
                    <input type="submit" class="Send" name="send" value="Wyślij"/>
                </div>
            </form>
            <div class="Error">
                <%--<?php--%>
                <%--if (!empty($_GET["error"]))--%>
                <%--echo "Niepoprawne dane!"--%>
                <%--?>--%>
            </div>
        </div>
    </div>
</div>
</body>
</html>