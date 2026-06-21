const API_URL = "http://localhost:8080";

function getToken(){
    return localStorage.getItem("token");
}

async function login(){

    const email =
        document.getElementById("email").value;

    const password =
        document.getElementById("password").value;

    const response =
        await fetch(
            API_URL + "/api/v1/auth/login",
            {
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body:JSON.stringify({
                    email,
                    password
                })
            }
        );

    if(!response.ok){
        alert("Login failed");
        return;
    }

    const data =
        await response.json();

    localStorage.setItem(
        "token",
        data.accessToken
    );

    localStorage.setItem(
        "refreshToken",
        data.refreshToken
    );

    window.location.href =
        "dashboard.html";
}

function logout(){


localStorage.clear();

window.location.href =
    "login.html";


}

async function loadStats(){


const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/stats",
        {
            headers:{
                Authorization:
                    "Bearer " +
                    getToken()
            }
        }
    );

const data =
    await response.json();

document.getElementById(
    "totalAppointments"
).innerText =
    data.totalAppointments;

document.getElementById(
    "verifiedAppointments"
).innerText =
    data.verifiedAppointments;

document.getElementById(
    "pendingAppointments"
).innerText =
    data.pendingAppointments;
}
async function initializeDashboard(){

    await loadStats();

}

async function addPatient(){

    const patient = {

        firstName:
        document.getElementById("firstName").value,

        lastName:
        document.getElementById("lastName").value,

        birthDate:
        document.getElementById("birthDate").value,

        gender:
        document.getElementById("gender").value,

        email:
        document.getElementById("email").value,

        mobileNumber:
        document.getElementById("mobileNumber").value
    };

    const response =
        await fetch(
            API_URL + "/api/v1/patients/addPatient",
            {
                method:"POST",

                headers:{
                    "Content-Type":"application/json",
                    "Authorization":
                        "Bearer " + getToken()
                },

                body:JSON.stringify(patient)
            }
        );

    if(!response.ok){

        alert("Patient save failed");
        return;
    }

    alert("Patient added");

    loadPatients();
}
async function loadPatients(){

    const response =
        await fetch(
            API_URL + "/api/v1/patients/all",
            {
                headers:{
                    Authorization:
                        "Bearer " + getToken()
                }
            }
        );

    const patients =
        await response.json();

    const table =
        document.getElementById(
            "patientsTable"
        );

    table.innerHTML = "";

    patients.forEach(patient => {

        table.innerHTML += `
        <tr>

            <td>${patient.id}</td>

            <td>${patient.firstName}</td>

            <td>${patient.lastName}</td>

            <td>${patient.email}</td>

            <td>${patient.mobileNumber}</td>

            <td>

                <button
                    onclick="deletePatient(${patient.id})">

                    Delete

                </button>
                
                <button
                    onclick="editPatient(${patient.id})">
                    
                    Edit
                </button>

            </td>

        </tr>
        `;
    });
}
async function deletePatient(id){

    if(!confirm(
        "Delete patient?"
    )){
        return;
    }

    const response =
        await fetch(
            API_URL +
            "/api/v1/patients/" +
            id,
            {
                method:"DELETE",

                headers:{
                    Authorization:
                        "Bearer " + getToken()
                }
            }
        );

    if(response.ok){

        alert("Deleted");

        loadPatients();
    }
}
async function getPatientById(){

    const id =
        document.getElementById(
            "searchPatientId"
        ).value;

    const response =
        await fetch(
            API_URL +
            "/api/v1/patients/" +
            id,
            {
                headers:{
                    Authorization:
                        "Bearer " + getToken()
                }
            }
        );

    if(!response.ok){

        alert("Patient not found");
        return;
    }

    const patient =
        await response.json();

    document.getElementById(
        "patientDetails"
    ).innerHTML = `

        <h3>Patient Details</h3>

        <p>ID: ${patient.id}</p>
        <p>Name: ${patient.firstName} ${patient.lastName}</p>
        <p>Email: ${patient.email}</p>
        <p>Phone: ${patient.mobileNumber}</p>

    `;
}
async function editPatient(id){

    const firstName =
        prompt("First Name");

    const lastName =
        prompt("Last Name");

    const birthDate =
        prompt("Birth Date (yyyy-MM-dd)");

    const gender =
        prompt("Gender (MALE/FEMALE)");

    const email =
        prompt("Email");

    const mobileNumber =
        prompt("Mobile Number");

    const response =
        await fetch(
            API_URL +
            "/api/v1/patients/" +
            id,
            {
                method:"PUT",

                headers:{
                    "Content-Type":
                        "application/json",

                    Authorization:
                        "Bearer " +
                        getToken()
                },

                body:JSON.stringify({

                    firstName,
                    lastName,
                    birthDate,
                    gender,
                    email,
                    mobileNumber

                })
            }
        );

    if(response.ok){

        alert("Updated");

        loadPatients();

    }else{

        alert("Update failed");
    }
}
// const API_URL = "http://localhost:8080";
//
// function getToken() {
//
//
// return localStorage.getItem("token");
//
//
// }
//
// function logout() {
//
//
// localStorage.clear();
//
// window.location.href = "login.html";
//
//
// }

