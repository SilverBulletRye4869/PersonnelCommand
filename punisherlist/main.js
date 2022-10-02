let db; //database
let arr = new Object(); //データ格納用
const type = ["id","uuid","type","reason","term","cond"] //表示項目

function init(){
    firebase.initializeApp(firebaseConfig);
    db = firebase.firestore().collection("punisherlist");
    db.doc("cnt").get().then((e)=>{
        arr[e.id] = e.data();
        read(1,30)
    })
}


function read(start,end){
    
    let puni_td = document.getElementById("puni_td");
    end = Math.min(end, arr["cnt"]["cnt"]-1)
    for(let i = start; i<=end; i++){
        if(arr[i.toString()] == undefined){
            db.doc(i.toString()).get().then((e)=>{
                arr[i.toString()] = e.data()
                arr[i.toString()]["id"] = e.id;
                console.log(e.id)
                puni_td.appendChild(dispOut(i,end))
            })
        }else puni_td.appendChild(dispOut(i,end))
    }
    
    
}
function dispOut(i,end){
    let row = document.createElement("tr");
    type.forEach(e=>{
        let td = document.createElement("td")     
        td.innerHTML = arr[i.toString()][e];
        row.appendChild(td)
    })
    if(i==end)load("fin");
    return row
}


function load(type){
    if(type=="fin"){
        document.getElementById("loading").style.display="none"
        document.getElementById("main").style.display="block"
    }else{
        document.getElementById("loading").style.display="block"
        document.getElementById("main").style.display="none"
    }
}
init();