//const url = "http://54.176.76.0:7000/" // ec2 URL
const url = "http://localhost:7000/" // local URL

// rerouting depending on who and if someone is logged in
if (sessionStorage.getItem("userSession") == null){
  window.location.replace(url + "login.html");
}else{
  let user = JSON.parse(sessionStorage.getItem("userSession"));
  if(user.userRoleId == 2){
      window.location.replace(url + "manager.html");
  }
} 

let submitBtn = document.getElementById("submitRequest");
let reimbTable = document.getElementById("reimTable");
let reimBtn = document.getElementById("getRequests");
let logoutBtn = document.getElementById("logoutBtn");
let description_verify = document.getElementById("description_verify");
let amount_verify = document.getElementById("amount_verify");
let login_name = document.getElementById("loginName");


submitBtn.addEventListener("click", sendRequest);
reimBtn.addEventListener("click", getAllRequests);
logoutBtn.addEventListener("click", logoutFunc);

// display name of who's logged in
let user = JSON.parse(sessionStorage.getItem("userSession"));
login_name.innerHTML = user.firstName + " " + user.lastName;

// logout function
async function logoutFunc(){
    
  
    let response = await fetch(
      url+"logout",
      {
        method : "POST",   
        credentials: "include"
      }
    );
  
    if(response.status===200){
        sessionStorage.clear();
        window.location.replace(url + "login.html");
    }else{
      console.log("Logout unsuccessful. Returned status code of:"+response.status);
    }
}

async function sendRequest() {  
  
  // changle button color back
  reimBtn.style.backgroundColor = "#0275d8";

    // sending reimbursement request
    console.log("The selected reimbursement type: " + document.querySelector('#select1').value);


    // image uploading ********************************************************************
    let image = document.getElementById('file-upload');
    let reader = new FileReader();
    
    // image uploading
    if (image.files && image.files[0]) {
      reader.onload = function(e) {
        requestFunc(e.target.result);
      }
  
      reader.readAsDataURL(image.files[0]);
    } else {
      requestFunc(' ');
    }
    
    // image uploading **********************************************************************  
}


async function requestFunc(fileResult){
  

  let typeId_value = 1;

    if (document.querySelector('#select1').value == "Travel"){
      typeId_value = 2;
    } else if(document.querySelector('#select1').value == "Food") {
      typeId_value = 3;
    } else if (document.querySelector('#select1').value == "Other") {
      typeId_value = 4;
    }

  // the request body
  let status = {
        
    amount:parseFloat(document.getElementById("amount").value),
    description:document.getElementById("description").value,
    typeId:typeId_value,
    // from the form data above **********************************************************
    receipt:fileResult

  } 

  // for parsing the input and only allowing two decimal places
  let num_amount = status.amount;
  let fixed_amount = num_amount.toFixed(2);
  console.log("your parsed amount: " + fixed_amount);

  // making sure employee enters valid amount
  if (status.amount == "" || status.amount < 0 || status.amount != fixed_amount){
    console.log("plz enter a valid amount");
    amount_verify.style.fontSize = "12px";
    amount_verify.style.color = "red";
    amount_verify.innerHTML = "Please enter a valid amount, only two decimal places";
    amount_verify.style.fontWeight = "bold";  
    
    return;
  }

  if (status.amount != ""){
    amount_verify.innerHTML = "";
  }

  // making sure they enter a description
  if (status.description == ""){
    console.log("plz put a description");
    description_verify.style.fontSize = "12px";
    description_verify.style.color = "red";
    description_verify.innerHTML = "Please enter a description for request";
    description_verify.style.fontWeight = "bold";  
    
    return;
  }

  if (status.description != ""){
    description_verify.innerHTML = "";
  }

  let response = await fetch(url + "reimbursements", {
      method:"POST",
      body:JSON.stringify(status),
      credentials:"include"
  })

  if(response.status === 201){

      document.querySelector('.response').textContent = "Your Request was sent successfully!"; // figure out if these work

      getAllRequests(); 
      console.log("Reimbursement request sent successfully!");
      
  }else {

      document.querySelector('.response').textContent = "Failure :("; // figure out how to make these work

      console.log("Request didn't go through!");
      
  }
}