/* ==========================
LOAD STATS
========================== */

async function loadStats() {


const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/stats",
        {
            headers: {
                Authorization:
                    "Bearer " +
                    getToken()
            }
        }
    );

const data =
    await response.json();

document.getElementById(
    "totalAppointments"
).innerText =
    data.totalAppointments;

document.getElementById(
    "verifiedAppointments"
).innerText =
    data.verifiedAppointments;

document.getElementById(
    "pendingAppointments"
).innerText =
    data.pendingAppointments;
}



async function loadAppointments() {


const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/all?page=0&size=20",
        {
            headers: {
                Authorization:
                    "Bearer " +
                    getToken()
            }
        }
    );

const data =
    await response.json();

renderAppointments(
    data.content
);


    }

        /* ==========================
        LOAD VERIFIED
        ========================== */

        async function loadVerifiedAppointments() {


const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/verified?page=0&size=20",
        {
            headers: {
                Authorization:
                    "Bearer " +
                    getToken()
            }
        }
    );

const data =
    await response.json();

renderAppointments(
    data.content
);


    }

        /* ==========================
        TABLE RENDER
        ========================== */

        function renderAppointments(
        appointments
        ) {


const table =
    document.getElementById(
        "appointmentsTable"
    );

table.innerHTML = "";

appointments.forEach(
    appointment => {

        table.innerHTML +=

            `
        <tr>

        <td>
        ${appointment.id}
        </td>

        <td>
        ${appointment.doctorName}
    </td>

    <td>
        ${appointment.appointmentPlace}
    </td>

    <td>
        ${appointment.complaintType}
    </td>

    <td>
        ${appointment.admissionVerified}
    </td>

    <td>

        <button
            onclick="verifyAppointment(${appointment.id})">

            Verify

        </button>

        <button
            onclick="deleteAppointment(${appointment.id})">

            Delete

        </button>

    </td>

</tr>
    `;
    }
);


}

/* ==========================
VERIFY
========================== */

async function verifyAppointment(id) {


const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/" +
        id +
        "/verify",
        {
            method: "PATCH",

            headers: {
                Authorization:
                    "Bearer " +
                    getToken()
            }
        }
    );

if (response.ok) {

    alert(
        "Appointment Verified"
    );

    loadAppointments();
    loadStats();
}


}

/* ==========================
DELETE
========================== */

async function deleteAppointment(id) {


if (!confirm(
    "Delete Appointment?"
)) {
    return;
}

const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/" +
        id,
        {
            method: "DELETE",

            headers: {
                Authorization:
                    "Bearer " +
                    getToken()
            }
        }
    );

if (response.ok) {

    alert("Deleted");

    loadAppointments();
    loadStats();
}


}

/* ==========================
CREATE APPOINTMENT
========================== */

async function createAppointment() {


const request = {

    patientId:
        parseInt(
            document.getElementById(
                "patientId"
            ).value
        ),

    appointmentDate:
        document.getElementById(
            "appointmentDate"
        ).value + ":00",

    appointmentPlace:
        document.getElementById(
            "appointmentPlace"
        ).value,

    doctorName:
        document.getElementById(
            "doctorName"
        ).value,

    complaintType:
        document.getElementById(
            "complaintType"
        ).value,

    rating:
        document.getElementById(
            "rating"
        ).value,

    feedback:
        document.getElementById(
            "feedback"
        ).value,

    likedAspect1:
        document.getElementById(
            "likedAspect1"
        ).value,

    likedAspect2:
        document.getElementById(
            "likedAspect2"
        ).value
};

const formData =
    new FormData();

formData.append(
    "data",
    new Blob(
        [
            JSON.stringify(request)
        ],
        {
            type:
                "application/json"
        }
    )
);

const file =
    document.getElementById(
        "file"
    ).files[0];

if (file) {

    formData.append(
        "file",
        file
    );
}

const response =
    await fetch(
        API_URL +
        "/api/v1/appointments/add",
        {
            method: "POST",

            headers: {
                Authorization:
                    "Bearer " +
                    getToken()
            },

            body: formData
        }
    );

if (response.ok) {

    alert(
        "Appointment Created"
    );

    loadAppointments();
    loadStats();

} else {

    alert(
        "Create Failed"
    );
}

}

