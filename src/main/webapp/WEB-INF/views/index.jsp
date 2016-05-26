<!DOCTYPE html>

<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Mariusz Gorzycki</title>
	<style>
		body {
			background-color: #e6e6e6;
			margin: 0;
		}

		.Main {
			padding: 16px;
		}

		.Zadania {
			float: right;
			margin: 16px;
			padding: 5px;
			width: 384px;
			height: 512px;
			box-shadow: 0px 0px 8px #404040;
			background-color: white;
		}

		.Zadania h1 {
			text-align: center;
		}

		.ZbiorZadan {
			display: table;
			margin: auto;
			max-width: 1280px;
		}

		.Autor {
			float: none;
			margin-left: auto;
			margin-right: auto;
			margin-bottom: 64px;
			padding: 5px;
			width: 512px;
			height: 148px;
			background-color: white;
			box-shadow: 0px 0px 8px #404040;
			text-align: center;
			padding: 5px;
		}

		.Validation {
			clear: both;
			margin: 16px;
		}

		ul {
			display: inline-block;
			height: 100%;
		}

		li {
			margin-bottom: 8px
		}

		.player {
			margin-left: auto;
			margin-right: auto;
			margin-bottom: 8px;
		}

		.board {
			margin-left: auto;
			margin-right: auto;
			border: 1px;
			background: black;
		}

		.box {
			height: 100px;
			width: 100px;
			text-align: center;
			background-color: #FFFFFF;
		}

		.newGame {
			margin-left: auto;
			margin-right: auto;
			height: 30px;
			width: 100px;
			text-align: center;
			border: 1px;
			background: silver;
			margin-top: 16px;
		}

		.green {
			height: 100px;
			width: 100px;
			text-align: center;
			background-color: green;
		}

		.yellow {
			height: 100px;
			width: 100px;
			text-align: center;
			background-color: yellow;
		}
	</style>

	<script type="text/javascript">
		var turn = Math.random() > 0.5 ? "X" : "O";
		var gameEnded = false;
		var b1, b2, b3, b4, b5, b6, b7, b8, b9;
		document.getElementById("player").innerHTML = turn;

		function newGame() {
			turn = Math.random() > 0.5 ? "X" : "O";
			for (var i = 1; i <= 9; i++) {
				document.getElementById("b" + i).innerHTML = "";
				document.getElementById("b" + i).className = "box";
			}
			document.getElementById("nextPlayer").innerHTML = "Kolej gracza: ";
			document.getElementById("player").innerHTML = turn;
			gameEnded = false;
		}

		function boxClick(elem) {
			if (elem.innerHTML == "" && gameEnded == false) {
				elem.innerHTML = turn;
				check();
				turn = turn == "O" ? "X" : "O";
			}
			if (gameEnded == false)
				document.getElementById("player").innerHTML = turn;
		}

		function check() {
			for (var i = 1; i <= 9; i++)
				this["b" + i] = document.getElementById("b" + i);

			var win = [[b1, b2, b3], [b4, b5, b6], [b7, b8, b9],
				[b1, b4, b7], [b2, b5, b8], [b3, b6, b9], [b1, b5, b9],
				[b3, b5, b7]];

			for (var i = 0; i < win.length; i++) {
				if (win[i][0].innerHTML === win[i][1].innerHTML && win[i][1].innerHTML === win[i][2].innerHTML && win[i][0].innerHTML !== "") {
					for (var j = 0; j < win[i].length; j++)
						win[i][j].className = "green";

					gameEnded = true;
					document.getElementById("nextPlayer").innerHTML = "Wygrał gracz: ";
					document.getElementById("player").innerHTML = turn;
				}
			}

			var filled = 0;
			for (var i = 1; i <= 9; i++)
				if (this["b" + i].innerHTML == "X" || this["b" + i].innerHTML == "O")
					filled++;

			if (filled >= 9) {
				gameEnded = true;
				document.getElementById("nextPlayer").innerHTML = "Remis";
				document.getElementById("player").innerHTML = "";
				for (var i = 1; i <= 9; i++)
					this["b" + i].className = "yellow";
			}
		}
	</script>