// get request for reimbursement list
async function getAllRequests(){

  // changle button color back
  submitBtn.style.backgroundColor = "#0275d8";
    

    let response = await fetch(url + "reimbursements", {
      credentials:"include"

    });
  
    if(response.status === 200){

      let requests = await response.json();
      populateRequests(requests);
    
    } else{

      console.log("Can't get the reimbursement list ??? ugh !!!")

    }
}

// create rows and fill them with data from reimbursement request
function populateRequests(requests){

  reimTable.innerHTML ="";

  for(let request of requests){

    let row = document.createElement("tr");
    //console.log(request)
    for(let data in request){
      
      //console.log(data)
      // We're skipping the information we don't need to show
      if(data != "authorId" && data != "resolverId" && data != "statusId" && data != "typeId"){            
        // data = Key
        // request[data] = Value
        let request_data = request[data];
        // We need to format the dates that come from our request
        let td = document.createElement("td");
        if (data == "dateSubmitted" || data == "dateResolved") {       
          request_data = formatDate(request_data);
        }
        if (data == "receipt" && request_data != null && request_data != ' ') {
         
          let img = new Image();
          
          
          img.src = request_data;
          

          img.height = 111;
          img.width = 111;

          img.classList.add("myImg");
          img.id = "myImg";
          
          img.setAttribute("onclick", "alert('plz no click me')");

          td.appendChild(img);
          //console.log("RECEIPT = " + request_data);
          
        }
        if (data == "amount"){
          request_data = "$" + request_data;
        }
        if (data == "status" || data == "type") {
          let btn = styleStatus(request_data);            
          td.appendChild(btn);
        } else {

          if (data != "receipt") {
            td.innerText = request_data; 
          }
        }
        row.appendChild(td); 
      }
    }
    reimTable.appendChild(row); 
  }
}

// formatting the date
function formatDate(dateData){
  // Our Result
  var result = "";
  // Ensures data is not null
  if (dateData != null) {
    // Creates new Date from data
    var d = new Date(dateData);
    // Uses ternary operator
    var ampm = (d.getHours() >= 12) ? "PM" : "AM";    
    // Creates our formatting
    var m = d.getMinutes();
    if (m < 10) {
      m = "0" + m;
    }
    result = ((d.getMonth() + 1) + "/"  + d.getDate() + "/" + d.getFullYear() +
      " " + ((d.getHours() + 11) % 12 + 1) + ":" + m + " " + ampm);
  }
  // Returns the result if data is null string will be empty
  return result;
}

// style buttons(sort of lol) for status & type
function styleStatus(statusData) {
  let btn = document.createElement("input");
  btn.type = "button";
  btn.className = "btn";
  btn.value = statusData;
  btn.style.borderRadius = "25px";
  btn.style.height = "30px";
  btn.style.width = "80px";
  btn.style.fontSize = "10px";
  btn.style.fontWeight = "bold";
  btn.disabled = true;

  switch(statusData){
    case "APPROVED":
      btn.style.backgroundColor = "#42f587";
      break;
    case "DENIED":
      btn.style.backgroundColor = "#f54242";
      break;
    case "PENDING":
      btn.style.backgroundColor = "#f5d442";
      break;
    case "OTHER":
      btn.style.backgroundColor = "gray";
      break;
    case "TRAVEL":
      btn.style.backgroundColor = "#99ccff";
      break;
    case "FOOD":
      btn.style.backgroundColor = "#cc6699";
      break;
    case "LODGING":
      btn.style.backgroundColor = "#669999";
      break;
    default:
      btn.style.backgroundColor = "blue";
      break;
  }
  return btn;
}

