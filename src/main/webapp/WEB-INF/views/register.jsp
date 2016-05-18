<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Rejestracja</title>
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
            width: 520px;
            height: 580px;
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
            <form action="register" method="post">
                <table>
                    <tr>
                        <td>Imie:</td>
                        <td><input type="text" name="imie"/></td>
                    </tr>
                    <tr>
                        <td>Nazwisko:</td>
                        <td><input type="text" name="nazwisko"/></td>
                    </tr>
                    <tr>
                        <td>Hasło:</td>
                        <td><input type="password" name="haslo"/></td>
                    </tr>
                    <tr>
                        <td>Ulica:</td>
                        <td><input type="text" name="ulica"/></td>
                    </tr>
                    <tr>
                        <td>Miasto:</td>
                        <td><input type="text" name="miasto"/></td>
                    </tr>
                    <tr>
                        <td>Data urodzenia:</td>
                        <td><input type="text" name="dataUrodzenia"/></td>
                    </tr>
                    <tr>
                        <td>Kod pocztowy:</td>
                        <td><input type="text" name="kodPocztowy"/></td>
                    </tr>
                    <tr>
                        <td>Telefon:</td>
                        <td><input type="text" name="telefon"/></td>
                    </tr>
                    <tr>
                        <td>Województwo:</td>
                        <td><select name="wojewodztwo">
                            <option>Mazowieckie</option>
                            <option>Lubelskie</option>
                            <option>Podlaskie</option>
                            <option>Kujawsko-pomorskie</option>
                            <option>Opolskie</option>
                            <option>Podkarpackie</option>
                            <option>Pomorskie</option>
                        </select></td>
                    </tr>
                    <tr>
                        <td>Zdjęcie:</td>
                        <td><input type="file" name="zdjecie"/></td>
                    </tr>
                    <tr>
                        <td>Czy posiadasz prawo jazdy:</td>
                        <td><input type="checkbox" name="prawoJazdy"/></td>
                    </tr>
                    <tr>
                        <td>Płeć:</td>
                        <td>
                            <table>
                                <tr>
                                    <td>K<input type="radio" name="plec" value="K" checked="true"/></td>
                                    <td>M<input type="radio" name="plec" value="M"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>Uwagi:</td>
                    </tr>
                    <tr>
                        <td><textarea name="uwagi" rows="10" cols="30"></textarea></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td><input type="reset" name="p2" value="Czysc"/></td>
                                    <td><input type="submit" name="p3" value="Wyslij"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>