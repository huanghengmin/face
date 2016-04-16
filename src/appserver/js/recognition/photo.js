drag = 0
move = 0

// 拖拽对象
var ie=document.all;
var nn6=document.getElementById&&!document.all;
var isdrag=false;
var y,x;
var oDragObj;


function moveMouse(e) {
    if (isdrag) {
        oDragObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y)+"px";
        oDragObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x)+"px";
        return false;
    }
}

function initDrag(e) {
    var oDragHandle = nn6 ? e.target : event.srcElement;
    var topElement = "HTML";
    while (oDragHandle.tagName != topElement && oDragHandle.className != "dragAble") {
        oDragHandle = nn6 ? oDragHandle.parentNode : oDragHandle.parentElement;
    }
    if (oDragHandle.className=="dragAble") {
        isdrag = true;
        oDragObj = oDragHandle;
        nTY = parseInt(oDragObj.style.top+0);
        y = nn6 ? e.clientY : event.clientY;
        nTX = parseInt(oDragObj.style.left+0);
        x = nn6 ? e.clientX : event.clientX;
        document.onmousemove=moveMouse;
        return false;
    }
}
document.onmousedown=initDrag;
document.onmouseup=new Function("isdrag=false");

function clickMove(s){
    if(s=="up"){
        dragObj.style.top = parseInt(dragObj.style.top) + 100;
    }else if(s=="down"){
        dragObj.style.top = parseInt(dragObj.style.top) - 100;
    }else if(s=="left"){
        dragObj.style.left = parseInt(dragObj.style.left) + 100;
    }else if(s=="right"){
        dragObj.style.left = parseInt(dragObj.style.left) - 100;
    }

}

function smallit(){
    var height1=images1.height;
    var width1=images1.width;
    images1.height=height1/1.2;
    images1.width=width1/1.2;

}

function bigit(){
    var height1=images1.height;
    var width1=images1.width;
    images1.height=height1*1.2;
    images1.width=width1*1.2;
}
function realsize() {
    images1.height=images2.height;
    images1.width=images2.width;
    block1.style.left = 0;
    block1.style.top = 0;

}
function featsize() {
    var width1=images2.width;
    var height1=images2.height;
    var width2=360;
    var height2=200;
    var h=height1/height2;
    var w=width1/width2;
    if(height1<height2&&width1<width2)
    {
        images1.height=height1;
        images1.width=width1;
    }
    else
    {
        if(h>w)
        {
            images1.height=height2;
            images1.width=width1*height2/height1;
        }
        else
        {
            images1.width=width2;
            images1.height=height1*width2/width1;
        }
    }
    block1.style.left = 0;
    block1.style.top = 0;
}

function switch_image(filePath){
    var image = document.getElementById('img.1');
    var image2 = document.getElementById('img.2');
    image2.src = '/center/service?c=show_photo&filePath='+filePath;
    image.src = '/center/service?c=show_photo&filePath='+filePath;
}

function initVar(){
    var query = location.search.substr(1);
    var values = query.split("&");
    var filePath = values[0].substr(3);
    switch_image(filePath);
}

function onLoadAutoResizeImage(id,objImg){
    var width = document.getElementById(id).width;
    var height = document.getElementById(id).height;
    AutoResizeImage(width,height,objImg);
    if(height > 500) {
        smallit();
        smallit();
        smallit();
    }
}

// 缩放图片，imgSrc用户延迟加载图片url
function AutoResizeImage(maxWidth,maxHeight,objImg,imgSrc){
    var img = new Image();
    img.src = imgSrc || objImg.src;
    var hRatio;
    var wRatio;
    var Ratio = 1;
    var w = img.width;
    var h = img.height;
    wRatio = maxWidth / w;
    hRatio = maxHeight / h;
    if (maxWidth ==0 && maxHeight==0){
        Ratio = 1;
    }else if (maxWidth==0){
        if (hRatio<1) Ratio = hRatio;
    }else if (maxHeight==0){
        if (wRatio<1) Ratio = wRatio;
    }else if (wRatio<1 || hRatio<1){
        Ratio = (wRatio<=hRatio?wRatio:hRatio);
    }
    if (Ratio<1){
        w = w * Ratio;
        h = h * Ratio;
    }
    objImg.style.height = Math.round(h) + "px";
    objImg.style.width = Math.round(w) + "px";
    if(h < maxHeight) { // 纵向有空余空间
        objImg.style.marginTop = Math.round((maxHeight - h) / 2) + "px";
    }
    if(w < maxWidth) { // 横向有空余空间
        objImg.style.marginLeft = Math.round((maxWidth - w) / 2) + "px";
    }
    if(!!!objImg.src)
        objImg.src = imgSrc;
}