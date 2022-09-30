let db;
let arr = new Object();

function init(){
    firebase.initializeApp(firebaseConfig);
    db = firebase.firestore();

    let puni = db.collection("punisherlist");
    puni.get().then(e =>{
        e.forEach(d => {
            arr[d.id] = d.data()
            arr[d.id]["id"] = d.id;
            if(d.id == arr["0"]["count"].toString())read(1,30);
        })
    })
    
    let row = document.createElement("tr")
    let type =["ID","UUID","理由","期限","状態"]
    type.forEach(e =>{
        let th = document.createElement("th")
        th.innerHTML= e;
        row.appendChild(th)
    })
    document.getElementById("puni_th").appendChild(row)

    
}


function read(start,end){
    
    let puni_td = document.getElementById("puni_td");
    
    end = Math.min(end, arr["0"]["count"])
    console.log("test0")
    const type = ["id","uuid","reason","term","cond"]
    console.log(end)
    for(let i = start; i<=end; i++){
        
        let row = document.createElement("tr");
        type.forEach(e=>{
            let td = document.createElement("td")
            
            td.innerHTML = arr[i.toString()][e];
            row.appendChild(td)
        })
        puni_td.appendChild(row)
    }
    
}

init();