async function loadMonthlyInsight(){

    const response =
        await fetch(
            API_URL +
            "/api/v1/appointments/insights/monthly",
            {
                headers:{
                    Authorization:
                        "Bearer " + getToken()
                }
            }
        );

    const data =
        await response.json();

    document.getElementById(
        "topComplaint"
    ).innerText =
        "Top Complaint: " +
        data.topComplaintType;

    document.getElementById(
        "topHospital"
    ).innerText =
        "Top Hospital: " +
        data.topHospital;

    document.getElementById(
        "aiSummary"
    ).innerText =
        data.aiSummary;
}
function formatDate(dateStr){

    return new Date(dateStr)
        .toLocaleString();
}
let currentPage = 0;
let totalPages = 0;
async function loadAppointments(){

    const response =
        await fetch(
            API_URL +
            `/api/v1/appointments/verified?page=${currentPage}&size=5`,
            {
                headers:{
                    Authorization:
                        "Bearer " + getToken()
                }
            }
        );

    const result =
        await response.json();

    totalPages =
        result.totalPages;

    document.getElementById(
        "pageInfo"
    ).innerText =
        `Page ${result.number + 1} / ${result.totalPages}`;

    renderAppointments(
        result.content
    );
}
function renderAppointments(data){

    const cards =
        document.getElementById("cards");

    cards.innerHTML = "";

    data.forEach(app => {

        cards.innerHTML += `

        <div class="card">

            <h3>
                🥼:
                ${app.doctorName}
            </h3>

            <p>
                🏥:
                ${app.appointmentPlace}
            </p>

            <p>
                😷:
                ${app.complaintType}
            </p>

            <p>
                📅:
                ${formatDate(
            app.appointmentDate
        )}
            </p>

            <p>
                ⭐:
                ${app.rating}
            </p>
            
            <p>
                📝:
                ${app.feedback}
            </p>
            
            <p>
                👍:
                ${app.likedAspect1}
            </p>
            
            <p>
                👍:
                ${app.likedAspect2}
            </p>
            
            <p>
                ${
                    app.admissionDocumentPath
                    ? "📄 Doctor Document ✅"
                    : "📄 Doctor Document ❌"
                }
            </p>

        </div>

        `;
    });
}
document.addEventListener(
    "DOMContentLoaded",
    () => {

        loadStats();

        loadAppointments();

        document
            .getElementById("prevBtn")
            ?.addEventListener(
                "click",
                () => {

                    if(currentPage > 0){

                        currentPage--;

                        loadAppointments();
                    }
                }
            );

        document
            .getElementById("nextBtn")
            ?.addEventListener(
                "click",
                () => {

                    if(
                        currentPage <
                        totalPages - 1
                    ){

                        currentPage++;

                        loadAppointments();
                    }
                }
            );

    }
);
async function filterAppointments(){
    console.log("FILTER TRIGGERED");

    const doctorName =
        document.getElementById("doctorFilter").value;

    const complaintType =
        document.getElementById("complaintFilter").value;

    const appointmentPlace =
        document.getElementById("appointmentPlaceFilter").value;

    const hasDocument =
        document.getElementById("documentFilter").checked;

    const query = `
    query {
      searchAppointments(
        filter:{
          doctorName:"${doctorName}"
          complaintType:"${complaintType}"
          appointmentPlace:"${appointmentPlace}"
          hasDocument:${hasDocument}
        }
      ){
        id
        doctorName
        appointmentPlace
        complaintType
        appointmentDate
        feedback
        likedAspect1
        likedAspect2
        rating
        admissionVerified
        admissionDocumentPath
      }
    }
    `;

    const response =
        await fetch(
            "http://localhost:8080/graphql",
            {
                method:"POST",

                headers:{
                    "Content-Type":"application/json",
                    "Authorization":"Bearer " + getToken()
                },

                body:JSON.stringify({
                    query
                })
            }
        );

    const result =
        await response.json();

    console.log(result);

    renderAppointments(
        result.data.searchAppointments
    );
}

document.getElementById("doctorFilter")
    ?.addEventListener("input", filterAppointments);

document.getElementById("complaintFilter")
    ?.addEventListener("input", filterAppointments);

document.getElementById("appointmentPlaceFilter")
    ?.addEventListener("input", filterAppointments);

document.getElementById("documentFilter")
    ?.addEventListener("change", filterAppointments);

async function register() {

    const name =
        document.getElementById("registerName").value;

    const email =
        document.getElementById("registerEmail").value;

    const password =
        document.getElementById("registerPassword").value;

    const response =
        await fetch(
            API_URL + "/api/v1/auth/register",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    name,
                    email,
                    password
                })
            }
        );

    if(response.ok){

        alert("Registration successful");

        window.location.href =
            "login.html";

    }else{

        const error =
            await response.text();

        console.log(error);

        alert("Registration failed");
    }
}
document.addEventListener(
    "DOMContentLoaded",
    () => {

        if(document.getElementById("cards")){

            loadStats();
            loadAppointments();

        }

    }
);
/* WEBSOCKET */

if (typeof SockJS !== "undefined") {

    const socket = new SockJS(
        "http://localhost:8082/ws"
    );

    const stompClient = Stomp.over(socket);

    stompClient.debug = null;

    stompClient.connect({}, function () {

        stompClient.subscribe(
            "/topic/appointments",
            function(){

                console.log("Appointment update received");

                loadAppointments();
                loadStats();

            });

    });

}

