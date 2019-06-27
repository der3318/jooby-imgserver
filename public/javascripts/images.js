
/** background */
document.getElementById("banner").style.opacity = 0.8;
document.getElementById("board").style.background = "url('/images/board.jpg') repeat-y center center";
document.getElementById("board").style.backgroundSize = "cover";
document.getElementById("board").style.opacity = 0.8;

/** status image */
var verificationURL = "/images/check.gif";
var computerURL = "/images/computer.gif";
setInterval(function() {
    if (document.getElementById("file").value != "") {
        if (!document.getElementById("status").src.endsWith(verificationURL)) {
            document.getElementById("status").src = verificationURL;
        }
        document.getElementById("submit").disabled = "";
    } else {
        if (!document.getElementById("status").src.endsWith(computerURL)) {
            document.getElementById("status").src = computerURL;
        }
        document.getElementById("submit").disabled = "disabled";
    }
}, 1000);

/** square images */
var images = document.getElementsByClassName("square");
for (index = 0; index < images.length; index++) {
    images[index].style.width = $(window).width() / 5;
    images[index].style.height = $(window).width() / 5;
    images[index].style.overflow = "hidden";
}

/** modal - fullscreen */
function popFullscreen(url) {
    document.getElementById("modalImage").src = url;
    $('.ui.modal.fullscreen').modal('show');
}

/** modal - confirmation */
function popConfirmation(url) {
    document.getElementById("modalFileURL").innerHTML = url;
    $('.ui.modal.confirmation').modal({onApprove: deleteFile}).modal('show');
}
function deleteFile() {
    $.ajax({
        type: "DELETE",
        url: "/remove?" + $.param({
            "url": document.getElementById("modalFileURL").innerHTML
        }),
        success: function(result) {
            console.log(result);
            window.location = "/images?" + $.param(result);
        }
    });
}
