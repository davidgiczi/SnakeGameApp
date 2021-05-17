/**
 * 
 */
var snakeStore = [];
var edibleStore = [];
var barrierStore = [];
var deletedSnakeComponent;
var score;
var level;
var tempo = 1000;
var YELLOW = "#f9e35f";
var RED =  "#f95151";
var GREEN = "#0b6623";
var BROWN = "#72601b";
var ROWS = parseInt(document.getElementById("rows").value);
var COLS  = parseInt(document.getElementById("cols").value);
var run = setInterval(function send(){sendAjaxRequest("goDirect")}, 1000);

document.addEventListener("keydown", pressButton);


function pressButton(e) {

    e = e || window.event;
	
    if (e.keyCode === 37) {
	
       sendAjaxRequest("goWest");
	}
else if (e.keyCode === 38) {
	
       sendAjaxRequest("goNorth");
	}
else if (e.keyCode === 39) {
	
       sendAjaxRequest("goEast");

    }
else if (e.keyCode === 40) {
	
       sendAjaxRequest("goSouth");
	}
 else if (e.keyCode === 13) {
	
   		alert("Bebábozódtál!?");
    } 
}



function sendAjaxRequest(req) {

    var xmlHTTP = new XMLHttpRequest();

    xmlHTTP.onreadystatechange = function () {

        if (xmlHTTP.readyState === 4 && xmlHTTP.status === 200) {

            var response = xmlHTTP.responseText;

			if(response.startsWith("Vége")){
				
				clearInterval(run);
				
				if(confirm(response)){
					
				location.reload();
				}
				else{
					
				document.removeEventListener("keydown", pressButton);
				}
					
				
			}
			else{
            parseResponse(response);
		}

        }

    };

    var url = document.location.protocol + "//" + document.location.host +
            document.location.pathname + "/ajaxRequest?usereq=" + req;


    xmlHTTP.open("GET", url, true);
    xmlHTTP.send();


}

function parseResponse(resp){
	
	var inputData = resp.split(";");
	snakeStore = inputData[0].split("_");
    deletedSnakeComponent = inputData[1];
	edibleStore = inputData[2].split("_");
	barrierStore = inputData[3].split("_");
	score = inputData[4];
	level = inputData[5];
	if(parseInt(inputData[6]) !== tempo){
		tempo = parseInt(inputData[6]);
		clearInterval(run);
		run = setInterval(function send(){sendAjaxRequest("goDirect")}, tempo);
	}
	
	displayResponse();
	
}
	
function displayResponse(){
	
	if(edibleStore.length !== 1){
		clearBoard();
		for(i = 0;  i < edibleStore.length; i++){
		document.getElementById(edibleStore[i]).style.backgroundColor = GREEN;
	}
	}
	if(deletedSnakeComponent !=="-1"){
	document.getElementById(deletedSnakeComponent).style.backgroundColor = "";
	}
	var i;
	for(i = 0;  i < snakeStore.length - 1; i++){
		document.getElementById(snakeStore[i]).style.backgroundColor = YELLOW;
	}
	document.getElementById(snakeStore[i]).style.backgroundColor = RED;
	if(barrierStore.length !== 1){
		for(i = 0;  i < barrierStore.length; i++){
		document.getElementById(barrierStore[i]).style.backgroundColor = BROWN;
	}
	}

	document.getElementById("score").innerHTML = score;
	document.getElementById("level").innerHTML = level;
}
	
	function clearBoard(){
		
		for(var i = 0; i < ROWS; i++){
			for(var j = 0; j < COLS; j++){
				document.getElementById(i * COLS + j).style.backgroundColor = "";
			}
		}
		
	}
	