</head>
<body>
<div class="Main">
	<div class="Autor">
		<p>Autor:</p>
		<h1>Mariusz Gorzycki</h1>
		<a href="#kolko">Kółko i krzyżyk</a>
		<a href="#facebook">Facebook login</a>
	</div>

	<div class="ZbiorZadan">
		<div class="Zadania" id="kolko">
			<h1>Kółko i krzyżyk</h1>

			<table class="player">
				<tr>
					<td id="nextPlayer">Kolej gracza:</td>
					<td id="player">X</td>
				</tr>
			</table>

			<table class="board">
				<tr>
					<td class="box" id="b1" onclick="boxClick(this)"></td>
					<td class="box" id="b2" onclick="boxClick(this)"></td>
					<td class="box" id="b3" onclick="boxClick(this)"></td>
				</tr>
				<tr>
					<td class="box" id="b4" onclick="boxClick(this)"></td>
					<td class="box" id="b5" onclick="boxClick(this)"></td>
					<td class="box" id="b6" onclick="boxClick(this)"></td>
				</tr>
				<tr>
					<td class="box" id="b7" onclick="boxClick(this)"></td>
					<td class="box" id="b8" onclick="boxClick(this)"></td>
					<td class="box" id="b9" onclick="boxClick(this)"></td>
				</tr>
			</table>

			<table class="player">
				<tr>
					<td>
						<button class="newGame" onclick="newGame()">Nowa Gra</button>
					</td>
				</tr>
			</table>
		</div>

		<div class="Zadania">
			<h1>User</h1>
			<ul>
				<li><a href="www/login">Zaloguj się</a></li>
				<li><a href="www/register">Zarejestruj się</a></li>
				<li><a href="www/register">Edytuj Dane</a></li>
				<li><a href="www/remindPassword">Przypomnij hasło</a></li>
			</ul>
		</div>

		<div class="Zadania">
			<h1>Treść</h1>
			<div style="padding-left: 20px; padding-right: 20px">"Sed ut perspiciatis unde omnis iste natus error sit
				voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore
				veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia
				voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione
				voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur,
				adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam
				quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit
				laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea
				voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas
				nulla pariatur?"
			</div>
		</div>

		<div class="Zadania">
			<h1>Treść</h1>
			<div style="padding-left: 20px; padding-right: 20px">"Lorem ipsum dolor sit amet, consectetur adipiscing
				elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis
				nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in
				reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat
				cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
			</div>
		</div>

		<div class="Zadania" id="facebook">
			<h1>Facebook Login</h1>
			<ul>
				<div id="zdjecie"></div>
				<div id="nazwa"></div>
				<div id="id"></div>
				<div id="plec"></div>
				<div id="urodziny"></div>
				<div id="szkola"></div>

				<fb:login-button autologoutlink="true" onLogin="pobierzDane"></fb:login-button>
			</ul>
		</div>

		<div class="Zadania">
			<h1>Treść</h1>
			<div style="padding-left: 20px; padding-right: 20px">"At vero eos et accusamus et iusto odio dignissimos
				ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias
				excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia
				animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio."
			</div>
		</div>

		<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
		<script>
			var login = false;
			window.onload = function () {
				FB.init({
					appId: '688506604586351',
					xfbml: true,
					status: true,
					cookie: true,
					version: 'v2.6'
				});
				FB.getLoginStatus(handleSessionResponse);
			};

			(function (d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) {
					return;
				}
				js = d.createElement(s);
				js.id = id;
				js.src = "//connect.facebook.net/en_US/sdk.js";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));

			function handleSessionResponse(response) {
				login = false;
				pobierzDane();
			}

			function pobierzDane() {
				if (login) {
					Wyczysc();
					login = false;
					return;
				}
				login = true;
				FB.api('/me',
						{fields: "birthday,context,email,first_name,gender,hometown,link,location,id,about,age_range,picture,bio,middle_name,name,timezone,website,work"},
						function (response) {
							console.log(response);
							$("#zdjecie").empty().append('<img src="' + response.picture.date.url + '"> ');
							$("#nazwa").empty().append(response.name);
							$("#id").empty().append(response.id);
							$("#plec").empty().append(response.gender);
							$("#urodziny").empty().append(response.birthday);
							$("#szkola").empty().append(response.hometown);
						}
				);
			}

			function Wyczysc() {
				$("#zdjecie").empty();
				$("#nazwa").empty();
				$("#id").empty();
				$("#plec").empty();
				$("#urodziny").empty();
				$("#szkola").empty();
			}
		</script>

	</div>
</div>
</body>
</html>