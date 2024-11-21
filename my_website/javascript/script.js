function darkMode() {
    var element = document.body;
    var content = document.getElementById("DarkModetext");
    element.className = "dark-mode";
    content.innerText = "Dark Mode is ON";
    let imageMode = document.getElementById("mode");
    
}
function lightMode() {
    var element = document.body;
    var content = document.getElementById("DarkModetext");
    element.className = "light-mode";
    content.innerText = "Dark Mode is OFF";
    let imageMode = document.getElementById("mode");
}

function messageOutput(){
    const name = document.getElementById('userName').value;
    document.getElementById("message").textContent = "Welcome back, " + name;
}

var tID; 
function animateScript() {
var    position = 233.33; 
const  interval = 100; 
tID = setInterval ( () => {
document.getElementById("image").style.backgroundPosition = `-${position}px 0px`; 
if (position < 1400)
{ position = position + 233.33;}
else
{ position = 233.33; }
}
, interval ); 
} 


function stopAnimate() {
clearInterval(tID);
}